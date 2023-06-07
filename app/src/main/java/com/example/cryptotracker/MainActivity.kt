package com.example.cryptotracker

import android.graphics.ColorSpace.Model
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptotracker.databinding.ActivityMainBinding
import java.util.Objects

class MainActivity : AppCompatActivity() {

    private lateinit var rvapdapter: Rvapdapter
    private lateinit var data: ArrayList<model>
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        apiData
        data=ArrayList<model>()
        rvapdapter=Rvapdapter(this,data)

        binding.rv.layoutManager=LinearLayoutManager(this)
        binding.rv.adapter=rvapdapter
    }

    val apiData:Unit

    get()
    {
        val url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"


        val queue= Volley.newRequestQueue(this)
        val jsonObjectRequest:JsonObjectRequest=object:JsonObjectRequest(Method.GET, url, null,
            Response.Listener { response ->

                try {
                    val dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length())
                    {
                        val dataobject = dataArray.getJSONObject(i)
                        val name=dataobject.getString("name")
                        val quote=dataobject.getJSONObject("quote")
                        val USD=quote.getJSONObject("USD")
                        val price=USD.getDouble("price")
                        data.add(model(name,price.toString()))
                    }
                    rvapdapter.notifyDataSetChanged()

                }catch (e:Exception)
                {
                    e.printStackTrace()
                }


            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()

            })
        {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-CMC_PRO_API_KEY"] = "5389cb84-5a1f-4064-9c8b-7366a814a633"

                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }
}