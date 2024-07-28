package com.example.noteapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.CategoryRecyclerBinding

class CategoryAdapter(private val categoryList:ArrayList<Category>):RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: CategoryRecyclerBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val binding=CategoryRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return categoryList.size
    }
    private var lastSelectedPosition=-1
    private var selectedPosition=-1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ctg=categoryList[position]
        holder.binding.category.text=ctg.name
        holder.itemView.setOnClickListener{
            notifyChanges(holder)
        }
        if(selectedPosition==holder.adapterPosition)
            setSelectedColor(holder,R.drawable.category_bg_selected,R.color.white)
        else
            setSelectedColor(holder,R.drawable.category_bg_default,R.color.black)
    }
    private fun notifyChanges(holder:ViewHolder){
        lastSelectedPosition=selectedPosition
        selectedPosition=holder.adapterPosition
        notifyItemChanged(lastSelectedPosition)
        notifyItemChanged(selectedPosition)
    }
    private fun setSelectedColor(holder:ViewHolder, drawable:Int, colorText:Int){
        val resources=holder.itemView.context.resources
        holder.binding.linearLayout.setBackgroundResource(drawable)
        holder.binding.category.setTextColor(resources.getColor(colorText))
    }
}