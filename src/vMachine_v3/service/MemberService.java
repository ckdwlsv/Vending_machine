package vMachine_v3.service;

import vMachine_v3.db.DBConn;
import vMachine_v3.dto.MemberDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    Connection conn = DBConn.getConnection();

    public boolean register(MemberDto dto) {
        boolean result = false;
        PreparedStatement pstmt = null;

        try {
            String sql = "INSERT INTO member(id, user_id, password, name, tel, balance, card_num, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, dto.getId());
            pstmt.setString(2, dto.getUserId());
            pstmt.setString(3, dto.getPassword());
            pstmt.setString(4, dto.getName());
            pstmt.setString(5, dto.getTel());
            pstmt.setInt(6, dto.getBalance());
            pstmt.setString(7,dto.getCardNum());
            pstmt.setInt(8, dto.getIsAdmin());

            int count = pstmt.executeUpdate();
            if (count > 0) {
                result = true;
            }

        } catch (Exception e) {
            System.out.println("insert 오류: " + e.getMessage());
        } finally {
            try {if (pstmt != null) pstmt.close();} catch (SQLException e) {}
            try {if (conn != null) conn.close();} catch (SQLException e) {}
        }
        return result;
    }

    public MemberDto login(String userId, String pw) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM member WHERE user_id = ? AND password = ?";
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, pw);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                MemberDto dto = new MemberDto();
                dto.setId(rs.getInt("id"));
                dto.setUserId(rs.getString("user_id"));
                dto.setPassword(rs.getString("password"));
                dto.setIsAdmin(rs.getInt("is_admin"));
                return dto;
            }
        } catch (Exception e) {
            System.out.println("insert 오류: " + e.getMessage());
        }
        return null;
    }

    public MemberDto getById(int id) {
        return null;
    }

    public int charge(int memberId, int amount) {
        return memberId;
    }

    public int update(MemberDto dto) {
        return 0;
    }

    public int delete(int id) {
        return id;
    }

    public List<MemberDto> getAll() {
        List<MemberDto> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM member";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MemberDto dto = new MemberDto();
                dto.setId(rs.getInt("id"));
                dto.setUserId(rs.getString("user_id"));
                dto.setPassword(rs.getString("password"));
                dto.setName(rs.getString("name"));
                dto.setTel(rs.getString("tel"));
                dto.setBalance(rs.getInt("balance"));
                dto.setCardNum(rs.getString("card_num"));
                dto.setIsAdmin(rs.getInt("is_admin"));
                list.add(dto);
            }
            //System.out.println(list);
            pstmt.close();
            conn.close();
        }catch(Exception e) {
            System.out.println("Get All Error: " + e.getMessage());
        }
        return list;
    }
}
