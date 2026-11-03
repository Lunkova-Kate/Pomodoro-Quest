package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pomodor0QuestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        "Admiral", 25, modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
}

fun addXP(totalXP: Int, earnedXP: Int): Int {
   return totalXP + earnedXP
}


@Composable
fun Example(xp:Int, modifier: Modifier = Modifier) {
    Text ( text = "Всего опыта: $xp (XP)",
    color = Color.Blue,
        modifier = modifier
    )
}
@Composable
fun Greeting(name: String,level: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        Text("Добрый день!", fontSize = 16.sp,color = Color.Red)
        Text("$name", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Red)
    }
}

