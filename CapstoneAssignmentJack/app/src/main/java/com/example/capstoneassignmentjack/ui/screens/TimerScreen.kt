package com.example.capstoneassignmentjack.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.capstoneassignmentjack.R
import com.example.capstoneassignmentjack.constants.Ints.FIFTY_NINE
import com.example.capstoneassignmentjack.constants.Ints.MINUTES_TO_SECONDS
import com.example.capstoneassignmentjack.constants.Ints.TWENTY_FOUR
import com.example.capstoneassignmentjack.constants.Strings.EARTH_QUAKE
import com.example.capstoneassignmentjack.constants.Strings.IDOL
import com.example.capstoneassignmentjack.constants.Strings.KEY_HOURS
import com.example.capstoneassignmentjack.constants.Strings.KEY_MINUTES
import com.example.capstoneassignmentjack.constants.Strings.KEY_SECONDS
import com.example.capstoneassignmentjack.constants.Strings.KEY_SELECTED_SONG
import com.example.capstoneassignmentjack.constants.Strings.KEY_VIBRATE
import com.example.capstoneassignmentjack.constants.Strings.NONE
import com.example.capstoneassignmentjack.constants.Strings.RABBIT_HOLE
import com.example.capstoneassignmentjack.constants.Strings.STUDY
import com.example.capstoneassignmentjack.constants.Strings.TAKE

// Define variables for timer.
private var timerHours = 0
private var timerMinutes = 0
private var timerSeconds = 0

// Define variable for vibration.
private var timerVibrate = false

// Define variable for selected song.
private var thisSongSelected = ""

@Composable
fun TimerScreen(context : Context) {

    // Get SharedPreferences instance.
    val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)

    // State variables for timer and vibration settings.
    var hours by remember { mutableStateOf(sharedPreferences.getInt(KEY_HOURS, 0)) }
    var minutes by remember { mutableStateOf(sharedPreferences.getInt(KEY_MINUTES, 0)) }
    var seconds by remember { mutableStateOf(sharedPreferences.getInt(KEY_SECONDS, 0)) }
    var vibrate by remember { mutableStateOf(sharedPreferences.getBoolean(KEY_VIBRATE, false)) }

    // Toast message.
    val toastAllconfirmed = stringResource(id = R.string.allConfirm)

    // List of songs.
    val songs = arrayOf(NONE, TAKE, STUDY, EARTH_QUAKE, IDOL, RABBIT_HOLE)

    // State variable for selected song.
    var selectedSong by remember {
        mutableStateOf(sharedPreferences.getString(KEY_SELECTED_SONG, songs[0]) ?: songs[0])
    }

    // Timer screen UI.
    Column(
        modifier = Modifier.padding(top = 60.dp),
    ) {
        Row {
            // Hours input field
            TextField(
                value = hours.toString(),
                onValueChange = {
                    handleTextFieldInput(
                        it,
                        null,
                        { hours = it },
                        { /* Handle error */ })
                },
                label = { Text(stringResource(id = R.string.hours)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )

            // Minutes input field.
            TextField(
                value = minutes.toString(),
                onValueChange = {
                    handleTextFieldInput(
                        it,
                        null,
                        { minutes = it },
                        { /* Handle error */ })
                },
                label = { Text(stringResource(id = R.string.minutes)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )

            // Seconds input field.
            TextField(
                value = seconds.toString(),
                onValueChange = {
                    handleTextFieldInput(
                        it,
                        null,
                        { seconds = it },
                        { /* Handle error */ })
                },
                label = { Text(stringResource(id = R.string.seconds)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Row {
            // Vibrate switch.
            Text(
                text = stringResource(id = R.string.vibrate),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
            )
            Switch(
                checked = vibrate,
                onCheckedChange = { vibrate = it },
                modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
            )

        }

        // Dropdown menu for selecting song.
        SimpleDropdownMenu(
            items = songs.toList(),
            selectedItem = selectedSong,
            onItemSelected = { item ->
                selectedSong = item
                Log.d("Spinner", "Selected item: $selectedSong")
            }
        )
        ConfirmFunction(hours,
            minutes,
            seconds,
            selectedSong,
            vibrate,
            sharedPreferences,
            context,
            toastAllconfirmed
        )
    }
}

@Composable
fun ConfirmFunction(
    hours: Int,
    minutes: Int,
    seconds: Int,
    selectedSong: String,
    vibrate: Boolean,
    sharedPreferences: SharedPreferences,
    context: Context,
    toastAllconfirmed: String
) {
    // Confirm button.
    Button(
        modifier = Modifier.padding(top = 240.dp),
        onClick = {
            if (hours in 0..TWENTY_FOUR &&
                minutes >= 0 && minutes < MINUTES_TO_SECONDS &&
                seconds >= 0 && seconds < MINUTES_TO_SECONDS
            ) {
                // Set timer, song, and vibration settings.
                setTimer(hours, minutes, seconds)
                setSong(selectedSong)
                setVibration(vibrate)

                // Store values in SharedPreferences.
                with(sharedPreferences.edit()) {
                    putInt(KEY_HOURS, hours)
                    putInt(KEY_MINUTES, minutes)
                    putInt(KEY_SECONDS, seconds)
                    putBoolean(KEY_VIBRATE, vibrate)
                    putString(KEY_SELECTED_SONG, selectedSong)
                    apply()
                }
                // Show confirmation toast.
                Toast.makeText(context, toastAllconfirmed, Toast.LENGTH_LONG).show()
            } else if (hours < 0 || hours > TWENTY_FOUR) {
                // Show error toast for hours.
                Toast.makeText(context, R.string.hastobehours, Toast.LENGTH_LONG).show()
            } else if (minutes < 0 || minutes > FIFTY_NINE) {
                // Show error toast for minutes.
                Toast.makeText(context, R.string.hastobeminutes, Toast.LENGTH_LONG).show()
            } else if (seconds < 0 || seconds > FIFTY_NINE) {
                // Show error toast for seconds.
                Toast.makeText(context, R.string.hastobeseconds, Toast.LENGTH_LONG).show()
            } else {
                // Show generic error toast.
                Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
            }
        }) {
        Text(text = stringResource(id = R.string.confirm))
    }
}

// Function to handle text field input.
fun handleTextFieldInput(
    input: String,
    defaultValue: Int?,
    onUpdate: (Int) -> Unit,
    onError: () -> Unit
) {
    if (input.isEmpty()) {
        defaultValue?.let { onUpdate(it) } ?: onUpdate(0)
    } else {
        try {
            onUpdate(input.toInt())
        } catch (e: NumberFormatException) {
            onError()
        }
    }
}

// Dropdown menu composable.
@Composable
fun SimpleDropdownMenu(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .clickable { expanded = true }
                .background(Color.LightGray)
        ) {
            Text(
                text = selectedItem,
                modifier = Modifier.padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        onItemSelected(item)
                        expanded = false
                        setSong(item)
                        Log.d("Is item empty?", item)
                    }) {
                        Text(text = item)
                    }
                }
            }
        }
    }
}

// Functions for setting and getting timer, vibration, and song settings.
fun setTimer(hours: Int, minutes: Int, seconds: Int) {
    timerHours = hours
    timerMinutes = minutes
    timerSeconds = seconds
}

fun getSeconds(context: Context): Int {
    val sharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getInt(KEY_SECONDS, 0)
}

fun getMinutes(context: Context): Int {
    val sharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getInt(KEY_MINUTES, 0)
}

fun getHours(context: Context): Int {
    val sharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getInt(KEY_HOURS, 0)
}
fun setVibration(vibrate: Boolean) {
    timerVibrate = vibrate
}

fun getVibration(context: Context): Boolean {
    val sharedPreferences =
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getBoolean(KEY_VIBRATE, false)
}

fun setSong(song: String) {
    thisSongSelected = song
    Log.d("Is song empty?", thisSongSelected)
}


fun getSong(): Int {
    Log.d("Does thisSongSelected have a value", thisSongSelected)
    return when (thisSongSelected) {
        NONE -> {
            Log.d("Does it go into none", "none")
            R.raw.silence
        }
        TAKE -> {
            Log.d("Does it go into take", "take")
            R.raw.take
        }
        STUDY -> {
            Log.d("Does it go into study", "study")
            R.raw.study
        }
        EARTH_QUAKE -> {
            Log.d("Does it go into earthquake", "earthquake")
            R.raw.earthquake
        }
        IDOL -> {
            Log.d("Does it go into idol", "idol")
            R.raw.idol
        }
        RABBIT_HOLE -> {
            Log.d("Does it go into rabbit hole", "rabbit hole")
            R.raw.rabbithole
        }
        else -> {
            Log.d("Does it go into error", thisSongSelected)
            R.raw.silence
        }
    }
}
