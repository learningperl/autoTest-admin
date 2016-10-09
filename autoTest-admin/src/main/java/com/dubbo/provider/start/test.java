package com.dubbo.provider.start;

import java.util.regex.Pattern;

public class test {
	private static final String REGEX = "[^\\#\\']+";
	 public static void main(String[] args) {

	 }
	 
		private static boolean Check(String str) {
			Pattern p = Pattern.compile(REGEX);
			boolean flg = p.matcher(str).matches();
			return flg;
		}
}
