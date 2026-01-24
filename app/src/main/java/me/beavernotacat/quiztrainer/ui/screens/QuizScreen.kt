package me.beavernotacat.quiztrainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.beavernotacat.quiztrainer.ui.components.QuestionVariants
import me.beavernotacat.quiztrainer.ui.components.QuizHeader
import me.beavernotacat.quiztrainer.ui.components.QuizNextButton
import me.beavernotacat.quiztrainer.models.Question
import me.beavernotacat.quiztrainer.models.QuizUiState

@Composable
fun QuizScreen(
    question: Question,
    quizUiState: QuizUiState,
    onAnswerSelected: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuizHeader(
            currentQuestion = quizUiState.totalAnswers + 1,
            totalQuestions = quizUiState.totalQuestions,
            correctAnswers = quizUiState.correctAnswers,
            percents = quizUiState.percents
        )

        Text(
            text = question.questionText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        QuestionVariants(
            options = question.options,
            selectedAnswer = quizUiState.selectedAnswer,
            correctAnswer = question.correctAnswer,
            currentState = quizUiState.questionState,
            onOptionClick = onAnswerSelected
        )

        QuizNextButton(
            quizUiState = quizUiState,
            onSubmitAnswer = onSubmitAnswer,
            onNextQuestion = onNextQuestion
        )
    }
}