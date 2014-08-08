package com.skt.ias.util;

import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * <pre>
 * <p>Title: com.skt.ias.util.ExcelUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012. 5. 16.</p>
 * <p>Company: SK Planet</p>
 * </pre>
 * 
 * @author kdblue11@naver.com
 * @version
 */
public class ExcelUtil {

	/**
	 * 액셀파일에서 jxl.Workbook 을 얻는다.
	 * @param fileName 액셀파일명
	 * @return jxl.Workbook
	 * @throws Exception
	 */
	public static Workbook getWorkBook(String fileName) throws Exception {
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(new java.io.File(fileName));
		} catch (BiffException ex) {
			throw new Exception("BiffException [" + ex.getMessage() + "]");
		} catch (IOException ex) {
			throw new Exception("IOException [" + ex.getMessage() + "]");
		}
		return workbook;
	}

	/**
	 * 액셀파일의 첫번째 Sheet를 얻는다.
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Sheet getSheet(String fileName) throws Exception {
		return getSheet(fileName, 0);
	}

	/**
	 * 액셀파일의 sheetNo번째 Sheet를 얻는다.
	 * sheetNo는 0부터 시작한다.
	 * @param fileName 액셀파일명
	 * @return jxl.Sheet
	 * @throws Exception
	 */
	public static Sheet getSheet(String fileName, int sheetNo) throws Exception {
		Sheet sheet = null;
		try {
			sheet = ExcelUtil.getWorkBook(fileName).getSheet(sheetNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Exception [" + e.getMessage() + "]");
		}

		return sheet;
	}
	
	/**
	 * 액셀파일의 모든 Sheet를 얻는다.
	 * sheetNo는 0부터 시작한다.
	 * @param fileName 액셀파일명
	 * @return jxl.Sheet[]
	 * @throws Exception
	 */
	public static Sheet[] getSheets(String fileName) throws Exception {
		Workbook workbook = ExcelUtil.getWorkBook(fileName);		
		return workbook.getSheets();
	}
}