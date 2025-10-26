package com.example.parvaznama.utils

fun convertGrinvichTime(time: String): String {
    if (time.isBlank()) return ""

    // فقط بخش ساعت مثل "16:04Z" رو استخراج می‌کنیم
    val pureTime = time.substringAfter("T").substringBefore("Z").substringAfterLast(" ")

    val newTime = pureTime.split(":")
    if (newTime.size < 2) return time  // ورودی نامعتبر بود

    var h = newTime[0].toInt() + 3
    var m = newTime[1].toInt() + 30

    if (m >= 60) {
        h += m / 60
        m %= 60
    }
    if (h >= 24) {
        h %= 24
    }

    return String.format("%02d:%02d", h, m)
}

