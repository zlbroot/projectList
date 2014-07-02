package com.test.entity;

import com.zlb.core.annos.dao.entity.Entity;
import com.zlb.core.annos.dao.entity.Table;

@Entity
@Table("zlb_user")
public class User extends IdEntity {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", id=" + id + "]";
	}
}
