package com.dubbo.providerImpl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.dubbo.provider.keyWordCenter;
import com.java.statics.Mysql;
import com.java.statics.outputList;

public class keyWordCenterImpl implements keyWordCenter {
	private static final String REGEX = "^[a-zA-Z0-9]+$";
	//private static final String REGEX_sql = "[^\\#\\']+";
	@SuppressWarnings("serial")
	private static final ArrayList<String> col =  new ArrayList<String>(){{add("id"); add("type"); add("keyName"); add("describes"); add("useCase");}};

	public ArrayList<Map<String, String>> serviceKey() { // 查询关键字
		if (outputList.l_allkey == null || outputList.l_allkey.isEmpty() || outputList.key_flag) {
			outputList.l_allkey = null;
			outputList.l_allkey = new ArrayList<Map<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
			String sql = "";
			try {
				Statement sm = Mysql.ct.createStatement();
				ResultSet rs = sm.executeQuery("select DISTINCT type from keywords  order by type");
				while (rs.next()) {
					Statement sm1 = Mysql.ct.createStatement();
					// System.out.println("+1");
					sql = "select * from keywords where type='" + rs.getString(1) + "' order by id";
					ResultSet rs1 = sm1.executeQuery(sql);
					while (rs1.next()) {
						ResultSetMetaData rsmd1 = rs1.getMetaData();
						for (int i = 1; i <= rsmd1.getColumnCount(); i++) {
							map.put(rsmd1.getColumnName(i), rs1.getString(i));
						}
						outputList.l_allkey.add(map);
						map = new HashMap<String, String>();
					}
				}
				/*
				 * System.out.println("size:"+outputList.l_key.size()); for (int
				 * i=0;i<outputList.l_key.size();i++)
				 * System.out.println(outputList.l_key.get(i).get("id"));
				 */
				sm = null;
				rs = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		outputList.key_flag = false;
		return outputList.l_allkey;
	}

	public ArrayList<Map<String, String>> serviceKey(String p) { // 按条件查询关键字
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "";
		try {
			Statement sm = Mysql.ct.createStatement();
			sql = "SELECT id FROM keywords WHERE describes LIKE '%" + p + "%' or keyName LIKE '%" + p + "%'";
			ResultSet rs = sm.executeQuery(sql);
			while (rs.next()) {
				Statement sm2 = Mysql.ct.createStatement();
				ResultSet rs2 = sm2.executeQuery("select DISTINCT type from keywords order by type");
				while (rs2.next()) {
					Statement sm1 = Mysql.ct.createStatement();
					sql = "select * from keywords where id='" + rs.getString(1) + "' and type='" + rs2.getString(1)
							+ "' order by id";
					ResultSet rs1 = sm1.executeQuery(sql);
					while (rs1.next()) {
						ResultSetMetaData rsmd1 = rs1.getMetaData();
						for (int i = 1; i <= rsmd1.getColumnCount(); i++) {
							map.put(rsmd1.getColumnName(i), rs1.getString(i));
						}
						l_all.add(map);
						map = new HashMap<String, String>();
					}
				}
			}

			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(sql);
			e.printStackTrace();
		}
		return l_all;
	}

	public Map<String, String> delById(String id) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		if (Check(id)) {
			int rs = 0;
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "delete from keywords where id='" + id + "'";
				rs = sm.executeUpdate(sql);
				sm = null;
				if (rs > 0) {
					//System.out.println(rs);
					mapret.put("status", Integer.toString(rs));
					mapret.put("msg", "删除成功！");
					outputList.key_flag = true;
					return mapret;
				} else {
					mapret.put("status", Integer.toString(101));
					mapret.put("msg", "删除失败！");
					return mapret;
				}
			} catch (Exception e) {
				String s = e.toString();
				s = s.substring(s.indexOf(":") + 2, s.length());
				mapret.put("msg", s);
			}
			mapret.put("status", Integer.toString(102));
			return mapret;
		} else{
			mapret.put("status", Integer.toString(103));
			mapret.put("msg", "参数错误！");
			return mapret;
		}
	}

	public Map<String, String> getById(String id) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (Check(id)) {
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "select * from keywords where id='" + id + "'";
				ResultSet rs = sm.executeQuery(sql);
				while (rs.next()) {
					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i <= rsmd.getColumnCount(); i++)
						map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				sm = null;
				rs = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public Map<String, String> updateKeyWords(Map<String, String> map) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		int id = 0;
		String sql = null;
		//if(Check(map)){
			try {
				id = Integer.parseInt(map.get("id").toString());
			} catch (Exception e) {
				id = 0;
			}
			if (id > 0) {
				sql = "update keywords set id='" + id + "'";
				for (int i=1;i<col.size();i++)
					sql += "," + col.get(i) + "='" + map.get(col.get(i)) + "'";
				sql += " where id='" + id + "'";
			} else {
				sql = "insert into keywords values('" + id + "'";
				for (int i=1;i<col.size();i++)
					sql += ",'" + map.get(col.get(i)) + "'";
				sql += ")";
			}
			try {
				Statement sm = Mysql.ct.createStatement();
				int rs = sm.executeUpdate(sql);
				sm = null;
				if (rs > 0) {
					mapret.put("status", Integer.toString(rs));
					mapret.put("msg", "更新成功！");
					outputList.key_flag = true;
					return mapret;
				} else {
					mapret.put("status", Integer.toString(101));
					mapret.put("msg", "更新失败！");
					return mapret;
				}
			} catch (Exception e) {
				System.out.println(sql);
				e.printStackTrace();
				String s = e.toString();
				s = s.substring(s.indexOf(":") + 2, s.length());
				mapret.put("msg", s);
			}
			mapret.put("status", Integer.toString(102));
			return mapret;
		/*	
		} else{
			mapret.put("status", Integer.toString(103));
			mapret.put("msg", "参数错误！");
			return mapret;
		}
		*/
	}

	private boolean Check(String str) {
		Pattern p = Pattern.compile(REGEX);
		boolean flg = p.matcher(str).matches();
		return flg;
	}
	/*
	private boolean Check(Map<String, String> map) {
		Pattern p = Pattern.compile(REGEX_sql);
		for (int i=0;i<col.size();i++)
			if(map.get(col.get(i))!=null && !p.matcher(map.get(col.get(i))).matches())
				return false;
		return true;
	}
	*/
}
