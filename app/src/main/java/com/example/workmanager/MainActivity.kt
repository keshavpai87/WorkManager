package com.example.workmanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
//        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>().build()

        val workManager =  WorkManager.getInstance(applicationContext)

        val data : Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        val constraints = Constraints.Builder()

            // Only when the device is in charging state
            .setRequiresCharging(true)

            // Only when the device is connected to a network
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        /*val filterRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()

        val compressRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()*/

        workManager.enqueue(uploadRequest)
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                findViewById<TextView>(R.id.textView).text = it?.state?.name
                if (it != null) {
                    if (it.state.isFinished) {
                        val data = it.outputData
                        val message = data.getString(UploadWorker.KEY_WORKER)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}