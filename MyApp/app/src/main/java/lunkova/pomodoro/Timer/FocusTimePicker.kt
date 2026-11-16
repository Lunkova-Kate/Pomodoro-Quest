package lunkova.pomodoro.Timer

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import lunkova.pomodoro.rewardXp

@Composable
fun FocusTimePicker(
    initialMinutes: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val durations = FocusSettings.durations

    var selectedIndex by remember(initialMinutes) {
        mutableIntStateOf(
            durations.indexOf(initialMinutes).coerceAtLeast(0)
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFD9D9D9)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = Color.Black
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF383838)),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(0.3f).height(180.dp),
                        factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 0
                                maxValue = durations.lastIndex

                                displayedValues = durations
                                    .map { "$it мин" }
                                    .toTypedArray()

                                value = selectedIndex
                                wrapSelectorWheel = false

                                setOnValueChangedListener { _, _, newValue ->
                                    selectedIndex = newValue
                                }

                                if (android.os.Build.VERSION.SDK_INT >= 29) {
                                    setTextColor(android.graphics.Color.rgb(230, 167, 167))
                                    setTextSize(22f * resources.displayMetrics.scaledDensity)
                                    setSelectionDividerHeight(0)
                                }
                            }
                        },
                        update = { picker ->
                            picker.value = selectedIndex
                        }
                    )
                }

                Text(
                    text = "Получаемый опыт:",
                    color = Color.Black,
                    fontSize = 12.sp
                )

                Text(
                    text = "${rewardXp(durations[selectedIndex])} XP",
                    color = Color.Black,
                    fontSize = 24.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }

                    TextButton(
                        onClick = {
                            onConfirm(durations[selectedIndex])
                        }
                    ) {
                        Text("Выбрать")
                    }
                }
            }
        }
    }
}