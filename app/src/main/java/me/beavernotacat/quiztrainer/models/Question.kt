package me.beavernotacat.quiztrainer.models


data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: Int
)