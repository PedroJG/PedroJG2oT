package com.dam2d.pedrojg2ot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {


    var player:Player = Player()
    lateinit var service : ApiService

    val champs = mutableListOf<String>()
    val champIDs = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://euw1.api.riotgames.com/lol/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<ApiService>(ApiService::class.java)

        var ins: InputStream = this.getAssets().open("champions_whole.csv")
        var reader = InputStreamReader(ins, Charset.forName("UTF-8"))
        var csv: MutableList<Array<String>>? = CSVReader(reader).readAll()


        for (i in 0..csv!!.size - 1) {
            ChampionDataHolder.getInstance().addChampion(csv!!.get(i)[1], Champion(csv!!.get(i)[0], csv!!.get(i)[1], csv!!.get(i)[2], csv!!.get(i)[3], csv!!.get(i)[4], csv!!.get(i)[5], csv!!.get(i)[6], csv!!.get(i)[7]))

            val champName = csv!!.get(i)[1].toString()
            val APIname = csv!!.get(i)[3].toString()

            champs.add(i, champName)
            champIDs.add(i, APIname)
        }

        ChampionDataHolder.getInstance().names = ArrayList(champs)
        ChampionDataHolder.getInstance().ids = ArrayList(champIDs)

        loadChamps.setOnClickListener {
            val champsScreen = Intent(this, ChampListActivity::class.java)
            startActivity(champsScreen)
        }

        go.setOnClickListener {
            getPlayer(input_summoner.text.toString()).toString()
        }
    }

    fun getPlayer(summonerName:String) {
        var post: JsonObject? = null
        service.getSummonerId(summonerName).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                clearSummoner()
                clearRanked()

                val id:String = response?.body()?.get("id").toString().replace("\"", "")
                getSummonerMasteries(id)
                getRankedInfo(id)
                val resp = response?.body()
                Log.i("pedrojg", "ID: --> " + response?.body()?.get("id").toString())
                if (resp != null) {
                    player.id = resp!!.get("id").toString()
                    player.accountId = resp!!.get("accountId").toString()
                    player.puuid = resp!!.get("puuid").toString()
                    player.name = resp!!.get("name").toString()
                    player.profileIconId = resp!!.get("profileIconId").toString().toInt()
                    player.revisionDate = resp!!.get("revisionDate").toString().toLong()
                    player.summonerLevel = resp!!.get("summonerLevel").toString().toInt()

                    displaySummoner()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }

    fun getSummonerMasteries(summonerId:String) {
        var post: JsonArray? = null
        service.getSummonerMasteriesById(summonerId).enqueue(object: Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.code() == 200) {
                    if (response.body()!!.get(0) != null) {
                        player.top3champs = arrayListOf<Champion>()
                        val bestChamp = response?.body()?.get(0) as JsonObject
                        for (i in 0..2) {
                            player.top3champs.add(ChampionDataHolder.getInstance().getChampionById((response?.body()?.get(i) as JsonObject).get("championId").toString()))
                            player.top3champs.get(i).mastery = (response?.body()?.get(i) as JsonObject).get("championLevel").toString().toInt()
                            player.top3champs.get(i).mastPoints = (response?.body()?.get(i) as JsonObject).get("championPoints").toString().toLong()
                        }
                        displayChampions()
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }

    fun getRankedInfo(summonerId: String) {
        var post: JsonArray? = null
        service.getRankedInfoById(summonerId).enqueue(object: Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.code() == 200) {
                    Log.i("pedrojg", "Ranked solo: " + response.body().toString())
                    if (response.body().toString() != "[]") {
                        if (response.body()!!.get(0) != null) {
                            var solo = JsonObject()
                            var flex = JsonObject()

                            if (response!!.body()?.size() == 2) {
                                solo = response!!.body()!!.get(0) as JsonObject
                                flex = response!!.body()!!.get(1) as JsonObject
                            } else if (response!!.body()?.size() == 1) {
                                solo = response!!.body()!!.get(0) as JsonObject
                            }

                            if (solo != null) {
                                player.solo_tier = solo.get("tier").toString().replace("\"", "")
                                player.solo_rank = solo.get("rank").toString().replace("\"", "")
                                player.solo_lp = solo.get("leaguePoints").toString().toInt()
                                player.solo_wins = solo.get("wins").toString().toInt()
                                player.solo_losses = solo.get("losses").toString().toInt()
                                Log.i("pedrojg", "SOLO: ---->" + player.solo_tier)
                                displayRanked()
                            }

                            if (flex != JsonObject()) {
                                Log.i("pedrojg", "FLEX: " + flex)
                                player.flex_tier = flex.get("tier").toString().replace("\"", "")
                                player.flex_rank = flex.get("rank").toString().replace("\"", "")
                                player.flex_lp = flex.get("leaguePoints").toString().toInt()
                                player.flex_wins = flex.get("wins").toString().toInt()
                                player.flex_losses = flex.get("losses").toString().toInt()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }

    fun displayChampions() {
        var rl = resultLayout

        // Make visible all invisible elements
        for (i in 0..rl.childCount - 1) {
            var cl:ConstraintLayout? = rl.getChildAt(i) as ConstraintLayout
            for (j in 0..cl!!.childCount - 1) {
                var v:View = cl.getChildAt(j)
                v.visibility = View.VISIBLE
            }
        }

        // Insert values in their respective places for each champion in the 3 most populars for the summoner provided
        var counter = 1
        for (champion:Champion in player.top3champs) {
            var txtview = findViewById<TextView>(resources.getIdentifier("name"+counter.toString(), "id", packageName))
            var mastery = findViewById<TextView>(resources.getIdentifier("mastery"+counter.toString(), "id", packageName))
            var masteryNo = findViewById<TextView>(resources.getIdentifier("masteryNo"+counter.toString(), "id", packageName))
            var image = findViewById<ImageView>(resources.getIdentifier("image"+counter.toString(), "id", packageName))
            txtview?.text = champion.getName()
            masteryNo?.text = champion.mastery.toString()
            mastery?.text = champion.mastPoints.toString() + " pts"

            val url = "http://ddragon.leagueoflegends.com/cdn/11.3.1/img/champion/" + champion.getImage()
            if (image != null) Glide.with(this).load(url).into(image!!)

            counter = counter + 1
        }
    }

    fun displaySummoner() {
        var icon = icon
        icon.visibility = View.VISIBLE
        val iconUrl = "http://ddragon.leagueoflegends.com/cdn/11.4.1/img/profileicon/" + player.profileIconId + ".png"
        if (iconUrl != null) Glide.with(this).load(iconUrl).into(icon)

        summoner_name.visibility = View.VISIBLE
        summoner_name.text = player.name.replace("\"", "")

        summoner_level.visibility = View.VISIBLE
        summoner_level.text = player.summonerLevel.toString()

        level_label.visibility = View.VISIBLE
    }

    fun displayRanked() {
        Log.i("pedrojg", "DISPLAYRANKED")
        rank_name.visibility = View.VISIBLE
        rank_name.text = player.solo_tier.replace("\"", "") + " " + player.solo_rank.replace("\"", "") + " " + player.solo_lp.toString() + "LP"

        rank_icon.visibility = View.VISIBLE

        if (player.solo_tier.equals("IRON")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iron))
        if (player.solo_tier.equals("BRONZE")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bronze))
        if (player.solo_tier.equals("SILVER")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.silver))
        if (player.solo_tier.equals("GOLD")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.gold))
        if (player.solo_tier.equals("PLATINUM")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.plat))
        if (player.solo_tier.equals("DIAMOND")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diamond))
        if (player.solo_tier.equals("MASTER")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.master))
        if (player.solo_tier.equals("GRANDMASTER")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.gm))
        if (player.solo_tier.equals("CHALLENGER")) rank_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.challenger))
    }

    fun clearSummoner() {
        summoner_name.visibility = View.INVISIBLE
        summoner_level.visibility = View.INVISIBLE
        level_label.visibility = View.INVISIBLE
        icon.visibility = View.INVISIBLE
    }

    fun clearRanked() {
        rank_name.visibility = View.INVISIBLE
        rank_icon.visibility = View.INVISIBLE
    }
}