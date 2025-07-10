package com.example.phygital

import retrofit2.Response
//import retrofit2.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TagApiService {
    @POST("tags")
    suspend fun createTag(@Body tag: TagCreate): Response<ResponseBody>

    @GET("tags")
    suspend fun getTag(@Query("tag") tag:String): Response<TagCreate>
}