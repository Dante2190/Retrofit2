package com.example.retrofit2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit2.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding = ItemDogBinding.bind(view)
    fun bing(image:String){
        Picasso.get().load(image).into(binding.ivDog)
    }
}