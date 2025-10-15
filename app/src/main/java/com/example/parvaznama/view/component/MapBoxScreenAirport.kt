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
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapBoxScreenAirport(
    long: Double = 0.1, lat: Double = 0.1
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }


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

    LaunchedEffect(long,lat) {
        mapView.getMapboxMap().loadStyleUri(Style.SATELLITE_STREETS) { style ->
            val skyLayer = SkyLayer("sky-layer").skyType(SkyType.ATMOSPHERE)
                .skyAtmosphereSun(listOf(0.0, 90.0)).skyAtmosphereSunIntensity(10.0)
            style.addLayer(skyLayer)



            val bitmap = getBitmapFromVectorDrawable(context, R.drawable.ico_loc)
            style.addImage("airport-icon", bitmap)

            val point = Point.fromLngLat(long, lat)
            mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                    .center(point)
                    .zoom(8.0)
                    .build()
            )

            mapView.getMapboxMap().flyTo(
                cameraOptions = CameraOptions.Builder()
                    .center(point)
                    .zoom(14.0)
                    .build(),
                animationOptions = com.mapbox.maps.plugin.animation.MapAnimationOptions.mapAnimationOptions {
                    duration(1500) // زمان انیمیشن 1.5 ثانیه
                }
            )
            val annotationManager = mapView.annotations.createPointAnnotationManager()
            val circleManager = mapView.annotations.createCircleAnnotationManager()
            annotationManager.annotations.forEach { annotationManager.delete(it) } // حذف مارکر قبلی
            // اضافه کردن دایره کوچک

            annotationManager.create(
                PointAnnotationOptions().withPoint(point).withIconImage("airport-icon")
                    .withIconSize(1.0)
            )
            circleManager.create(
                com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions()
                    .withPoint(point)
                    .withCircleRadius(80.0) // شعاع دایره به پیکسل
                    .withCircleColor("#FF0000") // رنگ قرمز
                    .withCircleOpacity(0.1)
            )

        }
    }

    AndroidView(
        factory = { mapView }, modifier = Modifier.fillMaxSize()
    )
}


