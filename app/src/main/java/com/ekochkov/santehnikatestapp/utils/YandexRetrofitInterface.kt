package com.ekochkov.santehnikatestapp.utils

import com.ekochkov.santehnikatestapp.data.entity.DataDTO
import com.ekochkov.santehnikatestapp.utils.Constants_API.PARAMETER_FORMAT_JSON
import com.ekochkov.santehnikatestapp.utils.Constants_API.PARAMETER_SCO_LAT_LONG
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexRetrofitInterface {

    @GET("/1.x/")
    suspend fun getAdressByCoords(
        @Query("geocode") geocode: String,
        @Query("format") format: String = PARAMETER_FORMAT_JSON,
        @Query("sco") sco: String = PARAMETER_SCO_LAT_LONG
    ): DataDTO?
}


