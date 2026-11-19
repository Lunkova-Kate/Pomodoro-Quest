package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lunkova.pomodoro.Screen.MainScreen
import lunkova.pomodoro.Screen.ResultScreen
import lunkova.pomodoro.Screen.TimerScreen
import lunkova.pomodoro.Timer.FocusSettings
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Pomodor0QuestTheme {

                val pomodoroViewModel: PomodoroViewModel = viewModel()
                val userProgress by pomodoroViewModel.userProgress.collectAsStateWithLifecycle()
                val timerState by pomodoroViewModel.timer.collectAsStateWithLifecycle()
                val level = Level.fromXp(userProgress.totalXp)
                var selectedMinutes by rememberSaveable {
                    mutableIntStateOf(FocusSettings.defaultMinutes)
                }
                var showTimer by rememberSaveable {
                    mutableStateOf(false)
                }

                if (showTimer) {

                    if (timerState.stateSession == StateSession.COMPLETED) {

                        ResultScreen(
                            minutes = timerState.userTimer,
                            earnedXp = rewardXp(timerState.userTimer),
                            totalXp = userProgress.totalXp,
                            onBackClick = {
                                showTimer = false
                            }
                        )

                    } else {

                        TimerScreen(
                            viewModel = pomodoroViewModel,
                            onCancelClick = {
                                showTimer = false
                            }
                        )
                    }

                } else {
                    MainScreen(
                        totalXp = userProgress.totalXp,
                        level = level,
                        selectedMinutes = selectedMinutes,
                        onMinutesSelected = { minutes ->
                            selectedMinutes = minutes
                        },
                        onStartClick = {
                            pomodoroViewModel.resetSession(selectedMinutes)
                            showTimer = true
                        }
                    )
                }
            }
        }
    }
}