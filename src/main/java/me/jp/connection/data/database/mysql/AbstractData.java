package me.jp.connection.data.database.mysql;

public interface AbstractData {
    void createTable(String table, String... values);
    void insertData(String table, String column, String... values);
    void updateData(String table, String... values);
    void removeData(String table, String... values);
}
