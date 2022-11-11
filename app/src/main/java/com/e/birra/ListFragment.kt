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
    private var adapter:BirraAdapter=BirraAdapter()
    private val job=Job()
    private var listaDeBirras= mutableListOf<BirraOb>()


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
        recyclerView.adapter=adapter
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
                listaDeBirras.clear()
                if (call.isSuccessful) {
                   listaDeBirras=((response?.map { Birra->
                       Birra.MapearBirra()
                   } ?: emptyList()) as MutableList<BirraOb>)
                        adapter.submitList(listaDeBirras)
                }
            }
        }
    }



       /* private fun parseCerveza(cerveza: List<Birra>) : List<BirraOb>{
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
    } */


    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openbrewerydb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

}