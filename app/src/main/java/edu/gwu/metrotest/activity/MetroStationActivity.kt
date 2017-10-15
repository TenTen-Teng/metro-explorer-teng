package edu.gwu.metrotest.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import edu.gwu.metrotest.R
import edu.gwu.metrotest.adapter.MetroStationsAdapter
import edu.gwu.metrotest.asyncTask.FetchMetroStationAsyncTask
import edu.gwu.metrotest.model.MetroStation
import kotlinx.android.synthetic.main.activity_metro_station.*
import org.jetbrains.anko.toast
import android.app.SearchManager
import android.content.Context
import android.support.v4.view.MenuItemCompat
import android.util.Log
import android.widget.SearchView
import edu.gwu.metrotest.R.id.*
import edu.gwu.metrotest.model.Landmark
import kotlinx.android.synthetic.main.search_toolbar.*


class MetroStationActivity : AppCompatActivity(), View.OnClickListener,
        FetchMetroStationAsyncTask.ItemsSearchCompletionListener,
        android.support.v7.widget.SearchView.OnQueryTextListener {

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

        setSupportActionBar(search_toolbar)
        supportActionBar?.title = "Metro Station"

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
        intent.putExtra("activity", "MetroStation")

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        val searchView : android.support.v7.widget.SearchView = menu.findItem(R.id.action_search).actionView as android.support.v7.widget.SearchView
        val searchManage : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManage.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(this)
        return true
    }

    private fun loadingBar(show: Boolean) {
        if(show) {
            progress_bar.visibility = ProgressBar.VISIBLE
        }
        else {
            progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        newText.toLowerCase()
        var newStation = ArrayList<MetroStation>()

        for (station in stations) {

            var name = station.name.toLowerCase()
            if (name.contains(newText)) {

                newStation.add(station)
            }
        }

        metroStationAdapter = MetroStationsAdapter(newStation, this)
        station_list.adapter = metroStationAdapter

        return true
    }

}
