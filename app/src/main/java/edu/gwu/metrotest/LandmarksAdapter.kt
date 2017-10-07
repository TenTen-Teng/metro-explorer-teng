package edu.gwu.metrotest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.gwu.metrotest.model.Landmark

/**
 * Created by liteng on 10/1/17.
 */

class LandmarksAdapter(private val landmarkList: ArrayList<Landmark>) : RecyclerView.Adapter<LandmarksAdapter.ViewHolder>() {
    private var mOnClickListener: View.OnClickListener? = null

    override fun getItemCount(): Int {
        return landmarkList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItem(landmarkList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_landmark, parent, false)
        itemView.setOnClickListener(mOnClickListener)
        return ViewHolder(itemView)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewHead = itemView.findViewById<TextView>(R.id.text_view_head)
        private val textViewAddress1 = itemView.findViewById<TextView>(R.id.text_view_address1)
        private val textViewAddress2 = itemView.findViewById<TextView>(R.id.text_view_address2)

        fun bindItem(landmark: Landmark) {
            textViewHead.text = landmark.name
            textViewAddress1.text = landmark.address1.replace("\"","")
            if (landmark.address2 == null || landmark.address2.length == 0) {
                textViewAddress2.text = ""
            } else {
                textViewAddress2.text = landmark.address2.replace("\"","")
            }
        }
    }

    fun setOnItemClickListener(itemClickListener: LandmarksActivity) {
        mOnClickListener = itemClickListener
    }
}