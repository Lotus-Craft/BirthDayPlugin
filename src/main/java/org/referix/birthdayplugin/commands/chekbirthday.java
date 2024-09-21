package org.referix.birthdayplugin.commands;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.selector.SelectorLogic;
import org.referix.birthdayplugin.sql.DatabaseManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class chekbirthday extends AbstractCommand{
    public chekbirthday(String command) {
        super(command);
    }
    DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        List<String> months_args = new ArrayList<>();
        months_args.add("January");
        months_args.add("February");
        months_args.add("March");
        months_args.add("April");
        months_args.add("May");
        months_args.add("June");
        months_args.add("July");
        months_args.add("August");
        months_args.add("September");
        months_args.add("October");
        months_args.add("November");
        months_args.add("December");
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length == 1) {
            LocalDate date = databaseManager.getBirthDay(args[0]);
            if (date == null) {
                sender.sendMessage("У игрока не указано день рождения");
                return true;
            }
            sender.sendMessage(date.toString());
            return true;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("mo") && months_args.contains(args[1])) {
            SelectorLogic selectorLogic = new SelectorLogic();
            Map<String, HashMap<String, String>> birthdayMapByMonth = selectorLogic.getBirthdayMapByMonth();
            HashMap<String, String> birthdaysInMonth = birthdayMapByMonth.get(args[1]);
            int page = 1;
            if (args.length == 3) {
                page = Integer.parseInt(args[2]);
            }
            int pageSize = 10;
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, birthdaysInMonth.size());

            int totalPages = (int) Math.ceil((double) birthdaysInMonth.size() / pageSize);
            if (page < 1 || page > totalPages) {
                sender.sendMessage("Страницы " + page + " не существует. Доступные страницы: 1 - " + totalPages);
                return true;
            }

            List<Map.Entry<String, String>> entries = new ArrayList<>(birthdaysInMonth.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            int maxNameLength = 0;
            int maxDateLength = 0;

            // Найдем максимальную длину ника и даты
            for (Map.Entry<String, String> entry : entries) {
                maxNameLength = Math.max(maxNameLength, entry.getKey().length());
                maxDateLength = Math.max(maxDateLength, entry.getValue().length());
            }

            // Выводим заголовок страницы
            sender.sendMessage(ChatColor.WHITE + "-------------" + ChatColor.GOLD + " Страница " + page + ChatColor.WHITE + " -------------");

            // Выводим каждую запись с учетом максимальной длины ника и даты
            for (int i = startIndex; i < endIndex; i++) {
                Map.Entry<String, String> entry = entries.get(i);
                String playerName = entry.getKey();
                String birthday = entry.getValue();
                String formattedName = String.format("%-" + (maxNameLength + 2) + "s", playerName);
                String formattedBirthday = String.format("%-" + (maxDateLength + 2) + "s", birthday);
                sender.sendMessage(ChatColor.WHITE + "         Игрок: " + ChatColor.GOLD + formattedName + ChatColor.WHITE + "  Дата: " + ChatColor.GOLD + formattedBirthday);
            }

            // -- Кликабельные страницы ---
            // Создаем один текстовый компонент для всех страниц
            TextComponent allPages = new TextComponent();
            TextComponent start = new TextComponent(ChatColor.GREEN + "             Страницы: ");
            allPages.addExtra(start);
            for (int i = 1; i <= totalPages; i++) {
                if (i != 1) {
                    // Добавляем пробел между страницамы
                    TextComponent space = new TextComponent(" ");
                    allPages.addExtra(space);
                }

                // Получаем кликабельный текст для числа страницы
                TextComponent clickablePage = sendClickableCommandText(args[1], i);

                // Если страница совпадает с текущей, делаем её другого цвета
                if (i == page) {
                    clickablePage.setColor(ChatColor.YELLOW.asBungee()); // Желтый цвет для выбранной страницы
                }

                // Добавляем кликабельную страницу к общему текстовому компоненту
                allPages.addExtra(clickablePage);
            }
            // Отправляем кликабельное сообщ игроку
            player.spigot().sendMessage(allPages);

            return true;
        }
        player.sendMessage(ChatColor.RED + "** Команда введена не верно!");
        return true;
    }

    public TextComponent sendClickableCommandText(String mo, int page) {
        TextComponent message = new TextComponent(String.valueOf(page));
        message.setColor(ChatColor.GREEN.asBungee());

        // Укажите команду, которую вы хотите выполнить. Например, "/help".
        String command = "/birthdaycheck mo " + mo + " " + page;
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        return message;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> playerNames = new ArrayList<>(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));
            playerNames.add("mo");
            return playerNames;
        }
        List<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        if(args.length == 2) return months;
        return Lists.newArrayList();
    }

}
