package com.example.pertemuan12.uicontroller.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pertemuan12.modeldata.DataSiswa
import com.example.pertemuan12.repositori.RepositoryDataSiswa
import retrofit2.Response

class EntryViewModel(private val repositoryDataSiswa: RepositoryDataSiswa) : ViewModel() {
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailsSiswa): Boolean {
        val valid = with(uiState) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
        return valid
    }

    fun updateUiState(detailsSiswa: DetailSiswa) {
        uiStateSiswa =
            UIStateSiswa(detailsSiswa = detailsSiswa, isEntryValid = validasiInput(detailsSiswa), errorMessage = null)
    }

    suspend fun addSiswa(): Boolean {
        val isValid = validasiInput()
        if (isValid) {
            try {
                val dataSiswa = uiStateSiswa.detailsSiswa.toDataSiswa()
                Log.d("EntryViewModel", "Mengirim data: $dataSiswa")
                val sip: Response<Void> = repositoryDataSiswa.postDataSiswa(dataSiswa)
                Log.d("EntryViewModel", "Response Code: ${sip.code()}, isSuccessful: ${sip.isSuccessful}")
                if (sip.isSuccessful) {
                    return true
                } else {
                    val errorMsg = sip.errorBody()?.string() ?: "Gagal (Kode: ${sip.code()})"
                    Log.e("EntryViewModel", "Error Body: $errorMsg")
                    uiStateSiswa = uiStateSiswa.copy(errorMessage = errorMsg)
                    return false
                }
            } catch (e: Exception) {
                Log.e("EntryViewModel", "Exception", e)
                uiStateSiswa = uiStateSiswa.copy(errorMessage = e.localizedMessage ?: "Terjadi kesalahan jaringan")
                return false
            }
        } else {
            uiStateSiswa = uiStateSiswa.copy(errorMessage = "Mohon lengkapi semua field!")
        }
        return false
    }
}

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

fun DetailSiswa.toDataSiswa(): DataSiswa = DataSiswa(
    id = id,
    nama = nama,
    alamat = alamat,
    telpon = telpon
)

fun DataSiswa.toUiStateSiswa(isEntryValid: Boolean = false): UIStateSiswa = UIStateSiswa(
    detailsSiswa = this.toDetailSiswa(),
    isEntryValid = isEntryValid
)

fun DataSiswa.toDetailSiswa(): DetailSiswa = DetailSiswa(
    id = id,
    nama = nama,
    alamat = alamat,
    telpon = telpon
)
