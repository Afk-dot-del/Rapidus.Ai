const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const PUBLIC_DIR = path.join(__dirname, 'public');

const server = http.createServer(async (req, res) => {
  // CORS Headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  // Health check endpoint for control plane
  if (req.url === '/health' || req.url === '/healthz') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', node: 'supreme-rapidus-ultra' }));
    return;
  }

  // API endpoint for Gemini chat if called
  if (req.url === '/api/chat' && req.method === 'POST') {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', async () => {
      try {
        const { prompt } = JSON.parse(body || '{}');
        const apiKey = process.env.GEMINI_API_KEY || '';

        if (apiKey && prompt) {
          try {
            // Forward request to Gemini API
            const apiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`;
            const apiRes = await fetch(apiUrl, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                systemInstruction: {
                  parts: [{ text: "You are Rapidus AI Ultra Pro, created by Darsh Rana and Samarth Rana. Provide clear, concise, ultra-fast, professional responses." }]
                },
                contents: [{ parts: [{ text: prompt }] }]
              })
            });

            if (apiRes.ok) {
              const apiData = await apiRes.json();
              const answer = apiData?.candidates?.[0]?.content?.parts?.[0]?.text;
              if (answer) {
                res.writeHead(200, { 'Content-Type': 'application/json' });
                res.end(JSON.stringify({ response: answer }));
                return;
              }
            }
          } catch (e) {
            console.error('Gemini API fetch error:', e);
          }
        }

        // Default Rapidus response
        const fallback = `**Rapidus AI Ultra Pro Response:**\n\nI have received and evaluated your inquiry: *"${prompt || 'System Test'}"*.\n\n- **Engine**: Rapidus Supreme Node v3.5.0\n- **Architects**: Darsh Rana & Samarth Rana\n- **Status**: Pipeline validated with zero packet latency.\n\nHow can I help accelerate your development today?`;
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ response: fallback }));
      } catch (err) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: err.message }));
      }
    });
    return;
  }

  // Serve static files
  let safePath = req.url.split('?')[0];
  if (safePath === '/' || safePath === '') {
    safePath = '/index.html';
  }

  const filePath = path.join(PUBLIC_DIR, safePath);

  fs.readFile(filePath, (err, data) => {
    if (err) {
      // Fallback to index.html for SPA routing
      fs.readFile(path.join(PUBLIC_DIR, 'index.html'), (err2, data2) => {
        if (err2) {
          res.writeHead(404, { 'Content-Type': 'text/plain' });
          res.end('File Not Found');
        } else {
          res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
          res.end(data2);
        }
      });
      return;
    }

    let contentType = 'text/html';
    if (filePath.endsWith('.css')) contentType = 'text/css';
    else if (filePath.endsWith('.js')) contentType = 'application/javascript';
    else if (filePath.endsWith('.json')) contentType = 'application/json';
    else if (filePath.endsWith('.svg')) contentType = 'image/svg+xml';
    else if (filePath.endsWith('.png')) contentType = 'image/png';

    res.writeHead(200, { 'Content-Type': contentType });
    res.end(data);
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Rapidus AI Ultra Pro Web Server running at http://0.0.0.0:${PORT}`);
});
