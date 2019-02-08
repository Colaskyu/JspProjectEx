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
		//JSON 데이터 출력용 object (library 파일로부터 인식)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String type = req.getParameter("type");
		String param = req.getParameter("param");
		String sql = SQL.SEL_VALIDATE + type +  "=? " ;
		int count = 0;
		String errmsg = "사용가능합니다.";
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
		
		//에러메세지 생성
		if(count != 1) {
			if(type.equals("uid")) {
				if(param.length() < 4) {
					errmsg="아이디는 4자 이상이어야합니다.";
					color="red";
				}
			} else if(type.equals("nick")) {
				if(param.length() < 4) {
					errmsg="별명은 4자 이상이어야합니다.";
					color="red";
				} else if(param.indexOf(" ") != -1) {
					errmsg="별명에 공백이 포함되면 안됩니다..";
					color="red";
				} else {
					for(int i = 0; i< spc.length(); i++){
						if(param.indexOf(spc.charAt(i)) != -1) {
							errmsg="별명에 특수문자가 포함되면 안됩니다..";
							color="red";
							break;
						}
					}
				}
			} else if(type.equals("email")) {
				if(param.indexOf("@") == -1) {
					errmsg="입력하신 이메일 주소가 형식에 맞지 않습니다..";
					color="red";
				}
			} else {
				if(param.length() != 13) {
					errmsg="휴대폰 번호 자리수가 맞지 않습니다.";
					color="red";
				} else if(param.indexOf("-") != -1) {
					errmsg="휴대폰 번호 입력형식이 맞지 않습니다.";
					color="red";
				}
			}
		} else {
			errmsg="이미 사용중입니다.";
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
