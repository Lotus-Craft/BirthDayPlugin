package org.referix.birthdayplugin.sql;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String pluginFolder) {
        String dbUrl = "jdbc:sqlite:" + pluginFolder + "/birthdayplugin.db";
        try {
            connection = DriverManager.getConnection(dbUrl);
            System.out.println("Соединение с базой данных установлено.");
        } catch (SQLException e) {
            System.err.println("Ошибка при установлении соединения с базой данных: " + e.getMessage());
        }
    }

    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "birthday DATE NOT NULL, " +
                "wishes TEXT NOT NULL, " +
                "tookPresent BOOLEAN, " +
                "isCongratulate BOOLEAN)";
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public boolean addBirthdayAndWishes(String playerName, String birthday, String wishes) {
        String checkQuery = "SELECT * FROM players WHERE name = ?";
        String insertQuery = "INSERT INTO players (name, birthday, wishes, tookPresent, isCongratulate) VALUES (?, ?, ?, ?, ?)";

        try {
            // Проверяем, существует ли уже запись с указанным именем
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, playerName);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Запись уже существует, возвращаем false
                System.err.println("Запись для игрока '" + playerName + "' уже существует.");
                return false;
            } else {
                // Записи не существует, добавляем новую запись
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, playerName);
                insertStatement.setString(2, birthday);
                insertStatement.setString(3, wishes);
                insertStatement.setBoolean(4, false);
                insertStatement.setBoolean(5, false);
                insertStatement.executeUpdate();
                System.out.println("Добавлена запись для игрока '" + playerName + "' с днем рождения " + birthday);
                System.out.println("Пожелания: " + wishes);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении записи: " + e.getMessage());
            return false;
        }
    }


    public LocalDate getBirthDay(String playerName) {
        String findPlayer = "SELECT birthday FROM players WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(findPlayer)) {
            stmt.setString(1, playerName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dateString = rs.getString("birthday");
                    if (dateString != null) {
                        return LocalDate.parse(dateString);  // Преобразование строки в LocalDate
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getPlayerIsCongratulate(String playerName) {
        String query = "SELECT isCongratulate FROM players WHERE name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerName);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("isCongratulate");
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            return true;
        }
    }

    public boolean getPlayerTookPresent(String playerName) {
        String query = "SELECT tookPresent FROM players WHERE name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerName);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("tookPresent");
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            return true;
        }
    }

    public void updatePlayerTookPresent(String playerName, Boolean state){
        String query = "UPDATE players SET tookPresent = ? WHERE name = ?";
        try {
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setBoolean(1, state);
            insertStatement.setString(2, playerName);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении записи: " + e.getMessage());
        }
    }

    public void updatePlayerIsCongratulate(String playerName, Boolean state){
        String query = "UPDATE players SET isCongratulate = ? WHERE name = ?";
        try {
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setBoolean(1, state);
            insertStatement.setString(2, playerName);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении записи: " + e.getMessage());
        }
    }

    public boolean deletePlayer(String playerName) {
        String deleteQuery = "DELETE FROM players WHERE name = ?";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setString(1, playerName);
            int affectedRows = deleteStatement.executeUpdate();

            if (affectedRows > 0) { // Удаление записи
                return true;
            } else { // Игрок не найден
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении записи: " + e.getMessage());
            return false;
        }
    }

    public boolean isUserInDatabase(String playerName) {
        String query = "SELECT birthday FROM players WHERE name = ?";
        try {
            PreparedStatement checkStatement = connection.prepareStatement(query);
            checkStatement.setString(1, playerName);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public List<String> getBirthdaysWithNames() {
        String query = "SELECT name, birthday FROM players ORDER BY birthday";
        List<String> birthdaysWithNames = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                while (resultSet.next()) {
                    String playerName = resultSet.getString("name");
                    LocalDate birthday = resultSet.getObject("birthday", LocalDate.class);
                    String formattedBirthday = birthday.format(formatter);
                    String entry = playerName + " = " + formattedBirthday;
                    birthdaysWithNames.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Лучше использовать логгирование в реальных приложениях
            return null;  // Возвращаем null для индикации ошибки
        }
        return birthdaysWithNames;
    }



    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Соединение с базой данных закрыто.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения с базой данных: " + e.getMessage());
        }
    }
}
