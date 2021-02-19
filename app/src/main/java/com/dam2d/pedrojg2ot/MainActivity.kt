package com.dam2d.pedrojg2ot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.bumptech.glide.Glide
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

    lateinit var service : ApiService
    var profileIconId:String = ""
    val champs = mutableListOf<String>()
    val champIDs = mutableListOf<String>()
    var top3champs:ArrayList<Champion> = arrayListOf<Champion>()

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
            getSummonerId(input_summoner.text.toString()).toString()
        }
    }

    fun getSummonerId(summonerName:String) {
        var post: JsonObject? = null
        service.getSummonerId(summonerName).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val id:String = response?.body()?.get("id").toString().replace("\"", "")
                getSummonerMasteries(id)
                Log.i("pedrojg", response?.body()?.get("id").toString())
                profileIconId = response?.body()?.get("profileIconId").toString()
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
                        top3champs = arrayListOf<Champion>()
                        val bestChamp = response?.body()?.get(0) as JsonObject
                        for (i in 0..2) {
                            top3champs.add(ChampionDataHolder.getInstance().getChampionById((response?.body()?.get(i) as JsonObject).get("championId").toString()))
                            top3champs.get(i).mastery = (response?.body()?.get(i) as JsonObject).get("championLevel").toString().toInt()
                            top3champs.get(i).mastPoints = (response?.body()?.get(i) as JsonObject).get("championPoints").toString().toLong()
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

    fun displayChampions() {
        var rl = resultLayout
        for (i in 0..rl.childCount - 1) {
            var cl:ConstraintLayout? = rl.getChildAt(i) as ConstraintLayout
            for (j in 0..cl!!.childCount - 1) {
                var v:View = cl.getChildAt(j)
                v.visibility = View.VISIBLE
            }
        }
        var counter = 1
        for (champion:Champion in top3champs) {
            var txtview = findViewById<TextView>(resources.getIdentifier("name"+counter.toString(), "id", packageName))
            var mastery = findViewById<TextView>(resources.getIdentifier("mastery"+counter.toString(), "id", packageName))
            var masteryNo = findViewById<TextView>(resources.getIdentifier("masteryNo"+counter.toString(), "id", packageName))
            var image = findViewById<ImageView>(resources.getIdentifier("image"+counter.toString(), "id", packageName))

            var icon = icon

            txtview?.text = champion.getName()
            masteryNo?.text = champion.mastery.toString()
            mastery?.text = champion.mastPoints.toString() + " pts"

            val url = "http://ddragon.leagueoflegends.com/cdn/11.3.1/img/champion/" + champion.getImage()
            val iconUrl = "http://ddragon.leagueoflegends.com/cdn/11.4.1/img/profileicon/" + profileIconId + ".png"
            if (image != null) Glide.with(this).load(url).into(image!!)

            if (iconUrl != null) Glide.with(this).load(iconUrl).into(icon)

            counter = counter + 1
        }
    }
}