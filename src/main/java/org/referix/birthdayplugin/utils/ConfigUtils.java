package org.referix.birthdayplugin.utils;

import org.bukkit.ChatColor;
import org.referix.birthdayplugin.BirthdayPlugin;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {

    // Регулярний вираз для знаходження Hex-кольорів у форматі &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    // Метод для конвертації рядка з Hex-кольорами у формат, який підтримується Minecraft
    private String formatColors(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + hexColor).toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public String ZoneTime(){
        return BirthdayPlugin.getInstance().getConfig().getString("currentDate");
    }

    public Boolean getCheckDiscod(){
        return BirthdayPlugin.getInstance().getConfig().getBoolean("Discord-setting.check");
    }

    public String getDiscordToken(){
        return BirthdayPlugin.getInstance().getConfig().getString("Discord-setting.Discord-token");
    }

    public String getDiscordChannel(){
        return BirthdayPlugin.getInstance().getConfig().getString("Discord-setting.Discord-channel");
    }

    public String getPrefixSet(){
        return BirthdayPlugin.getInstance().getConfig().getString("Birthday-boy-prefix");
    }

    public String userRepitingSend(){
        String message = Objects.requireNonNull(BirthdayPlugin.getInstance().getConfig().getString("Messages.user-repitsend"));
        return formatColors(message);
    }

    public String userDataInvalid(){
        String message = Objects.requireNonNull(BirthdayPlugin.getInstance().getConfig().getString("Messages.user-date-invalid"));
        return formatColors(message);
    }

    public String userDataAddWishes(){
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.user-date-addwishes");
        return formatColors(message);
    }

    public String userDataConfirm(){
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.user-date-confirm");
        return formatColors(message);
    }

    public String userAddConfirm(){
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.user-add-finish");
        return formatColors(message);
    }

    public String userHappyBirthdayEveryOne(String placeholder, int age) {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.user-happy-birthday-message-to-players");
        message = message.replace("%player%", placeholder).replace("%age%", String.valueOf(age));
        return formatColors(message);
    }

    public String userHappyBirthday(int age) {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.user-happy-birthday-message").replace("%age%", String.valueOf(age));
        return formatColors(message);
    }

    public String userNoEnterData() {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.user-no-enter-data");
        return formatColors(message);
    }

    public String birthDayWrongFormat(String date) {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.birthday-wrong-format");
        message = message.replace("%date%", date);
        return formatColors(message);
    }

    public String adminDeletedBirthday(String admin, String player) {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.admin-delete-birthday");
        message = message.replace("%admin%", admin).replace("%player%", player);
        return formatColors(message);
    }

    public String playerAlreadyGotPresent() {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.player-already-got-present");
        return formatColors(message);
    }

    public String todayNotYourBirthday() {
        String message = BirthdayPlugin.getInstance().getConfig().getString("Messages.today-not-your-birthday");
        return formatColors(message);
    }
}
