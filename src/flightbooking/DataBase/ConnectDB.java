/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightbooking.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectDB {
    Connection con = null;
    
    public Connection GetConnectDB(){
        try{
            String url = "jdbc:sqlserver://localhost:1433;databaseName=FlightBookingDB;";
            String user = "sa";
            String password = "123";         
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url,user,password);
        }catch(Exception e){
            System.out.println("Cannot connect: " + e.getMessage());
        }
        return con;
    }
}

