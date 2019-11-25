/**
 * Project Name:nettybook2
 * File Name:StudentTest.java
 * Package Name:com.common.sort
 * Date:2019年7月2日上午11:10:31
 * Copyright (c) 2019, kaiyun@qk365.com All Rights Reserved.
 *
*/

package com.common.sort;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:StudentTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2019年7月2日 上午11:10:31 <br/>
 * @author   kaiyun
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class StudentTest {
	
	public static Date getNowDate(String date) {
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   Date currentTime_2 = null;
		try {
			currentTime_2 = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		   return currentTime_2;
		}
	
	public static void main(String[] args) {
		List<Student> studentList = new ArrayList<Student>();
        Student stu=null;
        
        stu=new Student();
        stu.setName("1");
        stu.setAge(12);
        stu.setScore(80);
        stu.setCreateTime(StudentTest.getNowDate("2018-01-01 10:00:00"));
        studentList.add(stu);
        
        stu=new Student();
        stu.setName("2");
        stu.setAge(11);
        stu.setScore(90);
        stu.setCreateTime(StudentTest.getNowDate("2018-04-21 10:00:00"));
        studentList.add(stu);
        
        studentList = studentList.stream().sorted(Comparator.comparing(Student::getCreateTime).reversed()).collect(Collectors.toList());//根据创建时间倒排
        
        System.out.println(studentList);
	}

}

