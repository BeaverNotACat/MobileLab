package me.beavernotacat.quiztrainer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun QuizHeader(
    currentQuestion: Int,
    totalQuestions: Int,
    correctAnswers: Int,
    percents: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Вопрос $currentQuestion/$totalQuestions",
            fontSize = 16.sp,
        )
        Text(
            text = "Верно $correctAnswers/$currentQuestion ($percents%)",
            fontSize = 16.sp,
        )
    }
}