package lunkova.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import lunkova.pomodoro.ui.theme.Pomodor0QuestTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pomodor0QuestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        "Admiral!", 25, modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
}
//Создаёт переменную totalXp со значением 100.
//Создаёт переменную earnedXp со значением 25.
//Увеличивает totalXp на earnedXp.
//Выводит результат через println().
//fun Sum() {
//    var totalXp = 100
//    val earnedXp = 25
//    totalXp +=earnedXp
//    println("$totalXp (XP)")
//}

//Принимает два параметра типа Int: totalXp и earnedXp.
//Складывает их.
//Возвращает сумму через return.
//Ничего не выводит внутри самой функции.
fun addXP(totalXP: Int, earnedXP: Int): Int {
   return totalXP + earnedXP
}


//Создай новую Composable-функцию XpText, которая:
//1. Принимает параметр xp типа Int.
//2. Отображает текст с помощью Text().
//3. Показывает строку вида Всего опыта: 125 XP.

@Composable
fun Example(xp:Int, modifier: Modifier = Modifier) {
    Text ( text = "Всего опыта: $xp (XP)",
    color = Color.Blue,
        modifier = modifier
    )
}
@Composable
fun Greeting(name: String,level: Int, modifier: Modifier = Modifier) {
    Column() {
        Text( "Добрый день!")
        Text("$name")
        Text("$level")
//
    }
}
//
//@Preview(showBackground = false)
//@Composable
//fun GreetingPreview() {
//    Pomodor0QuestTheme {
//        Greeting("Android")
//    }
