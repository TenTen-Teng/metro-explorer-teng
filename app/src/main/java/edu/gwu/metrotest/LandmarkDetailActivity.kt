package edu.gwu.metrotest

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import edu.gwu.metrotest.model.Landmark
import kotlinx.android.synthetic.main.activity_landmark_detail.*


class LandmarkDetailActivity : AppCompatActivity(){
    private var text = ArrayList<TextView>()
    companion object {
        val EXTRA_PARAM_ID = "place_id"

        fun newIntent(context: Context, position: Int): Intent {
            val intent = Intent(context, LandmarkDetailActivity::class.java)
            intent.putExtra(EXTRA_PARAM_ID, position)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        val intent: Intent = getIntent()
        val bundle = intent.extras

        val name = bundle.getString("name")
        val imageUrl = bundle.getString("imageUrl")
        val distance = bundle.getString("distance")

        landmark_title_text.text = name
        landmark_name_text.text = name
        landmark_detail_info.text = distance
    }
}





