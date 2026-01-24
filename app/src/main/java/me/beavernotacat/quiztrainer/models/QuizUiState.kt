package me.beavernotacat.quiztrainer.models

data class QuizUiState(
    val questionState: QuizStates = QuizStates.WELCOME,
    val correctAnswers: Int = 0,
    val totalAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val selectedAnswer: Int? = null
) {
    val percents: Int
        get() = if (totalQuestions > 0) {
            (correctAnswers * 100 / totalQuestions)
        } else 0

    val comment: String
        get() = when (percents) {
            in 90..100 -> "Да вы ели все три сыра"
            in 70..89 -> "Ну такое"
            in 50..69 -> "Компартия забрать у вас возможность пользоваться базами данных"
            else -> "Вы расстроить Павел Ростиславович"
        }

    val isLastQuestion: Boolean
        get() = totalAnswers == totalQuestions - 1
}