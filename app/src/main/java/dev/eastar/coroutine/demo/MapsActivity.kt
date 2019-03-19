package dev.eastar.coroutine.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private var d: Disposable? = null

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Dokdo, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Dokdo and move the camera
        val dokdo = LatLng(37.0, 132.0)
        val coex = LatLng(37.512, 127.058)

        mMap.addMarker(MarkerOptions().position(dokdo).title("Marker in Dokdo").visible(true))
        mMap.addMarker(MarkerOptions().position(coex).title("Marker in Coex").visible(true))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dokdo, 5f))

        d = Observable.interval(1L, TimeUnit.SECONDS)
                .map { arrayOf(dokdo, coex)[(it % 2).toInt()] }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mMap.animateCamera(CameraUpdateFactory.newLatLng(it)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        d?.dispose()
    }

}
