package com.example.feedback3_2

import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.feedback3_2.databinding.ActivityMainBinding
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private  lateinit var binding : ActivityMainBinding
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private val RECORD_AUDIO_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartRecording.setOnClickListener{
            checkAndRequestPermissions()
        }
        binding.btnStopRecording.setOnClickListener{
            stopRecording()
        }
    }

    private fun checkAndRequestPermissions() {
        val permission = android.Manifest.permission.RECORD_AUDIO

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), RECORD_AUDIO_PERMISSION_CODE)
        } else {
            startRecording()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startRecording()
                }
                return
            }
        }
    }

    private fun startRecording() {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "audio_recording.mp4")
        val filePath = file.absolutePath
        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder(this)
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(filePath)

            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false
    }
}