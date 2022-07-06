package com.example.currencyconverter.domain.models

class ChatMessage {
    var text: String? = null
    var name: String? = null
    var email: String? = null
    var photoUrl: String? = null
    var imageUrl: String? = null

    constructor()

    constructor(text: String?, name: String?, email: String?, photoUrl: String?, imageUrl: String?) {
        this.text = text
        this.name = name
        this.email = email
        this.photoUrl = photoUrl
        this.imageUrl = imageUrl
    }
}