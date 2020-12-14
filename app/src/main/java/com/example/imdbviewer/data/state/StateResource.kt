package com.example.imdbviewer.data.state


import android.view.View


data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val messageType: MessageType
)


sealed class MessageType{
    object Success: MessageType()
    object Error: MessageType()
    object None: MessageType()

}

