package me.jp.connection.data.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    void connect() throws SQLException;
    void createTable(String table, String... attributes);
    void disconnect() throws SQLException;
    Connection getConnection();

    void tableInsert(String table, String... values) throws SQLException;
    void tableUpdate(String table, String value, String update) throws SQLException;
    void tableDelete(String table, String... values) throws SQLException;
}
