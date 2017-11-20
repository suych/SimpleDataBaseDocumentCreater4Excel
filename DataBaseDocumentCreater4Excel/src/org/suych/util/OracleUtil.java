package org.suych.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OracleUtil {

	// volatile变量禁止指令重排序，让DCL生效
	private static volatile Connection connection;
	private static volatile PreparedStatement preparedStatement = null;
	private static volatile ResultSet resultSet = null;

	private Connection getConnection() {
		if (connection == null) {
			synchronized (OracleUtil.class) {
				if (connection == null) {
					try {
						Map<String, String> properties = PropertyUtil.getProperties();
						String driverClassName = properties.get("jdbc.driverClassName");
						String url = properties.get("jdbc.url");
						String user = properties.get("jdbc.username");
						String password = properties.get("jdbc.password");
						Class.forName(driverClassName);
						connection = DriverManager.getConnection(url, user, password);// 获取连接
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return connection;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List query(String sql, Class clazz) {
		List result = null;
		Connection con = getConnection();
		try {
			result = new ArrayList();
			preparedStatement = con.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData rsm = resultSet.getMetaData();// 结果集 中列的名称和类型的信息
			int colNumber = rsm.getColumnCount();
			Field[] fields = clazz.getDeclaredFields();
			while (resultSet.next()) {
				Object obj = clazz.newInstance();
				// 取出每一个字段进行赋值
				for (int i = 1; i <= colNumber; i++) {
					Object value = resultSet.getObject(i);
					// 匹配实体类中对应的属性
					for (int j = 0; j < fields.length; j++) {
						Field f = fields[j];
						if ((f.getName().toUpperCase()).equals(rsm.getColumnName(i))) {
							boolean flag = f.isAccessible();
							f.setAccessible(true);
							f.set(obj, value);
							f.setAccessible(flag);
							break;
						}
					}

				}
				result.add(obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// OracleUtil handle = new OracleUtil();
	// String sql = "select table_name, comments from user_tab_comments t where
	// t.table_name = upper('LOCK_DDXX')";
	// List<TableModel> tableModels = handle.query(sql, TableModel.class);
	// tableModels.forEach(n -> {
	// System.out.println(n.getTable_name());
	// System.out.println(n.getComments());
	// });
	// handle.close();
	// }
}
