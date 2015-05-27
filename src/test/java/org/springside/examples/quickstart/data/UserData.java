package org.springside.examples.quickstart.data;

import org.springside.examples.quickstart.entity.Runner;
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
