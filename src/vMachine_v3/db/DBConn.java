package vMachine_v3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try{
                String dbDriver = "com.mysql.cj.jdbc.Driver";
                String dbUrl = "jdbc:mysql://localhost:3306/";
                String dbUser = "root";
                String dbPassword = "1234";

                Class.forName(dbDriver);
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                System.out.println("DB연결 성공");
            } catch (SQLException e) {
                System.out.println("DB연결 실패");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("드라이버를 찾을 수 없습니다.");
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }



}
