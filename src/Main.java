import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MonthlyReport monthlyReport = new MonthlyReport();
        YearlyReport yearlyReport = new YearlyReport();

        while (true) {
            printMenu();
            int userInput = scanner.nextInt();

            if (userInput == 1) {
                for (int i = 1; i < 4; i++) {
                    String fileContents = readFileContentsOrNull("resources/m.20210" + i + ".csv");
                    monthlyReport.saveReport(i, fileContents);
                }
            } else if (userInput == 2) {
                String fileContents = readFileContentsOrNull("resources/y.2021.csv");
                yearlyReport.saveReport(fileContents);
            } else if (userInput == 3) {
                checkReports(monthlyReport, yearlyReport);
            } else if (userInput == 4) {
                printMonthlyStat(monthlyReport);
            } else if (userInput == 5) {
                printYearlyStat(monthlyReport, yearlyReport);
            } else if (userInput == 0) {
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Считать все месячные отчёты");
        System.out.println("2 - Считать годовой отчёт");
        System.out.println("3 - Сверить отчёты");
        System.out.println("4 - Вывести информацию о всех месячных отчётах");
        System.out.println("5 - Вывести информацию о годовом отчёте");
        System.out.println("0 - Для выхода из программы");
    }

    private static String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно, файл не находится в нужной директории.");
            return null;
        }
    }

    public static void checkReports(MonthlyReport monthlyReport, YearlyReport yearlyReport) {
        if (monthlyReport.balanceForMonths.isEmpty()) {
            System.out.println("Сверка невозможна. Месячные отчёты не считаны, либо файлы отсутствуют");
        } else if (yearlyReport.balanceForYear.isEmpty()) {
            System.out.println("Сверка невозможна. Годовые отчёты не считаны, либо файлы отсутствуют");
        } else {
            boolean isProblem = false;
            HashMap<Integer, ArrayList<Double>> monthBalanceToCompare = monthlyReport.getBalanceForEachMonth();
            HashMap<Integer, ArrayList<Double>> yearBalanceToCompare = yearlyReport.getBalanceForMonthInYear();
            for (Integer month : monthBalanceToCompare.keySet()) {
                ArrayList<Double> monthToCompare = monthBalanceToCompare.get(month);
                for (int i = 0; i < monthToCompare.size(); i++) {
                    ArrayList<Double> yearToCompare = yearBalanceToCompare.get(month);
                    if (!monthToCompare.equals(yearToCompare)) {
                        System.out.println("Обнаружено несоответствие в месяце " + month + " " + monthlyReport.getMonthName(month));
                        isProblem = true;
                        break;
                    }
                }
            }
            if (!isProblem) {
                System.out.println("Сверка проведена успешно!");
            }
        }
    }

    public static void printMonthlyStat(MonthlyReport monthlyReport) {
        HashMap<Integer, ArrayList<Double>> monthBalanceToPrint = monthlyReport.getBalanceForEachMonth();
        for (Integer monthNum : monthBalanceToPrint.keySet()) {
            System.out.println(monthlyReport.getMonthName(monthNum));
            System.out.println("Самый прибыльный товар: " + monthlyReport.getMaxProfitableGoods(monthNum));
            System.out.println("Самая большая трата: " + monthlyReport.getMaxCostlyGoods(monthNum));
        }
    }

    public static void printYearlyStat(MonthlyReport monthlyReport, YearlyReport yearlyReport) {
        System.out.println(yearlyReport.getYearByPath("resources/y.2021.csv"));
        yearlyReport.printProfitForMonths(monthlyReport);
        yearlyReport.printAverageExpenseAndIncome();
    }
}

