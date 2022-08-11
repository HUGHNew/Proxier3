package com.github.hughnew.proxier3.extensions

import android.content.ContentResolver
import android.provider.Settings
import com.github.hughnew.proxier3.models.Proxy

fun ContentResolver.detectProxy(): Proxy {
    with(Settings.Global.getString(this, Settings.Global.HTTP_PROXY)){
        return when (this){
            null, ":0" -> Proxy.Disable
            else -> indexOf(':').run {
                Proxy(substring(0,this), substring(this+1).toInt())
            }
        }
    }
}

fun ContentResolver.enableProxy(proxy: Proxy): Boolean {
    return Settings.Global.putString(this, Settings.Global.HTTP_PROXY, proxy.toString())
}
fun ContentResolver.disableProxy(): Boolean =
    enableProxy(Proxy.Disable)