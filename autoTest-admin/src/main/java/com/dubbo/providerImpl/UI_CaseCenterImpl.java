package com.dubbo.providerImpl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.dubbo.provider.UI_CaseCenter;
import com.java.statics.Mysql;
import com.java.statics.outputList;

public class UI_CaseCenterImpl implements UI_CaseCenter{
	
	private static final String REGEX = "^[a-zA-Z0-9]+$";
	//private static final String REGEX_sql = "[^\\#\\']+";
	@SuppressWarnings("serial")
	private static final ArrayList<String> col_Case =  new ArrayList<String>(){{add("id"); add("casesId"); add("order_id"); add("optionss"); add("xPath"); add("datas"); add("checkName"); add("checkMethod"); add("expectedRes"); add("actualRes"); add("imgName"); add("caseDescription"); add("runState");}};
	@SuppressWarnings("serial")
	private static final ArrayList<String> col_Cases =  new ArrayList<String>(){{add("CasesId"); add("userid"); add("casesN"); add("Browser"); add("Bpath"); add("runStates");}};

	public ArrayList<Map<String, String>> SearchUI(String userId) { // 查询关键字
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "";
		try {
			Statement sm = Mysql.ct.createStatement();
			//查询用例场景
			ResultSet rs = sm.executeQuery("select * from casescene where userid='"+userId+"' order by casesId;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
				Statement sm1 = Mysql.ct.createStatement();
				//查询场景对应用例，并排序
				sql = "select * from caseoption where casesId='" + rs.getString(1) + "' order by order_id";
				ResultSet rs1 = sm1.executeQuery(sql);
				while (rs1.next()) {
					ResultSetMetaData rsmd1 = rs1.getMetaData();
					map = new HashMap<String, String>();
					for (int i = 1; i <= rsmd1.getColumnCount(); i++) {
						map.put(rsmd1.getColumnName(i),
								rs1.getString(i));
					}
					l_all.add(map);
				}
				map = new HashMap<String, String>();
			}
			/*
			 * System.out.println("size:"+l_all.size()); for (int
			 * i=0;i<l_all.size();i++)
			 * System.out.println(l_all.get(i).get("id"));
			 */
			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_all;
	}

	public ArrayList<Map<String, String>> SearchUI(String userId,String p) { // 按条件查询关键字
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "";
		try {
			Statement sm = Mysql.ct.createStatement();
			sql = "SELECT * FROM casescene WHERE casesN LIKE '%" + p + "%' and userid='"+userId+"' order by casesId;";
			ResultSet rs = sm.executeQuery(sql);
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
				map = new HashMap<String, String>();
				Statement sm1 = Mysql.ct.createStatement();
				sql = "select * from caseoption where casesId='"
						+ rs.getString(1) + "' order by order_id";
				ResultSet rs1 = sm1.executeQuery(sql);
				while (rs1.next()) {
					ResultSetMetaData rsmd1 = rs1.getMetaData();
					for (int i = 1; i <= rsmd1.getColumnCount(); i++) {
						map.put(rsmd1.getColumnName(i),
								rs1.getString(i));
					}
					l_all.add(map);
					map = new HashMap<String, String>();
				}
				map = new HashMap<String, String>();
			}

			sm = null;
			rs = null;
			/*
			 * System.out.println("size:"+l_all.size()); for (int
			 * i=0;i<l_all.size();i++)
			 * System.out.println(l_all.get(i).get("id"));
			 */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_all;
	}

	public Map<String, String> delCaseById(String id) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		if (Check(id)) {
			int rs = 0;
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "delete from caseoption where id='" + id + "'";
				rs = sm.executeUpdate(sql);
				sm = null;
				if (rs > 0) {
					//System.out.println(rs);
					mapret.put("status", Integer.toString(rs));
					mapret.put("msg", "删除成功！");
					Sort();
					outputList.key_flag = true;
					return mapret;
				} else {
					mapret.put("status", Integer.toString(101));
					mapret.put("msg", "删除失败！用例不存在");
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
	
	public Map<String, String> delCasesById(String id) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		if (Check(id)) {
			int rs = 0;
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "delete from casescene where casesId='" + id + "'";
				rs = sm.executeUpdate(sql);
				if (rs > 0) {
					//System.out.println(rs);
					sql = "delete from caseoption where casesId='" + id + "'";
					sm.executeUpdate(sql);
					sm = null;
					mapret.put("status", Integer.toString(rs));
					mapret.put("msg", "删除成功！");
					outputList.key_flag = true;
					return mapret;
				} else {
					sm = null;
					mapret.put("status", Integer.toString(101));
					mapret.put("msg", "删除失败！用例不存在。");
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

	public Map<String, String> getCaseById(String id) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (Check(id)) {
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "select * from caseoption where id='" + id + "'";
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
	
	public ArrayList<Map<String, String>> getCasesById(String id,String userId) {
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		String sql;
		if (Check(id)) {
			try {
				Statement sm = Mysql.ct.createStatement();
				//查询用例场景
				ResultSet rs = sm.executeQuery("select * from casescene where userid='"+userId+"' and casesId='"+id+"';");
				if (rs.next()) {
					map = new HashMap<String, String>();
					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						map.put(rsmd.getColumnName(i),
								rs.getString(i));
					}
					l_all.add(map);
					Statement sm1 = Mysql.ct.createStatement();
					//查询场景对应用例，并排序
					sql = "select * from caseoption where casesId='" + rs.getString(1) + "' order by order_id";
					ResultSet rs1 = sm1.executeQuery(sql);
					while (rs1.next()) {
						ResultSetMetaData rsmd1 = rs1.getMetaData();
						map = new HashMap<String, String>();
						for (int i = 1; i <= rsmd1.getColumnCount(); i++) {
							map.put(rsmd1.getColumnName(i),
									rs1.getString(i));
						}
						l_all.add(map);
					}
					map = new HashMap<String, String>();
				}
				/*
				 * System.out.println("size:"+l_all.size()); for (int
				 * i=0;i<l_all.size();i++)
				 * System.out.println(l_all.get(i).get("id"));
				 */
				sm = null;
				rs = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return l_all;
	}

	public Map<String, String> updateCase(Map<String, String> map) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		int id = 0;
		String sql = null;
		//if(Check(map,col_Case)){
			try {
				id = Integer.parseInt(map.get("id").toString());
			} catch (Exception e) {
				id = 0;
			}
			if (id > 0) {
				sql = "update caseoption set id='" + id + "'";
				for (int i=1;i<col_Case.size();i++)
					if(map.get(col_Case.get(i))!=null)
						sql += "," + col_Case.get(i) + "='" + map.get(col_Case.get(i)) + "'";
				sql += " where id='" + id + "'";
			} else {
				sql = "insert into caseoption values('" + id + "'";
				for (int i=1;i<col_Case.size();i++)
					sql += ",'" + map.get(col_Case.get(i)) + "'";
				sql += ")";
			}
			System.out.println(sql);
			try {
				Statement sm = Mysql.ct.createStatement();
				int rs = sm.executeUpdate(sql);
				sm = null;
				if (rs > 0) {
					mapret.put("status", Integer.toString(rs));
					mapret.put("msg", "更新成功！");
					Sort();
					outputList.key_flag = true;
					return mapret;
				} else {
					mapret.put("status", Integer.toString(101));
					mapret.put("msg", "更新失败！用例不存在");
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
		//} else{
		//	mapret.put("status", Integer.toString(103));
		//	mapret.put("msg", "参数错误！");
		//	return mapret;
		//}
	}
	
	public Map<String, String> updateCases(Map<String, String> map) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		int casesId = 0;
		String sql = null;
		//if(Check(map,col_Cases)){
			try {
				System.out.println(map.get("id"));
				casesId = Integer.parseInt(map.get("id").toString());
			} catch (Exception e) {
				casesId = 0;
			}
			if (casesId > 0) {
				sql = "update casescene set casesId='" + casesId + "'";
				for (int i=1;i<col_Cases.size();i++)
					sql += "," + col_Cases.get(i) + "='" + map.get(col_Cases.get(i)) + "'";
				sql += " where casesId='" + casesId + "'";
			} else {
				sql = "insert into casescene values('" + casesId + "'";
				for (int i=1;i<col_Cases.size();i++)
					sql += ",'" + map.get(col_Cases.get(i)) + "'";
				sql += ")";
			}
			System.out.println(sql);
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
					mapret.put("msg", "更新失败！用例不存在");
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
		//} else{
		//	mapret.put("status", Integer.toString(103));
		//	mapret.put("msg", "参数错误！");
		//	return mapret;
		//}
	}

	public Map<String, String> setState(String userid,String id){
		HashMap<String, String> mapret = new HashMap<String, String>();
		String sql="";
		if(Check(id)){
			try {
				ResultSet rs = null;
				Statement sm = Mysql.ct.createStatement();
				sql = "select id from caseoption where casesId=" + id + " and runState='FAIL';";
				rs = sm.executeQuery(sql);
				rs.next();
				try {
					if (rs != null && rs.getString(1) != null)
						sql = "update casescene set runStates='FAIL' where casesId=" + id;
					else
						sql = "update casescene set runStates='PASS' where casesId=" + id;
				} catch (Exception e) {
					sql = "update casescene set runStates='PASS' where casesId=" + id;
				}
				int rs1 = sm.executeUpdate(sql);
				if (rs1 > 0) {
					mapret.put("status", Integer.toString(rs1));
					mapret.put("msg", "更新成功！");
					outputList.key_flag = true;
					return mapret;
				} else {
					mapret.put("status", Integer.toString(101));
					mapret.put("msg", "更新失败！用例不存在");
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
		}else{
			mapret.put("status", Integer.toString(103));
			mapret.put("msg", "参数错误！");
			return mapret;
		}
	}
	
	private boolean Check(String str) {
		Pattern p = Pattern.compile(REGEX);
		boolean flg = p.matcher(str).matches();
		return flg;
	}	
	
	/*
	private boolean Check(Map<String, String> map,	ArrayList<String> cols) {
		Pattern p = Pattern.compile(REGEX_sql);
		for (int i=0;i<cols.size();i++)
			if(map.get(cols.get(i))!=null && !p.matcher(map.get(cols.get(i))).matches())
				return false;
		return true;
	}
	*/
	
	public static void Sort(){	//处理UI用例order_id不连续
		String id,casesId = "",sql;
		int count=0;
		try {
			Statement sm = Mysql.ct.createStatement();
			ResultSet rs = sm.executeQuery("select casesId from casescene");
			while (rs.next()) {
				rs.getMetaData();
				casesId = rs.getString(1);
				Statement sm1 = Mysql.ct.createStatement();
				ResultSet rs1 =null;
				
				sql="SELECT COUNT(id) FROM caseoption WHERE casesId="+casesId;
				rs1 = sm1.executeQuery(sql);
				rs1.next();
				try {
					count = Integer.parseInt(rs1.getString(1));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					count=0;
				}
				rs1=null;
				sql="SELECT MAX(order_id) FROM caseoption WHERE casesId="+casesId;
				rs1 = sm1.executeQuery(sql);
				rs1.next();
				try {
					Integer.parseInt(rs1.getString(1));
				} catch (NumberFormatException e) {
				}
				//System.out.println("max:"+max+"  count:"+count);
				if(count>=1){
					rs1=null;
					sql="SELECT id FROM caseoption WHERE casesId="+casesId+" ORDER BY order_id DESC";
					rs1 = sm1.executeQuery(sql);
					while (rs1.next()) {
						id=rs1.getString(1);
						sql="update caseoption set order_id="+count+" where id="+id;
						Statement sm2 = Mysql.ct.createStatement();
						sm2.executeUpdate(sql);
						count--;
					}
				}
			}
			/*
			System.out.println("size:"+outputList.l.size());
			for (int i=0;i<outputList.l.size();i++)
				System.out.println(outputList.l.get(i).get("id"));
				*/
			sm=null;
			rs=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
