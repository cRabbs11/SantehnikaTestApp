package com.ekochkov.santehnikatestapp.data.entity

data class DataDTO(
    val response: Response
)

fun DataDTO.toPointAdress(): PointAddress {
    return PointAddress(
        response.GeoObjectCollection.metaDataProperty.GeocoderResponseMetaData.Point.pos,
        response.GeoObjectCollection.featureMember.firstOrNull()?.GeoObject?.metaDataProperty?.GeocoderMetaData?.text?: "not found"
    )
}