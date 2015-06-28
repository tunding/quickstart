package org.springside.fi.service.running;

import org.springside.fi.service.rong.models.ContactNtfMessage;

public class ObjtoJson {
	public static void main(String[] args) {
		ContactNtfMessage msg = new ContactNtfMessage("attention", "123",
					"456", "this is a message");
		System.out.println(msg);
	}
}
