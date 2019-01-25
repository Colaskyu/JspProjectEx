<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="kr.co.boardEx.service.BoardService"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	//파일 다운로드 카운트 업데이트
	int seq = Integer.parseInt(request.getParameter("seq"));
	String fname = request.getParameter("fname");
	String ofname = request.getParameter("ofname");
	
	BoardService service = BoardService.getInstance();
	service.updateDownload(seq);

	//경로
	String path = request.getServletContext().getRealPath("/upload");
	File file = new File(path + "/" + fname);
	String name = new String(ofname.getBytes("UTF-8"), "iso-8859-1");
	
	//file download response 객체 준비 (header)
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment; filename="+name);
	response.setHeader("Content-Transfer-Encoding", "binary");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "private");

	//스트림 연결
	OutputStream fos = null;
	BufferedOutputStream bos = null;
	
	try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
		out.clear();
		out=pageContext.pushBody();
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);	
		
		byte b[] = new byte[1024];
		
		int read = 0;
		while(true) {
			read = bis.read(b);
			if(read == -1) {
				break;
			}
			bos.write(b, 0, read);
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		bos.flush();
		bos.close();
		fos.flush();
		fos.close();
	}	
%>