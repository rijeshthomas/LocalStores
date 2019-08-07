package com.example.localstores

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_items.*
import kotlinx.android.synthetic.main.app_bar_navigation.header_toolbar
import java.io.IOException
import java.util.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {
    private val tag = this::class.simpleName
    private var locationManager: LocationManager? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationManager: LocationManager? = null
    var imageButton: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.header_toolbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setTitle(null);


        val recyclerView = findViewById(R.id.listItems) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        //crating an arraylist to store users using the data class user
        val users = ArrayList<StoreInfo>()


        //adding some dummy data to the list
        users.add(
            StoreInfo(
                "Green Land Supermarket",
                "Green Glen Layout, Bellandur",
                "https://greenland-prod-images.s3.amazonaws.com/pineapple.jpg"
            )
        )
        users.add(
            StoreInfo(
                "MK Express",
                "Green Glen Layout, Bellandur",
                "https://greenland-prod-images.s3.amazonaws.com/pineapple.jpg"
            )
        )
        users.add(
            StoreInfo(
                "DS Supermarket",
                "Green Glen Layout, Bellandur ",
                "https://greenland-prod-images.s3.amazonaws.com/pineapple.jpg"
            )
        )
        users.add(
            StoreInfo(
                "Reliance Retail",
                "Green Glen Layout, Bellandur",
                "https://greenland-prod-images.s3.amazonaws.com/pineapple.jpg"
            )
        )

        var cnt = users.size.toString()
        Log.i(tag, "New Count $cnt")

        //creating our adapter
        val adapter = StoreViewAdapter(users)



        Toast.makeText(this, "hi", Toast.LENGTH_LONG).show()

        //Navigation bar code region
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, header_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
        //location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
        imageButton = findViewById(R.id.imageView2)
        imageButton?.setOnClickListener {
            Toast.makeText(this@HomeActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            try {
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this, permissions, 0)
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);


                mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()

                mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            } catch (ex: SecurityException) {
                Toast.makeText(this@HomeActivity, "Security Exception, no location available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            searchEditText.setText("" + location.longitude.toString() + ":" + location.latitude.toString());
            getAddress(location);

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onLocationChanged(location: Location) {

        val msg = "Updated Location: " +
                java.lang.Double.toString(location.latitude) + "," +
                java.lang.Double.toString(location.longitude)

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
        val latLng = LatLng(location.latitude, location.longitude)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onConnectionSuspended(i: Int) {

        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun handleSelection(message: String) {

    }

    fun getAddress(location: Location) {
        var errorMessage = ""
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault());
        var addresses: List<Address> = emptyList()
        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                // In this sample, we get just a single address.
                1
            )

            //Toast.makeText(this,addresses[0].locality?.toString() + addresses[0].getAddressLine(0).toString()
            //), Toast.LENGTH_SHORT).show()
            searchEditText.setText("" + location.longitude.toString() + ":" + location.latitude.toString());

            val address = addresses[0]
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            // searchEditText.setText(addressFragments.joinToString(separator = "\n"))
            //searchEditText.setText(addressFragments.joinToString(separator = "\n"))

        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available)

        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used)

        }
    }
}
