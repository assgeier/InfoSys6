# Практическая работа 6

Небольшая логическая задача с ветвлениями и конечным набором решений.

Сборка и запуск SelfCheck без Maven

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out ru.edu.pr03.SelfCheck
```

Запуск на файле данных

```bash
java -cp out ru.edu.pr03.CsvRunner data/sample.csv
java -cp out ru.edu.pr03.CsvRunner data/my_cases.csv
java -cp out ru.edu.pr03.CsvRunner data/invalid.csv
```

Состав проекта

- ApprovalRoute, RiskLevel: перечисления маршрутов и уровней риска
- ApprovalRouter: правило выбора маршрута, единственный порог FINANCE_LIMIT
- ApprovalRequest: данные одной заявки
- RequestParser: разбор и проверка строки CSV
- CsvRunner: чтение файла и вывод маршрута для каждой строки
- SelfCheck: таблица решений, граничные и ошибочные случаи
- diagrams: Activity-диаграмма и диаграмма классов
