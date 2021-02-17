package com.dam2d.pedrojg2ot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*

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
        button.setOnClickListener {
            getSteamUserById(76561197961885153)
        }

        //getAllPosts()
        //getPostById()
        //editPost()
    }

    fun getSteamUserById(id: Long) {
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
    }
}