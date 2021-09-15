package com.kh.toy.member.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dao.MemberDao;
import com.kh.toy.member.model.dto.Member;


//Service
//���ø����̼��� �����Ͻ������� �ۼ�
//������� ��û�� ��Ʈ�ѷ��� ���� ���ӹ޾� �ش� ��û�� ó���ϱ� ���� �ʿ��� �ٽ����� �۾��� ����
//�۾��� �����ϱ� ���� �����ͺ��̽��� ����� �����Ͱ� �ʿ��ϸ� Dao���� ��û
//�����Ͻ������� Service�� ����ϱ� ������ Transaction������ Service�� ���.

//Connection��ü ���� , closeó��
//commit, rollback
//SQLException�� ���� ����ó��(rollback)
public class MemberService {

	private MemberDao memberDao = new MemberDao();
	private JDBCTemplate template = JDBCTemplate.getInstance();
	
	public Member memberAuthenticate(String userId, String password) {
		Connection conn = template.getConnection();
		Member member = null;
		
		try {
			member = memberDao.memberAuthenticate(userId,password,conn);
		}finally {
			template.close(conn);
		}
		
		return member;
	}
	public Member selectMemberById(String userId) {
		
		Connection conn = template.getConnection();
		Member member = null;
		
		try {
			member = memberDao.selectMemberById(userId, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		
		return member;
	}

	public List<Member> selectMemberList() {
		Connection conn = template.getConnection();
		List<Member> memberList = null;
		
		try {
			memberList = memberDao.selectMemberList(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		
		return memberList;
	}

	public int insertMember(Member member){
		Connection conn = template.getConnection();
		int res = 0;
		
		try {
			res = memberDao.insertMember(member,conn);
			//ȸ�����Կ� �����ϸ� ���̵�� ȸ���� ������ �޾ƿͼ� ��ȯ
			//Member member = memberDao.selectMemberById(member.getUserId(), conn);			
			template.commit(conn);			
		}  catch (Exception e) {
			template.rollback(conn);
			throw e;
		}  finally {
			template.close(conn);
		}
		
		return res;
	}

	public int updateMemeberPassword(String userId, String password) {
		Connection conn = template.getConnection();
		int res= 0;
		try {
			res = memberDao.updateMemberPassword(userId, password,conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			throw e;
		}finally {
			template.close(conn);
		}
		
		return res;
	}

	public int deleteMember(String userId) {
		Connection conn = template.getConnection();
		int res= 0;
		try {
			res = memberDao.deleteMember(userId,conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			throw e;
		}finally {
			template.close(conn);
		}
		return res;
	}
	
	public void authenticateByEmail(Member member, String persistToken) {
		MailSender mailSender = new MailSender();
		HttpConnector conn = new HttpConnector();
		
		String queryString = conn.urlEncodedForm(RequestParams
												.builder()
												.param("mailTemplate","join-auth-mail")
												.param("userId", member.getUserId())
												.param("presistToken",persistToken).build());
										
		String response = conn.get("http://localhost:9090/mail?mailTemplate=join-auth-mail&userId="+member.getUserId());
		mailSender.sendEmail(member.getEmail(), "회원가입 축하합니다.",response);
	}
	
}