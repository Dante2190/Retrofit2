package com.example.retrofit2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call:Response<DogsResponse> = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppuies:DogsResponse? = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    //show recyclerview
                    val images:List<String> = puppuies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()
                }else{
                    //show error
                    showError()
                }
                hideKeyboard()
            }

        }
    }

    private fun showError() {
        Toast.makeText(this, "error al cargar imagenes", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByName(query.lowercase(Locale.getDefault()))
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
       return true
    }

    @SuppressLint("ServiceCast")
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }
}