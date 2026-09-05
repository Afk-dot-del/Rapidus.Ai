package com.example.data.model

import java.util.UUID

enum class MessageStatus {
    SENDING,
    SENT,
    ERROR
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String = "default",
    val text: String = "",
    val isUser: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT
) {
    // For Firestore conversion
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "conversationId" to conversationId,
            "text" to text,
            "isUser" to isUser,
            "timestamp" to timestamp,
            "status" to status.name
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): ChatMessage {
            return ChatMessage(
                id = map["id"] as? String ?: UUID.randomUUID().toString(),
                conversationId = map["conversationId"] as? String ?: "default",
                text = map["text"] as? String ?: "",
                isUser = map["isUser"] as? Boolean ?: false,
                timestamp = (map["timestamp"] as? Long) ?: System.currentTimeMillis(),
                status = try {
                    MessageStatus.valueOf(map["status"] as? String ?: "SENT")
                } catch (e: Exception) {
                    MessageStatus.SENT
                }
            )
        }
    }
}

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "New Conversation",
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "lastMessage" to lastMessage,
            "timestamp" to timestamp
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Conversation {
            return Conversation(
                id = map["id"] as? String ?: UUID.randomUUID().toString(),
                title = map["title"] as? String ?: "New Conversation",
                lastMessage = map["lastMessage"] as? String ?: "",
                timestamp = (map["timestamp"] as? Long) ?: System.currentTimeMillis()
            )
        }
    }
}

data class DiagnosticItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val buttonText: String,
    val isRunning: Boolean = false,
    val statusText: String = "Ready",
    val detailMessage: String = "",
    val lastRunTimestamp: Long = 0L
)

data class UserProfile(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val isAnonymous: Boolean = false
)
