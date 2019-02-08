package service.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;

public class ValidateService implements CommonAction {

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		//JSON ������ ��¿� object (library ���Ϸκ��� �ν�)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String type = req.getParameter("type");
		String param = req.getParameter("param");
		String sql = SQL.SEL_VALIDATE + type +  "=? " ;
		int count = 0;
		String errmsg = "��밡���մϴ�.";
		String color = "green";
		String spc = "{}[]()<>?|`~'!@#$%^&*-+=,.;:\"'\\/ ";
		
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,param);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		
		//�����޼��� ����
		if(count != 1) {
			if(type.equals("uid")) {
				if(param.length() < 4) {
					errmsg="���̵�� 4�� �̻��̾���մϴ�.";
					color="red";
				}
			} else if(type.equals("nick")) {
				if(param.length() < 4) {
					errmsg="������ 4�� �̻��̾���մϴ�.";
					color="red";
				} else if(param.indexOf(" ") != -1) {
					errmsg="���� ������ ���ԵǸ� �ȵ˴ϴ�..";
					color="red";
				} else {
					for(int i = 0; i< spc.length(); i++){
						if(param.indexOf(spc.charAt(i)) != -1) {
							errmsg="���� Ư�����ڰ� ���ԵǸ� �ȵ˴ϴ�..";
							color="red";
							break;
						}
					}
				}
			} else if(type.equals("email")) {
				if(param.indexOf("@") == -1) {
					errmsg="�Է��Ͻ� �̸��� �ּҰ� ���Ŀ� ���� �ʽ��ϴ�..";
					color="red";
				}
			} else {
				if(param.length() != 13) {
					errmsg="�޴��� ��ȣ �ڸ����� ���� �ʽ��ϴ�.";
					color="red";
				} else if(param.indexOf("-") != -1) {
					errmsg="�޴��� ��ȣ �Է������� ���� �ʽ��ϴ�.";
					color="red";
				}
			}
		} else {
			errmsg="�̹� ������Դϴ�.";
			color="red";
		}
		/*
		 * Gson gson = new Gson(); ArrayList<String> rlist = new ArrayList<>();
		 * rlist.add(errmsg); rlist.add(color); String result = gson.toJson(rlist);
		 */
		
		
		JsonObject json = new JsonObject(); 
		json.addProperty("result", errmsg);
		json.addProperty("color", color); 
		String result = json.toString();
		 
		
		return result;
	}

}
