package org.referix.birthdayplugin.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Color;
import org.referix.birthdayplugin.BirthdayPlugin;

import java.util.List;
import java.util.Objects;

public class Hook {

    private static final String channelId = BirthdayPlugin.getInstance().configUtils.getDiscordChannel();
    private static JDA jda;

    public static void init() {

            try {
                jda = JDABuilder.createLight(BirthdayPlugin.getInstance().configUtils.getDiscordToken())
                        .build()
                        .awaitReady();
                System.out.println("Discord successfully started");

            } catch (Exception e) {
                System.out.println(Color.RED + "Error with initializing Discord hook");
                e.printStackTrace();
            }


    }
    public static boolean isInitialized() {
        return jda != null;
    }

    public static void sendPlayerSetBirthDay(String playerName, String date, String wish) {
        TextChannel channel = jda.getTextChannelById(Objects.requireNonNull(channelId));
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**" + playerName + "**")
                .setDescription("Set their birthday to **" + date + "**\n" + "Wishes: **" + wish + "**\n");

        if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    // Метод для отправки сообщения на день рождения
    public static void sendPlayerHappyBirthDay(String playerName, String date) {
        if (!isInitialized()) return;

        TextChannel channel = jda.getTextChannelById(Objects.requireNonNull(channelId));
        EmbedBuilder builder = new EmbedBuilder();

        // Получаем список сообщений на день рождения из конфига
        List<String> birthdayMessages = BirthdayPlugin.getInstance().getConfig().getStringList("Discord-setting.birthday-messages");

        // Проходим по каждому сообщению, заменяем плейсхолдеры и добавляем их в EmbedBuilder
        for (String message : birthdayMessages) {
            String formattedMessage = message.replace("{player}", playerName).replace("{date}", date);
            builder.setDescription(formattedMessage);
        }

        builder.setTitle("**" + playerName + "**")
                .setColor(java.awt.Color.GREEN);

        if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    // Метод для отправки сообщения об удалении дня рождения администратором
    public static void sendAdminDeletedBirthDay(String admin, String player) {
        if (!isInitialized()) return;

        TextChannel channel = jda.getTextChannelById(Objects.requireNonNull(channelId));
        EmbedBuilder builder = new EmbedBuilder();

        // Получаем сообщение об удалении из конфига
        String message = BirthdayPlugin.getInstance().getConfig().getString("Discord-setting.admin-remove-message");
        if (message != null) {
            // Заменяем плейсхолдеры
            String formattedMessage = message.replace("{admin}", admin).replace("{player}", player);
            builder.setDescription(formattedMessage);
        } else {
            builder.setDescription("Administrator removed birthday.");
        }

        builder.setTitle("**Administrator - " + admin + "**")
                .setColor(java.awt.Color.RED);

        if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

}
