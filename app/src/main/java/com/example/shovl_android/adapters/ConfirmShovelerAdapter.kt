package com.example.shovl_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shovl_android.R
import com.example.shovl_android.data.Bidders
import com.example.shovl_android.data.Post

class ConfirmShovelerAdapter(private val bidders: ArrayList<Bidders>
                             , viewClickListener: ConfirmRVClickListener
)
    : RecyclerView.Adapter<ConfirmShovelerAdapter.ConfirmViewHolder>() {

    private val listener: ConfirmRVClickListener? = viewClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmViewHolder {
        return ConfirmViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.rv_confirm_shoveler,parent,false))
    }

    override fun onBindViewHolder(holder: ConfirmViewHolder, position: Int) {
        val bidder = bidders?.get(position)

        with(holder){
            shovelerNameTv.text=bidder?.name.toString()
            rateTimeTv.text="$"+bidder?.price+" * "+bidder?.time+" hours"
            if (bidder.rejected==false){
                rejectedTv.visibility=View.GONE
                confirmedTv.visibility=View.GONE

                tickIv.setOnClickListener {
                    listener?.onConfirmClicked(bidder)
                }

                cancelIv.setOnClickListener {
                    listener?.onDeleteClicked(bidder)
                }
            } else if(bidder.confirmed==true){
                rejectedTv.visibility=View.GONE
                confirmedTv.visibility=View.VISIBLE
                tickIv.visibility=View.GONE
                cancelIv.visibility=View.GONE
            }
            else{
                rejectedTv.visibility=View.VISIBLE
                confirmedTv.visibility=View.GONE
                tickIv.visibility=View.GONE
                cancelIv.visibility=View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return bidders?.size ?: 0
    }

    class ConfirmViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val shovelerNameTv= itemView.findViewById<TextView>(R.id.tv_shoveler_name_confirm_rv_item)
        val rateTimeTv= itemView.findViewById<TextView>(R.id.tv_rate_and_time_confirm_rv_item)
        val tickIv= itemView.findViewById<ImageView>(R.id.iv_tick_confirm_rv_item)
        val cancelIv= itemView.findViewById<ImageView>(R.id.iv_delete_confirm_rv_item)
        val rejectedTv= itemView.findViewById<TextView>(R.id.tv_rejected_confirm_rv_item)
        val confirmedTv= itemView.findViewById<TextView>(R.id.tv_confirmed_confirm_rv_item)

    }

    interface ConfirmRVClickListener {
        fun onConfirmClicked(bidder: Bidders)
        fun onDeleteClicked(bidder: Bidders)
    }

}