package org.referix.birthdayplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.commands.birthday;
import org.referix.birthdayplugin.discord.Hook;
import org.referix.birthdayplugin.sql.DatabaseManager;
import org.referix.birthdayplugin.utils.ConfigUtils;

import java.time.LocalDate;
import java.util.UUID;

public class WishesListener implements Listener {

    public WishesListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return; // Если событие уже отменено другим плагином, ничего не делаем
        }
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String message = event.getMessage();
        // Проверяем, начинается ли сообщение с "/", что указывает на команду
        if (message.startsWith("/")) {
            return; // Не обрабатываем команды
        }
        if (birthday.isEnterWishes.getOrDefault(playerId, true)) {
            return;
        }
        String mess = message.replace("§x§F§B§F§B§F§B","");

        LocalDate playerBirthday = birthday.playersBirthday.get(playerId);
        DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;
        databaseManager.addBirthdayAndWishes(player.getName(), String.valueOf(playerBirthday), mess);
        if (Hook.isInitialized()){
            Hook.sendPlayerSetBirthDay(player.getName(), String.valueOf(playerBirthday), mess);
        }
        birthday.isEnterWishes.put(playerId, true);
        player.sendMessage(BirthdayPlugin.getInstance().configUtils.userAddConfirm());
        event.setCancelled(true);
    }
}
