package lunkova.pomodoro.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lunkova.pomodoro.Level

private val BackgroundColor = Color(0xFF3C444D)
private val CardRed = Color(0xFF850F0E)
private val PastelPink = Color(0xFFE6A7A7)
private val MutedPink = Color(0xFFAD797D)
private val BrightRed = Color(0xFFF20D19)

@Composable
fun ResultScreen(
    minutes: Int,
    earnedXp: Int,
    totalXp: Int,
    onBackClick: () -> Unit
) {
    val level = Level.fromXp(totalXp)
    val currentXp = Level.xpInCurrentLevel(totalXp)
    val requiredXp = Level.xpToNextLevel(level)

    val progress = currentXp.toFloat() / requiredXp

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
                .padding(16.dp)
                .background(
                    CardRed,
                    RoundedCornerShape(26.dp)
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "РЕЗУЛЬТАТ СЕССИИ",
                modifier = Modifier.fillMaxWidth(),
                color = PastelPink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Сессия завершена — ты получил награду " +
                        "за выполнение фокус-сессии.",
                color = PastelPink,
                fontSize = 13.sp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MutedPink,
                        RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp)
            ) {
                Text(
                    text = "ДЛИТЕЛЬНОСТЬ",
                    color = CardRed,
                    fontSize = 10.sp
                )

                Text(
                    text = "$minutes минут",
                    color = BackgroundColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFD1B4B4),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp)
            ) {
                Text(
                    text = "ПОЛУЧЕНО XP",
                    color = CardRed,
                    fontSize = 10.sp
                )

                Text(
                    text = "+$earnedXp XP",
                    color = CardRed,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Уровень $level",
                        color = PastelPink,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "$currentXp / $requiredXp",
                        color = PastelPink,
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

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrightRed
                )
            ) {
                Text("Вернуться на главный экран")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}