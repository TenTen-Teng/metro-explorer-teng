package edu.gwu.metrotest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark
import edu.gwu.metrotest.model.MetroStation

/**
 * Created by liteng on 10/8/17.
 */

class MetroStationsAdapter(private val stations: ArrayList<MetroStation>,
                           var clickListener : View.OnClickListener?)
    : RecyclerView.Adapter<MetroStationsAdapter.ViewHolder>(){

    var stationsList : ArrayList<MetroStation> = stations
    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val station = stations[position]
        holder?.textViewHead?.text = station.name
        holder?.textViewAddress?.text = station.address
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_station, parent, false))
    }

    fun getStation(adapterPosition: Int) : MetroStation {
        return stations[adapterPosition]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewHead : TextView
        var textViewAddress : TextView

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(clickListener)
            }
            itemView.tag = this
            textViewHead = itemView.findViewById(R.id.station_head)
            textViewAddress = itemView.findViewById(R.id.station_address)
        }
    }
}