package com.cdroho.modle;

import java.io.Serializable;

import javax.persistence.*;


/**
 * JPA实体类没有被扫描到（Not a managed type）----->没有加@Entity
 * @author HZL
 *
 */
@Entity
@Table(name = "PERSON")
public class Person implements Serializable{
	@Id
    @GeneratedValue
    private long id;

	private int age;

    private String name;
    
	
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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

}
