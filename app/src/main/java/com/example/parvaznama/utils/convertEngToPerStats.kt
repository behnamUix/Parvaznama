package com.example.parvaznama.utils

fun convertEngToPerStats(status: String?): String {
    val result = when (status?.lowercase()) {
        "scheduled", "expected" -> "برنامه ریزی شده"
        "active" -> "در حال انجام"
        "landed" -> "فرود آمده"
        "cancelled" -> "لغو شده"
        "incident" -> "حادثه دیده"
        "diverted" -> "تغییر مسیر داده"
        else -> "نامعلوم"
    }
    return result
}