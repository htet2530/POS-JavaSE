package com.jdc.util;

public class LoginSetting {

	public static void login(String loginId, String password) {
		
		if(loginId.isEmpty() || loginId == null) 
			throw new PosException("Please enter login Id !");
		
		if(password.isEmpty() || password == null)
			throw new PosException("Please enter password !");
		
		if(!loginId.equals(PosSettingUtil.get("pos.user.login")))
			throw new PosException("Please check login Id !");
		
		if(!password.equals(PosSettingUtil.get("pos.user.password")))
			throw new PosException("Please check password !");
		
	}
}
