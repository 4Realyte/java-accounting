import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReport {
    MonthlyReport() {
        monthToData.put(1, "Январь");
        monthToData.put(2, "Февраль");
        monthToData.put(3, "Март");
        monthToData.put(4, "Апрель");
        monthToData.put(5, "Май");
        monthToData.put(6, "Июнь");
        monthToData.put(7, "Июль");
        monthToData.put(8, "Август");
        monthToData.put(9, "Сентябрь");
        monthToData.put(10, "Октябрь");
        monthToData.put(11, "Ноябрь");
        monthToData.put(12, "Декабрь");
    }

    HashMap<Integer, HashMap<String, ArrayList<String>>> balanceForMonths = new HashMap<>();
    HashMap<Integer, String> monthToData = new HashMap<>();
    HashMap<Integer, ArrayList<Double>> commonMonthBalance = new HashMap<Integer, ArrayList<Double>>();

    void saveReport(Integer month, String fileContents) {
        HashMap<String, ArrayList<String>> reportForMonth = new HashMap<>();
        String[] lines = fileContents.split(System.lineSeparator());
        // проходимся по массиву изначальных строк, начиная с 1 элемента, так как заголовок не нужен
        for (int i = 1; i < lines.length; i++) {
            // сплитим кажду строку изначального массива на подстроки
            String[] lineContents = lines[i].split(",");
            // Проверяем содержится ли таблице ключ с таким же наименованием товара
            if (reportForMonth.containsKey(lineContents[0])) {
                // если да, то получаем ссылку на список по данному ключу
                ArrayList<String> monthlyList = reportForMonth.get(lineContents[0]);
                // проходимся по массиву строк и добавляем каждый элемент в список
                for (int j = 1; j < lineContents.length; j++) {
                    // если массив содержит трату, то добавляем к цене за единицу отрицательное значение
                    if ("TRUE".equals(lineContents[j])) {
                        lineContents[3] = "-" + lineContents[3];
                    }
                    monthlyList.add(lineContents[j]);
                }
                // Если ключа с таким же товаром нет, то создаем новый список
            } else {
                ArrayList<String> monthlyList = new ArrayList<>();
                // и добваляем каждый элемент в список
                for (int j = 1; j < lineContents.length; j++) {
                    // если массив содержит трату аналогично добавляем минус
                    if ("TRUE".equals(lineContents[j])) {
                        lineContents[3] = "-" + lineContents[3];
                    }
                    monthlyList.add(lineContents[j]);
                }
                // добавляем список в таблицу с ключом: наименование товара
                reportForMonth.put(lineContents[0], monthlyList);
            }
        }
        // добавляем таблицу с ключами по наименованию товара в общую таблицу с ключом по месяцу
        balanceForMonths.put(month, reportForMonth);
    }

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

    String getMaxProfitableGoods(Integer monthNum) {
        String goodsName = "";
        double maxProfit = 0;
        HashMap<String, ArrayList<String>> reportForMonth = balanceForMonths.get(monthNum);
        for (String name : reportForMonth.keySet()) {
            double sum = 0;
            ArrayList<String> goodsValues = reportForMonth.get(name);
            for (int i = 0; i < goodsValues.size(); i++) {
                if ("FALSE".equals(goodsValues.get(i))) {
                    sum += Double.parseDouble(goodsValues.get(i + 1)) * Double.parseDouble(goodsValues.get(i + 2));
                }
            }
            if (sum > maxProfit) {
                maxProfit = sum;
                goodsName = name;
            }
        }
        return goodsName + " сумма " + Double.toString(maxProfit);
    }

    String getMaxCostlyGoods(Integer monthNum) {
        String goodsName = "";
        double maxCost = 0;
        HashMap<String, ArrayList<String>> reportForMonth = balanceForMonths.get(monthNum);
        for (String name : reportForMonth.keySet()) {
            double sum = 0;
            ArrayList<String> goodsValues = reportForMonth.get(name);
            for (int i = 0; i < goodsValues.size(); i++) {
                if ("TRUE".equals(goodsValues.get(i))) {
                    sum += Double.parseDouble(goodsValues.get(i + 1)) * Double.parseDouble(goodsValues.get(i + 2));
                }
            }
            if (sum < maxCost) {
                maxCost = sum;
                goodsName = name;
            }
        }
        return goodsName + " сумма " + Double.toString(maxCost);
    }

    public String getMonthName(Integer monthNum) {
        String monthName = monthToData.get(monthNum);
        return monthName;
    }

    public HashMap<Integer, HashMap<String, ArrayList<String>>> getBalanceForMonths() {
        return balanceForMonths;
    }
}
