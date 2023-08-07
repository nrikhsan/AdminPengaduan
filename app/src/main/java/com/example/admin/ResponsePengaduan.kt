package com.example.admin

data class ResponsePengaduan(
    var pengaduId : String? = null,
    var nik : String? = null,
    var catatan : String? = null,
    var konfirmasi : Boolean = false,
    var tanggal : String? = null
)
