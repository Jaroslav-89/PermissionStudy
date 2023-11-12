package com.example.permissionstudy

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.permissionstudy.databinding.ActivityMainBinding

const val CAMERA_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionProvided = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permissionProvided == PackageManager.PERMISSION_GRANTED) {
            // Всё хорошо, разрешение предоставлено!
            binding.permissionRequestFrame.visibility = View.GONE
            binding.permissionGranted.visibility = View.VISIBLE
        } else if (permissionProvided == PackageManager.PERMISSION_DENIED) {
            // Запрашиваем разрешение
            binding.permissionRequestFrame.visibility = View.VISIBLE
            binding.permissionGranted.visibility = View.GONE
        }



        binding.permissionRequestFrame.setOnClickListener {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != CAMERA_REQUEST_CODE) {
            Log.e("PermissionActivity", "Пришел результат не с тем requestCode, который ожидался")
            return
        }
        val cameraPermissionIndex = permissions.indexOf(android.Manifest.permission.CAMERA)
        val permissionProvided = grantResults[cameraPermissionIndex]
        if (permissionProvided == PackageManager.PERMISSION_GRANTED) {
            // Пользователь дал разрешение, можно продолжать работу
            binding.permissionRequestFrame.visibility = View.GONE
            binding.permissionGranted.visibility = View.VISIBLE
        } else if (permissionProvided == PackageManager.PERMISSION_DENIED) {
            // Пользователь отказал в предоставлении разрешения
            binding.permissionRequestFrame.visibility = View.VISIBLE
            binding.permissionGranted.visibility = View.GONE
            if(count >= 3){
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data= Uri.fromParts("package", applicationContext.packageName, null)
                applicationContext.startActivity(intent)
            } else {
                count++
            }
        }
    }
}