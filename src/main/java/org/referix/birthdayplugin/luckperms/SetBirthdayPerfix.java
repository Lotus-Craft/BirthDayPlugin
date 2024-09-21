package org.referix.birthdayplugin.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.entity.Player;
import org.referix.birthdayplugin.BirthdayPlugin;
import org.referix.birthdayplugin.utils.ConfigUtils;

import java.time.Duration;

public class SetBirthdayPerfix {
    public void setPrefix(Player player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId()); // Получение объекта пользователя
        String prefix = BirthdayPlugin.getInstance().configUtils.getPrefixSet(); // Новый временный префикс
        Node node = PrefixNode.builder(prefix, 100)
                .value(true)// Создание узла префикса с весом 100
                .expiry(Duration.ofDays(1)) // Установка времени истечения срока действия
                .build();
        user.data().add(node); // Добавление временного префикса
        luckPerms.getUserManager().saveUser(user); // Сохранение изменений

    }

    public void removePrefix(Player player) {
        // Получаем экземпляр LuckPerms API
        LuckPerms luckPerms = LuckPermsProvider.get();

        // Получаем объект пользователя LuckPerms для игрока
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        // Удаление префикса с весом 100
        user.data().clear(node -> node instanceof PrefixNode && ((PrefixNode) node).getPriority() == 100);

        // Сохраняем изменения
        luckPerms.getUserManager().saveUser(user);
    }



}
