package me.beavernotacat.quiztrainer


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import me.beavernotacat.quiztrainer.models.QuizStates
import me.beavernotacat.quiztrainer.models.QuizViewModel
import me.beavernotacat.quiztrainer.models.questionsList
import me.beavernotacat.quiztrainer.ui.theme.QuizTrainerTheme
import me.beavernotacat.quiztrainer.ui.screens.MainScreen
import me.beavernotacat.quiztrainer.ui.screens.QuizScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTrainerTheme {
                Box(Modifier.safeDrawingPadding()) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App(
    viewModel: QuizViewModel = viewModel()
) {
    val uiState = viewModel.quizUiState.value

    when (uiState.questionState) {
        QuizStates.WELCOME, QuizStates.COMPLETED ->
            MainScreen(
                quizUiState = uiState,
                onRestart = viewModel::onRestartQuiz
            )

        QuizStates.ASK, QuizStates.ANSWER ->
            QuizScreen(
                question = questionsList[uiState.totalAnswers],
                quizUiState = uiState,
                onAnswerSelected = viewModel::onAnswerSelected,
                onSubmitAnswer = viewModel::onSubmitAnswer,
                onNextQuestion = viewModel::onNextQuestion
            )
    }
}