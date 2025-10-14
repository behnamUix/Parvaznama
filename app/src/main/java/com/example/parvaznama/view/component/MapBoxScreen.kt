package com.example.parvaznama.view.component

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.parvaznama.R
import com.example.parvaznama.utils.getBitmapFromVectorDrawable
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.FillExtrusionLayer
import com.mapbox.maps.extension.style.layers.generated.SkyLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.SkyType
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.RasterDemSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.terrain.generated.Terrain
import com.mapbox.maps.extension.style.terrain.generated.setTerrain
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location

@SuppressLint("MissingPermission")
@Composable
fun MapBoxScreen(
    Ulocation: Boolean = true,
    long1: Double = 0.1,
    lat1: Double = 0.1,
    long2: Double = 0.1,
    lat2: Double = 0.1
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

    var userLocation by remember { mutableStateOf<Point?>(null) }
    var granted by remember { mutableStateOf(false) }

    // کنترل Lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    // درخواست مجوز لوکیشن
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> granted = isGranted }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (!granted) return

    // گرفتن لوکیشن کاربر
    LaunchedEffect(granted) {
        getUserLocation(context) { loc -> userLocation = loc }
    }

    // بارگذاری استایل و تنظیم مسیر و مارکرها
    LaunchedEffect(userLocation) {
        mapView.getMapboxMap().loadStyleUri(Style.STANDARD_SATELLITE) { style ->

            val demSource = RasterDemSource.Builder("terrain-source")
                .url("mapbox://mapbox.mapbox-terrain-dem-v1")
                .tileSize(512)
                .build()
            style.addSource(demSource)
            style.setTerrain(Terrain("terrain-source"))

            val skyLayer = SkyLayer("sky-layer")
                .skyType(SkyType.ATMOSPHERE)
                .skyAtmosphereSun(listOf(0.0, 90.0))
                .skyAtmosphereSunIntensity(10.0)
            style.addLayer(skyLayer)

            // 4️⃣ فعال‌کردن ساختمان‌های سه‌بعدی
            val buildingLayer = FillExtrusionLayer("3d-buildings", "composite")
                .sourceLayer("building")
                .filter(Expression.eq(Expression.get("extrude"), Expression.literal(true)))
                .minZoom(15.0)
                .fillExtrusionColor("#aaa")
                .fillExtrusionHeight(Expression.get("height"))
                .fillExtrusionBase(Expression.get("min_height"))
                .fillExtrusionOpacity(0.8)
            style.addLayer(buildingLayer)
            // افزودن آیکون
            val bitmap = getBitmapFromVectorDrawable(context, R.drawable.icon_gps)
            style.addImage("airport-icon", bitmap)

            // اگر لوکیشن کاربر فعال است، استایل ماهواره‌ای بگذار
            if (Ulocation) {

            }

            // تعریف مبدا و مقصد
            val origin = Point.fromLngLat(long1, lat1)
            val destination = Point.fromLngLat(long2, lat2)

            // ساخت مسیر بین دو نقطه
            val lineString = LineString.fromLngLats(listOf(origin, destination))
            style.addSource(
                geoJsonSource("flight-route") { geometry(lineString) }
            )

            // لایه مسیر
            style.addLayer(
                lineLayer("flight-line", "flight-route") {
                    lineDasharray(listOf(2.0, 2.0))
                    lineColor("#F44336")
                    lineCap(LineCap.ROUND)
                    lineWidth(3.0)
                }
            )

            // فعال‌سازی نمایش موقعیت کاربر
            mapView.location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }

            // افزودن Marker برای مبدا و مقصد
            val annotationManager = mapView.annotations.createPointAnnotationManager()
            listOf(origin, destination).forEach { point ->
                annotationManager.create(
                    PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage("airport-icon")
                        .withIconSize(1.5)
                )
            }

            // تنظیم دوربین روی موقعیت کاربر یا مبدا
            val targetPoint = if (Ulocation) userLocation else origin
            targetPoint?.let {
                mapView.getMapboxMap().setCamera(
                    CameraOptions.Builder()
                        .center(it)
                        .pitch(if (Ulocation) 60.0 else 0.0)
                        .zoom(if (Ulocation) 8.0 else 4.0)
                        .build()
                )
            }
        }
    }

    // نمایش نقشه در Compose
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}

@SuppressLint("MissingPermission")
fun getUserLocation(context: Context, onLocation: (Point) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocation(Point.fromLngLat(it.longitude, it.latitude))
        }
    }
}
