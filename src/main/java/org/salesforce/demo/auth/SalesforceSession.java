package org.salesforce.demo.auth;

public class SalesforceSession {
	private String accessToken;
	private String orgId;
	private String instanceUrl;
	
	public SalesforceSession(String accessToken, String orgId, String instanceUrl) {
		this.accessToken = accessToken;
		this.orgId = orgId;
		this.instanceUrl = instanceUrl;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public String getOrgId() {
		return orgId;
	}
	public String getInstanceUrl() {
		return instanceUrl;
	}
}
