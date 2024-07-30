package me.jp.connection.data;

import lombok.Getter;
import lombok.Setter;
import me.jp.connection.data.database.Database;

import java.sql.*;



@Getter
@Setter
public class MySQLDatabase implements Database {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public MySQLDatabase(String host, int port, String database, String user, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.user = user;
        this.password = password;
    }


    @Override
    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public void connect(String host, String port, String database, String user, String password) throws SQLException {
        this.url = "jdbc:mysql//" + host + ":" + port + "/" + database;
        this.connection = DriverManager.getConnection(url, user, password);
    }

    @Override
    public void createTable(String table, String... attributes) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXIST " + table + " (");
        for (int i = 0; i < attributes.length; i++) {
            query.append(attributes[i]);
            if (i < attributes.length - 1) {
                query.append(", ");
            }
        }
        query.append(");");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public void tableInsert(String table, String... values) throws SQLException {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            placeholders.append("?,");
        }

        String query = "INSERT INTO " + table + " VALUES (" + placeholders.substring(0, placeholders.length() - 1) + ")";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            ps.executeUpdate();
        }
    }

    @Override
    public void tableUpdate(String table, String value, String update) throws SQLException {
        String query = "UPDATE " + table + " SET " + update + " WHERE " + value;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        }
    }

    @Override
    public void tableDelete(String table, String... values) throws SQLException {
        StringBuilder condition = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            condition.append(values[i]).append(" AND ");
        }

        String query = "DELETE FROM " + table + " WHERE " + condition.substring(0, condition.length() - 5);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        }
    }
}