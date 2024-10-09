package org.referix.birthdayplugin.commands;


import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.sql.DatabaseManager;
import org.referix.birthdayplugin.utils.ConfigUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class birthday extends AbstractCommand {

    public birthday(String command) {
        super(command);
    }
    DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;
    public static HashMap<UUID, Long> confirmationTimes = new HashMap<>();
    public static HashMap<UUID, LocalDate> playersBirthday = new HashMap<>();
    public static HashMap<UUID, Boolean>  isEnterWishes = new HashMap<>();
    private static HashMap<UUID, String> tempData = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("birthday.reload")) {
            sender.sendMessage("Config reloading");
            BirthdayPlugin.getInstance().reloadConfig();
        }
        if (args.length == 2 || args.length == 1 && args[0].equalsIgnoreCase("set")){
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();
            if (databaseManager.isUserInDatabase(player.getName())) {
                player.sendMessage(BirthdayPlugin.getInstance().configUtils.userRepitingSend());
                return false;
            }
            if (args.length == 2) {
                // Проверка на подтверждение даты
                if (confirmationTimes.containsKey(playerId) && (currentTime - confirmationTimes.get(playerId)) < 30000) { // 30 секунд на подтверждение
                    String tempDate = tempData.get(playerId);
                    if (!Objects.equals(tempDate, args[1])) {
                        player.sendMessage(BirthdayPlugin.getInstance().configUtils.userDataInvalid());
                        tempData.remove(playerId);
                        confirmationTimes.remove(playerId);
                        return false;
                    }
                    try {
                        LocalDate date = LocalDate.parse(args[1]);
                        playersBirthday.put(playerId, date);
                        sender.sendMessage(BirthdayPlugin.getInstance().configUtils.userDataAddWishes().replace("%data%", date.toString()));
                        isEnterWishes.put(playerId, false);
                    } catch (DateTimeParseException e) {
                        sender.sendMessage(ChatColor.RED + BirthdayPlugin.getInstance().configUtils.birthDayWrongFormat(args[1]));
                    }
                }
                else {
                    player.sendMessage(BirthdayPlugin.getInstance().configUtils.userDataConfirm().replace("%data%", args[1]));
                    tempData.put(playerId, args[1]);
                    confirmationTimes.put(playerId, currentTime); // Обновление времени для подтверждения
                }

                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("set","reload");
        if(args.length == 2) return Lists.newArrayList("Set your birthday (example: year:2020 - month:12 - day:01)");
        return Lists.newArrayList();
    }
}
