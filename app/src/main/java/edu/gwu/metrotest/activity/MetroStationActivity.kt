package edu.gwu.metrotest.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import edu.gwu.metrotest.R
import edu.gwu.metrotest.adapter.MetroStationsAdapter
import edu.gwu.metrotest.asyncTask.FetchMetroStationAsyncTask
import edu.gwu.metrotest.model.MetroStation
import kotlinx.android.synthetic.main.activity_metro_station.*
import org.jetbrains.anko.toast

class MetroStationActivity : AppCompatActivity(), View.OnClickListener,
        FetchMetroStationAsyncTask.ItemsSearchCompletionListener {
    private val TAG = "MetroStationActivity"

    lateinit var stations: List<MetroStation>
    var metroStationAdapter: MetroStationsAdapter?= null
    lateinit var recyclerView: RecyclerView
    var fetchMetroStationAsyncTask = FetchMetroStationAsyncTask(this)

    companion object {
        val METROSTATION = "STATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_station)

        initView()
    }

    fun initView() {
        loadingBar(true)
        recyclerView = findViewById(R.id.station_list)
        station_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)

        fetchMetroStationAsyncTask.itemsSearchCompletionListener = this
        fetchMetroStationAsyncTask.loadStationData()

        station_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(this, LandmarksActivity::class.java)
        val holder = p0?.tag as MetroStationsAdapter.ViewHolder
        intent.putExtra(MetroStationActivity.METROSTATION, metroStationAdapter?.getStation(holder.adapterPosition))
        startActivity(intent)
    }

    override fun stationItemsLoaded(stations: ArrayList<MetroStation>) {
        loadingBar(false)

        metroStationAdapter?:let {
            this@MetroStationActivity.stations = fetchMetroStationAsyncTask.loadStationData()
            metroStationAdapter = MetroStationsAdapter(stations, this)
            station_list.adapter = metroStationAdapter
        }
    }

    override fun stationItemsNotLoaded() {
        toast("Item didn't load :(")
        loadingBar(false)
    }

    private fun loadingBar(show: Boolean) {
        if(show) {
            station_progress_bar.visibility = ProgressBar.VISIBLE
        }
        else {
            station_progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }
}
