package com.example.capstoneassignmentjack

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class MusicServiceConnection(private val onServiceConnected: (MusicPlayerService) -> Unit) :
    ServiceConnection {
    private var boundService: MusicPlayerService? = null

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicPlayerService.MusicBinder
        boundService = binder.getService()
        onServiceConnected(boundService!!)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        boundService = null
    }
}