package org.dartmouth.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yaozhong Kang
 * @date May 20, 2014
 */
public class UserDO implements Serializable {
	private static final long serialVersionUID = 6978089255719569084L;
	private Long id;
	private Long third_party_id;
	private Integer tag;
	private String name;
	private String email;
	private String pwd;
	private Boolean gender;
	private String profile_img;
	private String twitter_link;
	private String instagram_link;
	private String facebook_link;
	private Date last_modified;
	private Date created_at;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getThird_party_id() {
		return third_party_id;
	}

	public void setThird_party_id(Long third_party_id) {
		this.third_party_id = third_party_id;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	public String getTwitter_link() {
		return twitter_link;
	}

	public void setTwitter_link(String twitter_link) {
		this.twitter_link = twitter_link;
	}

	public String getInstagram_link() {
		return instagram_link;
	}

	public void setInstagram_link(String instagram_link) {
		this.instagram_link = instagram_link;
	}

	public String getFacebook_link() {
		return facebook_link;
	}

	public void setFacebook_link(String facebook_link) {
		this.facebook_link = facebook_link;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

}
