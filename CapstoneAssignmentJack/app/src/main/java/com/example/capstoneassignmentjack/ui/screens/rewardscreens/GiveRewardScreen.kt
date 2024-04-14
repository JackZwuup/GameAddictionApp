package com.example.capstoneassignmentjack.ui.screens.rewardscreens

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.capstoneassignmentjack.R
import com.example.capstoneassignmentjack.model.Reward
import com.example.capstoneassignmentjack.ui.screens.Screen
import com.example.level5task1.viewmodel.AddictionViewModel
import java.util.Date

@Composable
fun GiveRewardScreen(navController: NavHostController, viewModel: AddictionViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // State variable for the rating.
        var rating by remember { mutableStateOf(0) }

        // AlertDialog Builder.
        val builder = AlertDialog.Builder(context)

        // String resources for star ratings.
        val zeroStarsText = stringResource(id = R.string.zerostars)
        val oneStarText = stringResource(id = R.string.onestar)
        val twoStarsText = stringResource(id = R.string.twostars)
        val threeStarsText = stringResource(id = R.string.threestars)

        // Display rating text.
        Text(
            text = stringResource(id = R.string.ratingtext)
        )

        // Display star icons for rating selection
        Row {
            for (i in 1..3) {
                IconButton(onClick = { rating = i }) {
                    Icon(
                        painter = painterResource(
                            id = if (i <= rating) R.drawable.baseline_star_rate_24 else R.drawable.baseline_star_border_24
                        ),
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        // Confirm button.
        Button(onClick = {
            when (rating) {
                0 -> {
                    buildPopup(builder, context, zeroStarsText, rating, viewModel)
                }
                1 -> {
                    buildPopup(builder, context, oneStarText, rating, viewModel)
                }
                2 -> {
                    buildPopup(builder, context, twoStarsText, rating, viewModel)
                }
                3 -> {
                    buildPopup(builder, context, threeStarsText, rating, viewModel)
                }
            }
        }) {
            Text(
                stringResource(id = R.string.confirm)
            )
        }

        // History button.
        Button(onClick = {
            navController.navigate(Screen.RewardHistoryScreen.route)
        }) {
            Text(
                stringResource(id = R.string.historyscreen)
            )
        }
    }
}

// Function to insert reward into the database.
fun insertInDatabase(rating: Int, viewModel: AddictionViewModel) {
    val reward = Reward(
        daterecent = Date(),
        getstars = rating
    )
    viewModel.insertReward(reward)
}

// Function to build confirmation popup.
fun buildPopup(
    builder: AlertDialog.Builder,
    context: Context,
    Text: String,
    rating: Int,
    viewModel: AddictionViewModel
) {
    // Strings for popup.
    val sure = "Are you sure?"
    val toastThink = "It's always good to think things over"

    // Build AlertDialog.
    builder.setTitle(Text)
    builder.setMessage(sure)
    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
        insertInDatabase(rating = rating, viewModel = viewModel)
    }
    builder.setNegativeButton(android.R.string.no) { dialog, which ->
        Toast.makeText(context, toastThink, Toast.LENGTH_SHORT).show()
    }
    builder.show()
}
