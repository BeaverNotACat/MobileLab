package me.beavernotacat.splitmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.beavernotacat.splitmate.models.Split

@Composable
fun ResultView(
    split: Split,
    onBackToEdit: () -> Unit,
    onNewCalculation: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "Результат расчета",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.headlineLarge

        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Начальная сумма:"
            )
            Text(
                text = "${split.totalAmount}₽"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Начальная сумма:"
            )
            Text(
                text = "${split.totalAmount}₽"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Чаевые (${split.tipPercentage}%):"
            )
            Text(
                text = "${split.tipAmount}₽"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Итого с чаевыми:"
            )
            Text(
                text = "${split.totalWithTip}₽"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "На каждого человека:",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${split.perPerson}₽",
                fontWeight = FontWeight.Black
            )
        }
        Button(
            onClick = onBackToEdit,
        ) {
            Text("Редактировать")
        }

        Button(
            onClick = onNewCalculation,
        ) {
            Text("Новый расчет")
        }

    }
}


