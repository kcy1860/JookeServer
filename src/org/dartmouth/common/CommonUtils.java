package org.dartmouth.common;

import java.lang.reflect.Method;
/**
 * 
 * @author Yaozhong Kang
 * @date   May 21, 2014
 */
public class CommonUtils {

	/**
	 * A function used to test
	 * Serialize an object into an string, output all of the results
	 * of public getter functions
	 * 
	 * @param o 
	 * @return
	 */
	public static String testObj2String(Object o) {
		StringBuffer buffer = new StringBuffer();
		try {
			Class<? extends Object> c = o.getClass();
			Method[] ms = c.getMethods();
			for (Method m : ms) {
				String functionName = m.getName();
				if (functionName.startsWith("get")) {
					Object k = m.invoke(o, new Object[] {});
					buffer.append(functionName.substring(3));
					buffer.append(":");
					buffer.append(k==null?"null":k.toString());
					buffer.append(" ");
				}
			}
		} catch (Exception e) {
		}
		return buffer.toString();
	}
}
