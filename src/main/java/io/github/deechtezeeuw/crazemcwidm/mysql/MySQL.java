package io.github.deechtezeeuw.crazemcwidm.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
//    protected String host = "localhost";
//    protected String port = "3306";
//    protected String database = "crazemc";
//    protected String username = "root";
//    protected String password = "lol123lol";
    protected String host = "141.95.117.156";
    protected String port = "3306";
    protected String database = "s1_crazemc";
    protected String username = "u1_GgY2EcolNa";
    protected String password = "7YIfckx!!dfH0Y8X=AZKNP+A";

    private Connection connection;

    public SQLCreate sqlCreate = new SQLCreate();
    public SQLSelect sqlSelect = new SQLSelect();
    public SQLInsert sqlInsert = new SQLInsert();
    public SQLDelete sqlDelete = new SQLDelete();
    public SQLUpdate sqlUpdate = new SQLUpdate();

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false",
                    username, password);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
