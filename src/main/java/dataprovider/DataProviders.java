package dataprovider;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;

import util.ExcelLibrary;

public class DataProviders extends ExcelLibrary{

	
	//valid credential login details
	@DataProvider(name="credentials")
	public Object[][]getCredentials(){
		//Total row count
		
		int rows= gerRowCount("Credentials");
		
		//Total column
		int column= getColumnCount("Credentials");
		
		int actRows= rows-1;
		
		Object[][]data= new Object[actRows][1];
		
		for (int i = 0; i < actRows; i++) {
			Map<String, String> hashMap= new HashMap<String, String>();
			
			for (int j = 0; j < column; j++) {
				
				//data[i][j]=getCellData("Credentials", j, i+2);
				//hashmap.put(getCellData("Credentials", j, 1), getCellData("Credentials", j, i+2));
				hashMap.put(getCellData("Credentials", j, 1),
						getCellData("Credentials", j, i + 2));
			}
			data[i][0]=hashMap;
		} 
		
		return data;
	}
	
	//invalid credential login details
	@DataProvider(name="InvalidCredentials")
	public Object[][]getInvalidCredentials(){
		//Total row count
		
		int rows= gerRowCount("InvalidCredentials");
		
		//Total column
		int column= getColumnCount("InvalidCredentials");
		
		int actRows= rows-1;
		
		Object[][]data= new Object[actRows][1];
		
		for (int i = 0; i < actRows; i++) {
			Map<String, String> hashMap= new HashMap<String, String>();
			
			for (int j = 0; j < column; j++) {
				
				//data[i][j]=getCellData("Credentials", j, i+2);
				//hashmap.put(getCellData("Credentials", j, 1), getCellData("Credentials", j, i+2));
				hashMap.put(getCellData("InvalidCredentials", j, 1),
						getCellData("InvalidCredentials", j, i + 2));
			}
			data[i][0]=hashMap;
		} 
		
		return data;
	}
	
	}
