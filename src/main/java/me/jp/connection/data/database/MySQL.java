package me.jp.connection.data.database;

import me.jp.connection.data.database.mysql.AbstractData;

import java.sql.*;
import java.util.Collections;

public class MySQL implements AbstractData {
    private Connection connection;
    private final String url;
    private final String user;
    private final String pass;

    public MySQL(String host, int port, String user, String pass, String database) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
        this.user = user;
        this.pass = pass;
    }

    public void connection() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
        }
    }

    @Override
    public void createTable(String table, String... columns) {
        if (columns.length == 0) {
            System.err.println("Erro: É necessário especificar as colunas para criar a tabela.");
            return;
        }
        String columnsDefinition = String.join(", ", columns);
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (" + columnsDefinition + ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Tabela '" + table + "' criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela: " + e.getMessage());
        }
    }

    @Override
    public void insertData(String table, String columns, String... values) {
        String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));

        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ");";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }

            preparedStatement.executeUpdate();
            System.out.println("Dados inseridos com sucesso na tabela '" + table + "'!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao inserir dados: " + e.getMessage());
        }
    }

    @Override
    public void updateData(String table, String... values) {
        if (values.length < 3) {
            System.err.println("Erro: Deve fornecer pelo menos 3 valores (coluna, novo valor e condição).");
            return;
        }

        String column = values[0];
        String newValue = values[1];
        String condition = values[2];
        String sql = "UPDATE " + table + " SET " + column + " = ? WHERE " + condition + ";";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newValue);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Dados atualizados com sucesso na tabela '" + table + "'!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dados: " + e.getMessage());
        }
    }

    @Override
    public void removeData(String table, String... values) {
        if (values.length < 1) {
            System.err.println("Erro: Deve fornecer pelo menos 1 valor (condição de remoção).");
            return;
        }

        String condition = values[0];
        String sql = "DELETE FROM " + table + " WHERE " + condition + ";";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Dados removidos com sucesso da tabela '" + table + "'!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover dados: " + e.getMessage());
        }
    }

    public boolean hasData(String table, String... conditions) {
        if (conditions.length == 0) {
            System.err.println("Erro: Nenhuma condição fornecida para verificação de dados.");
            return false;
        }

        String combinedConditions = String.join(" AND ", conditions);
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE " + combinedConditions + ";";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Retorna true se existir pelo menos um registro
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar dados: " + e.getMessage());
        }
        return false; // Retorna false em caso de erro ou se não houver dados
    }
}
