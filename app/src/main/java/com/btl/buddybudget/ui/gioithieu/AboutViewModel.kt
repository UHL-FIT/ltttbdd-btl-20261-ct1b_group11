package com.btl.buddybudget.ui.gioithieu

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.dto.BackupData
import com.btl.buddybudget.data.repo.Repo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AboutViewModel(private val repo: Repo) : ViewModel() {

    private val _showImportConfirm = MutableStateFlow(false)
    val showImportConfirm: StateFlow<Boolean> = _showImportConfirm.asStateFlow()

    private var pendingImportUri: Uri? = null

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun onImportFileSelected(uri: Uri) {
        pendingImportUri = uri
        _showImportConfirm.value = true
    }

    fun dismissImportDialog() {
        _showImportConfirm.value = false
        pendingImportUri = null
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun confirmImport(context: Context) {
        val uri = pendingImportUri ?: return
        _showImportConfirm.value = false
        importFromJson(context, uri)
    }

    fun exportToJson(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val backup = repo.getBackupData()
                val jsonString = Gson().toJson(backup)

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
                _toastMessage.value = "Xuất dữ liệu thành công!"
            } catch (e: Exception) {
                e.printStackTrace()
                _toastMessage.value = "Lỗi khi xuất dữ liệu"
            }
        }
    }

    private fun importFromJson(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val stringBuilder = StringBuilder()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line)
                        }
                    }
                }

                val backup = Gson().fromJson(stringBuilder.toString(), BackupData::class.java)
                repo.restoreBackupData(backup)
                _toastMessage.value = "Khôi phục dữ liệu thành công!"
            } catch (e: Exception) {
                e.printStackTrace()
                _toastMessage.value = "Khôi phục thất bại hoặc file không hợp lệ"
            }
        }
    }
}
