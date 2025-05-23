package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.UploadWorker.Companion.KEY_WORKER
import java.text.SimpleDateFormat
import java.util.Date

class DownloadingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        try {
            for (i in 0..3000) {
                Log.i("MYTAG", "Downloading $i")
            }

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            Log.i("MYTAG", "Completed $currentDate")

            return Result.success()
        } catch (e : Exception) {
            return Result.failure()
        }
    }
}