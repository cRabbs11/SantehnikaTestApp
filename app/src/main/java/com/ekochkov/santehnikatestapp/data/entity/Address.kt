package com.ekochkov.santehnikatestapp.data.entity

data class Address(
    val Components: List<Component>,
    val country_code: String,
    val formatted: String
)