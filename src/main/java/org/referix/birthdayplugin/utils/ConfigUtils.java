package org.referix.birthdayplugin.utils;

import org.referix.birthdayplugin.BirthdayPlugin;

import java.util.Objects;

public class ConfigUtils {
    public String ZoneTime(){
        return BirthdayPlugin.getInstance().getConfig().getString("currentDate");
    }

    public Boolean getCheckDiscod(){
        return BirthdayPlugin.getInstance().getConfig().getBoolean("Discord-setting.check");
    }

    public  String getDiscordToken(){
        return BirthdayPlugin.getInstance().getConfig().getString("Discord-setting.Discord-token");
    }

    public  String getDiscordChannel(){
        return BirthdayPlugin.getInstance().getConfig().getString("Discord-setting.Discord-channel");
    }

    public  String getPrefixSet(){
        return BirthdayPlugin.getInstance().getConfig().getString("Birthday-boy-prefix");
    }

    public  String userRepitingSend(){
        return Objects.requireNonNull(BirthdayPlugin.getInstance().getConfig().getString("Messages.user-repitsend")).replace("&","§");
    }
    public  String userDataInvalid(){
        return Objects.requireNonNull(BirthdayPlugin.getInstance().getConfig().getString("Messages.user-date-invalid")).replace("&","§");
    }
    public  String userDataAddWishes(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.user-date-addwishes").replace("&","§");
    }
    public  String userDataConfirm(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.user-date-confirm").replace("&","§");
    }
    public  String userAddConfirm(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.user-add-finish").replace("&","§");
    }
    public  String userHappyBirthdayEveryOne(String placeholder){ // Указать %player%
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.user-happy-birthday-message-to-players").replace("&","§").replace("%player%", placeholder);
    }
    public  String userHappyBirthday(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.user-happy-birthday-message").replace("&","§");
    }

    public  String userNoEnterData(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.user-no-enter-data").replace("&","§");
    }

    public  String birthDayWrongFormat(String date){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.birthday-wrong-format").replace("&","§").replace("%date%", date);
    }

    public  String adminDeletedBirthday(String admin, String player){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.admin-delete-birthday").replace("&","§").replace("%admin%", admin).replace("%player%", player);
    }

    public  String playerAlreadyGotPresent(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.player-already-got-present").replace("&","§");
    }

    public  String todayNotYourBirthday(){
        return BirthdayPlugin.getInstance().getConfig().getString("Messages.today-not-your-birthday").replace("&","§");
    }

}
