package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Pomodor0QuestTheme {

                val pomodoroViewModel: PomodoroViewModel = viewModel()
                val userProgress by pomodoroViewModel.userProgress.collectAsStateWithLifecycle()
                var selectedMinutes by rememberSaveable {
                    mutableIntStateOf(25)
                }
                var showTimer by remember {
                    mutableStateOf(false)
                }

                if (showTimer) {
                    TimerScreen(
                        viewModel = pomodoroViewModel,
                        onCancelClick = {
                            showTimer = false
                        }
                    )
                } else {
                    MainScreen(
                        totalXp = userProgress.totalXp,
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