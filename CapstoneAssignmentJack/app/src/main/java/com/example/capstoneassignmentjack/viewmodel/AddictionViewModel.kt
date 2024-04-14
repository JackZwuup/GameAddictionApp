package com.example.level5task1.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.capstoneassignmentjack.FloatWidgetService
import com.example.capstoneassignmentjack.model.Popup
import com.example.capstoneassignmentjack.repository.RewardRepository
import com.example.capstoneassignmentjack.model.Reward
import com.example.capstoneassignmentjack.repository.PopupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.asLiveData

class AddictionViewModel(application: Application) : AndroidViewModel(application) {
    // Mutable state representing the current screen.
    var currentScreen = mutableStateOf("Home")

    // Reference to the FloatWidgetService for managing the floating widget.
    @SuppressLint("StaticFieldLeak")
    private var floatWidgetService: FloatWidgetService? = null

    // Repositories for handling data operations.
    private val rewardRepository = RewardRepository(application.applicationContext)
    private val popupRepository = PopupRepository(application.applicationContext)

    // Coroutine scope for managing coroutines.
    private val mainScope = CoroutineScope(Dispatchers.Main)

    // LiveData to observe rewards data changes.
    val getRewards: LiveData<List<Reward>> = rewardRepository.getRewards()

    // LiveData to observe popup data changes.
    val getPopups: LiveData<List<Popup>> = popupRepository.getPopup()

    // LiveData to observe the most recent popup data changes.
    val getRecentPopup: LiveData<Popup?> = popupRepository.getRecentPopup()

    // Method to set the FloatWidgetService instance.
    fun setFloatWidgetService(service: FloatWidgetService) {
        floatWidgetService = service
    }

    // Method to update the text displayed in the floating widget.
    fun updateFloatWidgetText(newText: String) {
        floatWidgetService?.updateText(newText)
    }

    // Method to update the background color of the floating widget.
    fun updateFloatWidgetColor(color: Drawable) {
        floatWidgetService?.updateColor(color)
    }

    // Method to update the size of the floating widget.
    fun updateFloatWidgetSize(size: Int) {
        floatWidgetService?.updateSize(size)
    }

    // Method to insert a new reward into the database.
    fun insertReward(reward: Reward) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                rewardRepository.insert(reward)
            }
        }
    }

    // Method to insert a new popup into the database.
    fun insertPopup(popup: Popup) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                popupRepository.insert(popup)
            }
        }
    }

    // Method to delete all rewards from the database.
    fun deleteRewardBacklog() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                rewardRepository.deleteAll()
            }
        }
    }

    // Method to delete all popups from the database.
    fun deletePopupBacklog() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                popupRepository.deleteAll()
            }
        }
    }

    // Method to delete a specific reward from the database.
    fun deleteReward(reward: Reward) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                rewardRepository.delete(reward)
            }
        }
    }
}





