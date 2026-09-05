package com.example.data.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.model.ChatMessage
import com.example.data.model.Conversation
import com.example.data.model.UserProfile
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseManager(private val context: Context) {

    private var auth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private val credentialManager = CredentialManager.create(context)

    init {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                auth = FirebaseAuth.getInstance()
                firestore = FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Firebase initialization check: ${e.message}")
        }
    }

    val isFirebaseAvailable: Boolean
        get() = auth != null && firestore != null

    fun getCurrentUser(): UserProfile {
        val fbUser = auth?.currentUser
        return if (fbUser != null) {
            UserProfile(
                uid = fbUser.uid,
                displayName = fbUser.displayName?.ifEmpty { "Darsh Rana" } ?: "Darsh Rana",
                email = fbUser.email?.ifEmpty { "Mylove.you09@gmail.com" } ?: "Mylove.you09@gmail.com",
                photoUrl = fbUser.photoUrl?.toString(),
                isAnonymous = fbUser.isAnonymous
            )
        } else {
            UserProfile(
                uid = "dr-samarth-node",
                displayName = "Darsh & Samarth",
                email = "Mylove.you09@gmail.com",
                isAnonymous = false
            )
        }
    }

    suspend fun signInWithGoogle(webClientId: String? = null): Result<UserProfile> {
        val authInstance = auth
        if (authInstance == null) {
            // Local fallback simulation when Firebase is in prototype mode
            return Result.success(
                UserProfile(
                    uid = "darsh-user-" + UUID.randomUUID().toString().take(6),
                    displayName = "Darsh Rana",
                    email = "Mylove.you09@gmail.com",
                    isAnonymous = false
                )
            )
        }

        return try {
            val serverClientId = webClientId ?: "default-client-id"
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = authInstance.signInWithCredential(authCredential).await()
                val user = authResult.user
                Result.success(
                    UserProfile(
                        uid = user?.uid ?: UUID.randomUUID().toString(),
                        displayName = user?.displayName ?: "Darsh Rana",
                        email = user?.email ?: "Mylove.you09@gmail.com",
                        photoUrl = user?.photoUrl?.toString(),
                        isAnonymous = false
                    )
                )
            } else {
                // Anonymous or dev fallback
                signInAnonymously()
            }
        } catch (e: GetCredentialException) {
            Log.w("FirebaseManager", "Google sign-in credential exception: ${e.message}")
            signInAnonymously()
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Google sign in error: ${e.message}")
            signInAnonymously()
        }
    }

    suspend fun signInAnonymously(): Result<UserProfile> {
        val authInstance = auth
        if (authInstance != null) {
            return try {
                val authResult = authInstance.signInAnonymously().await()
                val user = authResult.user
                Result.success(
                    UserProfile(
                        uid = user?.uid ?: "user-anon",
                        displayName = "Darsh Rana (Secure Node)",
                        email = "Mylove.you09@gmail.com",
                        isAnonymous = true
                    )
                )
            } catch (e: Exception) {
                Log.w("FirebaseManager", "Anonymous auth error: ${e.message}")
                Result.success(
                    UserProfile(
                        uid = "dev-user-darsh",
                        displayName = "Darsh Rana",
                        email = "Mylove.you09@gmail.com",
                        isAnonymous = false
                    )
                )
            }
        }
        return Result.success(
            UserProfile(
                uid = "dev-user-darsh",
                displayName = "Darsh Rana",
                email = "Mylove.you09@gmail.com",
                isAnonymous = false
            )
        )
    }

    suspend fun signOut() {
        try {
            auth?.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Sign out error: ${e.message}")
        }
    }

    // --- Firestore Persistence for Conversations & Messages ---

    suspend fun saveConversation(userId: String, conversation: Conversation) {
        val db = firestore ?: return
        try {
            db.collection("users")
                .document(userId)
                .collection("conversations")
                .document(conversation.id)
                .set(conversation.toMap())
                .await()
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Error saving conversation to Firestore: ${e.message}")
        }
    }

    suspend fun saveMessage(userId: String, message: ChatMessage) {
        val db = firestore ?: return
        try {
            db.collection("users")
                .document(userId)
                .collection("conversations")
                .document(message.conversationId)
                .collection("messages")
                .document(message.id)
                .set(message.toMap())
                .await()
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Error saving message to Firestore: ${e.message}")
        }
    }

    suspend fun saveDiagnosticLog(userId: String, toolName: String, status: String, details: String) {
        val db = firestore ?: return
        try {
            val logData = mapOf(
                "toolName" to toolName,
                "status" to status,
                "details" to details,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(userId)
                .collection("diagnostics")
                .document(UUID.randomUUID().toString())
                .set(logData)
                .await()
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Error saving diagnostic to Firestore: ${e.message}")
        }
    }

    fun getMessagesFlow(userId: String, conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = db.collection("users")
            .document(userId)
            .collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirebaseManager", "Firestore listen failed: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { ChatMessage.fromMap(it) }
                    }
                    trySend(messages)
                }
            }

        awaitClose { listener.remove() }
    }
}
