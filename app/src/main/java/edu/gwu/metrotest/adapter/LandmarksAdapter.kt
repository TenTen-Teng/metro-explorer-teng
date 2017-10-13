package edu.gwu.metrotest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark

/**
 * Created by liteng on 10/1/17.
 */

class LandmarksAdapter(private val landmarks: ArrayList<Landmark>, var clickListener : View.OnClickListener?)
    : RecyclerView.Adapter<LandmarksAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return landmarks.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val landmark = landmarks[position]
        holder?.textViewHead?.text = landmark.name
        holder?.textViewAddress1?.text = landmark.address1
        holder?.textViewAddress2?.text = landmark.address2
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_landmark, parent, false))
    }

    fun getLandmark(adapterPosition: Int) : Landmark {
        return landmarks[adapterPosition]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewHead : TextView
        var textViewAddress1 : TextView
        var textViewAddress2 : TextView

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(clickListener)
            }
            itemView.tag = this
            textViewHead = itemView.findViewById(R.id.landmark_head)
            textViewAddress1 = itemView.findViewById(R.id.landmark_address1)
            textViewAddress2 = itemView.findViewById(R.id.landmark_address2)
        }
    }
}