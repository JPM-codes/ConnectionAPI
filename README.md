﻿# ConnectionAPI (outdated)
****

_How to use it?_
<br>
<b>Example usage of the MySQL class:
````java
package example.project;

import me.jp.connection.data.database.MySQL;

public class ConnectionClass extends MySQL {
    // Class used to connect to the mysql
    public ConnectionClass(String host, int port, String user, String pass, String database) {
        super(host, port, user, pass, database);
    }
}
````
<b>Example usage of ConnectionClass:
````java
import example.project.ConnectionClass;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        ConnectionClass connection = new ConnectionClass("0.0.0.0", 3306, "root", "root", "myDataBase");
        // Method for connection
        connection.connection();
        // Method to create table
        connection.createTable("myTable", "name TEXT", "age INT", "email VARCHAR(50)");
        // Method to insert data
        connection.insertData("myTable", "John", "21", "testEmail@test.com");
        // Method to update data
        connection.updateData("myTable", "age", "23", "name = John");
        // Method to remove data
        connection.removeData("myTable", "name = John");
        // Method to has data
        if (connection.hasData("myTable", "name = John")) {
            System.out.println("John exists in table");
        } else {
            System.out.println("John doesn't exist in table");
        }
    }
}
````

****
_pom.xml usage:_
````xml
<dependency>
    <groupId>me.jp</groupId>
    <artifactId>ConnectionAPI</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
````
