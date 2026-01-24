package me.beavernotacat.quiztrainer.models

val questionsList = listOf(
    Question(
        "Какой оператор используется для выборки данных из таблицы?",
        listOf("GET", "SELECT", "FETCH", "EXTRACT"),
        1
    ),
    Question(
        "Какой оператор используется для добавления новых строк в таблицу?",
        listOf("ADD", "INSERT", "UPDATE", "CREATE"),
        1
    ),
    Question(
        "Какой оператор используется для изменения существующих данных?",
        listOf("MODIFY", "CHANGE", "UPDATE", "ALTER"),
        2
    ),
    Question(
        "Какой оператор удаляет строки из таблицы?",
        listOf("REMOVE", "DROP", "DELETE", "TRUNCATE"),
        2
    ),
    Question(
        "Какой оператор используется для создания новой таблицы?",
        listOf("NEW TABLE", "ADD TABLE", "CREATE TABLE", "MAKE TABLE"),
        2
    ),
    Question(
        "Какое ключевое слово используется для фильтрации строк?",
        listOf("WHERE", "FILTER", "HAVING", "ORDER BY"),
        0
    ),
    Question(
        "Какое ключевое слово используется для сортировки результата?",
        listOf("SORT BY", "ORDER BY", "GROUP BY", "ALIGN BY"),
        1
    ),
    Question(
        "Какое агрегатное значение считает количество строк?",
        listOf("SUM()", "COUNT()", "TOTAL()", "AMOUNT()"),
        1
    ),
    Question(
        "Какой тип JOIN возвращает только совпадающие строки из обеих таблиц?",
        listOf("LEFT JOIN", "RIGHT JOIN", "FULL JOIN", "INNER JOIN"),
        3
    ),
    Question(
        "Какое ограничение обеспечивает уникальность значений в столбце?",
        listOf("PRIMARY KEY", "FOREIGN KEY", "UNIQUE", "INDEX"),
        2
    ),
    Question(
        "Посчитайте, сколько раз сыр упоминается в библии",
        listOf("1", "2", "3", "68"),
        2
    )
)