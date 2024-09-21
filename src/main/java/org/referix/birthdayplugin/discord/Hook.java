package org.referix.birthdayplugin.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Color;
import org.referix.birthdayplugin.BirthdayPlugin;

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

    public static void sendPlayerHappyBirthDay(String playerName, String date) {
        if (!isInitialized()) return;
        TextChannel channel = jda.getTextChannelById(Objects.requireNonNull(channelId));
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**" + playerName + "**")
                .setDescription("**Today is `" + playerName + "`'s birthday! Let's congratulate them in the Minecraft chat " +
                        "and Discord on this wonderful day. Let's wish them all the best and give them gifts!** \n")
                .setColor(java.awt.Color.GREEN);

        if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    public static void sendAdminDeletedBirthDay(String admin, String player) {
        if (!isInitialized())return;
        TextChannel channel = jda.getTextChannelById(Objects.requireNonNull(channelId));
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Administrator - " + admin + "**")
                .setDescription("Removed the birthday of **" + player + "**\n")
                .setColor(java.awt.Color.RED);

        if (channel != null) {
            channel.sendMessageEmbeds(builder.build()).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

}
