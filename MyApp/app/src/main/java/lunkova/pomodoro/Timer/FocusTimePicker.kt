package lunkova.pomodoro.Timer


import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import lunkova.pomodoro.rewardXp

@Composable
fun FocusTimePicker(
    initialMinutes: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val durations = FocusSettings.durations

    var selectedIndex by remember {
        mutableIntStateOf(
            durations.indexOf(initialMinutes).coerceAtLeast(0)
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Время фокуса") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AndroidView(
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 0
                            maxValue = durations.lastIndex

                            displayedValues = durations
                                .map { "$it мин" }
                                .toTypedArray()

                            value = selectedIndex

                            setOnValueChangedListener { _, _, newValue ->
                                selectedIndex = newValue
                            }
                        }
                    },
                    update = { picker ->
                        picker.value = selectedIndex
                    }
                )

                Text("Получаемый опыт: +${rewardXp(durations[selectedIndex])} XP")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(durations[selectedIndex])
                }
            ) {
                Text("Выбрать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}