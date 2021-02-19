package com.dam2d.pedrojg2ot

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

public interface ApiService {
    @Headers("X-Riot-Token: RGAPI-5cad5a92-a987-4b04-afa6-c2870cf821b2")
    @GET("summoner/v4/summoners/by-name/{summoner}")
    fun getSummonerId(@Path("summoner") summonerName:String):Call<JsonObject>

    @Headers("X-Riot-Token: RGAPI-5cad5a92-a987-4b04-afa6-c2870cf821b2")
    @GET("champion-mastery/v4/champion-masteries/by-summoner/{summonerId}")
    fun getSummonerMasteriesById(@Path("summonerId")summonerId: String): Call<JsonArray>

    @Headers("X-Riot-Token: RGAPI-5cad5a92-a987-4b04-afa6-c2870cf821b2")
    @GET("league/v4/entries/by-summoner/{summonerId}")
    fun getRankedInfoById(@Path("summonerId")summonerId: String): Call<JsonArray>
}