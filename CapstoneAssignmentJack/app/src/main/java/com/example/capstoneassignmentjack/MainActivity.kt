package com.example.capstoneassignmentjack

import HomeScreen
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.capstoneassignmentjack.ui.screens.Screen
import com.example.capstoneassignmentjack.ui.screens.popupscreens.PopupHomeScreen
import com.example.capstoneassignmentjack.ui.screens.popupscreens.PopupLayoutScreen
import com.example.capstoneassignmentjack.ui.screens.popupscreens.PopupTextScreen
import com.example.capstoneassignmentjack.ui.screens.rewardscreens.GiveRewardScreen
import com.example.capstoneassignmentjack.ui.screens.rewardscreens.RewardHistoryScreen
import com.example.capstoneassignmentjack.ui.theme.CapstoneAssignmentJackTheme
import com.example.level5task1.viewmodel.AddictionViewModel
import com.example.capstoneassignmentjack.ui.screens.TimerScreen
import com.example.capstoneassignmentjack.ui.screens.popupscreens.PopupHistoryScreen
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            CapstoneAssignmentJackTheme {
                // A surface container using the 'background' color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CapstoneAssignmentJackTheme {
                        val navController = rememberNavController()
                        val addictionViewModel: AddictionViewModel = viewModel()

                        NavHost(
                            navController = navController,
                            startDestination = Screen.HomeScreen.route
                        ) {
                            // Composable for Home Screen.
                            composable(Screen.HomeScreen.route) {
                                HomeScreen(
                                    navController = navController,
                                    viewModel = addictionViewModel,
                                    context = this@MainActivity
                                )
                            }
                            // Composable for Timer Screen.
                            composable(Screen.TimerScreen.route) {
                                TimerScreen(
                                    context = this@MainActivity
                                )
                                ClickOnRewardIcon(navController)
                                BottomNavForSomeScreens(navController, addictionViewModel.currentScreen)
                            }
                            // Composable for Popup Home Screen.
                            composable(Screen.PopupHomeScreen.route) {
                                PopupHomeScreen(
                                    navController = navController
                                )
                                ClickOnRewardIcon(navController)
                                BottomNavForSomeScreens(navController, addictionViewModel.currentScreen)
                            }

                            // Composable for Popup Text Screen.
                            composable(Screen.PopupTextScreen.route) {
                                PopupTextScreen(
                                    viewModel = addictionViewModel,
                                    context = this@MainActivity,
                                )
                                ClickOnRewardIcon(navController)
                                BottomNavForSomeScreens(navController, addictionViewModel.currentScreen)
                            }

                            // Composable for Popup Layout Screen.
                            composable(Screen.PopupLayoutScreen.route) {
                                PopupLayoutScreen(
                                    viewModel = addictionViewModel,
                                    context = this@MainActivity,
                                )
                                ClickOnRewardIcon(navController)
                                BottomNavForSomeScreens(navController, addictionViewModel.currentScreen)
                            }

                            // Composable for Popup History Screen.
                            composable(Screen.PopupHistoryScreen.route) {
                                PopupHistoryScreen(
                                    viewModel = addictionViewModel,
                                    context = this@MainActivity,
                                )
                                ClickOnRewardIcon(navController)
                                BottomNavForSomeScreens(navController, addictionViewModel.currentScreen)
                            }

                            // Composable for Give Reward Screen.
                            composable(Screen.GiveRewardScreen.route) {
                                GiveRewardScreen(
                                    navController = navController,
                                    viewModel = addictionViewModel,
                                    context = this@MainActivity
                                )
                                BottomNavForSomeScreens(navController, addictionViewModel.currentScreen)
                            }

                            // Composable for Reward History Screen.
                            composable(Screen.RewardHistoryScreen.route) {
                                RewardHistoryScreen(
                                    viewModel = addictionViewModel,
                                    context = this@MainActivity,
                                )
                                ClickOnRewardIcon(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Composable function for bottom navigation on some screens.
@Composable
fun BottomNavForSomeScreens(navController: NavController, currentScreen: MutableState<String>) {
    val homeString = stringResource(id = R.string.home)
    val timerString = stringResource(id = R.string.timer)
    val popupHomeString = stringResource(id = R.string.popup_home)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        // For pop-up or home screen, if you click on a icon you navigate to the following and the text + icon changes to the other.
        Column(
            verticalArrangement = Arrangement.Bottom,
        ) {
            IconButton(onClick = {
                // When currentscreen = home, and click on the icon, you navigate to PopupHomeScreen and change the value accordingly.
                // When currentscreen = timer, and click on the icon, you still navigate to PopupHomeScreen and change the value accordingly.
                // But when currentscreen = popup itself, and click on the icon, you will instead go back to Home and change the value accordingly.
                when (currentScreen.value) {
                    homeString -> {
                        navController.navigate(Screen.PopupHomeScreen.route)
                        currentScreen.value = popupHomeString
                    }
                    timerString -> {
                        navController.navigate(Screen.PopupHomeScreen.route)
                        currentScreen.value = popupHomeString
                    }
                    else -> {
                        navController.navigate(Screen.HomeScreen.route)
                        currentScreen.value = homeString
                    }
                }
            }) {
                // Icon for home or popup home screen
                val icon = if (currentScreen.value == popupHomeString)
                    rememberVectorPainter(image = Icons.Filled.Home)
                else painterResource(id = R.drawable.baseline_add_box_24)
                val contentDescription =
                    if (currentScreen.value == popupHomeString) homeString else popupHomeString
                Icon(icon, contentDescription = contentDescription)
            }
            Text(if (currentScreen.value == popupHomeString) homeString else stringResource(id = R.string.makepopup))
        }

        // For timer or home screen, if you click on a icon you navigate to the following and the text + icon change to the other.
        Column(
            verticalArrangement = Arrangement.Bottom,
        ) {
            // When currentscreen = home, and click on the icon, you navigate to TimerScreen and change the value accordingly.
            // When currentscreen = popup, and click on the icon, you still navigate to TimerScreen and change the value accordingly.
            // But when currentscreen = timer itself, and click on the icon, you will instead go back to Home and change the value accordingly.
            IconButton(onClick = {
                when (currentScreen.value) {
                    homeString -> {
                        navController.navigate(Screen.TimerScreen.route)
                        currentScreen.value = timerString
                    }

                    popupHomeString -> {
                        navController.navigate(Screen.TimerScreen.route)
                        currentScreen.value = timerString
                    }

                    else -> {
                        navController.navigate(Screen.HomeScreen.route)
                        currentScreen.value = homeString
                    }
                }
            }) {
                val icon = if (currentScreen.value == timerString)
                    rememberVectorPainter(image = Icons.Filled.Home)
                else painterResource(id = R.drawable.baseline_history_24)
                val contentDescription =
                    if (currentScreen.value == timerString) homeString else timerString
                Icon(icon, contentDescription = contentDescription)
            }
            Text(if (currentScreen.value == timerString) homeString else stringResource(id = R.string.settimer))
        }
    }
}

// Composable function to click on reward icon.
@Composable
fun ClickOnRewardIcon(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navController.navigate(Screen.GiveRewardScreen.route) },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_star_24),
                contentDescription = "reward icon"
            )
        }
    }
}