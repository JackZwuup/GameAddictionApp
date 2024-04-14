package com.example.capstoneassignmentjack

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi

class FloatWidgetService : Service() {
    // WindowManager to add and remove the floating widget.
    private lateinit var mWindowManager: WindowManager
    // View representing the floating widget
    private lateinit var mFloatingWidget: View
    // TextView inside the floating widget to display text.
    private lateinit var textView: TextView
    // RelativeLayout inside the floating widget for layout purposes.
    private lateinit var relativeLayout: RelativeLayout

    // onCreate method to initialize the floating widget.
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        // Inflate the layout for the floating widget.
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)
        // Initialize the textView and relativeLayout.
        textView = mFloatingWidget.findViewById(R.id.textView)
        relativeLayout = mFloatingWidget.findViewById(R.id.relativeLayout)

        // Define the layout parameters for the floating widget based on Android version.
        val params: WindowManager.LayoutParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        // Initialize the WindowManager.
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        // Add the floating widget to the WindowManager.
        mWindowManager.addView(mFloatingWidget, params)
    }

    // Method to update the text displayed in the floating widget.
    fun updateText(text: String) {
        // Ensure that mFloatingWidget is initialized before accessing its children.
        if (::mFloatingWidget.isInitialized) {
            // Check if textView is initialized.
            if (::textView.isInitialized) {
                textView.text = text
            } else {
                // If textView is not initialized, try to initialize it.
                textView = mFloatingWidget.findViewById(R.id.textView)
                textView.text = text
            }
        } else {
            // If mFloatingWidget is not initialized, log an error.
            Log.e("FloatWidgetService", "mFloatingWidget is not initialized")
        }
    }

    // Method to update the background color of the floating widget.
    fun updateColor(color: Drawable) {
        // Ensure that mFloatingWidget is initialized before accessing its children.
        if (::mFloatingWidget.isInitialized) {
            // Check if relativeLayout is initialized
            if (::relativeLayout.isInitialized) {
                relativeLayout.background = color
            } else {
                // If relativeLayout is not initialized, try to initialize it.
                relativeLayout = mFloatingWidget.findViewById(R.id.relativeLayout)
                relativeLayout.background = color
            }
        } else {
            // If mFloatingWidget is not initialized, log an error.
            Log.e("FloatWidgetService", "mFloatingWidget is not initialized")
        }
    }

    // Method to update the size of the floating widget.
    fun updateSize(padding: Int) {
        // Ensure that mFloatingWidget is initialized before accessing its children.
        if (::mFloatingWidget.isInitialized) {
            // Check if relativeLayout is initialized.
            if (::relativeLayout.isInitialized) {
                relativeLayout.setPadding(padding, padding, padding, padding)
            } else {
                // If relativeLayout is not initialized, try to initialize it.
                relativeLayout = mFloatingWidget.findViewById(R.id.relativeLayout)
                relativeLayout.setPadding(padding, padding, padding, padding)
            }
        } else {
            // If mFloatingWidget is not initialized, log an error.
            Log.e("FloatWidgetService", "mFloatingWidget is not initialized")
        }
    }

    // onDestroy method to remove the floating widget when the service is destroyed.
    override fun onDestroy() {
        super.onDestroy()
        // Check if mFloatingWidget is initialized before removing it.
        if (::mFloatingWidget.isInitialized) mWindowManager.removeView(mFloatingWidget)
    }

    // Binder class to provide a reference to the service.
    inner class FloatWidgetServiceBinder : Binder() {
        fun getService(): FloatWidgetService = this@FloatWidgetService
    }

    // Binder instance for clients to interact with the service.
    val binder = FloatWidgetServiceBinder()

    // onBind method to return the binder instance when the service is bound.
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    // onUnbind method to remove the floating widget when the service is unbound.
    override fun onUnbind(intent: Intent): Boolean {
        // Check if mFloatingWidget is initialized before removing it.
        if (::mFloatingWidget.isInitialized) mWindowManager.removeView(mFloatingWidget)
        return super.onUnbind(intent)
    }
}