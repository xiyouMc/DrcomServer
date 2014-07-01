package model;

public class Session {

	private String picurl;
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	private String sessionID;
	private String cookieSessionID;
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getCookieSessionID() {
		return cookieSessionID;
	}
	public void setCookieSessionID(String cookieSessionID) {
		this.cookieSessionID = cookieSessionID;
	}
	
}
