package org.suych.model;

public class ConstraintModel {

	/**
	 * 主键名称
	 */
	private String constraint_name;

	/**
	 * 主键类型
	 */
	private String constraint_type;

	/**
	 * 主键列名
	 */
	private String column_name;

	public String getConstraint_name() {
		return constraint_name;
	}

	public void setConstraint_name(String constraint_name) {
		this.constraint_name = constraint_name;
	}

	public String getConstraint_type() {
		return constraint_type;
	}

	public void setConstraint_type(String constraint_type) {
		this.constraint_type = constraint_type;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

}
