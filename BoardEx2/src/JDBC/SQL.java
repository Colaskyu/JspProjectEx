package JDBC;

public class SQL {
	public static final String SEL_TERMS = "SELECT * FROM `JSP_TERMS` ";
	public static final String INS_REGISTER = "INSERT INTO `JSP_MEMBER` SET UID=?,PASS=password(?),NAME=?,NICK=?,EMAIL=?,HP=?,ZIP=?,ADDR1=?,ADDR2=?,REGIP=?,REGDATE=NOW() ";
	public static final String SEL_VALIDATE = "SELECT COUNT(1) FROM `JSP_MEMBER` WHERE ";
	public static final String SEL_LOGIN = "SELECT * FROM `JSP_MEMBER` WHERE uid= ? AND pass=password(?) ";
	public static final String INS_BOARD = "INSERT INTO `JSP_BOARD` SET cate='notice', title=?, content=?, uid=?, file=?, regip=?, rdate=NOW() ";
	public static final String SEL_FILE = "SELECT NEWNAME FROM `JSP_FILE` WHERE PARENT = ? ";
	public static final String INS_FILE = "INSERT INTO `JSP_FILE` SET parent=?, oldName=?, newName=?, rdate=now() ";
	public static final String UPT_DOWNLOADCNT = "UPDATE `JSP_FILE` SET DOWNLOAD = DOWNLOAD + 1 WHERE PARENT = ?  ";
	public static final String SEL_DOWNLOADCNT = "SELECT download FROM `JSP_FILE` WHERE PARENT = ?  ";
	public static final String DEL_FILE = "DELETE FROM `JSP_FILE` WHERE PARENT = ? ";
	public static final String SEL_LIST = "SELECT A.*, B.NICK FROM `JSP_BOARD` A JOIN `JSP_MEMBER` B ON A.UID = B.UID WHERE A.PARENT = 0 ORDER BY A.SEQ DESC LIMIT ?, ? ";
	public static final String SEL_LISTCNT = "SELECT COUNT(1) FROM `JSP_BOARD` WHERE PARENT=0 OR PARENT = null ";
	public static final String SEL_BOARD = "SELECT A.*, B.NICK, C.OLDNAME, C.NEWNAME, C.DOWNLOAD FROM `JSP_BOARD` A JOIN `JSP_MEMBER` B ON A.UID = B.UID LEFT JOIN `JSP_FILE` C ON A.SEQ = C.PARENT WHERE A.SEQ=? ";
	public static final String UPT_BOARDHIT = "UPDATE `JSP_BOARD` SET HIT = HIT + 1 WHERE SEQ=? ";
	public static final String DEL_BOARD = "DELETE FROM `JSP_BOARD` WHERE SEQ=? ";
	public static final String SEL_CHKBOARDID = "SELECT 1 FROM `JSP_BOARD` WHERE SEQ=? AND UID=? ";
	public static final String UPT_BOARD = "UPDATE `JSP_BOARD` SET TITLE=?, CONTENT=?, FILE=? WHERE SEQ=? ";
	public static final String INS_COMMENT = "CALL insertComment(?,?,?,?,0) ";
	public static final String SEL_COMMENT = "SELECT A.SEQ, A.CONTENT, A.RDATE, B.NICK FROM `JSP_BOARD` A JOIN `JSP_MEMBER` B ON A.UID = B.UID WHERE A.parent=? order by A.seq desc ";
	public static final String UPT_CMTCNT = "UPDATE `JSP_BOARD` A JOIN (SELECT PARENT, COUNT(1) AS CNT FROM `JSP_BOARD` WHERE PARENT=? GROUP BY PARENT) B ON A.SEQ = B.PARENT SET A.COMMENT = B.CNT WHERE A.SEQ = ? ";
	public static final String DEL_COMMENT = "DELETE FROM `JSP_BOARD` WHERE seq = ? ";
	public static final String SEL_CMTUSER = "SELECT UID FROM `JSP_BOARD` WHERE SEQ = ? ";
}
