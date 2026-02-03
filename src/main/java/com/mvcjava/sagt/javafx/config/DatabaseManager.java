/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author lucas
 */
public class DatabaseManager {
    private static final HikariConfig config = new HikariConfig("com/mvcjava/sagt/javafx/database.properties");
    private static final HikariDataSource ds;
    
    static {
        ds = new HikariDataSource(config);
    }
    
    private DatabaseManager(){}
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
