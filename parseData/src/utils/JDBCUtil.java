package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName: DataBaseConf
 * @Description: 配置 Mysql 数据库
 * @author: fanjh
 * @date: 2018年1月4日 下午10:27:06
 */
public class JDBCUtil {

	private static final String driver = "com.mysql.jdbc.Driver";

	private static final String url = "jdbc:mysql://rm-uf683iy0k0xm1a7t7o.mysql.rds.aliyuncs.com/kikuu";

	private static final String user = "kikuuroot";

	private static final String password = "KiKUU2017";

	public static int exexuteQuery(String sql) {
		ResultSet rs = null;
		Connection con = null;
		try {
		    Class.forName(driver);
		    con = DriverManager.getConnection(url, user, password);
            if(!con.isClosed()){
            	System.out.println("已成功链接数据库！");
            }
            PreparedStatement ps = con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
        } catch(Exception e) {
    	    e.printStackTrace();
        } finally {
        	try {
        		if (null != con) {
        			rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
            try {
            	if (null != con) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
		return 0;
	}
	
	public static Connection getConnection() {
		Connection con = null;
		try {
		    Class.forName(driver);
		    con = DriverManager.getConnection(url, user, password);
            if(!con.isClosed()){
            	System.out.println("已成功链接数据库！");
            	return con;
            }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
		}
		return null;
	}
	
	public static void releaseConnection(Connection con, Statement st, ResultSet rs) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (null != st) {
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (null != con) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
