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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.capstoneassignmentjack.constants.Ints.SIZE_BIG
import com.example.capstoneassignmentjack.constants.Ints.SIZE_MEDIUM
import com.example.capstoneassignmentjack.constants.Ints.SIZE_SMALL
import com.example.capstoneassignmentjack.constants.Strings.BIG
import com.example.capstoneassignmentjack.constants.Strings.BLUE
import com.example.capstoneassignmentjack.constants.Strings.GREEN
import com.example.capstoneassignmentjack.constants.Strings.HEX_BLUE
import com.example.capstoneassignmentjack.constants.Strings.HEX_GREEN
import com.example.capstoneassignmentjack.constants.Strings.HEX_RED
import com.example.capstoneassignmentjack.constants.Strings.MEDIUM
import com.example.capstoneassignmentjack.constants.Strings.RED
import com.example.capstoneassignmentjack.constants.Strings.SMALL
import com.example.level5task1.viewmodel.AddictionViewModel

private var globalColor = HEX_BLUE

private var globalSize = SIZE_MEDIUM

// Composable function for the Popup Layout Screen.
@Composable
fun PopupLayoutScreen(
    viewModel: AddictionViewModel,
    context: Context
) {
    // Observe recentPopupSettings LiveData.
    val recentPopupSettings by viewModel.getRecentPopup.observeAsState()

    // Mutable state for the FloatWidgetService instance.
    var floatService: FloatWidgetService? by remember { mutableStateOf(null) }

    // Mutable state for checking if the service is bound.
    var isServiceBound by remember { mutableStateOf(false) }

    // Service connection to bind to the FloatWidgetService.
    val floatServiceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                // Get the FloatWidgetService instance.
                floatService = (service as FloatWidgetService.FloatWidgetServiceBinder).getService()

                val floatWidgetService = (service).getService()

                // Parse color.
                val colorParsed = Color.parseColor(recentPopupSettings?.color)
                val getHexColor = ColorDrawable(colorParsed)

                // Call ViewModel methods to update FloatWidgetService settings.
                viewModel.setFloatWidgetService(floatWidgetService)
                recentPopupSettings?.text?.let { viewModel.updateFloatWidgetText(it) }
                viewModel.updateFloatWidgetColor(getHexColor)
                recentPopupSettings?.size?.let { viewModel.updateFloatWidgetSize(it) }

                // Mark the service as bound.
                isServiceBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                floatService = null

                // Mark the service as unbound.
                isServiceBound = false
            }
        }
    }

    // Check if the service is registered before accessing it.
    if (!isServiceBound) {
        Log.d("It isn't bound", "Service disconnected")
    } else {
        // Bind to the service.
        val serviceIntent = Intent(context, FloatWidgetService::class.java)
        context.bindService(serviceIntent, floatServiceConnection, Context.BIND_AUTO_CREATE)
    }

    // DisposableEffect to unbind the service when the composable is disposed.
    DisposableEffect(Unit) {
        val serviceIntent = Intent(context, FloatWidgetService::class.java)
        context.bindService(serviceIntent, floatServiceConnection, Context.BIND_AUTO_CREATE)
        onDispose {
            context.unbindService(floatServiceConnection)
        }
    }

    // Column to arrange UI components vertically.
    Column(
        modifier = Modifier.padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // List of color options.
        val colorOptions = listOf(BLUE, RED, GREEN)

        // List of size options.
        val sizeOptions = listOf(BIG, MEDIUM, SMALL)

        // Mutable state for selected color and size.
        var selectedColor by remember { mutableStateOf(colorOptions[0]) }
        var selectedSize by remember { mutableStateOf(sizeOptions[0]) }

        // Row to arrange size radio buttons horizontally.
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            // Iterate over size options.
            sizeOptions.forEach { size ->
                Column {
                    // Radio button for each size option.
                    RadioButton(
                        selected = (size == selectedSize),
                        onClick = { selectedSize = size }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // Text displaying size option.
                    Text(text = size)
                }
            }
        }

        // Button to apply selected size.
        Button(onClick = {
            // Update FloatWidgetService size and insert data into the database.
            viewModel.updateFloatWidgetSize(differentSize(selectedSize))
            setSize(differentSize(selectedSize))
            insertInDatabase(viewModel)
        }) {
            // Text on the apply size button.
            Text(
                stringResource(id = R.string.applysize)
            )
        }

        // Row to arrange color radio buttons horizontally.
        Row(modifier = Modifier.padding(top = 300.dp)) {
            // Iterate over color options.
            colorOptions.forEach { color ->
                Column {
                    // Radio button for each color option.
                    RadioButton(
                        selected = (color == selectedColor),
                        onClick = { selectedColor = color }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // Text displaying color option.
                    Text(text = color)
                }
            }
        }

        // Button to apply selected color
        Button(onClick = {
            // Update global color, parse it, update FloatWidgetService color, and insert data into the database.
            setColor(differentColor(selectedColor))

            val colorParsed = Color.parseColor(getColor())
            val getHexColor = ColorDrawable(colorParsed)

            viewModel.updateFloatWidgetColor(getHexColor)
            insertInDatabase(viewModel)
        }) {
            // Text on the apply color button.
            Text(
                stringResource(id = R.string.applycolor)
            )
        }
    }
}

// Function to determine the color based on the selected color string.
fun differentColor(color: String): String {
    var hexColor = HEX_BLUE
    // Convert hex color string to Color object.
    when (color) {
        RED -> {
            hexColor = HEX_RED
        }

        BLUE -> {
            hexColor = HEX_BLUE
        }

        GREEN -> {
            hexColor = HEX_GREEN
        }
    }
    return hexColor
}

// Function to determine the size based on the selected size string.
fun differentSize(size: String): Int {
    var intSize = 0

    when (size) {
        SMALL -> {
            intSize = SIZE_SMALL
        }

        MEDIUM -> {
            intSize = SIZE_MEDIUM
        }

        BIG -> {
            intSize = SIZE_BIG
        }
    }
    return intSize
}

// Function to set the global color value.
fun setColor(color: String) {
    globalColor = color
}

// Function to get the global color value.
fun getColor(): String {
    return globalColor
}

// Function to set the global size value.
fun setSize(size: Int) {
    globalSize = size
}

// Function to get the global size value.
fun getSize(): Int {
    return globalSize
}