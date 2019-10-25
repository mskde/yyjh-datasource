package com.mdq.yyjhservice.utils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;

public class ExcelUtils {

	//获取sheet数量
	public static int getSheetNum(File f) throws Exception{
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Workbook wb = null;//Excel文档对象
		Sheet sheet = null;//工作表
		// 选择2003/2007，构造Excel文档对象
		String fileName = f.getName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (suffix.equalsIgnoreCase("xls")) {
			wb = new HSSFWorkbook(new NPOIFSFileSystem(f).getRoot(), true);
		} else if (suffix.equalsIgnoreCase("xlsx")) {
			try {
				wb = new XSSFWorkbook(f);
			} catch (Exception ex) {
				wb = new HSSFWorkbook(new FileInputStream(f));
			}
		}
		int i=wb.getNumberOfSheets();
		return i;
	}

	public static List<Map<String, Object>> findDate(File f,int num) throws Exception{
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Workbook wb = null;//Excel文档对象
		Sheet sheet = null;//工作表
		// 选择2003/2007，构造Excel文档对象
		String fileName = f.getName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (suffix.equalsIgnoreCase("xls")) {
			wb = new HSSFWorkbook(new NPOIFSFileSystem(f).getRoot(), true);
		} else if (suffix.equalsIgnoreCase("xlsx")) {
			try {
				wb = new XSSFWorkbook(f);
			} catch (Exception ex) {
				wb = new HSSFWorkbook(new FileInputStream(f));
			}
		}
		//获取Excel中的工作表 getSheet(String name)根据sheet名称来获取
		sheet = wb.getSheetAt(num);//根据下标获取sheet
		//循环取行对象
		for(int i=0;i<=sheet.getLastRowNum();i++){
			//每行当做一个Map来存储
			Row row = sheet.getRow(i);
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			//循环取列对象
			if(row != null){
				for(int index=0;index<row.getLastCellNum();index++){
					//获取列对象
					Cell cell = row.getCell(index);
					Object value = getCellValue(cell);
					data.put("cell"+(index+1), value);
				}
			}
			//存储行getCellValue(Cell cell)
			datas.add(data);
		}
		return datas;
	}

	//判断类型
	private static Object getCellValue(Cell cell){
		Object result = "";
		//判断列类型
		if (null != cell) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC://判断是否数值
					result = getCellByDate(cell);
					break;
				case Cell.CELL_TYPE_BOOLEAN://判断是布尔型
					result = cell.getBooleanCellValue();
					break;
				case Cell.CELL_TYPE_STRING://判断是字符串型
					result = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_FORMULA://判断是数字
					result = cell.getNumericCellValue();
					break;
				default:
					result = "";
					break;
			}
		}
		return result;
	}

	private static Object getCellByDate(Cell cell) {
		Object obj = null;
		if (HSSFDateUtil.isCellDateFormatted(cell)) {
			Date date = cell.getDateCellValue();
			obj = date;
		} else {
			DecimalFormat df = new DecimalFormat("0");
			// 如果是纯数字
			double value = cell.getNumericCellValue();
			int i = (int) value;
			// 判断是整数还是小数
			if (i == value) {
				obj = i;
			} else {
				obj = df.format(cell.getNumericCellValue());
			}
		}
		return obj;
	}



	//根据list数据内容以及map过滤要求  判断是否符合过滤需求
	public  static boolean isAccordFilter(List<String> content,Map<String,String> filterDemand){
		content.get(0).indexOf(filterDemand.get("start_string"));
		content.get(content.size()-1).indexOf(filterDemand.get("end_string"));
		if(content.get(0).indexOf(filterDemand.get("start_string"))>-1 &&
				content.get(content.size()-1).indexOf(filterDemand.get("end_string"))>-1){
			for(String element: content){
				if(element.indexOf(filterDemand.get("content_string"))>-1){
					return true;
				}
			}
		}
		return false;
	}
	//

}
