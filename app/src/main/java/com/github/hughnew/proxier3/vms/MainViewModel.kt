package com.github.hughnew.proxier3.vms

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel

class MainViewModel():ViewModel() {
    var proxyStatus = false
    var ProxyOnDrawable: Drawable? = null
    var ProxyOffDrawable: Drawable? = null
}