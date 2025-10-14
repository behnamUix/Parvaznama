package com.example.parvaznama.utils

fun convertEngToPerStats(status: String?): String {
    var result = when (status) {
        "scheduled" -> "برنامه ریزی شده"
        "active" -> "در حال انجام"
        "landed" -> "فرود آمده"
        "cancelled" -> "لغد شده"
        "incident" -> "حادثه دیده"
        "diverted" -> "تغییر مسیر داده"
        else -> "نامعلوم"
    }
    return result
}