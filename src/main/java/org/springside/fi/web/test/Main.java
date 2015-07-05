package org.springside.fi.web.test;

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
		List<String> test = new ArrayList<String>();
		test.add("1");
		test.add("2");
		test.add("3");
		test.add("4");
		System.out.println(test);
		System.out.println(test.get(0));
		System.out.println(test.get(1));
		System.out.println(test.get(2));
		System.out.println(test.get(3));
		System.out.println(test.subList(0, 3));
	}
}
