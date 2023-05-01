package com.app.weatherhaven.viewpager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherhaven.R
import com.app.weatherhaven.retrofit.coldshelter.ColdModel
import com.app.weatherhaven.retrofit.heatshelter.Row

class HeatViewPagerAdapter(val itemClicked: (Row) -> Unit): ListAdapter<Row, HeatViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bind(heatModel: Row){
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val contentTextView = view.findViewById<TextView>(R.id.contentTextView)

            titleTextView.text = heatModel.R_AREA_NM // 쉼터 명
            contentTextView.text = "이용가능 인원 : ${heatModel.USE_PRNB}명"

            view.setOnClickListener {
                itemClicked(heatModel)
            }
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): ItemViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_shelter_detail_for_viewpager, parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int){
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<Row>(){
            override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
                return oldItem.R_SEQ_NO == newItem.R_SEQ_NO
            }

            override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
                return oldItem == newItem
            }
        }
    }
}