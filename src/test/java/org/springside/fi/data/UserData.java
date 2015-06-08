package org.springside.fi.data;

import org.springside.fi.entity.Runner;
import org.springside.modules.test.data.RandomData;

public class UserData {

	public static Runner randomNewUser() {
		Runner user = new Runner();
		user.setLoginName(RandomData.randomName("user"));
		user.setName(RandomData.randomName("User"));
		user.setPlainPassword(RandomData.randomName("password"));

		return user;
	}
}
