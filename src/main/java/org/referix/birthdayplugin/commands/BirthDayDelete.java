package org.referix.birthdayplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.discord.Hook;
import org.referix.birthdayplugin.sql.DatabaseManager;
import org.referix.birthdayplugin.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BirthDayDelete extends AbstractCommand{

    public BirthDayDelete(String command) {
        super(command);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("birthday.delUser")) return true;
        DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;
        if (args.length == 1){
            if (databaseManager.deletePlayer(args[0])) {
                sender.sendMessage(BirthdayPlugin.getInstance().configUtils.adminDeletedBirthday(sender.getName(), args[0]));
                Hook.sendAdminDeletedBirthDay(sender.getName(), args[0]);
            } else {
                sender.sendMessage("Игрок не был найден");
            }
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));
        }
        return null;
    }
}
