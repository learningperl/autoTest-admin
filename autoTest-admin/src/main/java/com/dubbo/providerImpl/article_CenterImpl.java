package com.dubbo.providerImpl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.dubbo.provider.article_Center;
import com.java.statics.Mysql;

public class article_CenterImpl implements article_Center {

	private static final String REGEX = "^[a-zA-Z0-9]+$";
	// private static final String REGEX_sql = "[^\\#\\']+";
	@SuppressWarnings("serial")
	private static final ArrayList<String> col_article = new ArrayList<String>() {
		{
			add("id");
			add("userid");
			add("author");
			add("title");
			add("agree");
			add("content");
			add("comment");
		}
	};
	@SuppressWarnings("serial")
	private static final ArrayList<String> col_comment = new ArrayList<String>() {
		{
			add("id");
			add("userid");
			add("articleid");
			add("content");
		}
	};
	private Map<String, String> mapret = null;

	public ArrayList<Map<String, String>> getCommentsById(String id, int index) { // 查询关键字
		index = index * 5;
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Statement sm = Mysql.ct.createStatement();
			// 查询用例场景
			ResultSet rs = sm.executeQuery(
					"SELECT comment.*,userinfo.nickname FROM comment,userinfo WHERE comment.articleid='"+id+"' and comment.userid=userinfo.id ORDER BY comment.id limit " + index + ",5;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
			}
			/*
			 * System.out.println("size:"+ArrayList<Map<String, String>>
			 * l_all.size()); for (int i=0;i<ArrayList<Map<String, String>>
			 * l_all.size();i++) System.out.println(ArrayList<Map<String,
			 * String>> l_all.get(i).get("id"));
			 */
			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_all;
	}

	public ArrayList<Map<String, String>> getEmojis() { // 获取emoji表情列表
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Statement sm = Mysql.ct.createStatement();
			// 查询用例场景
			ResultSet rs = sm.executeQuery("select img from emoji order by id;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put("img", rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
			}

			rs = sm.executeQuery("select COUNT(*) from emoji;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put("count", rs.getString(i));
				}
				l_all.add(map);
			}
			/*
			 * System.out.println("size:"+ArrayList<Map<String, String>>
			 * l_all.size()); for (int i=0;i<ArrayList<Map<String, String>>
			 * l_all.size();i++) System.out.println(ArrayList<Map<String,
			 * String>> l_all.get(i).get("id"));
			 */
			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_all;
	}

	public ArrayList<Map<String, String>> getFeatureArticle() { // 查询热点文章
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Statement sm = Mysql.ct.createStatement();
			// 查询用例场景
			ResultSet rs = sm.executeQuery("select * from article order by (agree+comment) desc limit 0,5;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (rsmd.getColumnName(i).equals("content"))
						try {
							map.put(rsmd.getColumnName(i), rs.getString(i).substring(0, 150));
						} catch (Exception e) {
							map.put(rsmd.getColumnName(i), rs.getString(i));
						}
					else
						map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
			}
			/*
			 * System.out.println("size:"+ArrayList<Map<String, String>>
			 * l_all.size()); for (int i=0;i<ArrayList<Map<String, String>>
			 * l_all.size();i++) System.out.println(ArrayList<Map<String,
			 * String>> l_all.get(i).get("id"));
			 */
			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(l_all.toString());
		return l_all;
	}

	public Map<String, String> updateComment(Map<String, String> map) {
		mapret = new HashMap<String, String>();
		String sql = null;
		if (Check(map.get("articleid").toString())) {
			try {
				Statement sm = Mysql.ct.createStatement();
				sql = "select id from article where id='" + map.get("articleid").toString() + "';";
				ResultSet rs = sm.executeQuery(sql);
				if (rs.next()) {
					sql = "insert into comment values('0'";
					for (int i = 1; i < col_comment.size(); i++)
						sql += ",'" + map.get(col_comment.get(i)) + "'";
					sql += ",CURRENT_TIMESTAMP)";
					System.out.println(sql);
					int rs1 = sm.executeUpdate(sql);
					if (rs1 > 0) {
						rs1 = 0;
						// 更新评论人次
						sql = "UPDATE article SET comment=comment+1 WHERE id='" + map.get("articleid").toString()
								+ "';";
						rs1 = sm.executeUpdate(sql);
						if (rs1 > 0) {
							mapret.put("status", "200");
							mapret.put("msg", "评论成功！");
							sm = null;
							return mapret;
						} else {
							// 默认两次更新一定会成功，可能不准。
							sql = "UPDATE article SET comment=comment+1 WHERE id='" + map.get("articleid").toString()
									+ "';";
							sm.executeUpdate(sql);
							mapret.put("status", "200");
							mapret.put("msg", "评论成功！");
							sm = null;
							return mapret;
						}
					} else {
						mapret.put("status", "201");
						mapret.put("msg", "评论失败，请重试！");
						return mapret;
					}
				} else {
					sm = null;
					mapret.put("status", "202");
					mapret.put("msg", "评论失败，帖子不存在或者已经删除！");
					return mapret;
				}
			} catch (Exception e) {
				System.out.println(sql);
				e.printStackTrace();
				String s = e.toString();
				s = s.substring(s.indexOf(":") + 2, s.length());
				mapret.put("msg", s);
			}
			mapret.put("status", "203");
			return mapret;
		} else {
			mapret.put("status", "204");
			mapret.put("msg", "参数错误！");
			return mapret;
		}
	}

	public Map<String, String> setAgree(String userid, String articleid) {
		mapret = new HashMap<String, String>();
		if (Check(userid) && Check(articleid)) {
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "select id from agree where articleid='" + articleid + "' and userid='" + userid + "';";
				ResultSet rs = sm.executeQuery(sql);
				if (rs.next()) {
					// System.out.println(rs);
					mapret.put("status", "201");
					mapret.put("msg", "您已经赞过该帖子啦！");
					return mapret;
				} else {
					sql = "insert into agree values('0','" + articleid + "','" + userid + "');";
					int rs1 = 0;
					rs1 = sm.executeUpdate(sql);
					if (rs1 > 0) {
						rs1 = 0;
						sql = "UPDATE article SET agree=agree+1 WHERE id='" + articleid + "';";
						rs1 = sm.executeUpdate(sql);
						if (rs1 > 0) {
							mapret.put("status", "200");
							mapret.put("msg", "点赞成功！");
							sm = null;
							return mapret;
						} else {
							sql = "delete from agree where articleid='" + articleid + "' and userid='" + userid + "';";
							sm.executeUpdate(sql);
							mapret.put("status", "202");
							mapret.put("msg", "点赞失败，请重试！");
							sm = null;
							return mapret;
						}
					} else {
						mapret.put("status", "203");
						mapret.put("msg", "点赞失败，请重试！");
						sm = null;
						return mapret;
					}
				}
			} catch (Exception e) {
				String s = e.toString();
				s = s.substring(s.indexOf(":") + 2, s.length());
				mapret.put("msg", s);
			}
			mapret.put("status", "204");
			return mapret;
		} else {
			mapret.put("status", "205");
			mapret.put("msg", "参数错误！");
			return mapret;
		}
	}

	public ArrayList<Map<String, String>> SearchArticle(String userId, int index) { // 查询关键字
		index = index * 5;
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Statement sm = Mysql.ct.createStatement();
			// 查询用例场景
			ResultSet rs = sm.executeQuery("select * from article where userid='" + userId
					+ "' order by createtime desc limit " + index + ",5;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (rsmd.getColumnName(i).equals("content"))
						try {
							map.put(rsmd.getColumnName(i), rs.getString(i).substring(0, 180));
						} catch (Exception e) {
							map.put(rsmd.getColumnName(i), rs.getString(i));
						}
					else
						map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
			}
			/*
			 * System.out.println("size:"+ArrayList<Map<String, String>>
			 * l_all.size()); for (int i=0;i<ArrayList<Map<String, String>>
			 * l_all.size();i++) System.out.println(ArrayList<Map<String,
			 * String>> l_all.get(i).get("id"));
			 */
			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_all;
	}

	public ArrayList<Map<String, String>> SearchArticle(int index) { // 查询关键字
		index = index * 5;
		ArrayList<Map<String, String>> l_all = new ArrayList<Map<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Statement sm = Mysql.ct.createStatement();
			// 查询用例场景
			ResultSet rs = sm.executeQuery("select * from article order by createtime desc limit " + index + ",5;");
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (rsmd.getColumnName(i).equals("content"))
						try {
							map.put(rsmd.getColumnName(i), rs.getString(i).substring(0, 150));
						} catch (Exception e) {
							map.put(rsmd.getColumnName(i), rs.getString(i));
						}
					else
						map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				l_all.add(map);
				map = new HashMap<String, String>();
			}
			/*
			 * System.out.println("size:"+ArrayList<Map<String, String>>
			 * l_all.size()); for (int i=0;i<ArrayList<Map<String, String>>
			 * l_all.size();i++) System.out.println(ArrayList<Map<String,
			 * String>> l_all.get(i).get("id"));
			 */
			sm = null;
			rs = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l_all;
	}

	public Map<String, String> delArticleById(String id) {
		mapret = new HashMap<String, String>();
		if (Check(id)) {
			int rs = 0;
			try {
				Statement sm = Mysql.ct.createStatement();
				String sql = "";
				sql = "delete from article where id='" + id + "'";
				rs = sm.executeUpdate(sql);
				sm = null;
				if (rs > 0) {
					// System.out.println(rs);
					mapret.put("status", "200");
					mapret.put("msg", "删除成功！");
					return mapret;
				} else {
					mapret.put("status", "201");
					mapret.put("msg", "删除失败！帖子不存在，或已删除。");
					return mapret;
				}
			} catch (Exception e) {
				String s = e.toString();
				s = s.substring(s.indexOf(":") + 2, s.length());
				mapret.put("msg", s);
			}
			mapret.put("status", "202");
			return mapret;
		} else {
			mapret.put("status", "203");
			mapret.put("msg", "参数错误！");
			return mapret;
		}
	}

	public Map<String, String> getArticleById(String id) {
		Map<String, String> map = new HashMap<String, String>();
		if (Check(id)) {
			try {
				Statement sm = Mysql.ct.createStatement();
				// 查询用例场景
				ResultSet rs = sm.executeQuery("select * from article where id='" + id + "';");
				while (rs.next()) {
					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						if (rsmd.getColumnName(i).equals("content"))
							map.put(rsmd.getColumnName(i), rs.getString(i));
						else
							map.put(rsmd.getColumnName(i), rs.getString(i));
					}
				}
				sm = null;
				rs = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public Map<String, String> updateArticle(Map<String, String> map) {
		mapret = new HashMap<String, String>();
		int id = 0;
		String sql = null;
		// if(Check(map,col_Case)){
		try {
			id = Integer.parseInt(map.get("id").toString());
		} catch (Exception e) {
			id = 0;
		}
		if (id > 0) {
			sql = "update article set id='" + id + "'";
			for (int i = 1; i < col_article.size(); i++)
				if (map.get(col_article.get(i)) != null)
					sql += "," + col_article.get(i) + "='" + map.get(col_article.get(i)) + "'";
			sql += " where id='" + id + "'";
		} else {
			sql = "insert into article values('" + id + "'";
			for (int i = 1; i < col_article.size(); i++)
				sql += ",'" + map.get(col_article.get(i)) + "'";
			sql += ",CURRENT_TIMESTAMP)";
		}
		System.out.println(sql);
		try {
			Statement sm = Mysql.ct.createStatement();
			int rs = sm.executeUpdate(sql);
			sm = null;
			if (rs > 0) {
				mapret.put("status", "200");
				mapret.put("msg", "更新成功！");
				return mapret;
			} else {
				mapret.put("status", "201");
				mapret.put("msg", "更新失败！帖子不存在，或已删除！");
				return mapret;
			}
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
			String s = e.toString();
			s = s.substring(s.indexOf(":") + 2, s.length());
			mapret.put("msg", s);
		}
		mapret.put("status", "202");
		return mapret;
		/*
		 * } else{ mapret.put("status", "203)); mapret.put("msg", "参数错误！");
		 * return mapret; }
		 */
	}

	private boolean Check(String str) {
		Pattern p = Pattern.compile(REGEX);
		boolean flg = p.matcher(str).matches();
		return flg;
	}

	/*
	 * private boolean Check(Map<String, String> map, ArrayList<String> cols) {
	 * Pattern p = Pattern.compile(REGEX_sql); for (int i=0;i<cols.size();i++)
	 * if(map.get(cols.get(i))!=null &&
	 * !p.matcher(map.get(cols.get(i))).matches()) return false; return true; }
	 */
}
