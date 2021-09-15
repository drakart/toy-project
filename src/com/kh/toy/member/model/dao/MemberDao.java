package com.kh.toy.member.model.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.member.model.dto.Member;

//DAO(DATA ACCESS OBJECT)
//DBMS�� ������ �������� ��ȸ, ����, ����, ���� ��û�� ������ Ŭ����
//DAO�� �޼���� �ϳ��� �޼��� �� �ϳ��� ������ ó���ϵ��� �ۼ�
public class MemberDao {
	
	private JDBCTemplate template = JDBCTemplate.getInstance();

	public Member memberAuthenticate(String userId, String password, Connection conn){
		Member member = null;	
		PreparedStatement pstm = null;
		ResultSet rset = null;
		
		String query = "select * from member where user_id = ? and password = ?";
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, userId);
			pstm.setString(2, password);
			rset = pstm.executeQuery();
			
			if(rset.next()) {
				member = convertAllToMember(rset);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}  finally {
			template.close(rset, pstm);
		}
		
		return member;
	}

	public Member selectMemberById(String userId, Connection conn) throws SQLException {
		Member member = null;
		PreparedStatement pstm = null;
		ResultSet rset = null;
		String query = "select * from member where user_id =?";
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1,userId);
			rset = pstm.executeQuery();
			if(rset.next()) {
				member = convertAllToMember(rset);
			}
		} finally {
			template.close(rset, pstm);
		}
		
		return member;
	}

	public List<Member> selectMemberList(Connection conn) throws SQLException {
		List<Member> memberList = new ArrayList<Member>();
		PreparedStatement pstm = null;
		ResultSet rset = null;
		
		String columns = "user_id, password, email, tell, grade";
		String query = "select " + columns +" from member";
		
		try {
			pstm = conn.prepareStatement(query);
			rset = pstm.executeQuery();
			
			while(rset.next()) {
				Member member = convertRowToMember(columns.split(","),rset);
				memberList.add(member);
			}
		} finally {
			template.close(rset, pstm);
		}
		
		return memberList;
	}

	public int insertMember(Member member, Connection conn){	
		int res = 0;
		PreparedStatement pstm = null;
		String query = "insert into member(user_id,password,email,tell) values(?,?,?,?)";
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, member.getUserId());
			pstm.setString(2, member.getPassword());
			pstm.setString(3, member.getEmail());
			pstm.setString(4, member.getTell());
			res = pstm.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}
		
		return res;
	}
	
	//userId�� ' or 1=1 or user_id = ' ���� ���޹����� ��� ȸ���� ��й�ȣ�� ����
	//SQL Injection ����
	//�������� SQL������ �����ؼ� ������ DataBase�� �����ϴ� ���
	
	//SQL Injection ���� ���� ���� PreparedStatement ���
	//�ν��Ͻ��� ������ �� ���� ���ø��� �̸� ���
	//������ ��ϵ� ���� ���ø��� ������ ����Ǵ� ���� ����
	//���ڿ��� ���ؼ� �ڵ����� �̽������� ó�� 
	//ex) ->\' or 1=1 or user_id = \'
	public int updateMemberPassword(String userId, String password,Connection conn) {
		int res = 0;
		
		PreparedStatement pstm = null;
		String query = "update member set password = ? where user_id = ? ";
		
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, password);
			pstm.setString(2, userId);
			res = pstm.executeUpdate();
		
		}catch(SQLException e){
			throw new DataAccessException(e);
		} 
		finally {
			template.close(pstm);
		}
		return res;
	}

	public int deleteMember(String userId,Connection conn) {
		int res = 0;
		PreparedStatement pstm = null;
		String query = "delete from member where user_id = ?";
		
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, userId);
			res = pstm.executeUpdate();
			
		}catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(pstm);
		}
	
		return res;
		
	}
	
	private Member convertAllToMember(ResultSet rset) throws SQLException {
		Member member = new Member();
		member.setUserId(rset.getString("user_id"));
		member.setPassword(rset.getString("password"));
		member.setEmail(rset.getString("email"));
		member.setGrade(rset.getString("grade"));
		member.setIsLeave(rset.getInt("is_leave"));
		member.setRegDate(rset.getDate("reg_date"));
		member.setRentableDate(rset.getDate("rentable_date"));
		member.setTell(rset.getString("tell"));
		return member;
	}
	
	private Member convertRowToMember(String[] columns, ResultSet rset) throws SQLException {
		Member member = new Member();
		for (int i = 0; i < columns.length; i++) {			
			String column = columns[i].toLowerCase();
			column = column.trim();
			
			switch (column) {
			case "user_id": member.setUserId(rset.getString("user_id")); break;
			case "password": member.setPassword(rset.getString("password")); break;
			case "email" : member.setEmail(rset.getString("email")); break;
			case "grade" : member.setGrade(rset.getString("grade")); break;
			case "is_leave" : member.setIsLeave(rset.getInt("is_leave")); break;
			case "reg_date" : member.setRegDate(rset.getDate("reg_date")); break;
			case "rentable_date" : member.setRentableDate(rset.getDate("rentable_date")); break;
			case "tell" : member.setTell(rset.getString("tell")); break;
			default : throw new SQLException("�������� �÷����� �����߽��ϴ�."); //����ó��
			}
		}
		return member;
	}
	
	

	
	
}
