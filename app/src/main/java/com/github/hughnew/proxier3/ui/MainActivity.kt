package com.github.hughnew.proxier3.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.github.hughnew.proxier3.R
import com.github.hughnew.proxier3.databinding.ActivityMainBinding
import com.github.hughnew.proxier3.extensions.detectProxy
import com.github.hughnew.proxier3.extensions.disableProxy
import com.github.hughnew.proxier3.extensions.makeSnack
import com.github.hughnew.proxier3.models.Proxy
import com.github.hughnew.proxier3.vms.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    // region binding and results
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val vm by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    // endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        checkPermission()
        setContentView(binding.root)
        setUpDrawables()
        setUpViews()
    }

    override fun onResume() {
        super.onResume()
        contentResolver.detectProxy().apply {
            vm.proxyStatus = this.ip.isNotEmpty()
            switchLauncher(this)
        }
    }

    private fun checkPermission() {
        when (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS)) {
            PackageManager.PERMISSION_DENIED -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.dialog_title_special_permissions))
                    .setMessage(getString(R.string.dialog_message_special_permissions))
                    .setCancelable(false)
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }
            PackageManager.PERMISSION_GRANTED -> {
                Log.w(TAG, "SECURE_WRITE granted")
            }
        }
    }

    private fun setUpDrawables() {
        if (vm.ProxyOnDrawable == null) {
            vm.ProxyOnDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_proxy_pause_96, null
            )
        }
        if (vm.ProxyOffDrawable == null) {
            vm.ProxyOffDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_proxy_resume_96, null
            )
        }
    }

    private fun setUpViews() {
        binding.ivLauncher.setOnClickListener {
            if (vm.proxyStatus) {
                it.isEnabled = true
                contentResolver.disableProxy()
                it.makeSnack("Proxy: turned off")
            } else {
                // disable the functionality to set proxy
                it.isEnabled = false
            }
            vm.proxyStatus = !vm.proxyStatus
            switchLauncher(Proxy.Disable)
        }
    }

    private fun switchLauncher(proxy: Proxy) {
        when (proxy.ip.isNotEmpty()) {
            true -> {
                binding.ivLauncher.setImageDrawable(vm.ProxyOnDrawable)
                binding.tvProxy.text = resources.getString(
                    R.string.setting_current_proxy, proxy.ip, proxy.port
                )
            }
            false -> {
                binding.ivLauncher.setImageDrawable(vm.ProxyOffDrawable)
                binding.tvProxy.setText(R.string.setting_no_proxy)
            }
        }
    }

    companion object {
        const val TAG = "Proxier"
    }
}