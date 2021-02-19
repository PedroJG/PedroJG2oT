package com.dam2d.pedrojg2ot

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class ListChampsThread(cnt:Context) : Thread() {
    val context: Context
    val activity:Activity
    init {
        context = cnt
        activity = this.context as Activity
    }
    override fun run() {

    }
}