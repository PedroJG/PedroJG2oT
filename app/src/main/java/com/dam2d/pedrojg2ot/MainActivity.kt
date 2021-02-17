package com.dam2d.pedrojg2ot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*
import com.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    lateinit var service : ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://api.steampowered.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create<ApiService>(ApiService::class.java)
        loadChamps.setOnClickListener {
            linearLayoutRecords.removeAllViews()
            var ins: InputStream = getAssets().open("champions_edit.csv")
            var reader: InputStreamReader = InputStreamReader(ins, Charset.forName("UTF-8"))
            var csv: MutableList<Array<String>>? = CSVReader(reader).readAll()
            for (i in 1 until csv!!.size) {
                val cv = CardView(this)
                val champName = csv.get(i)[1].toString()
                val APIname = csv.get(i)[11].toString()
                val url = "http://ddragon.leagueoflegends.com/cdn/11.3.1/img/champion/" + APIname + ".png"
                val iv = ImageView(this)
                Glide.with(this).load(url).into(iv)
                val tv = TextView(this)
                tv.textSize = 18f
                tv.text = champName
                cv.addView(iv)
                cv.addView(tv)
                linearLayoutRecords.addView(cv)
            }
        }
    }

    /*fun getSteamUserById(id: Long) {
        var post: JsonObject? = null
        service.getSteamUserById(id).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                post = response?.body()
                result.text = post?.toString()
                Log.i("pedrojg", Gson().toJson(post))
            }
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

    fun getAllPosts() {
        service.getAllPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>?, response: Response<List<Post>>?) {
                val posts = response?.body()
                result.text = posts.toString()
                Log.i("pedrojg", Gson().toJson(posts))
            }

            override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

    fun getPostById() {
        var post: Post? = null
        service.getPostById(1).enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
                post = response?.body()
                result.text = post?.title
                Log.i("pedrojg", Gson().toJson(post))
            }
            override fun onFailure(call: Call<Post>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

    fun editPost() {
        var post: Post? = Post(1, 1, "Hello k", "body")

        service.editPostById(1, post).enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
                post = response?.body()
            }

            override fun onFailure(call: Call<Post>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }*/
}