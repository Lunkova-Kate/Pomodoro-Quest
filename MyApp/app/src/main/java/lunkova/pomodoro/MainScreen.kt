package lunkova.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import lunkova.pomodoro.Timer.FocusTimePicker
import androidx.compose.foundation.clickable

private val DarkRed = Color(0xFF3D0807)
private val PasteDarklPink = Color(0xFFAD797D)
private val PastelPink = Color(0xFFE6A7A7)
private val GrayBac = Color(0xFF3C444D)

private val brush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF7C110F),
        Color(0xFF5B0C0B)
    )
)

@Composable
fun MainScreen(
    totalXp: Int,
    selectedMinutes: Int,
    level: Int,
    onMinutesSelected: (Int) -> Unit,
    onStartClick: () -> Unit
) {
    var showTimePicker by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = GrayBac
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            Greeting(Rank.getTitle(level))

            ProgressCard(
                totalXp = totalXp,
                level = level,
                selectedMinutes = selectedMinutes,
                onStartClick = onStartClick,
                onTimeClick = {
                    showTimePicker = true
                }
            )
        }
    }

    if (showTimePicker) {
        FocusTimePicker(
            initialMinutes = selectedMinutes,
            onConfirm = { minutes ->
                onMinutesSelected(minutes)
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = "Привет!",
            fontSize = 12.sp,
            color = PasteDarklPink
        )

        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PastelPink
        )
    }
}

@Composable
fun ProgressCard(
    totalXp: Int,
    level: Int,
    selectedMinutes: Int,
    onStartClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(350.dp)
            .background(
                brush = brush,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            LevelInfo(level)
            XpProgress(totalXp )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FocusButton(
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )

                FocusDuration(
                    minutes = selectedMinutes,
                    onClick = onTimeClick
                )
            }
        }
    }
}

@Composable
fun LevelInfo(level: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "ТВОЙ ПРОГРЕСС",
            color = PasteDarklPink,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Уровень $level",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = PastelPink
        )
    }
}

@Composable
fun XpProgress(totalXp: Int) {
    val level = Level.fromXp(totalXp)
    val currentXp = Level.xpInCurrentLevel(totalXp)
    val requiredXp = Level.xpToNextLevel(level)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LinearProgressIndicator(
            progress = { currentXp.toFloat() / requiredXp },
            modifier = Modifier.fillMaxWidth(),
            color = PastelPink,
            trackColor = PasteDarklPink
        )

        Text(
            text = "Опыт: $currentXp / $requiredXp",
            color = PastelPink,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FocusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PasteDarklPink,
            contentColor = DarkRed
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Начать фокус",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FocusDuration(
    minutes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.AccessTime,
            contentDescription = null,
            tint = PastelPink,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = "Фокус: $minutes мин",
            color = PastelPink,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
