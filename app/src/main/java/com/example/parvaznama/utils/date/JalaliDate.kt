package com.example.parvaznama.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class JalaliDate(miladiDate: String) {

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(miladiDate, formatter)

        // تبدیل میلادی به شمسی
        val jd = gregorianToJalali(date.year, date.monthValue, date.dayOfMonth)
        year = jd[0]
        month = jd[1]
        day = jd[2]
    }

    fun asString(): String = "$year/$month/$day"

    // الگوریتم جدید تبدیل میلادی به شمسی
    private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): IntArray {
        val g_d_m = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        var jy: Int
        var jm: Int
        var jd: Int
        var gy2 = gy - 1600
        var gm2 = gm - 1
        var gd2 = gd - 1

        var g_day_no = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400
        g_day_no += g_d_m[gm2] + gd2
        if (gm2 > 1 && ((gy % 4 == 0 && gy % 100 != 0) || gy % 400 == 0)) g_day_no++

        var j_day_no = g_day_no - 79
        val j_np = j_day_no / 12053
        j_day_no %= 12053

        jy = 979 + 33 * j_np + 4 * (j_day_no / 1461)
        j_day_no %= 1461

        if (j_day_no >= 366) {
            jy += (j_day_no - 1) / 365
            j_day_no = (j_day_no - 1) % 365
        }

        val jm_arr = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
        var i = 0
        while (i < 12 && j_day_no >= jm_arr[i]) {
            j_day_no -= jm_arr[i]
            i++
        }
        jm = i + 1
        jd = j_day_no + 1
        return intArrayOf(jy, jm, jd)
    }}
