package com.e.birra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var Adapter:BirraAdapter
    private val job=Job()
    private var listadoDeCervezas= mutableListOf<BirraOb>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView=myView.findViewById(R.id.recyclerview)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager=LinearLayoutManager(requireActivity())
        Adapter= BirraAdapter()
        recyclerView.adapter=Adapter
        getListaDeCervezas()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun getListaDeCervezas() {
        CoroutineScope(Dispatchers.IO + job).launch {
            val call = getRetrofit().create(ApiService::class.java).getBreweryList()
            val response = call.body()
            activity?.runOnUiThread {
                listadoDeCervezas.clear()
                if (call.isSuccessful){
                listadoDeCervezas= response?.let {
                    parseCerveza(it)

                } ?: mutableListOf()
                    Adapter.submitList(listadoDeCervezas)

                }


            }
        }
    }

        private fun parseCerveza(cerveza:MutableList<Birra>) : MutableList<BirraOb>{
            val listacerveza = mutableListOf<BirraOb>()
            for (beer in cerveza){
                val id = beer.id
                val name= beer.name
                val breweryType= beer.brewery_type
                val phone = beer.phone
                val beerObj= BirraOb(
                    id,name,breweryType,phone
                )
                listacerveza.add(beerObj)
            }
            return listacerveza
    }


    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openbrewerydb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

}