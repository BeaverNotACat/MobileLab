package me.beavernotacat.quiztrainer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.beavernotacat.quiztrainer.models.QuizUiState

@Composable
fun Results(stats: QuizUiState) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${stats.percents}%",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "правильных ответов",
            fontSize = 18.sp,
        )
        Text(
            text = "(${stats.correctAnswers} из ${stats.totalQuestions})",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp),
        )
        Text(
            text = stats.comment,
            fontSize = 18.sp,
        )
    }
}