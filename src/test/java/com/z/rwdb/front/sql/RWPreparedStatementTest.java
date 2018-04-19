/**
 * 
 */
package com.z.rwdb.front.sql;

import org.junit.Test;

/**
 * @author sunff
 *
 */
public class RWPreparedStatementTest {

	@Test
	public void searchIndex() {
		int parameterIndex = 1;
		StringBuffer sb = new StringBuffer("id=? and name=?");
		int index = 0;
		for (int i = 0; i <= parameterIndex; i++) {
			index = sb.indexOf("?", index);
			System.out.println("index=" + index);
		}

	}
}
