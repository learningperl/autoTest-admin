package com.dubbo.provider;

import java.util.ArrayList;
import java.util.Map;

public interface article_Center {
	ArrayList<Map<String, String>> SearchArticle(String userid,int index);
	ArrayList<Map<String, String>> SearchArticle(int index);
	Map<String, String> delArticleById(String id);
	Map<String, String> getArticleById(String id);
	ArrayList<Map<String, String>> getCommentsById(String id,int index);
	Map<String, String> updateArticle(Map<String, String> map);
	Map<String, String> setAgree(String userid,String articleid);
	Map<String, String> updateComment(Map<String, String> map);
	ArrayList<Map<String, String>> getFeatureArticle();
	ArrayList<Map<String, String>> getEmojis();
}
