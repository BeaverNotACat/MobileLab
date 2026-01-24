package me.beavernotacat.quiztrainer.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.beavernotacat.quiztrainer.models.QuizStates
import me.beavernotacat.quiztrainer.models.QuizUiState

@Composable
fun QuizNextButton(
    quizUiState: QuizUiState, onSubmitAnswer: () -> Unit, onNextQuestion: () -> Unit
) {
    val buttonText = when {
        quizUiState.questionState == QuizStates.ASK -> "Ответить!"
        !quizUiState.isLastQuestion -> "Следующий вопрос"
        else -> "Закончить"
    }

    Button(
        onClick = {
            if (quizUiState.questionState == QuizStates.ASK) {
                onSubmitAnswer()
            } else {
                onNextQuestion()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = quizUiState.selectedAnswer !== null,
    ) {
        Text(
            text = buttonText, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
    }
}