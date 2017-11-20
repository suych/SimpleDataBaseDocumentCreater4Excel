package org.suych.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.suych.model.ColumnModel;
import org.suych.model.CompleteModel;
import org.suych.model.ConstraintModel;
import org.suych.model.IndexModel;
import org.suych.model.TableModel;
import org.suych.util.OracleUtil;
import org.suych.util.PropertyUtil;
import org.suych.util.StringUtil;

public class ExcelService {

	/**
	 * 表名，逗号分割
	 */
	private String tableName;

	/**
	 * 文档路径
	 */
	private String filePath;

	public ExcelService() {
		init();
	}

	private void init() {
		Map<String, String> properties = PropertyUtil.getProperties();
		this.tableName = properties.get("table.name");
		this.filePath = properties.get("file.path");
	}

	public List<CompleteModel> getCompleteModels() {
		String[] tableNameArray = tableName.split(",");
		List<CompleteModel> completeModels = new ArrayList<CompleteModel>();
		for (String tableName : tableNameArray) {
			CompleteModel completeModel = getCompleteModel(tableName.trim());
			completeModels.add(completeModel);
		}
		return completeModels;
	}

	@SuppressWarnings("unchecked")
	private CompleteModel getCompleteModel(String tableName) {
		// 表信息Sql
		String tableSql = "select table_name, comments from user_tab_comments t where t.table_name = upper('"
				+ tableName + "')";
		// 列信息Sql
		String columnSql = "select t.COLUMN_NAME, t.DATA_TYPE, t.DATA_LENGTH, t.NULLABLE, t.DATA_DEFAULT, c.comments from user_tab_columns t left join (select table_name, column_name, comments from user_col_comments t) c on t.table_name = c.table_name and t.column_name = c.column_name where t.TABLE_NAME = upper('"
				+ tableName + "') order by column_id";
		// 主键信息Sql
		String constraintSql = "select t.constraint_name, 'Primary' as constraint_type, t.column_name from user_cons_columns t where constraint_name = (select constraint_name from user_constraints where table_name = upper('"
				+ tableName + "') and constraint_type = 'P')";
		// 索引信息Sql
		String indexSql = "select t.index_name, t.index_type,i.column_name from user_indexes t left join (select t.table_name,t.index_name,t.column_name from user_ind_columns t) i on t.table_name = i.table_name and t.index_name = i.index_name where t.table_name = upper('"
				+ tableName + "')";

		CompleteModel result = new CompleteModel();
		OracleUtil handle = new OracleUtil();
		List<TableModel> tableModels = handle.query(tableSql, TableModel.class);
		List<ColumnModel> columnModels = handle.query(columnSql, ColumnModel.class);
		List<ConstraintModel> constraintModels = handle.query(constraintSql, ConstraintModel.class);
		List<IndexModel> indexModels = handle.query(indexSql, IndexModel.class);

		result.setTableModel(tableModels.get(0));
		result.setColumnModels(columnModels);
		result.setConstraintModels(constraintModels);
		result.setIndexModels(indexModels);
		return result;
	}

	@SuppressWarnings("unused")
	public void createDataBaseDocument2Excel2007(List<CompleteModel> completeModels) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// cell边框
		CellStyle cellThin = createBorderCellStyle(workBook);
		// cell字体加粗
		CellStyle cellFondBord = createFontCellStyle(workBook);

		for (int i = 0; i < completeModels.size(); i++) {
			int lineNumber = 0; // 行号
			XSSFSheet sheet = workBook.createSheet();

			CompleteModel completeModel = completeModels.get(i);

			TableModel tableModel = completeModel.getTableModel();
			String tableName = StringUtil.null2Empty(tableModel.getTable_name());
			String comments = StringUtil.null2Empty(tableModel.getComments());

			// 设置sheet名称
			workBook.setSheetName(i, tableName + comments);

			// 表名行
			XSSFRow row1 = sheet.createRow(lineNumber);
			XSSFCell cell1_1 = row1.createCell(0);
			cell1_1.setCellStyle(cellFondBord);
			cell1_1.setCellValue(tableName + comments);
			lineNumber++;

			// 空行
			XSSFRow rowEmpty = sheet.createRow(lineNumber);
			lineNumber++;

			// 表信息-标题
			XSSFRow row2 = sheet.createRow(lineNumber);
			XSSFCell cell2_1 = row2.createCell(0);
			cell2_1.setCellStyle(cellFondBord);
			cell2_1.setCellValue("表信息:");
			lineNumber++;

			// 表信息-头
			XSSFRow row3 = sheet.createRow(lineNumber);
			XSSFCell cell3_1 = row3.createCell(0);
			cell3_1.setCellStyle(cellThin);
			cell3_1.setCellValue("名称");
			XSSFCell cell3_2 = row3.createCell(1);
			cell3_2.setCellStyle(cellThin);
			cell3_2.setCellValue("类型");
			XSSFCell cell3_3 = row3.createCell(2);
			cell3_3.setCellStyle(cellThin);
			cell3_3.setCellValue("可为空");
			XSSFCell cell3_4 = row3.createCell(3);
			cell3_4.setCellStyle(cellThin);
			cell3_4.setCellValue("默认值");
			XSSFCell cell3_5 = row3.createCell(4);
			cell3_5.setCellStyle(cellThin);
			cell3_5.setCellValue("注释");
			lineNumber++;

			// 第4行至后续---表信息-实际信息
			List<ColumnModel> columnModels = completeModel.getColumnModels();
			int columnSize = columnModels.size();
			for (int j = 0; j < columnSize; j++) {
				ColumnModel columnModel = columnModels.get(j);
				XSSFRow tempRow = sheet.createRow(lineNumber);

				XSSFCell cellTemp_1 = tempRow.createCell(0);
				cellTemp_1.setCellStyle(cellThin);
				cellTemp_1.setCellValue(columnModel.getColumn_name());// 名称

				XSSFCell cellTemp_2 = tempRow.createCell(1);
				cellTemp_2.setCellStyle(cellThin);
				String dataType = columnModel.getData_type();
				if ("VARCHAR2".equals(dataType)) {
					dataType = dataType + "(" + columnModel.getData_length() + ")";
				} else if ("NUMBER".equals(dataType)) {
					// TODO 如果需要整数位/小数位在此修改
					dataType = "INTEGER";
				}
				cellTemp_2.setCellValue(dataType);// 类型

				XSSFCell cellTemp_3 = tempRow.createCell(2);
				cellTemp_3.setCellStyle(cellThin);
				cellTemp_3.setCellValue(columnModel.getNullable());// 可为空

				XSSFCell cellTemp_4 = tempRow.createCell(3);
				cellTemp_4.setCellStyle(cellThin);
				cellTemp_4.setCellValue(columnModel.getData_default());// 默认值

				XSSFCell cellTemp_5 = tempRow.createCell(4);
				cellTemp_5.setCellStyle(cellThin);
				cellTemp_5.setCellValue(columnModel.getComments());// 注释

				lineNumber++;
			}

			// 空行
			rowEmpty = sheet.createRow(lineNumber);
			lineNumber++;

			// 主键信息-标题
			XSSFRow rowConstraintTitle = sheet.createRow(lineNumber);
			XSSFCell cellConstraintTitle = rowConstraintTitle.createCell(0);
			cellConstraintTitle.setCellStyle(cellFondBord);
			cellConstraintTitle.setCellValue("主键信息:");
			lineNumber++;

			// 主键信息-头
			XSSFRow rowConstraintHead = sheet.createRow(lineNumber);
			XSSFCell cellConstraintHead_1 = rowConstraintHead.createCell(0);
			cellConstraintHead_1.setCellStyle(cellThin);
			cellConstraintHead_1.setCellValue("名称");
			XSSFCell cellConstraintHead_2 = rowConstraintHead.createCell(1);
			cellConstraintHead_2.setCellStyle(cellThin);
			cellConstraintHead_2.setCellValue("类型");
			XSSFCell cellConstraintHead_3 = rowConstraintHead.createCell(2);
			cellConstraintHead_3.setCellStyle(cellThin);
			cellConstraintHead_3.setCellValue("列");
			lineNumber++;

			// 主键信息-实际信息
			List<ConstraintModel> constraintModels = completeModel.getConstraintModels();
			columnSize = constraintModels.size();
			for (int k = 0; k < columnSize; k++) {
				ConstraintModel constraintModel = constraintModels.get(k);

				XSSFRow tempRow = sheet.createRow(lineNumber);

				XSSFCell cellTemp_1 = tempRow.createCell(0);
				cellTemp_1.setCellStyle(cellThin);
				cellTemp_1.setCellValue(constraintModel.getConstraint_name()); // 名称

				XSSFCell cellTemp_2 = tempRow.createCell(1);
				cellTemp_2.setCellStyle(cellThin);
				cellTemp_2.setCellValue(constraintModel.getConstraint_type()); // 类型

				XSSFCell cellTemp_3 = tempRow.createCell(2);
				cellTemp_3.setCellStyle(cellThin);
				cellTemp_3.setCellValue(constraintModel.getColumn_name()); // 列

				lineNumber++;
			}

			// 空行
			rowEmpty = sheet.createRow(lineNumber);
			lineNumber++;

			// 索引信息-标题
			XSSFRow rowIndexTitle = sheet.createRow(lineNumber);
			XSSFCell cellIndexTitle = rowIndexTitle.createCell(0);
			cellIndexTitle.setCellStyle(cellFondBord);
			cellIndexTitle.setCellValue("索引信息:");
			lineNumber++;

			// 索引信息-头
			XSSFRow rowIndexHead = sheet.createRow(lineNumber);
			XSSFCell cellIndexHead_1 = rowIndexHead.createCell(0);
			cellIndexHead_1.setCellStyle(cellThin);
			cellIndexHead_1.setCellValue("名称");
			XSSFCell cellIndexHead_2 = rowIndexHead.createCell(1);
			cellIndexHead_2.setCellStyle(cellThin);
			cellIndexHead_2.setCellValue("类型");
			XSSFCell cellIndexHead_3 = rowIndexHead.createCell(2);
			cellIndexHead_3.setCellStyle(cellThin);
			cellIndexHead_3.setCellValue("列");
			lineNumber++;

			// 索引信息-实际信息
			List<IndexModel> indexModels = completeModel.getIndexModels();
			columnSize = indexModels.size();
			for (int l = 0; l < columnSize; l++) {
				IndexModel indexModel = indexModels.get(l);
				XSSFRow tempRow = sheet.createRow(lineNumber);
				XSSFCell cellTemp_1 = tempRow.createCell(0);
				cellTemp_1.setCellStyle(cellThin);
				cellTemp_1.setCellValue(indexModel.getIndex_name()); // 名称
				XSSFCell cellTemp_2 = tempRow.createCell(1);
				cellTemp_2.setCellStyle(cellThin);
				cellTemp_2.setCellValue(indexModel.getIndex_type()); // 类型
				XSSFCell cellTemp_3 = tempRow.createCell(2);
				cellTemp_3.setCellStyle(cellThin);
				cellTemp_3.setCellValue(indexModel.getColumn_name()); // 列
				lineNumber++;
			}

			/* 自动调整宽度 */
			for (int n = 0; n < 6; n++) {
				sheet.autoSizeColumn(n);
			}
		}

		FileOutputStream os = new FileOutputStream(filePath);

		workBook.write(os);// 将文档对象写入文件输出流
		os.close();// 关闭文件输出流
		workBook.close();
		System.out.println("创建成功《数据库设计文档》！");

	}

	private CellStyle createBorderCellStyle(XSSFWorkbook workBook) {
		CellStyle style = workBook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	private CellStyle createFontCellStyle(XSSFWorkbook workBook) {
		CellStyle style = workBook.createCellStyle();
		XSSFFont fontBord = createFont(workBook);
		style.setFont(fontBord);
		return style;
	}

	private XSSFFont createFont(XSSFWorkbook workBook) {
		XSSFFont font = workBook.createFont();
		font.setBold(true);
		return font;
	}

}
