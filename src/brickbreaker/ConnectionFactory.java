/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brickbreaker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author wwwsh
 */
public class ConnectionFactory {
    public static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:4306/gamebrickbreaker","root", "");         
        }catch(ClassNotFoundException e){
            System.out.println("classNotFoundException " + e);
        }catch(SQLException e){
            System.out.println("SQLException " + e);
        }
        return c;  
    }
}
