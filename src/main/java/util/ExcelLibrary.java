package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelLibrary {

	public static String path = "G:\\eclipse workplace\\SwagLabs\\src\\test\\java\\resources\\TestData.xlsx";

	public FileInputStream fis = null;
	public FileOutputStream fisOut = null;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet = null;
	public XSSFRow row = null;
	public XSSFCell cell = null;

	public ExcelLibrary() {
		this.path=path;
		try {
			fis = new FileInputStream(path);
			System.out.println(fis.toString());
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) { 
			e.printStackTrace();
		}
			
	}
	

	public int gerRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			return 0;
		} else {

			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;

		}

	}

	public int getColumnCount(String sheetName) {

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();

		
	}
	
	public String getCellData( String sheeName, int colNum, int rowNum) {
		try {
		
		if(rowNum<0)
			return " ";
		
		int index= workbook.getSheetIndex(sheeName);
		
		if(index==-1)
			return "";
		
		sheet= workbook.getSheetAt(index);
		row= sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		
		cell= row.getCell(colNum);
		return cell.getStringCellValue();
		
		}catch(Exception e) {
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist in xls";
		}
		
	}

}
























