package org.referix.birthdayplugin.commands;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.sql.DatabaseManager;
import org.referix.birthdayplugin.utils.ConfigUtils;

import static org.referix.birthdayplugin.birthdaysetsystem.BirthdaySetLissener.isTodayPlayerBirthday;


public class PresentGet extends AbstractCommand{
    public PresentGet(String command) { super(command);}

    DatabaseManager databaseManager = BirthdayPlugin.getInstance().databaseManager;
    private HashMap<UUID, Boolean> isPlayerGotPresent = new HashMap<>();
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        FileConfiguration config = BirthdayPlugin.getInstance().getConfig();
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (!isTodayPlayerBirthday(player, databaseManager)) {
            player.sendMessage(BirthdayPlugin.getInstance().configUtils.todayNotYourBirthday());
            databaseManager.updatePlayerTookPresent(player.getName(), false);
            return false;
        }
        if (databaseManager.getPlayerTookPresent(player.getName())) {
            player.sendMessage(BirthdayPlugin.getInstance().configUtils.playerAlreadyGotPresent());
            return false;
        }
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    Material material = Material.getMaterial(itemSection.getString("type"));
                    if (material == null) {
                        System.out.println("Invalid material type specified for item: " + itemSection.getString("type"));
                        continue;
                    }
                    ItemStack item = new ItemStack(material, itemSection.getInt("amount", 1));
                    ItemMeta meta = item.getItemMeta();

                    if (meta != null) {
                        if (itemSection.contains("name")) {
                            meta.setDisplayName(itemSection.getString("name"));
                        }

                        if (itemSection.contains("enchantments")) {
                            ConfigurationSection enchSection = itemSection.getConfigurationSection("enchantments");
                            for (String enchKey : enchSection.getKeys(false)) {
                                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchKey));
                                if (enchantment != null) {
                                    int level = enchSection.getInt(enchKey);
                                    meta.addEnchant(enchantment, level, true);
                                    System.out.println("Enchantment " + enchKey + " added to the item with level " + level);
                                } else {
                                    System.out.println("Enchantment " + enchKey + " not found.");
                                }
                            }
                        }

                        item.setItemMeta(meta);
                    }

                    player.getInventory().addItem(item);
                }
            }
            databaseManager.updatePlayerTookPresent(player.getName(), true);
        }


    return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return Lists.newArrayList();
    }
}
