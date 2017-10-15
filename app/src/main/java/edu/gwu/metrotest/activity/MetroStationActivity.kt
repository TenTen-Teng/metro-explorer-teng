package edu.gwu.metrotest.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
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
import kotlinx.android.synthetic.main.search_toolbar.*

class MetroStationActivity : AppCompatActivity(), View.OnClickListener,
        FetchMetroStationAsyncTask.ItemsSearchCompletionListener,
        android.support.v7.widget.SearchView.OnQueryTextListener {

    private lateinit var stations: List<MetroStation>
    private var metroStationAdapter: MetroStationsAdapter?= null
    private lateinit var recyclerView: RecyclerView
    private var fetchMetroStationAsyncTask = FetchMetroStationAsyncTask(this)

    companion object {
        val METROSTATION = "STATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_station)

        setSupportActionBar(search_toolbar)

        initView()
    }

    private fun initView() {
        loadingBar(true)
        recyclerView = findViewById(R.id.station_list)
        station_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //fetch metro station list from WMATA API
        fetchMetroStationAsyncTask.itemsSearchCompletionListener = this
        fetchMetroStationAsyncTask.loadStationData()

        station_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    //handle click item: go to landmark activity and show landmarks near current station
    override fun onClick(p0: View?) {
        val intent = Intent(this, LandmarksActivity::class.java)
        intent.putExtra("activity", "MetroStation")

        val holder = p0?.tag as MetroStationsAdapter.ViewHolder
        intent.putExtra(MetroStationActivity.METROSTATION, metroStationAdapter?.getStation(holder.adapterPosition))
        startActivity(intent)
    }

    //listener from fetchMetroStationAsyncTask, get a list of station from API
    override fun stationItemsLoaded(stations: ArrayList<MetroStation>) {
        loadingBar(false)

        //add stations to adapter
        metroStationAdapter?:let {
            this@MetroStationActivity.stations = fetchMetroStationAsyncTask.loadStationData()
            metroStationAdapter = MetroStationsAdapter(stations, this)
            station_list.adapter = metroStationAdapter
        }
    }

    //listener from fetchMetroStationAsyncTask, can't get station lists, toast info to user
    override fun stationItemsNotLoaded() {
        toast("Item didn't load :(")
        loadingBar(false)
    }

    //search tool bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        val searchView : android.support.v7.widget.SearchView = menu.findItem(R.id.action_search).actionView as android.support.v7.widget.SearchView
        val searchManage : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManage.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(this)
        return true
    }

    //progress bar
    private fun loadingBar(show: Boolean) {
        if(show) {
            progress_bar.visibility = ProgressBar.VISIBLE
        }
        else {
            progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }

    //handle search text
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    //handle search text change
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
