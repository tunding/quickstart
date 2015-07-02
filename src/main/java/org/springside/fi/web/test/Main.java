package org.springside.fi.web.test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tunding:wzc@tcl.com
 * @description 用来测试一些基本调用的java application
 * @version 1.0
 * @date 创建时间：2015年7月2日 上午9:12:34
 */
public class Main {
	/**
	 * @description
	 */
	public static void main(String[] args) {
		Date now = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(now));
	}
}
