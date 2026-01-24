package me.beavernotacat.splitmate.models

import kotlinx.serialization.Serializable

@Serializable
data class Split(
    val totalAmount: String = "0",
    val peopleCount: String = "1",
    val tipPercentage: String = "15.0"
) {
    val isValid: Boolean
        get() {
            val totalAmountNumber = totalAmount.toDoubleOrNull();
            val peopleCountNumber = peopleCount.toIntOrNull();
            val tipPercentageNumber = tipPercentage.toDoubleOrNull();
            return totalAmountNumber != null && peopleCountNumber != null && tipPercentageNumber != null && peopleCountNumber > 0
        }
    val tipAmount: Double
        get() = totalAmount.toDouble() * (tipPercentage.toDouble() / 100)

    val totalWithTip: Double
        get() = totalAmount.toDouble() + tipAmount

    val perPerson: Double
        get() = totalWithTip / peopleCount.toInt()
}