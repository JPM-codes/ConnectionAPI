package me.jp.connection;

import me.jp.connection.data.database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public class ConnectionAPI extends MySQL {

    public ConnectionAPI(String host, int port, String user, String pass, String database) {
        super(host, port, user, pass, database);
    }

    public class Main extends JavaPlugin {
        @Override
        public void onEnable() {
            ConnectionAPI connection = new ConnectionAPI("0.0.0.0", 3306, "root", "root", "myDataBase");
            // Method for connection
            connection.connection();
            // Method to create table
            connection.createTable("myTable", "name TEXT", "age INT", "email VARCHAR(50)");
        }
    }
}
