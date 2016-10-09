package com.dubbo.provider;

import java.util.ArrayList;
import java.util.Map;

public interface keyWordCenter {
	 ArrayList<Map<String, String>> serviceKey();
	 ArrayList<Map<String, String>> serviceKey(String p);
	 Map<String, String> delById(String id);
	 Map<String, String> getById(String id);
	 Map<String, String> updateKeyWords(Map<String, String> map);
}
