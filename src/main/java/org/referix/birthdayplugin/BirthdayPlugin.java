package org.referix.birthdayplugin;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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

import java.util.ArrayList;
import java.util.List;

public final class BirthdayPlugin extends JavaPlugin {

    public DatabaseManager databaseManager;
    private static BirthdayPlugin instance;
    public ConfigUtils configUtils;


    public static BirthdayPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
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
