package org.salesforce.demo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static Config config;

	private String loginUrl;
	private String username;
	private String password;
	private String token;
	
	private String pubSubEndpoint;
	private String pubSubTopicName;
	private int pubSubEventReceiveLimit;

	public String getPubSubEndpoint() {
		return pubSubEndpoint;
	}

	public String getPubSubTopicName() {
		return pubSubTopicName;
	}

	public int getPubSubEventReceiveLimit() {
		return pubSubEventReceiveLimit;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getToken() {
		return token;
	}

	public static Config get() {
		if (config == null) {
			try (InputStream input = new FileInputStream("config.properties")) {
				Properties prop = new Properties();
				prop.load(input);
				config = new Config();
				config.loginUrl = readMandatoryProp(prop, "loginUrl");
				config.username = readMandatoryProp(prop, "user.username");
				config.password = readMandatoryProp(prop, "user.password");
				config.token = readMandatoryProp(prop, "user.token");
				config.pubSubEndpoint = readMandatoryProp(prop, "pubSub.endpoint");
				config.pubSubTopicName = readMandatoryProp(prop, "pubSub.topicName");
				config.pubSubEventReceiveLimit = readMantoryIntProp(prop, "pubSub.eventReceiveLimit");
			} catch (IOException e) {
				throw new RuntimeException("Failed to load configuration: " + e.getMessage(), e);
			}
		}
		return config;
	}

	private static String readMandatoryProp(Properties prop, String key) throws IOException {
		String value = prop.getProperty(key);
		if (value == null || value.trim().equals("")) {
			throw new IOException("Missing mandatory property: " + key);
		}
		return value;
	}
	
	private static int readMantoryIntProp(Properties prop, String key) throws IOException {
		String stringValue = readMandatoryProp(prop, key);
		return Integer.valueOf(stringValue);
	}
}
