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
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            //-1. 메뉴존재 확인 여부
            conn = DBConn.getConnection();
            String sql = "SELECT * FROM vending_menu WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, menuId);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            int price = rs.getInt("price");
            int stock = rs.getInt("stock");
            if (stock <= 0) {
                return -2;
            }

            rs.close();
            pstmt.close();

            //회원조회
            sql = "SELECT * FROM member WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return -4;
            }
            int balance = rs.getInt("balance");
            if (balance < price) {
                return -3;
            }
            rs.close();
            pstmt.close();

            //계산
            //3.잔액차감
            sql = "UPDATE member SET balance= balance-? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, price);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
            //4. 재고 감소
            sql = "UPDATE vending_menu SET stock = stock -1 WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, menuId);
            pstmt.executeUpdate();
            pstmt.close();
            //판매 기록 저장
            sql = "INSERT INTO sales(member_id, menu_id, price) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, menuId);
            pstmt.setInt(3, price);
            pstmt.executeUpdate();
            return 1;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally {
            try {
                if (pstmt != null) pstmt.close();
                if (rs != null) rs.close();
            }catch (Exception e) {
                e.getMessage();
            }
        }
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
