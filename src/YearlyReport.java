import java.util.ArrayList;
import java.util.HashMap;

public class YearlyReport {
    HashMap<Integer, ArrayList<String>> balanceForYear = new HashMap<>();
    HashMap<Integer, ArrayList<Double>> balanceForMonthInYear = new HashMap<>();

    void saveReport(String fileContents) {
        String[] lines = fileContents.split(System.lineSeparator());
        for (int i = 1; i < lines.length; i++) {
            String[] lineContents = lines[i].split(",");
            if (balanceForYear.containsKey(Integer.parseInt(lineContents[0]))) {
                ArrayList<String> yearlyList = balanceForYear.get(Integer.parseInt(lineContents[0]));
                for (int j = 1; j < lineContents.length; j++) {
                    if (j + 1 < lineContents.length && lineContents[j + 1].equals("true")) {
                        lineContents[j] = "-" + lineContents[j];
                    }
                    yearlyList.add(lineContents[j]);
                }
            } else {
                ArrayList<String> yearlyList = new ArrayList<>();
                for (int j = 1; j < lineContents.length; j++) {
                    if (j + 1 < lineContents.length && lineContents[j + 1].equals("true")) {
                        lineContents[j] = "-" + lineContents[j];
                    }
                    yearlyList.add(lineContents[j]);
                }
                balanceForYear.put(Integer.parseInt(lineContents[0]), yearlyList);
            }
        }
    }

    HashMap<Integer, ArrayList<Double>> getBalanceForMonthInYear() {
        for (Integer monthNum : balanceForYear.keySet()) {
            ArrayList<String> yearlyList = balanceForYear.get(monthNum);
            double monthlyIncome = 0;
            double monthlyExpense = 0;
            for (int i = 0; i < yearlyList.size(); i++) {
                if (i + 1 < yearlyList.size() && yearlyList.get(i + 1).equals("true")) {
                    monthlyExpense += Double.parseDouble(yearlyList.get(i));
                } else if (i + 1 < yearlyList.size() && yearlyList.get(i + 1).equals("false")) {
                    monthlyIncome += Double.parseDouble(yearlyList.get(i));
                }
            }
            ArrayList<Double> monthlySum = new ArrayList<>();
            monthlySum.add(monthlyIncome);
            monthlySum.add(monthlyExpense);
            balanceForMonthInYear.put(monthNum, monthlySum);
        }
        return balanceForMonthInYear;
    }

    void printProfitForMonths(MonthlyReport monthlyReport) {
        for (Integer monthNum : balanceForMonthInYear.keySet()) {
            double profitOrWaste = 0;
            ArrayList<Double> monthlySum = balanceForMonthInYear.get(monthNum);
            for (Double value : monthlySum) {
                profitOrWaste += value;
            }
            if (profitOrWaste > 0) {
                System.out.println("Прибыль за месяц " + monthlyReport.getMonthName(monthNum) + " составила " + profitOrWaste + " руб.");
            } else {
                System.out.println("Убыток за месяц " + monthlyReport.getMonthName(monthNum) + " составил " + profitOrWaste + " руб.");
            }
        }
    }

    int getYearByPath(String pathName) {
        String[] pathContents = pathName.split("\\Q.\\E");
        int year = Integer.parseInt(pathContents[1]);
        return year;
    }

    void printAverageExpenseAndIncome() {
        double commonIncome = 0;
        double commonExpense = 0;
        for (ArrayList<Double> monthlyExpense : balanceForMonthInYear.values()) {
            commonIncome += monthlyExpense.get(0);
            commonExpense += monthlyExpense.get(1);
        }
        double averageIncome = commonIncome / balanceForMonthInYear.size();
        double averageExpense = commonExpense / balanceForMonthInYear.size();
        System.out.println("Средний расход за все месяцы в " + getYearByPath("resources/y.2021.csv") +
                " году: " + averageExpense);
        System.out.println("Средний доход за все месяцы в " + getYearByPath("resources/y.2021.csv") +
                " году: " + averageIncome);
    }
}
