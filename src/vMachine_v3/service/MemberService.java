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

    public boolean register(MemberDto dto) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;

        try {
            conn = DBConn.getConnection();
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
            result = count > 0 ? true : false;
        } catch (Exception e) {
            System.out.println("insert 오류: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public MemberDto login(String userId, String pw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn =DBConn.getConnection();
            String sql = "SELECT * FROM member WHERE user_id = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
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
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {}
        }
        return null;
    }

    public MemberDto getById(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String sql = "SELECT * FROM member WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MemberDto dto = new MemberDto();
                dto.setId(rs.getInt("id"));
                dto.setUserId(rs.getString("user_id"));
                dto.setName(rs.getString("name"));
                dto.setPassword(rs.getString("password"));
                dto.setIsAdmin(rs.getInt("is_admin"));
                dto.setCardNum(rs.getString("card_num"));
                dto.setBalance(rs.getInt("balance"));
                dto.setCardNum(rs.getString("card_num"));
                return dto;
            }
        } catch (Exception e) {
        System.out.println("insert 오류 : " + e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e){}
        }
    return null;
    }

    public int charge(int memberId, int amount) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String sql = "UPDATE member SET balance = balance + ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, memberId);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("insert 오류 : " +  e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e){}
        }
        return 0;
    }

    public int update(MemberDto dto) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        int result = 0;
        try {
            conn = DBConn.getConnection();
            String sql = "UPDATE member SET user_id=?, name=?, password=?, tel=?, card_num=? WHERE id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getUserId());
            pstmt.setString(2, dto.getName());
            pstmt.setString(3, dto.getPassword());
            pstmt.setString(4, dto.getTel());
            pstmt.setString(5, dto.getCardNum());
            pstmt.setInt(6, dto.getId());
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("insert 오류 : " +  e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (rs != null) rs.close();
            } catch (Exception e) {}
        }
        return result;
    }

    public int delete(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        int  result = 0;
        try {
            conn = DBConn.getConnection();
            String sql = "DELETE FROM member WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            result = pstmt.executeUpdate();
        }catch (Exception e) {
            System.out.println("insert 오류 : "+e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<MemberDto> getAll() {
        List<MemberDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConn.getConnection();
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
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e) {}
        }
        return list;
    }
}
