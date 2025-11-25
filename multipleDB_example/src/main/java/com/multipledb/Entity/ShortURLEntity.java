package com.multipledb.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

public class ShortURLEntity {
	
	private Long urlId;
	
	private String apiDevKey;
	
	private String originalUrl;
	
	private String customAlias;
	
	private String userName;
	
	private Date expireDate;
	
	private String shortKey;
	
	
	public String getShortKey() {
		return shortKey;
	}
	public void setShortKey(String shortKey) {
		this.shortKey = shortKey;
	}
	public Long getUrlId() {
		return urlId;
	}
	public void setUrlId(Long urlId) {
		this.urlId = urlId;
	}
	public String getApiDevKey() {
		return apiDevKey;
	}
	public void setApiDevKey(String apiDevKey) {
		this.apiDevKey = apiDevKey;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public String getCustomAlias() {
		return customAlias;
	}
	public void setCustomAlias(String customAlias) {
		this.customAlias = customAlias;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShortURLEntity [urlId=");
		builder.append(urlId);
		builder.append(", apiDevKey=");
		builder.append(apiDevKey);
		builder.append(", originalUrl=");
		builder.append(originalUrl);
		builder.append(", customAlias=");
		builder.append(customAlias);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", expireDate=");
		builder.append(expireDate);
		builder.append(", shortKey=");
		builder.append(shortKey);
		builder.append("]");
		return builder.toString();
	}


	
}
