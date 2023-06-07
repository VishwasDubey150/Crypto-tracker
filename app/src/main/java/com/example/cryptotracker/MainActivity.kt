package com.example.cryptotracker

import android.graphics.ColorSpace.Model
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptotracker.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var rvapdapter: Rvapdapter
    private lateinit var data: ArrayList<model>
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pb.isVisible=true
        supportActionBar?.hide()
        apiData
        data=ArrayList<model>()
        rvapdapter=Rvapdapter(this,data)

        binding.rv.layoutManager=LinearLayoutManager(this)
        binding.rv.adapter=rvapdapter

        binding.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val filterdata = ArrayList<model>()
                for(item in data)
                {
                    if(item.name.lowercase(Locale.getDefault()).contains(p0.toString().lowercase(
                            Locale.getDefault())))
                    {
                        filterdata.add(item)
                    }
                }
                if(filterdata.isEmpty())
                {
                    Toast.makeText(this@MainActivity,"Not present in list",Toast.LENGTH_SHORT).show()
                }
                else{
                    rvapdapter.changeData(filterdata)
                }

            }

        })
    }

    val apiData:Unit

    get()
    {
        val url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"


        val queue= Volley.newRequestQueue(this)
        val jsonObjectRequest:JsonObjectRequest=object:JsonObjectRequest(Method.GET, url, null,
            Response.Listener { response ->
                binding.pb.isVisible=false
                try {
                    val dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length())
                    {
                        val dataobject = dataArray.getJSONObject(i)
                        val name=dataobject.getString("name")
                        val quote=dataobject.getJSONObject("quote")
                        val USD=quote.getJSONObject("USD")
                        val price=String.format("$"+"%.2f",USD.getDouble("price"))
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