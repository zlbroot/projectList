package com.bos.kit.constant;

public class UserConstant {
	public static final String USER_DEFAULT_ROLE = "user";
	
	public enum UserStatus{
		Normal(1,"正常"),
		Locked(2,"锁定");
		public int value;
		public String name;
		private UserStatus(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
	}
}
