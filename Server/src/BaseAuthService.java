
import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseAuthService implements AuthService {
    private class Entry {
        public int id;
        public String login;
        public String pass;
        public String nick;

        public Entry(int id, String login, String pass, String nick) {
            this.id = id;
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }

        @Override
        public String toString() {
            return String.format("id: %s | Логин: %s | Пароль: %s | Ник: %s", this.id, this.login, this.pass, this.nick);
        }
    }

    private List<Entry> entries;

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }


    static Connection connection;
    static Statement statement;

    public void createDB () throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public List<Entry> getAllProducts() {

        // Statement используется для того, чтобы выполнить sql-запрос
        try (Statement statement = this.connection.createStatement()) {

            List<Entry> products = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery("SELECT id, login, password, nick FROM products");

            while (resultSet.next()) {
                entries.add(new Entry(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("nick")));
            }
            // Возвращаем наш список
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
    }

    public BaseAuthService() throws SQLException {
        createDB();
//        ResultSet resultSet = statement.executeQuery("SELECT id, login, password, nick FROM entries");
//        entries = new ArrayList<>();
//        while (resultSet.next()) {
//            entries.add(new Entry(resultSet.getInt("id"),
//                    resultSet.getString("login"),
//                    resultSet.getString("password"),
//                    resultSet.getString("nick")));
//        }
        getAllProducts();
    }
    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (Entry o : entries) {
            if (o.login.equals(login) && o.pass.equals(pass)) return o.nick;
        }
        return null;
    }
}
