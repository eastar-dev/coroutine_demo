@file:Suppress("SpellCheckingInspection")

package dev.eastar.coroutine.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dev.eastar.coroutine.demo.databinding.ActivityMapsBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.concurrent.TimeUnit

typealias Position = Pair<LatLng, Marker>

@Suppress("SpellCheckingInspection")
val Position.latlon
    get() = first
val Position.maker get() = second

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var vm: MapsViewModel
    private lateinit var bb: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(MapsViewModel::class.java)
        bb = DataBindingUtil.setContentView<ActivityMapsBinding>(this, R.layout.activity_maps)
                .apply {
                    lifecycleOwner = this@MapsActivity
                    viewModel = vm
                }


        runBlocking.setOnClickListener { finish() }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private var d: Disposable? = null

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Dokdo, Korea.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("CheckResult")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Dokdo and move the camera
        val dokdo = LatLng(37.2426732, 131.8635432)
        val coex = LatLng(37.512, 127.058)
        val makerOfDokdo = mMap.addMarker(MarkerOptions().position(dokdo).title("Dokdo").visible(true))
        val makerOfCodx = mMap.addMarker(MarkerOptions().position(coex).title("Coex").visible(true))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dokdo, 5f))

        d = Observable.zip<Position, Long, Position>(
                Observable.just(dokdo to makerOfDokdo, coex to makerOfCodx),
                Observable.interval(1, TimeUnit.SECONDS).take(2),
                BiFunction { position, _ -> position })
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { position ->
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(position.latlon))
                    position.maker.showInfoWindow()
                }

    }

    override fun onDestroy() {
        super.onDestroy()
        d?.dispose()
    }

}
