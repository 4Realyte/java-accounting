# Accounting automation

Это простое приложение с консольным интерфейсом, выполненное в рамках учебного проекта и реализующее следующие __функции__:

1. Считывать годовые и месячные отчеты о расходах/доходах организации из файла формата .csv
2. Сверять данные годовых отчетов с месячными и в случае несоответствия --> выводить информацию о месяце, в котором оно
   выявлено
3. Выводить информацию о самом прибыльном товаре за месяц и самой большое трате
4. Выводить о прибыли по кажому месяцу, среднем доходе и расходе за год

Прилежние написано на языке Java. Пример кода:
````java
HashMap<Integer, ArrayList<Double>> getBalanceForEachMonth() {
        // проходимся по каждому месяцу в таблице
        for (Integer monthNum : balanceForMonths.keySet()) {
            double monthlyExpense = 0;
            double monthlyIncome = 0;
            // получаем хэшмап каждого месяца и присваиваем его переменной
            HashMap<String, ArrayList<String>> reportForMonth = balanceForMonths.get(monthNum);
            // проходимся по каждому списку в таблице
            for (ArrayList<String> balanceForGood : reportForMonth.values()) {
                // проходимся по каждому значению в списке
                for (int i = 0; i < balanceForGood.size(); i++) {
                    // если элемент под индексом i true, то cледующие за ним 2 элемента количество и цена за ед.
                    if ("TRUE".equals(balanceForGood.get(i))) {
                        // перемножаем их и записываем в переменную общих трат за месяц
                        monthlyExpense += Double.parseDouble(balanceForGood.get(i + 1)) * Double.parseDouble(balanceForGood.get(i + 2));
                        // если аналогично если false, записываем в переменную доходов за месяц
                    } else if ("FALSE".equals(balanceForGood.get(i))) {
                        monthlyIncome += Double.parseDouble(balanceForGood.get(i + 1)) * Double.parseDouble(balanceForGood.get(i + 2));
                    }
                }
            }
            // создаем новый список со значениями типа Double
            ArrayList<Double> monthlySum = new ArrayList<>();
            // добавляем суммированные значения всех доходов и трат в список
            monthlySum.add(monthlyIncome);
            monthlySum.add(monthlyExpense);
            // добавляем значения в таблицу по каждому месяцу
            commonMonthBalance.put(monthNum, monthlySum);
        }
        return commonMonthBalance;
    }
````
