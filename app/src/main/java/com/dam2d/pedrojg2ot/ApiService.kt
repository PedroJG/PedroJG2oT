package com.dam2d.pedrojg2ot

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

public interface ApiService {
    @GET("ISteamUser/GetPlayerSummaries/v0002/?key=704061DF21CF6817BA49180554881A19")
    fun getSteamUserById(@Query("steamids") id: Long): Call<JsonObject>

    @GET("posts/")
    fun getAllPosts(): Call<List<Post>>

    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<Post>

    @POST("posts/{id}")
    fun editPostById(@Path("id") id: Int, @Body post: Post?): Call<Post>
}