package lunkova.pomodoro.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import lunkova.pomodoro.PomodoroViewModel
import lunkova.pomodoro.StateSession
import lunkova.pomodoro.rewardXp

private val BackgroundColor = Color(0xFF3C444D)
private val CardRed = Color(0xFF850F0E)
private val PastelPink = Color(0xFFE6A7A7)
private val MutedPink = Color(0xFFAD797D)
private val BrightRed = Color(0xFFF20D19)

@Composable
fun TimerScreen(viewModel: PomodoroViewModel,
                onCancelClick: () -> Unit) {

    val timerState by viewModel.timer.collectAsStateWithLifecycle()

    val remainingMillis = timerState.remainingMillis
    val totalMillis = timerState.userTimer * 60_000L

    val remainingSeconds = (remainingMillis + 999L) / 1000L

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    val formattedTime = "%02d:%02d".format(minutes, seconds)
    val progress = if (totalMillis > 0L) {
        ((totalMillis - remainingMillis).toFloat() / totalMillis)
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    val progressPercent = (progress * 100).toInt()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(
                    color = CardRed,
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ФОКУС-СЕССИЯ",
                        color = MutedPink,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = formattedTime,
                        color = PastelPink,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            BrightRed,
                            RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                ) {
                    Text(
                        text = "+${rewardXp(timerState.userTimer)} XP",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = "Оставайся в фокусе — когда таймер " +
                        "достигнет нуля, сессия будет " +
                        "успешно завершена.",
                color = PastelPink,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Прогресс выполнения",
                        color = MutedPink,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "$progressPercent%",
                        color = BrightRed,
                        fontSize = 12.sp
                    )
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp),
                    color = BrightRed,
                    trackColor = MutedPink
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TimerControlButton(
                    onClick = {
                        when (timerState.stateSession) {
                            StateSession.SLEEPING -> viewModel.startSession()
                            StateSession.RUNNING -> viewModel.pauseSession()
                            StateSession.PAUSED -> viewModel.resumeSession()
                            else -> Unit
                        }
                    },
                    backgroundColor = BrightRed,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = if (timerState.stateSession == StateSession.RUNNING) {
                            Icons.Default.Pause
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = if (timerState.stateSession == StateSession.RUNNING) {
                            "Пауза"
                        } else {
                            "Начать или продолжить"
                        },
                        tint = PastelPink
                    )
                }

                TimerControlButton(
                    onClick = { viewModel.cancelSession()
                              onCancelClick()},
                    backgroundColor = MutedPink,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Отмена",
                        tint = BackgroundColor
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerControlButton(
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        content()
    }
}