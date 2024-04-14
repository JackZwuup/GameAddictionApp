package com.example.capstoneassignmentjack.ui.screens.rewardscreens

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.capstoneassignmentjack.R
import com.example.level5task1.viewmodel.AddictionViewModel

@Composable
fun RewardHistoryScreen(viewModel: AddictionViewModel, context: Context) {
    // Get rewards list from the view model
    val reward by viewModel.getRewards.observeAsState(emptyList())

    // Builder for AlertDialog
    val builder = AlertDialog.Builder(context)

    // Column layout for the reward history screen
    Column(
        modifier = Modifier.padding(top = 60.dp, bottom = 60.dp),
    ) {
        // LazyColumn to display the list of rewards
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(reward) { reward ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    backgroundColor = Color.LightGray,
                    elevation = 4.dp
                ) {
                    Column {
                        // Display reward date
                        Text(text = "${reward.daterecent}")
                        // Display star ratings
                        val rating = reward.getstars
                        Row {
                            for (i in 1..3) {
                                Icon(
                                    painter = painterResource(
                                        id = if (i <= rating) R.drawable.baseline_star_rate_24 else R.drawable.baseline_star_border_24
                                    ),
                                    contentDescription = null,
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                            // Spacer for layout spacing
                            Spacer(modifier = Modifier.width(100.dp))
                            // Button to delete a reward
                            Button(
                                onClick = { viewModel.deleteReward(reward) },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // Button to delete all rewards
    deleteRewards(
        builder,
        viewModel,
        context
    )
}

// Composable function to show AlertDialog for deleting all rewards
@Composable
fun deleteRewards(builder: AlertDialog.Builder, viewModel: AddictionViewModel, context: Context) {
    // Column layout for the delete all rewards button
    Column(
        modifier = Modifier.padding(top = 550.dp),
    ) {
        // Texts for AlertDialog
        val deleteAllText = "You're going to delete all your rewards"
        val certain = "Are you certain?"
        val safed = "All your precious rewards are safed"

        // Button to delete all rewards
        Button(
            onClick = {
                builder.setTitle(deleteAllText)
                builder.setMessage(certain)
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    viewModel.deleteRewardBacklog()
                }
                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(
                        context,
                        safed,
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
                text = stringResource(id = R.string.deleteall)
            )
        }
    }
}
