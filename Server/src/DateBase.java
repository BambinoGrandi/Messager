import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DateBase implements AuthService {
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
            return String.format("id %s | Логин: %s | Пароль: %s | Ник: %s", this.id, this.login, this.pass, this.nick);
        }
    }

    private static final String CON_STR = "jdbc:sqlite:ClientHandler";

    private static DateBase instance = null;

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }

    private Connection connection;

    private List<Entry> entries;

   public DateBase() throws SQLException{
           DriverManager.registerDriver(new JDBC());
           this.connection = DriverManager.getConnection(CON_STR);
        Statement statement = this.connection.createStatement();
            //В данный список будем загружать клиентовб полученых из БД
            entries = new ArrayList<>();
            //В ResultSet будет храниться результат нашего запроса,
            //который выполняется командой statement.executeQuery(
            ResultSet resultSet = statement.executeQuery("SELECT id, login, password, nick FROM entries");
            //Проходимся по нашему запросу и заносим данные в коллекцию
            while (resultSet.next()){
                entries.add(new Entry(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("nick")));
            }
   }

    @Override
    public String getNickByLoginPass(String login, String pass) {
            for (Entry o : entries) {
                if (o.login.equals(login) && o.pass.equals(pass)) return o.nick;
            }
        return null;
    }
}
