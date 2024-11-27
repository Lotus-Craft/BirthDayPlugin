package org.referix.birthdayplugin;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.referix.birthdayplugin.birthdaysetsystem.BirthdaySetLissener;
import org.referix.birthdayplugin.commands.BirthDayDelete;
import org.referix.birthdayplugin.commands.PresentGet;
import org.referix.birthdayplugin.commands.birthday;
import org.referix.birthdayplugin.commands.chekbirthday;
import org.referix.birthdayplugin.discord.Hook;
import org.referix.birthdayplugin.listeners.WishesListener;
import org.referix.birthdayplugin.sql.DatabaseManager;
import org.referix.birthdayplugin.utils.ConfigUtils;

public final class BirthdayPlugin extends JavaPlugin {

    public DatabaseManager databaseManager;
    private static BirthdayPlugin instance;
    public ConfigUtils configUtils;


    public static BirthdayPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        int pluginId = 23439;
        Metrics metrics = new Metrics(this, pluginId);
        // Проверяем, существует ли папка плагина
        if (!getDataFolder().exists()) {
            // Если папка не существует, создаем ее
            if (getDataFolder().mkdir()) {
                getLogger().info("The plugin folder has been created.");
            } else {
                getLogger().warning("Failed to create the plugin folder!");
            }
        }
        saveDefaultConfig();
        instance = this;
        this.databaseManager = new DatabaseManager(getDataFolder().getPath());
        this.databaseManager.createTable();
        configUtils = new ConfigUtils();
        if (configUtils.getCheckDiscod().equals(true)){
            Hook.init();
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new BirthdayPlaceholderExpansion(this).register();
        } else {
            System.out.println("Placeholder didn't hook");
        }

        new birthday("birthday");
        new BirthDayDelete("birthdaydel");
        new chekbirthday("birthdaycheck");
        new PresentGet("birthdaypresent");
        // Лисенеры
        new WishesListener(this);
        new BirthdaySetLissener(this);
    }

    @Override
    public void onDisable() {
        databaseManager.closeConnection();

    }


}
