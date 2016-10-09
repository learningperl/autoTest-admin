package com.dubbo.providerImpl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.dubbo.provider.memberCenter;
import com.java.statics.Mysql;

public class memberCenterImpl implements memberCenter {

	private static final String REGEX = "^[a-zA-Z0-9]+$";

	/**
	 * @登录逻辑处理
	 */
	public Map<String, Object> login(String name, String pwd) {
		HashMap<String, Object> mapret = new HashMap<String, Object>();
		boolean res = true;

		// System.out.println(name+" : "+pwd);

		if (name == null || name.length() < 6 || pwd == null || pwd.length() < 6) {
			mapret.put("status", "105"); // 返回状态码105：用户名密码不能为空或者小于6位
			mapret.put("msg", "用户名密码不能为空或者小于6位");
			return mapret;
		}

		if (name.length() > 16 || pwd.length() > 16) {
			mapret.put("status", "104"); // 返回状态码104：用户名密码过长
			mapret.put("msg", "用户名密码过长");
			return mapret;
		}

		res = this.Check(name);
		if (!res) {
			mapret.put("status", "101"); // 返回状态码101：用户名有非法字符
			mapret.put("msg", "用户名有非法字符");
			return mapret;
		}

		res = this.Check(pwd);
		if (!res) {
			mapret.put("status", "101"); // 返回状态码101：用户名密码有非法字符
			mapret.put("msg", "用户名密码有非法字符");
			return mapret;
		}

		res = this.loginName(name);
		if (!res) {
			mapret.put("status", "102"); // 返回状态码102：用户名不存在
			mapret.put("msg", "用户名不存在");
			return mapret;
		}
		Map<String, String> mapInfo = new HashMap<String, String>();
		res = this.Password(name, pwd, mapInfo);
		if (!res) {
			mapret.put("status", "103"); // 返回状态码101：密码错误
			mapret.put("msg", "密码错误");
			return mapret;
		} else
			mapret.put("userInfo", mapInfo);

		mapret.put("status", "100"); // 返回状态码101：登录成功
		mapret.put("msg", "登录成功");
		return mapret;
	}

	public Map<String, String> register(Map<String, String> map) {
		HashMap<String, String> mapret = new HashMap<String, String>();
		boolean res = true;
		// System.out.println(name+" : "+pwd);

		String name = map.get("loginName");
		String pwd = map.get("password");

		if (name == null || name.length() < 6 || pwd == null || pwd.length() < 6) {
			mapret.put("status", "105"); // 返回状态码105：用户名密码不能为空或者小于6位
			mapret.put("msg", "用户名密码不能为空或者小于6位");
			return mapret;
		}

		if (name.length() > 16 || pwd.length() > 16) {
			mapret.put("status", "104"); // 返回状态码104：用户名密码过长
			mapret.put("msg", "用户名密码过长");
			return mapret;
		}

		res = this.Check(name);
		if (!res) {
			mapret.put("status", "101"); // 返回状态码101：用户名有非法字符
			mapret.put("msg", "用户名有非法字符");
			return mapret;
		}

		res = this.Check(pwd);
		if (!res) {
			mapret.put("status", "101"); // 返回状态码101：用户名密码有非法字符
			mapret.put("msg", "用户名密码有非法字符");
			return mapret;
		}

		res = this.loginName(name);
		if (res) {
			mapret.put("status", "102"); // 返回状态码102：用户名已经存在
			mapret.put("msg", "用户名已经存在");
			return mapret;
		}

		res = this.Reg(map);
		if (!res) {
			mapret.put("status", "103"); // 返回状态码101：数据库错误
			mapret.put("msg", "数据库错误");
			return mapret;
		}

		mapret.put("status", "100"); // 返回状态码101：用户名有非法字符
		mapret.put("msg", "注册成功");
		return mapret;

	}

	private boolean Reg(Map<String, String> map) {

		try {
			map.put("nickname", map.get("nickname").substring(0, 31));
		} catch (Exception e) {
		}
		try {
			map.put("describe", map.get("describe").substring(0, 63));
		} catch (Exception e) {
		}
		String sql = "insert into userinfo values(0,'" + map.get("nickname") + "','" + map.get("describe") + "','"
				+ map.get("loginName") + "','" + map.get("password") + "');";
		Statement sm;
		System.out.println(sql);
		try {
			sm = Mysql.ct.createStatement();
			int rs = 0;
			rs = sm.executeUpdate(sql);
			if (rs > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public Map<String, String> changePwd(Map<String, String> map) {

		HashMap<String, String> mapret = new HashMap<String, String>();
		boolean res = true;
		// System.out.println(name+" : "+pwd);

		String name = map.get("loginName");
		String pwd = map.get("password");

		if (name == null || name.length() < 6 || pwd == null || pwd.length() < 6) {
			mapret.put("status", "105"); // 返回状态码105：用户名密码不能为空或者小于6位
			mapret.put("msg", "用户名密码不能为空或者小于6位");
			return mapret;
		}

		if (name.length() > 16 || pwd.length() > 16) {
			mapret.put("status", "104"); // 返回状态码104：用户名密码过长
			mapret.put("msg", "用户名密码过长");
			return mapret;
		}

		res = this.Check(name);
		if (!res) {
			mapret.put("status", "101"); // 返回状态码101：用户名密码有非法字符
			mapret.put("msg", "用户名密码有非法字符");
			return mapret;
		}

		res = this.Check(map.get("newpwd"));
		if (!res) {
			mapret.put("status", "101"); // 返回状态码101：用户名密码有非法字符
			mapret.put("msg", "用户名密码有非法字符");
			return mapret;
		}

		res = this.loginName(name);
		if (!res) {
			mapret.put("status", "102"); // 返回状态码102：用户名不存在
			mapret.put("msg", "用户名已经存在");
			return mapret;
		}
		Map<String, String> mapInfo = new HashMap<String, String>();
		res = this.Password(name, pwd, mapInfo);
		if (!res) {
			mapret.put("status", "103"); // 返回状态码101：原密码错误
			mapret.put("msg", "原密码错误");
			return mapret;
		}

		res = this.updatePwd(name, map.get("newpwd"));
		if (!res) {
			mapret.put("status", "106"); // 返回状态码101：数据库错误
			mapret.put("msg", "数据库错误");
			return mapret;
		}

		mapret.put("status", "100"); // 返回状态码101：用户名有非法字符
		mapret.put("msg", "密码重置成功");
		return mapret;

	}

	private boolean updatePwd(String name, String new_pwd) {
		String sql = "update userinfo set password='" + new_pwd + "' where username='" + name + "';";
		Statement sm;
		System.out.println(sql);
		try {
			sm = Mysql.ct.createStatement();
			int rs = 0;
			rs = sm.executeUpdate(sql);
			if (rs > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @校验用户名是否存在
	 */
	private boolean loginName(String name) {
		String sql = "select id from userinfo where username='" + name + "'";
		Statement sm;
		try {
			sm = Mysql.ct.createStatement();
			ResultSet rs = null;
			rs = sm.executeQuery(sql);
			// rs.next();
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库错误1");
			return false;
		}
		return false;
	}

	/**
	 * @校验密码是否正确
	 */
	private boolean Password(String name, String pwd, Map<String, String> userInfo) {
		String sql = "select * from userinfo where username='" + name + "' and password='" + pwd + "'";
		try {
			Statement sm = Mysql.ct.createStatement();
			ResultSet rs = null;
			rs = sm.executeQuery(sql);

			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					if (!rsmd.getColumnName(i).equals("password"))
						userInfo.put(rsmd.getColumnName(i), rs.getString(i));
				}
				//System.out.println(userInfo.toString());
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库错误2");
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @校验是否包含非法字符
	 */
	private boolean Check(String str) {
		Pattern p = Pattern.compile(REGEX);
		boolean flg = p.matcher(str).matches();
		return flg;
	}

}
