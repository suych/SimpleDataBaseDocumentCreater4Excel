package org.suych.model;

import java.util.List;

/**
 * 完整的Model，用于生成文档
 */
public class CompleteModel {

	private TableModel tableModel;

	private List<ColumnModel> columnModels;

	private List<ConstraintModel> constraintModels;

	private List<IndexModel> indexModels;

	public TableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	public List<ColumnModel> getColumnModels() {
		return columnModels;
	}

	public void setColumnModels(List<ColumnModel> columnModels) {
		this.columnModels = columnModels;
	}

	public List<ConstraintModel> getConstraintModels() {
		return constraintModels;
	}

	public void setConstraintModels(List<ConstraintModel> constraintModels) {
		this.constraintModels = constraintModels;
	}

	public List<IndexModel> getIndexModels() {
		return indexModels;
	}

	public void setIndexModels(List<IndexModel> indexModels) {
		this.indexModels = indexModels;
	}

}
