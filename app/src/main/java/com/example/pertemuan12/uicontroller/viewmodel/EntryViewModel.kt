package com.example.pertemuan12.uicontroller.viewmodel



data class UIStateSiswa(
    val detailsSiswa: DetailSiswa = DetailSiswa(),
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null
)

data class DetailSiswa(
    val id: Int = 0,
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)


