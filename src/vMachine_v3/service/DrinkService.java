package vMachine_v3.service;

import vMachine_v3.db.DBConn;
import vMachine_v3.dto.DrinkDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrinkService {
    Connection conn = DBConn.getConnection();

    public List<DrinkDto> getAll() {
        List<DrinkDto> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM member";
            pstmt = conn.prepareCall(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DrinkDto dto = new DrinkDto();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setPrice(rs.getInt("price"));
                dto.setStock(rs.getInt("stock"));
                list.add(dto);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(" "+e.getMessage());
        }
        return list;
    }

    public int sell(int memberId, int menuId) {
        return memberId;
    }

    public int insert(DrinkDto dto) {
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            String sql = "INSERT INTO vending_menu(name, price, stock) VALUES (?, ?, ?)";
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, dto.getId());
            pstmt.setString(2, dto.getName());
            pstmt.setInt(3, dto.getPrice());
            pstmt.setInt(4, dto.getStock());
            result = pstmt.executeUpdate();
            pstmt.close();
            } catch (Exception e){
            System.out.println(" "+e.getMessage());
        }
        return result;
    }

    public int update(DrinkDto dto) {
        return 0;
    }

    public int delete(int id) {
        return 0;
    }
}
