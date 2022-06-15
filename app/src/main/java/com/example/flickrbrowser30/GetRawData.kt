package com.example.flickrbrowser30

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}
class GetRawData(private val listener: OnDownloadComplete): AsyncTask<String, Void, String>() {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

//    private var listener: MainActivity? = null
//    fun setDownloadCompleteListener(callbackobject: MainActivity) {
//        listener =  callbackobject
//    }

    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG,"doInBackground: Called")
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL Specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }catch (e:Exception) {
            val errorMessage = when(e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "doInBackground: Security Exception : Needs Permission ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "unknown Error: ${e.message}"
                }
            }
            Log.e(TAG,errorMessage)
            return errorMessage
        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG,"onPostExecute: Called")
        listener.onDownloadComplete(result, downloadStatus)
    }
}