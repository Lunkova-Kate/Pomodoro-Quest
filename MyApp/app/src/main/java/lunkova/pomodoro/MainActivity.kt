package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme

val DarkRed = Color(0xFF3D0807)
val PasteDarklPink = Color(0xFFAD797D)
val PastelPink = Color(0xFFE6A7A7)
val GrayBac = Color(0xFF3C444D)
val brush = Brush.verticalGradient(
    colors = listOf(Color(0xFF7C110F), Color(0xFF5B0C0B))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Pomodor0QuestTheme {
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
                        Greeting("Admiral")
                        ProgressCard(level = 25)
                    }
                }
            }
        }
    }
}

fun addXP(totalXP: Int, earnedXP: Int): Int {
    return totalXP + earnedXP
}

@Composable
fun Example(xp: Int, modifier: Modifier = Modifier) {
    Text(
        text = "Всего опыта: $xp (XP)",
        color = Color.Blue,
        modifier = modifier
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = "Добрый день!",
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
fun ProgressCard(level: Int, modifier: Modifier = Modifier) {
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
            XpProgress()

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FocusButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )
                FocusDuration()
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
fun XpProgress() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LinearProgressIndicator(
            progress = { 0.124f },
            modifier = Modifier.fillMaxWidth(),
            color = PastelPink,
            trackColor = PasteDarklPink
        )
        Text(
            text = "Опыт: 124 / 1000",
            color = PastelPink,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FocusButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { },
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
fun FocusDuration(minutes: Int = 25, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
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
