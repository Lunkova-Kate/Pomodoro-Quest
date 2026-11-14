package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            Pomodor0QuestTheme {

                val pomodoroViewModel: PomodoroViewModel = viewModel()

                var showTimer by remember {
                    mutableStateOf(false)
                }

                if (showTimer) {
                    TimerScreen(pomodoroViewModel)
                } else {
                    MainScreen(
                        onStartClick = {
                            showTimer = true
                        }
                    )
                }
            }
        }
    }
}