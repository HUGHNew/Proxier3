package com.github.hughnew.proxier3.models

data class Proxy(val ip:String, val port:Int) {
    companion object{
        @JvmField val Disable = Proxy("",0)
    }

    override fun toString(): String {
        return "$ip:$port"
    }
}