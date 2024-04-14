package com.example.capstoneassignmentjack.ui.screens.popupscreens

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

@Composable
fun PopupHistoryScreen(viewModel: AddictionViewModel, context : Context) {
    // Get popups list from the view model.
    val popups by viewModel.getPopups.observeAsState(emptyList())

    // Builder for AlertDialog.
    val builder = AlertDialog.Builder(context)

    // Column layout for the popup history screen.
    Column(
        modifier = Modifier.padding(top = 60.dp, bottom = 100.dp),
    ) {
        // LazyColumn to display the list of popups.
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(popups) { popups ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), // Add padding to the card.
                    backgroundColor = Color.LightGray, // Set the card background color.
                    elevation = 4.dp // Add elevation for a shadow effect.
                ) {
                    Column {
                        // Display popup text.
                        Text(text = popups.text)
                        // Display popup color.
                        when (popups.color) {
                            HEX_RED -> {
                                Text(text = RED)
                            }
                            HEX_BLUE -> {
                                Text(text = BLUE)
                            }
                            HEX_GREEN -> {
                                Text(text = GREEN)
                            }
                        }

                        // Display popup size.
                        when (popups.size) {
                            SIZE_SMALL -> {
                                Text(text = SMALL)
                            }
                            SIZE_MEDIUM -> {
                                Text(text = MEDIUM)
                            }
                            SIZE_BIG -> {
                                Text(text = BIG)
                            }
                        }
                    }
                }
            }
        }
    }
    // Column layout for the delete all popups button.
    Column(
        modifier = Modifier.padding(top = 500.dp),
    ) {
        // Texts for AlertDialog
        val deleteAllText = "You're going to delete all your pop-up settings"
        val certain = "Are you certain?"
        val saved = "All your precious rewards are saved"

        // Button to delete all popups.
        Button(
            onClick = {
                builder.setTitle(deleteAllText)
                builder.setMessage(certain)
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    viewModel.deletePopupBacklog()
                }
                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(
                        context,
                        saved,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                builder.show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
            Text(
                text = stringResource(id = R.string.deleteallpopups)
            )
        }
    }
}