package edu.gwu.metrotest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import edu.gwu.metrotest.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*

class FavorityActivity : AppCompatActivity() {
    private lateinit var persistanceManager: PersistanceManager
    private lateinit var favoriteAdapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        persistanceManager = PersistanceManager(this)

        val favLandmarks = persistanceManager.fetchFavorite()

        favoriteAdapter = FavoriteAdapter(favLandmarks)

        favorite_recycler.layoutManager = LinearLayoutManager(this)
        favorite_recycler.adapter = favoriteAdapter
    }
}
