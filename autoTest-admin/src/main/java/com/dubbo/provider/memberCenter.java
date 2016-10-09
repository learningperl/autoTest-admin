package com.dubbo.provider;

import java.util.Map;

public interface memberCenter {
	
	Map<String, Object> login(String name,String pwd);
	Map<String, String> register(Map<String,String> map);
	Map<String, String> changePwd(Map<String,String> map);

}
