package com.redis.domain;

import java.io.Serializable;

/**
 * ����
 * @author wjj0065
 *
 */
public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5115588295977406105L;
	/**
	 * ����
	 */
	public String name;
	/**
	 * ����
	 */
	public int age;
	/**
	 * �Ա�
	 */
	public String sex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
