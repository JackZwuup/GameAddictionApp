package com.example.capstoneassignmentjack.ui.screens.popupscreens

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.IBinder
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.capstoneassignmentjack.FloatWidgetService
import com.example.capstoneassignmentjack.R
import com.example.level5task1.viewmodel.AddictionViewModel
import kotlinx.coroutines.flow.MutableStateFlow

private var globalText = ""
val changeTheText: MutableStateFlow<String> = MutableStateFlow("")

@Composable
fun PopupTextScreen(
    viewModel: AddictionViewModel,
    context: Context
) {
    // Observe recentPopupSettings LiveData.
    val recentPopupSettings by viewModel.getRecentPopup.observeAsState()

    // Collect textState from MutableStateFlow
    val textState by changeTheText.collectAsState()

    // Variables for managing FloatWidgetService
    var floatService: FloatWidgetService? by remember { mutableStateOf(null) }
    var isServiceBound by remember { mutableStateOf(false) }

    // Service connection for FloatWidgetService
    val floatServiceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                floatService = (service as FloatWidgetService.FloatWidgetServiceBinder).getService()
                val floatWidgetService = (service).getService()

                // Parse color and update service settings
                val colorParsed = Color.parseColor(recentPopupSettings?.color)
                val getHexColor = ColorDrawable(colorParsed)
                viewModel.setFloatWidgetService(floatWidgetService)
                recentPopupSettings?.text?.let { viewModel.updateFloatWidgetText(it) }
                viewModel.updateFloatWidgetColor(getHexColor)
                recentPopupSettings?.size?.let { viewModel.updateFloatWidgetSize(it) }

                // Mark the service as bound
                isServiceBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                floatService = null
                // Mark the service as unbound
                isServiceBound = false
            }
        }
    }

    // Check if the service is registered before accessing it.
    if (!isServiceBound) {
        Log.d("Floating widget", "Not bound")
    } else {
        // Bind to the service
        val serviceIntent = Intent(context, FloatWidgetService::class.java)
        context.bindService(serviceIntent, floatServiceConnection, Context.BIND_AUTO_CREATE)
    }

    // Dispose the service connection when the composable is removed from the composition.
    DisposableEffect(Unit) {
        val serviceIntent = Intent(context, FloatWidgetService::class.java)
        context.bindService(serviceIntent, floatServiceConnection, Context.BIND_AUTO_CREATE)
        onDispose {
            context.unbindService(floatServiceConnection)
        }
    }

    // Column layout for the popup text screen.
    Column(
        modifier = Modifier.padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Button to stop the service.
        Button(onClick = {
            if (floatService != null) {
                val unbindIntent = Intent(context, FloatWidgetService::class.java)
                context.unbindService(floatServiceConnection)
                context.stopService(unbindIntent)
                floatService = null // Reset the reference to the service.
            }
        }) {
            Text(
                stringResource(id = R.string.stopservice)
            )
        }

        // Button to start the service.
        Button(onClick = {
            viewModel.updateFloatWidgetText(textState)
            setText(textState)
            insertInDatabase(viewModel)
        }) {
            Text(
                stringResource(id = R.string.startservice)
            )
        }

        // TextField to enter text.
        TextField(
            value = textState,
            onValueChange = { changeTheText.value = it },
            modifier = Modifier.weight(1f)
        )

        // Button to change text.
        Button(onClick = {
            viewModel.updateFloatWidgetText(textState)
            setText(textState)
            insertInDatabase(viewModel)
        }) {
            Text(
                stringResource(id = R.string.changetext)
            )
        }
    }
}

// Function to set the text, because I did the sizes, colours and text on different screens, so I can't put te data inside the database all at once.
fun setText(text: String) {
    globalText = text
}

// Function to get the text.
fun getText(): String {
    return globalText
}