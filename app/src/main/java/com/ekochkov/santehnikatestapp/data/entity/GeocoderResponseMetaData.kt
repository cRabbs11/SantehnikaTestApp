package com.ekochkov.santehnikatestapp.data.entity

data class GeocoderResponseMetaData(
    val Point: PointX,
    val found: String,
    val request: String,
    val results: String
)