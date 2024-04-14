package com.example.capstoneassignmentjack.ui.screens

sealed class Screen(
    val route: String
) {
    object HomeScreen: Screen("home_screen")
    object TimerScreen: Screen("timer_screen")
    object PopupHomeScreen: Screen("popup_home_screen")
    object PopupLayoutScreen: Screen("popup_layout_screen")
    object PopupTextScreen: Screen("popup_text_screen")
    object PopupHistoryScreen: Screen("popup_history_screen")
    object GiveRewardScreen: Screen("give_reward_screen")
    object RewardHistoryScreen: Screen("reward_history_screen")
}