/**
 * 
 */
package com.zz.rwdb.front.route.support;

import static org.junit.Assert.*;

import java.sql.SQLSyntaxErrorException;

import org.junit.Before;
import org.junit.Test;

import com.zz.rwdb.front.route.support.DruidRouteStrategy;
import com.zz.rwdb.util.Constant;

/**
 * @author Administrator
 *
 */
public class DruidRouteStrategyTest {

	DruidRouteStrategy s = null;
	String dbType = null;

	@Before
	public void init() {
		s = new DruidRouteStrategy();
		dbType = "oracle";
	}

	@Test
	public void route1() {
		String sqlStmt = "insert into user(user_id,user_name) values(?,?)";
		try {
			String result = s.route(sqlStmt, dbType);
			assertEquals(Constant.RW.WRITE.name(), result);
		} catch (SQLSyntaxErrorException e) {
		
			e.printStackTrace();
		}

	}

	@Test
	public void route2() {

		String sqlStmt = "select * from user where user_id=?";
		try {
			String result = s.route(sqlStmt, dbType);
			assertEquals(Constant.RW.READ.name(), result);
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void route3() {

		String sqlStmt = "merge into user s using (select user_id,user_name from dual) t on (s.user_id=t.user_id) ";
		try {
			String result = s.route(sqlStmt, dbType);
			assertEquals(Constant.RW.WRITE.name(), result);
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void route4() {
		String sqlStmt = "update user set user_id=(select user_id from user where user_id=?)";
		try {
			String result = s.route(sqlStmt, dbType);
			assertEquals(Constant.RW.WRITE.name(), result);
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void route5() {
		String sqlStmt = "insert into user(user_id,user_name) select user_id,user_name from user";
		try {
			String result = s.route(sqlStmt, dbType);
			assertEquals(Constant.RW.WRITE.name(), result);
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void route6() {
		String sqlStmt = "call pro_user()";  //调用存储过程是写入
		try {
			String result = s.route(sqlStmt, dbType);
			assertEquals(Constant.RW.WRITE.name(), result);
		} catch (SQLSyntaxErrorException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
