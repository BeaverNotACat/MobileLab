package me.beavernotacat.quiztrainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.beavernotacat.quiztrainer.ui.components.Results
import me.beavernotacat.quiztrainer.ui.components.Welcome
import me.beavernotacat.quiztrainer.models.QuizStates
import me.beavernotacat.quiztrainer.models.QuizUiState

@Composable
fun MainScreen(
    quizUiState: QuizUiState,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (quizUiState.questionState === QuizStates.WELCOME) Welcome() else Results(quizUiState)

        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(56.dp)
        ) {
            Text(
                text = if (quizUiState.questionState == QuizStates.WELCOME) "Начать" else "Пройти ещё раз",
                fontSize = 18.sp
            )
        }
    }
}