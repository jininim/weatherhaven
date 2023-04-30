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

class ColdViewPagerAdapter(val itemClicked: (ColdModel) -> Unit): ListAdapter<ColdModel, ColdViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bind(coldModel: ColdModel){
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val contentTextView = view.findViewById<TextView>(R.id.contentTextView)

            titleTextView.text = coldModel.r_area_nm
            contentTextView.text = "이용가능 인원 : ${coldModel.use_prnb}명"

            view.setOnClickListener {
                itemClicked(coldModel)
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
        val differ = object : DiffUtil.ItemCallback<ColdModel>(){
            override fun areItemsTheSame(oldItem: ColdModel, newItem: ColdModel): Boolean {
                return oldItem.r_seq_no == newItem.r_seq_no
            }

            override fun areContentsTheSame(oldItem: ColdModel, newItem: ColdModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}