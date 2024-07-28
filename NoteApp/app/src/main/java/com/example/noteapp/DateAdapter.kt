package com.example.noteapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.DateRecyclerBinding

class DateAdapter(private val dateList:ArrayList<Date>): RecyclerView.Adapter<DateAdapter.ViewHolder>() {
    class ViewHolder(val binding: DateRecyclerBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.ViewHolder {
        val binding=DateRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return dateList.size
    }

    private var lastSelectedPosition=-1
    private var selectedPosition=-1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date=dateList[position]
        holder.binding.weekday.text=date.weekDay
        holder.binding.day.text=date.day.toString()
        holder.binding.month.text=date.month
        holder.itemView.setOnClickListener{
            notifyChanges(holder)
        }

        if(selectedPosition==holder.adapterPosition)
            setSelectedColor(holder,R.drawable.date_bg_selected,R.color.white)
        else
            setSelectedColor(holder,R.drawable.date_bg_default,R.color.black)
    }
    private fun notifyChanges(holder:ViewHolder){
        lastSelectedPosition=selectedPosition
        selectedPosition=holder.adapterPosition
        notifyItemChanged(lastSelectedPosition)
        notifyItemChanged(selectedPosition)
    }
    private fun setSelectedColor(holder:ViewHolder,drawable:Int, colorText:Int){
        val resources=holder.itemView.context.resources
        holder.binding.linearLayout.setBackgroundResource(drawable)
        holder.binding.weekday.setTextColor(resources.getColor(colorText))
        holder.binding.day.setTextColor(resources.getColor(colorText))
        holder.binding.month.setTextColor(resources.getColor(colorText))
    }
}