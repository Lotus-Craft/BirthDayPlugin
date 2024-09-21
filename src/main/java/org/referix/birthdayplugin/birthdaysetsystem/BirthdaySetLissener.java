package org.referix.birthdayplugin.birthdaysetsystem;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.luckperms.SetBirthdayPerfix;
import org.referix.birthdayplugin.sql.DatabaseManager;
import org.referix.birthdayplugin.utils.ConfigUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

import static org.referix.birthdayplugin.discord.Hook.sendPlayerHappyBirthDay;


public class BirthdaySetLissener implements Listener {

    String nameBirthDayBoy;
    DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;
    SetBirthdayPerfix setBirthdayPerfix = new SetBirthdayPerfix();
    public BirthdaySetLissener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void birthdayJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String playerName = e.getPlayer().getName();

        // Получаем дату дня рождения игрока из базы данных
        LocalDate birthdayFromDB = databaseManager.getBirthDay(playerName);

        // Сравниваем даты
        if (isTodayPlayerBirthday(p, databaseManager)) {
                p.sendMessage(BirthdayPlugin.getInstance().configUtils.userHappyBirthday());
                spawnFireworks(p.getLocation(), 5);
                nameBirthDayBoy = p.getName();
            if (!databaseManager.getPlayerIsCongratulate(playerName)) {
                setBirthdayPerfix.setPrefix(p); // вызываем метод установки префикса только один раз
                sendAllPlayers(BirthdayPlugin.getInstance().configUtils.userHappyBirthdayEveryOne(playerName));
                sendPlayerHappyBirthDay(playerName,birthdayFromDB.toString());
                databaseManager.updatePlayerIsCongratulate(playerName, true);
//                birthdayPrefixSet.put(e.getPlayer().getUniqueId(), true);
            }
        } else {
            nameBirthDayBoy = null;
            setBirthdayPerfix.removePrefix(p);
            databaseManager.updatePlayerIsCongratulate(playerName, false);
            //System.out.println("День рождения игрока " + playerName + " не совпадает с текущей датой в Москве.");
        }
    }

    public void sendAllPlayers(String message){
        for (Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(message);
        }
    }
    private void spawnFireworks(Location location, int amount) {
        for (int i = 0; i < amount; i++) {
            Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();

            // Устанавливаем эффект фейерверка
            FireworkEffect.Builder builder = FireworkEffect.builder();
            builder.withColor(Color.RED); // Замените цвет на тот, который вы хотите
            builder.with(FireworkEffect.Type.BALL);
            meta.addEffect(builder.build());

            meta.setPower(1); // Устанавливаем силу фейерверка

            firework.setFireworkMeta(meta);
        }
    }

    public static boolean isTodayPlayerBirthday(Player p, DatabaseManager databaseManager) {
        LocalDate birthdayFromDB = databaseManager.getBirthDay(p.getName());
        if (birthdayFromDB == null){
            p.sendMessage(BirthdayPlugin.getInstance().configUtils.userNoEnterData());
            return false;
        }

        LocalDate currentDateInMoscow = LocalDate.now(ZoneId.of(BirthdayPlugin.getInstance().configUtils.ZoneTime()));
        if (birthdayFromDB.getMonthValue() == currentDateInMoscow.getMonthValue() &&
                birthdayFromDB.getDayOfMonth() == currentDateInMoscow.getDayOfMonth()) {
            return true;
        }
        return false;
    }


}
