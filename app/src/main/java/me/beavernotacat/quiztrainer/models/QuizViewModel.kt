package me.beavernotacat.quiztrainer.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    private val _quizUiState = mutableStateOf(QuizUiState())
    val quizUiState: State<QuizUiState> = _quizUiState

    fun onAnswerSelected(answerIndex: Int) {
        _quizUiState.value = _quizUiState.value.copy(
            selectedAnswer = answerIndex
        )
    }

    fun onSubmitAnswer() {
        val currentState = _quizUiState.value
        val currentQuestion = questionsList[currentState.totalAnswers]
        val isCorrect = currentState.selectedAnswer == currentQuestion.correctAnswer

        _quizUiState.value = currentState.copy(
            questionState = QuizStates.ANSWER,
            correctAnswers = currentState.correctAnswers + if (isCorrect) 1 else 0,
            totalAnswers = currentState.totalAnswers
        )
    }

    fun onNextQuestion() {
        val currentState = _quizUiState.value

        if (currentState.isLastQuestion) {
            _quizUiState.value = currentState.copy(
                questionState = QuizStates.COMPLETED
            )
        } else {
            _quizUiState.value = currentState.copy(
                totalAnswers = currentState.totalAnswers + 1,
                selectedAnswer = null,
                questionState = QuizStates.ASK,
            )
        }
    }

    fun onRestartQuiz() {
        _quizUiState.value = QuizUiState(
            questionState = QuizStates.ASK,
            totalQuestions = questionsList.size
        )
    }
}