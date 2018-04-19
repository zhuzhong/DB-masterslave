/**
 * 
 */
package com.z.rwdb.front.route.support;

import org.junit.Test;

/**
 * @author sunff
 *
 */
public class RWPreparedStatementTest {

	@Test
	public void replaceStrBufferDemo() {
		String sql = "select id,name from t_test where id=? and name=? and name2=?";
		StringBuffer sb = new StringBuffer(sql);
		System.out.println("0=="+sb.toString());
		replaceStrBuffer(sb, 1, "1000");
		System.out.println("1=="+sb.toString());
		replaceStrBuffer(sb, 2, "0 0 ? *");
		
		System.out.println("2=="+sb.toString());
		
		replaceStrBuffer(sb, 3, "test");
		System.out.println("3=="+sb.toString());
	}

	
	 private void replaceStrBuffer(StringBuffer sb, int parameterIndex, Object strObj) {
	        int strIndex = searchIndex(sb,parameterIndex);
	        
	        sb = sb.replace(strIndex, strIndex + 1, warpParameter(strObj.toString()));
	    }

	    private String warpParameter(String param) {
	    	
	        return String.format("'%s'", param);
	    }
	    
	    
	private int searchIndex(StringBuffer sb, int parameterIndex) {
		int index = sb.lastIndexOf("'");
		System.out.println("index1="+index);
		if(index<0) {
			
			index=0;
		}
		System.out.println("index2="+index);
		
		for (int i = 0; i <= parameterIndex; i++) {
			index = sb.indexOf("?", index);
		}
		return index;
	}
	
	
	private int orignalsearchIndex(StringBuffer sb, int parameterIndex) {
		int index = 0;
		for (int i = 0; i <= parameterIndex; i++) {
			/*
			 * 这种查找会出问题，原因是入参中含有?，这样就无法区分是初始的参数问号，还是更改之后的参数问号？
			 */
			index = sb.indexOf("?", index);
		}
		return index;
	}
}
