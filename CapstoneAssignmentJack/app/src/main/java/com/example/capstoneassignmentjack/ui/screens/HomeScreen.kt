import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.capstoneassignmentjack.MusicPlayerService
import com.example.capstoneassignmentjack.R
import com.example.level5task1.viewmodel.AddictionViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.capstoneassignmentjack.FloatWidgetService
import com.example.capstoneassignmentjack.MusicServiceConnection
import com.example.capstoneassignmentjack.BottomNavForSomeScreens
import com.example.capstoneassignmentjack.ClickOnRewardIcon
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.capstoneassignmentjack.constants.Ints.HOURS_TO_SECONDS
import com.example.capstoneassignmentjack.constants.Ints.MINUTES_TO_SECONDS
import com.example.capstoneassignmentjack.constants.Ints.THIRTY
import com.example.capstoneassignmentjack.ui.screens.getHours
import com.example.capstoneassignmentjack.ui.screens.getMinutes
import com.example.capstoneassignmentjack.ui.screens.getSeconds
import com.example.capstoneassignmentjack.ui.screens.getVibration

// Define StateFlow objects for timeInSeconds and isTimerRunning.
private val timeInSecondsState: MutableStateFlow<Int> = MutableStateFlow(0)
private val isTimerRunningState: MutableStateFlow<Boolean> = MutableStateFlow(false)

// Declare floatService as a global variable, so it can be changed in the function.
@SuppressLint("StaticFieldLeak")
private var floatService: FloatWidgetService? = null

private var activateDisposable = false

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: AddictionViewModel,
    context: Context
) {
    // Log activation status.
    Log.d("Is activateDisposable true or false?", activateDisposable.toString())
    // Check if activateDisposable is false, then render UI components.
    if (!activateDisposable) {
        ClickOnRewardIcon(navController)
        BottomNavForSomeScreens(navController, viewModel.currentScreen)
    }

    // Calculate total time in seconds.
    val timeSetInSeconds =
        getHours(context) * HOURS_TO_SECONDS + getMinutes(context) * MINUTES_TO_SECONDS + getSeconds(
            context
        )
    // Collect the StateFlow values.
    val timeInSeconds by timeInSecondsState.collectAsState()
    val isTimerRunning by isTimerRunningState.collectAsState()

    // Observe recentPopupSettings LiveData.
    val recentPopupSettings by viewModel.getRecentPopup.observeAsState()

    // Initialize vibrator based on Android version.
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    // Initialize the music service connection.
    var musicService: MusicPlayerService? by remember { mutableStateOf(null) }
    val musicServiceConnection = remember {
        MusicServiceConnection { service ->
            musicService = service
        }
    }

    // Initialize floatServiceConnection and its state.
    var isServiceBound by remember { mutableStateOf(false) }
    val floatServiceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                // Bind to FloatWidgetService.
                floatService = (service as FloatWidgetService.FloatWidgetServiceBinder).getService()
                val floatWidgetService = (service).getService()

                // Parse color.
                val colorParsed = android.graphics.Color.parseColor(recentPopupSettings?.color)
                val getHexColor = ColorDrawable(colorParsed)

                // Call ViewModel methods to update FloatWidgetService settings.
                viewModel.setFloatWidgetService(floatWidgetService)
                recentPopupSettings?.text?.let { viewModel.updateFloatWidgetText(it) }
                viewModel.updateFloatWidgetColor(getHexColor)
                recentPopupSettings?.size?.let { viewModel.updateFloatWidgetSize(it) }

                // Update service binding status.
                isServiceBound = true

                // Set activateDisposable to true.
                activateDisposable = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                // Reset floatService and binding status.
                floatService = null
                isServiceBound = false

                // Set activateDisposable to false.
                activateDisposable = false
            }
        }
    }

    // DisposableEffect to unbind the music service when the composable is disposed.
    DisposableEffect(Unit) {
        val serviceIntent = Intent(context, MusicPlayerService::class.java)
        context.bindService(serviceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE)
        onDispose {
            context.unbindService(musicServiceConnection)
        }
    }

    // LaunchedEffect to update the timer.
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000L)
            // Update timeInSecondsState value.
            timeInSecondsState.value++
        }
    }

    // Render UI.
    Column(
        modifier = Modifier.padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DisplayTime(
            timeInSeconds,
            isTimerRunning
        )
        EffectWhenTimeHits(
            timeSetInSeconds,
            timeInSeconds,
            vibrator,
            musicService,
            floatServiceConnection,
            context
        )
    }
}

@Composable
fun DisplayTime(
    timeInSeconds: Int,
    isTimerRunning: Boolean,
) {
    // Display "Game Begins" text.
    Text(
        text = stringResource(id = R.string.game_begins),
        color = Color.Black,
        style = MaterialTheme.typography.h4
    )

    // Row for buttons.
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 60.dp),
    ) {
        // Start/Stop button.
        Button(onClick = { isTimerRunningState.value = !(isTimerRunningState.value) }) {
            Text(
                text = if (isTimerRunning) stringResource(id = R.string.stop) else stringResource(
                    id = R.string.start
                ),
                color = Color.Black,
                style = MaterialTheme.typography.h4
            )
        }

        // Reset button.
        Button(onClick = {
            isTimerRunningState.value = false
            timeInSecondsState.value = 0
            activateDisposable = false
        }) {
            Text(
                text = stringResource(id = R.string.reset),
                color = Color.Black,
                style = MaterialTheme.typography.h4
            )
        }
    }

    // Display timer.
    Text(
        text = String.format(
            "%02d  :  %02d  :  %02d",
            timeInSeconds / HOURS_TO_SECONDS,
            (timeInSeconds % HOURS_TO_SECONDS) / MINUTES_TO_SECONDS,
            timeInSeconds % MINUTES_TO_SECONDS
        ),
        color = Color.Black,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(top = 60.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun EffectWhenTimeHits(
    timeSetInSeconds: Int,
    timeInSeconds: Int,
    vibrator: Vibrator,
    musicService: MusicPlayerService?,
    floatServiceConnection: ServiceConnection,
    context: Context
) {
    // LaunchedEffect to start/stop music, vibrate or not vibrate, and show floating widget.
    LaunchedEffect(timeInSeconds) {
        val musicPlayerService = musicService
        if (musicPlayerService != null) {
            if (timeSetInSeconds <= timeInSeconds &&
                timeInSeconds < timeSetInSeconds + THIRTY &&
                timeInSeconds != 0
            ) {
                activateDisposable = true
                // Start vibration.
                if (getVibration(context)) {
                    // Cancel any ongoing vibrations.
                    vibrator.cancel()
                    // Create a custom vibration pattern.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val pattern = longArrayOf(0, 1000, 1000) // 1 second on, 1 second off.
                        val effect =
                            VibrationEffect.createWaveform(pattern, 0) // Repeat indefinitely.
                        vibrator.vibrate(effect)
                    } else {
                        // For older versions, fallback to simple vibration, Deprecated though.
                        vibrator.vibrate(1000)
                    }
                }
                // Start music.
                if (!musicPlayerService.isPlaying()) {
                    musicPlayerService.startMusic()
                    Log.d("MusicPlayerService", "Music started")
                } else {
                    Log.d("MusicPlayerService", "Music is already playing")
                }
                // Start float widget service.
                if (floatService == null) {
                    val serviceIntent = Intent(context, FloatWidgetService::class.java)
                    context.bindService(
                        serviceIntent,
                        floatServiceConnection,
                        Context.BIND_AUTO_CREATE
                    )
                }
            } else {
                activateDisposable = false
                // Stop music if it's playing.
                if (musicPlayerService.isPlaying()) {
                    musicPlayerService.stopMusic()
                    Log.d("MusicPlayerService", "Music stopped")
                }
                // Cancel vibration.
                if (getVibration(context)) {
                    vibrator.cancel()
                }
                // Stop float widget service.
                if (floatService != null) {
                    val unbindIntent = Intent(context, FloatWidgetService::class.java)
                    context.unbindService(floatServiceConnection)
                    context.stopService(unbindIntent)
                    floatService = null
                }
            }
        }
    }
}
