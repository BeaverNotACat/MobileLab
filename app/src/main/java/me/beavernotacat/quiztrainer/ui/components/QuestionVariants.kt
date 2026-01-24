package me.beavernotacat.quiztrainer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.beavernotacat.quiztrainer.models.QuizStates

@Composable
fun QuestionVariants(
    options: List<String>,
    selectedAnswer: Int?,
    correctAnswer: Int,
    currentState: QuizStates,
    onOptionClick: (Int) -> Unit
) {
    val successColors = ButtonColors(
        Color.Green,
        Color.White,
        Color.Green,
        Color.White,
    )
    val selectedColors = ButtonColors(
        Color.Red,
        Color.White,
        Color.Red,
        Color.White,
    )
    val defaultColors = ButtonDefaults.buttonColors()


    fun getColors(index: Int): ButtonColors {
        if (correctAnswer == index && currentState == QuizStates.ANSWER) return successColors
        if (selectedAnswer == index) return selectedColors
        return defaultColors
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            Button(
                onClick = { onOptionClick(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = currentState == QuizStates.ASK,
                colors = getColors(index)
            ) {
                Text(
                    text = option,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}