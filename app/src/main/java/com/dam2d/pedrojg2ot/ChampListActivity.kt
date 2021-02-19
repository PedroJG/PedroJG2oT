package com.dam2d.pedrojg2ot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class ChampListActivity : AppCompatActivity() {
    var champions:ArrayList<String> = arrayListOf<String>()
    var championIDs:ArrayList<String> = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_champ_list)

        champions = ChampionDataHolder.getInstance().names
        championIDs = ChampionDataHolder.getInstance().ids

        var recycler = this.findViewById<RecyclerView>(R.id.recyclerview)

        val c_adap:ChampAdapter = ChampAdapter(this, champions.toTypedArray(), championIDs.toTypedArray())
        recycler.adapter = c_adap
        recycler.layoutManager = LinearLayoutManager(this)
    }
}