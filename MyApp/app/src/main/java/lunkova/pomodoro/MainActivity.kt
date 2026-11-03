package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
val DarkRed = Color(0xFF3D0807)
val PasteDarklPink = Color(0xFFAD797D)
val PastelPink = Color(0xFFE6A7A7)
val GrayBac = Color(0xFF3C444D)
val brush = Brush.verticalGradient(listOf(Color(0xFF7C110F),Color(0xFF5B0C0B)))
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pomodor0QuestTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    containerColor =  GrayBac) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).background(GrayBac),
                        verticalArrangement = Arrangement.spacedBy(50.dp)){
                            Greeting("Admiral", )
                            ProgressCard(25)
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
fun Example(xp:Int, modifier: Modifier = Modifier) {
    Text ( text = "Всего опыта: $xp (XP)",
    color = Color.Blue,
        modifier = modifier
    )
}
@Composable
fun ProgressCard(level: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(16.dp).height(350.dp).background(brush = brush,shape = RoundedCornerShape(20.dp)  ),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)) {
            Text("ТВОЙ ПРОГРЕСС", color = PasteDarklPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Уровень $level", fontSize = 35.sp,fontWeight = FontWeight.Bold, color = PastelPink)
        }

    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)

    ) {
        Text("Добрый день!", fontSize = 12.sp,color = PasteDarklPink)
        Text("$name", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PastelPink)
    }

}

