package com.dam2d.pedrojg2ot

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

public interface ApiService {
    @Headers("X-Riot-Token: RGAPI-2ebf80ce-332e-4cb0-ab84-c8571d38e779")
    @GET("summoner/v4/summoners/by-name/{summoner}")
    fun getSummonerId(@Path("summoner") summonerName:String):Call<JsonObject>

    @Headers("X-Riot-Token: RGAPI-2ebf80ce-332e-4cb0-ab84-c8571d38e779")
    @GET("champion-mastery/v4/champion-masteries/by-summoner/{summonerId}")
    fun getSummonerMasteriesById(@Path("summonerId")summonerId: String): Call<JsonArray>
}