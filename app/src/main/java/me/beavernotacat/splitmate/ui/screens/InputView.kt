package me.beavernotacat.splitmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import me.beavernotacat.splitmate.models.Split


@Composable
fun InputView(
    split: Split,
    onCalculateClicked: () -> Unit,
    onTotalAmountChanged: (String) -> Unit,
    onPeopleCountChanged: (String) -> Unit,
    onTipPercentageChanged: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Введите сумму счета",
            modifier = Modifier.padding(bottom = 32.dp),
            style = MaterialTheme.typography.headlineLarge
        )

        TextField(
            value = split.totalAmount,
            onValueChange = onTotalAmountChanged,
            label = { Text("Полная сумма счета (₽)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = split.peopleCount,
            onValueChange = onPeopleCountChanged,
            label = { Text("Количество человек") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = split.tipPercentage,
            onValueChange = onTipPercentageChanged,
            label = { Text("Чаевые (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCalculateClicked,
            enabled = split.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
    }
}
