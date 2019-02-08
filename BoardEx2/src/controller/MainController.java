package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainController extends HttpServlet {
	private static final long serialVersionUID = 115005766590597929L;
	private Map<String, Object> instances = new HashMap<>(); 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext ctx = config.getServletContext();
		String path = ctx.getRealPath("/WEB-INF") + "/commandURI.properties";
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			prop.load(fis);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {}
		}
		Iterator itr = prop.keySet().iterator();
		
		while(itr.hasNext()) {
			String k = itr.next().toString();
			String v = prop.getProperty(k);
			try {
				Class<?> obj = Class.forName(v);
				Object instance = obj.newInstance();
				instances.put(k, instance);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processProc(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processProc(req, resp);
	}
	
	private void processProc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String result = null;
		String path = req.getContextPath();
		String uri = req.getRequestURI();
		String action = uri.substring(path.length());
		
		CommonAction instance = (CommonAction)instances.get(action);
		result = instance.processProc(req, resp);
		if(result == null) result = "";
		if(result.startsWith("redirect:")) {
			String redirectAddr = result.substring(9);
			resp.sendRedirect(redirectAddr);
		} else if(instance instanceof service.member.ValidateService || instance instanceof service.board.CommentViewService || instance instanceof service.board.CommentDeleteService || instance instanceof service.board.DownloadService) {
			if(result != "") {
				resp.setContentType("text/html;charset=UTF-8");
				PrintWriter out = resp.getWriter();
				out.print(result);
			}
		} else {
			RequestDispatcher dispatcher = req.getRequestDispatcher(result);
			dispatcher.forward(req, resp);
		}
	}
}
