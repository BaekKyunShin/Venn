package com.example.weroo.becksco.model

class Page<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Long
)