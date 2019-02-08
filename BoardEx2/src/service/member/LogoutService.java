package service.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.CommonAction;

public class LogoutService implements CommonAction{

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		session.invalidate();
		return "redirect:/BoardEx2/member/login.do";
	}

}
