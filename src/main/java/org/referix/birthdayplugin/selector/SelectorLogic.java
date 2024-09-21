package org.referix.birthdayplugin.selector;

import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.sql.DatabaseManager;

import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectorLogic {

    DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;
    private List<String> list = databaseManager.getBirthdaysWithNames();
    public Map<String, HashMap<String, String>> getBirthdayMapByMonth() {
        Map<String, HashMap<String, String>> birthdayMapByMonth = new HashMap<>();

        // Инициализация HashMap для каждого месяца
        for (int i = 1; i <= 12; i++) {
            birthdayMapByMonth.put(getMonthName(i), new HashMap<>());
        }
        // Заполнение HashMap для каждого месяца
        for (String entry : list) {
            String[] parts = entry.split("="); // Предполагается, что имя и день рождения разделены символом "="
            if (parts.length == 2) { // Проверка корректности формата
                String name = parts[0].trim();
                String birthday = parts[1].trim();
                int month = getMonthFromDate(birthday);
                if (month != -1) {
                    HashMap<String, String> monthMap = birthdayMapByMonth.get(getMonthName(month));
                    monthMap.put(name, birthday);
                }
            }
        }
        return birthdayMapByMonth;
    }

    // Получение названия месяца по его номеру (1 - январь, 2 - февраль и т.д.)
    private String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    // Получение номера месяца из даты в формате "dd.MM.yyyy"
    private int getMonthFromDate(String date) {
        String[] parts = date.split("-");
        if (parts.length == 3) {
            return Integer.parseInt(parts[1]);
        }
        return -1;
    }



}
