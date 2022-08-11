package com.github.hughnew.proxier3.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import com.github.hughnew.proxier3.R
import com.github.hughnew.proxier3.databinding.ActivityMainBinding
import com.github.hughnew.proxier3.extensions.makeSnack

class MainActivity : AppCompatActivity() {

    // region binding and results
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        checkPermission()
        setContentView(binding.root)
        setUpDrawables()
        setUpViews()
    }

    private fun checkPermission() {
        when (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS)) {
            PackageManager.PERMISSION_DENIED -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_title_special_permissions))
                    .setMessage(getString(R.string.dialog_message_special_permissions))
                    .setCancelable(false)
                    .show()
            }
            PackageManager.PERMISSION_GRANTED -> {
                Log.w(TAG, "SECURE_WRITE granted")
            }
        }
    }

    private fun setUpDrawables() {
        if (ProxyOnDrawable == null) {
            ProxyOnDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_proxy_pause_96, null
            )
        }
        if (ProxyOffDrawable == null) {
            ProxyOffDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_proxy_resume_96, null
            )
        }
    }

    private fun setUpViews() {
        binding.ivLauncher.setOnClickListener {
            if (proxyStatus) {
                disableProxy()
                it.makeSnack("Proxy: turned off")
                binding.tvProxy.visibility = View.INVISIBLE
            } else {
                // TODO: Dialog to get
                val ip = "127.0.0.1"
                val port = 12345
                val result = enableProxy(ip, port) // TODO: check success
                it.makeSnack("Proxy: turned on")
                binding.tvProxy.run {
                    visibility = View.VISIBLE
                    text = getString(R.string.setting_current_proxy,ip,port) // TODO: apply span
                }
            }
            proxyStatus = !proxyStatus
            switchLauncher(proxyStatus)
        }
        detectProxy().apply {
            proxyStatus = this
            switchLauncher(proxyStatus)
        }
    }

    private fun switchLauncher(turnOn:Boolean) {
        binding.ivLauncher.setImageDrawable(
            if (turnOn) ProxyOnDrawable else ProxyOffDrawable
        )
    }
    // TODO: move to helper
    private fun detectProxy(): Boolean {
        val proxy = Settings.Global.getString(contentResolver, Settings.Global.HTTP_PROXY)
        Log.w(TAG, "proxy:${proxy ?: "empty"}")
        return false
    }

    private fun enableProxy(ip: String, port: Int): Boolean {
        require(port in ValidPortRange)
        return Settings.Global.putString(contentResolver, Settings.Global.HTTP_PROXY, "$ip:$port")
    }
    private fun disableProxy(): Boolean =
        Settings.Global.putString(contentResolver, Settings.Global.HTTP_PROXY, ":0")

    companion object {
        var proxyStatus = false
        const val TAG = "Proxier"
        var ProxyOnDrawable: Drawable? = null
        var ProxyOffDrawable: Drawable? = null
        val ValidPortRange = 1025..65535
    }
}