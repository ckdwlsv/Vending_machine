package vMachine_v3.service;

import vMachine_v3.db.DBConn;
import vMachine_v3.dto.MemberDto;
import vMachine_v3.dto.SalesDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalesService {
    public List<SalesDto> getByMember(int memberId) {
        List<SalesDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = DBConn.getConnection();
            String sql = "SELECT s.*, v.name AS menu_name FROM sales s JOIN vending_menu v ON s.menu_id = v.id WHERE s.member_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SalesDto dto = new SalesDto();
                dto.setId(rs.getInt("id"));
                dto.setSoldAt(rs.getTimestamp("sold_at").toLocalDateTime());
                dto.setMenuName(rs.getString("menu_id"));
                dto.setPrice(rs.getInt("price"));
                dto.setMemberName(rs.getString("menu_name"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<SalesDto> getSummaryByMenu() {
        List<SalesDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = DBConn.getConnection();
            String sql = "select v.name AS menu_name, count(*) AS totalQuantity, sum(s.price) AS totalAmount from sales s Join vending_menu v ON s.menu_id = v.id group by s.menu_id";
            pstmt = conn.prepareCall(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SalesDto dto = new SalesDto();
                dto.setMenuName(rs.getString("menu_name"));
                dto.setTotalQuantity(rs.getInt("totalQuantity"));
                dto.setTotalAmount(rs.getInt("totalAmount"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<SalesDto> getSummaryByMember() {
        List<SalesDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = DBConn.getConnection();
            String sql = "SELECT m.user_id as userid, m.name as memberName, coalesce(sum(s.price),0) as totalAmount, m.balance as balance FROM member m left join sales s on m.id = s.member_id group by m.id, m.user_id, m.name, m.user_id, m.name, m.balance order by m.id";
            pstmt = conn.prepareCall(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SalesDto dto = new SalesDto();
                dto.setUserId(rs.getString("userid"));
                dto.setMemberName(rs.getString("memberName"));
                dto.setTotalAmount(rs.getInt("totalAmount"));
                dto.setBalance(rs.getInt("balance"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
