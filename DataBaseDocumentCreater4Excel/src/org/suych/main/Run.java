package org.suych.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.suych.model.CompleteModel;
import org.suych.service.ExcelService;

public class Run {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ExcelService service = new ExcelService();
		// 1.生成完整数据
		List<CompleteModel> completeModels = service.getCompleteModels();
		// 2.创建数据库文档
		service.createDataBaseDocument2Excel2007(completeModels);
	}
}
