package org.suych.model;

public class IndexModel {

	/**
	 * 索引名称
	 */
	private String index_name;

	/**
	 * 索引类型
	 */
	private String index_type;

	/**
	 * 索引列名
	 */
	private String column_name;

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public String getIndex_type() {
		return index_type;
	}

	public void setIndex_type(String index_type) {
		this.index_type = index_type;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

}
