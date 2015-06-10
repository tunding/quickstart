package org.springside.fi.common;

import org.springside.modules.utils.PropertiesLoader;

public class SystemGlobal {
	private static PropertiesLoader propertiesLoader;
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("system.properties","application.properties");
		}
		return propertiesLoader.getProperty(key);
	}
}
