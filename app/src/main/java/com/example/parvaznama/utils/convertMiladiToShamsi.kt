package com.example.parvaznama.utils

import android.os.Build
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
fun convertMiladiToShamsi(miladi:String):String{
    val jd= JalaliDate(miladi)
    return jd.asString()

}