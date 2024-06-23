package com.example.milkymate


import kotlinx.serialization.Serializable

@Serializable
data class User(
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val uid: String
)