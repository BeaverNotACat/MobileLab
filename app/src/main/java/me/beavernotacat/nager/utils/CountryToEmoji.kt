package me.beavernotacat.nager.utils

fun CountryToEmoji(code: String): String {
    val firstLetter = Character.codePointAt(code, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(code, 1) - 0x41 + 0x1F1E6

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}