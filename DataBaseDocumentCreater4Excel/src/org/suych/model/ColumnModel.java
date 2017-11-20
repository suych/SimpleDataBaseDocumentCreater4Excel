package org.suych.model;

import java.math.BigDecimal;

public class ColumnModel {

	/**
	 * 列名
	 */
	private String column_name;

	/**
	 * 列类型
	 */
	private String data_type;

	/**
	 * 列长度
	 */
	private BigDecimal data_length;

	/**
	 * 是否为空
	 */
	private String nullable;

	/**
	 * 默认值
	 */
	private String data_default;

	/**
	 * 列注释
	 */
	private String comments;

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public BigDecimal getData_length() {
		return data_length;
	}

	public void setData_length(BigDecimal data_length) {
		this.data_length = data_length;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getData_default() {
		return data_default;
	}

	public void setData_default(String data_default) {
		this.data_default = data_default;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}