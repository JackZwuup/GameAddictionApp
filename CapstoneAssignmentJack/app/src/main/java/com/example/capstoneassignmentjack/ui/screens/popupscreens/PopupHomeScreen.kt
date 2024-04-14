package com.example.capstoneassignmentjack.ui.screens.popupscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.capstoneassignmentjack.R
import com.example.capstoneassignmentjack.model.Popup
import com.example.capstoneassignmentjack.ui.screens.Screen
import com.example.level5task1.viewmodel.AddictionViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PopupHomeScreen(
    navController: NavHostController,
) {
    Column (
        modifier = Modifier.padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.makepopup)
        )
        Row {
            Button(onClick = {
                navController.navigate(Screen.PopupTextScreen.route)
            }) {
                Text("Change text")
            }
            Button(onClick = {
                navController.navigate(Screen.PopupLayoutScreen.route)
            }) {
                Text("Change layout")
            }
        }
        Button(onClick = {
            navController.navigate(Screen.PopupHistoryScreen.route)
        }) {
            Text("pop-up history")
        }
    }
}
fun insertInDatabase(viewModel: AddictionViewModel) {
    // When the reward is confirmed, insert the result into the database.
    val popup = Popup(
        color = getColor(),
        size = getSize(),
        text = getText()
    )
    viewModel.insertPopup(popup)
}
