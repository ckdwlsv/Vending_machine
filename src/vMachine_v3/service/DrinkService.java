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

    public List<DrinkDto> getAll() {
        List<DrinkDto> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String sql = "SELECT * FROM vending_menu";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DrinkDto dto = new DrinkDto();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setPrice(rs.getInt("price"));
                dto.setStock(rs.getInt("stock"));
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println(" "+e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e) {}
        }
        return list;
    }

    public int sell(int memberId, int menuId) {
        return memberId;
    }

    public int insert(DrinkDto dto) {
        PreparedStatement pstmt = null;
        int result = 0;
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String sql = "INSERT INTO vending_menu(name, price, stock) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getName());
            pstmt.setInt(2, dto.getPrice());
            pstmt.setInt(3, dto.getStock());
            result = pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (pstmt != null) pstmt.close();
            }catch (Exception e) {}
        }
        return result;
    }

    public int update(DrinkDto dto) {
        PreparedStatement pstmt = null;
        int result = 0;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DBConn.getConnection();
            String sql= "UPDATE vending_menu SET name=?, price=?, stock=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getName());
            pstmt.setInt(2, dto.getPrice());
            pstmt.setInt(3, dto.getStock());
            pstmt.setInt(4, dto.getId());
            result = pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (pstmt != null) pstmt.close();
                if (rs != null) rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int delete(int id) {
        PreparedStatement pstmt = null;
        Connection conn = null;
        int result = 0;
        try {
            conn = DBConn.getConnection();
            String sql = "DELETE FROM vending_menu WHERE id = ?";
            pstmt= conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
