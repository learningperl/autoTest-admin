package com.dubbo.provider;

import java.util.ArrayList;
import java.util.Map;

public interface Interface_Center {
	ArrayList<Map<String, String>> SearchInterface(String userid);
	ArrayList<Map<String, String>> SearchInterface(String userid,String p);
	Map<String, String> delCaseById(String id);
	Map<String, String> delCasesById(String id);
	Map<String, String> getCaseById(String id);
	ArrayList<Map<String, String>> getCasesById(String id,String userid);
	Map<String, String> updateCase(Map<String, String> map);
	Map<String, String> updateCases(Map<String, String> map);
	Map<String, String> setState(String userid,String id);
}
