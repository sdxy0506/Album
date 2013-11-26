package com.xuyan.album;

import android.os.Bundle;

/**
 * Created with IntelliJ IDEA. User: hetuo Date: 13-10-28 Time: 下午2:33 To change
 * this template use File | Settings | File Templates.
 */
public class TestEvent {
	private String event;
	private Bundle bundle;

	public TestEvent(String event) {
		this.event = event;
	}

	public String get_string() {
		return event;
	}

	public Bundle get_bundle() {
		return bundle;
	}

	public void set_bundle(Bundle bundle) {
		this.bundle = bundle;
	}
}