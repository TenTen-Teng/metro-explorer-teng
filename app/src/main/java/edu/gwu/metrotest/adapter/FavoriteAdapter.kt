package edu.gwu.metrotest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark

/**
 * Created by liteng on 10/13/17.
 */

class FavoriteAdapter(private val landmarks: List<Landmark>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val landmark = landmarks?.get(position)

        landmark?.let {
            (holder as ViewHolder).bind(landmark)
        }
    }

    override fun getItemCount(): Int {
        return landmarks.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)

        //inflate our score row layout
        return ViewHolder(layoutInflater.inflate(R.layout.list_favorite, parent, false))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameLandmark: TextView = view.findViewById(R.id.landmark_name)
        private val realtiveStation: TextView = view.findViewById(R.id.station_name)

        //update score row ui with score and date
        fun bind(landmark: Landmark) {
            nameLandmark.text = landmark.name
            realtiveStation.text = landmark.relativeStation
        }
    }
}