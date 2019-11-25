/**
 * Project Name:nettybook2
 * File Name:Student.java
 * Package Name:com.common.sort
 * Date:2019年7月2日上午11:09:16
 * Copyright (c) 2019, kaiyun@qk365.com All Rights Reserved.
 *
*/

package com.common.sort;

import java.util.Date;

/**
 * ClassName:Student <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2019年7月2日 上午11:09:16 <br/>
 * 
 * @author kaiyun
 * @version
 * @since JDK 1.8
 * @see
 */
public class Student {

	/***
	 * 姓名
	 */
	private String name;
	private int age;
	private String address;
	private Date createTime;
	/***
	 * 考试得分
	 */
	private int score;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + ", address=" + address + ", createTime=" + createTime
				+ ", score=" + score + "]";
	}

}
