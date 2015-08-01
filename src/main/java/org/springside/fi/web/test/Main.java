package org.springside.fi.web.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		String time = null;
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			Date date = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
