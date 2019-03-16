package com.example.weroo.becksco.model

data class HomeResponse(
    val user: User,
    val posts: List<PostSummaryDTO>
)