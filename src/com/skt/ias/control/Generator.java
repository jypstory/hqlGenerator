package com.skt.ias.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;

import com.skt.ias.model.Column;
import com.skt.ias.model.Table;
import com.skt.ias.util.ExcelUtil;

/**
 * <pre>
 * <p>Title: com.skt.ias.control.Generator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012. 5. 16.</p>
 * </pre>
 * 
 * @author kdblue11@naver.com
 * @version
 */
public class Generator {

	private static String ADD_ENCRYPT_JAR = "create temporary function EncodeAES as 'com.skt.spade.EncodeAES';";

	public void generate(String interfaceDefExcelFullPath, String qlFilePath, String siteName) throws Exception {
		String templete = null;
		String initTemplete = null;
		String ql = null;
		String initQl = null;
		ArrayList<Table> tableList = parseTable(ExcelUtil.getSheets(interfaceDefExcelFullPath));
		for (Table table : tableList) {
			templete = readTemplete(table.templeteName);
			initTemplete = readTemplete(table.initTempleteName);
			ql = generateQl(templete, table, siteName);
			initQl = generateQl(initTemplete, table, siteName);
			writeQlFile(qlFilePath, table.tableNameHead + "_" + table.tableName, ql);
			writeQlFile(qlFilePath + "/" + "init", table.tableNameHead + "_" + table.tableName + "_" + "INIT", initQl);
		}
	}

	private String generateQl(String templete, Table table, String siteName) {
		String tmp = templete;
		tmp = tmp.replaceAll("#<AddEncryptJar>", table.hasEncrypt ? Generator.ADD_ENCRYPT_JAR : "");
		tmp = tmp.replaceAll("#<TableName>", table.tableName);
		tmp = tmp.replaceAll("#<Delimeter>", table.delimeter);
		tmp = tmp.replaceAll("#<SiteName>", siteName);
		tmp = tmp.replaceAll("#<TableNameHead>", table.tableNameHead);
		tmp = tmp.replaceAll("#<PK1>", table.primaryKey != null && table.primaryKey.size() > 0 ? table.primaryKey.get(0) : "");

		StringBuffer interfaceDef = new StringBuffer();
		StringBuffer interfaceDef_CDC = new StringBuffer();
		StringBuffer columnList = new StringBuffer();
		StringBuffer columnMergeList = new StringBuffer();
		StringBuffer onClause = new StringBuffer();
		StringBuffer columnEncryptList = new StringBuffer();
		boolean isEncrypt = false;
		
		Column column = null;
		for (int i = 0; i < table.columnList.size(); i++) { 
			column = table.columnList.get(i);
			isEncrypt = column.isEncrypt;
			columnList.append(column.columnName);
			//interfaceDef.append(column.columnName + " " + column.columnDataType + " " + "comment '"+column.columnComment+"'");
			interfaceDef.append(column.columnName + " " + column.columnDataType);
			interfaceDef_CDC.append(column.columnName + " " + "String");
			if (table.isMerge) {
				//columnMergeList.append("trim(IF(B." + table.primaryKey.get(0) + " IS NULL, A." + column.columnName + ", " + (isEncrypt ? "EncodeAES(B." + column.columnName + ")) " : "B." + column.columnName + ")) ") + column.columnName);
				if(column.columnDataType.equals("BIGINT")){
					columnMergeList.append("cast(trim(IF(B." + table.primaryKey.get(0) + " IS NULL, A." + column.columnName + ", " + (isEncrypt ? "EncodeAES(B." + column.columnName + ")) " : "B." + column.columnName + ")) as BIGINT) ") + column.columnName);
				} else {
					columnMergeList.append("trim(IF(B." + table.primaryKey.get(0) + " IS NULL, A." + column.columnName + ", " + (isEncrypt ? "EncodeAES(B." + column.columnName + ")) " : "B." + column.columnName + ")) ") + column.columnName);
				}
			}
			columnEncryptList.append(isEncrypt ? "EncodeAES(trim(" + column.columnName + ")) " + column.columnName : 
				(column.columnDataType.equals("BIGINT") ? "cast(trim("+column.columnName+") as BIGINT) " + column.columnName : "trim("+column.columnName+") " + column.columnName));
			if (i < table.columnList.size() - 1) {
				columnList.append(",\n       ");
				interfaceDef.append(",\n    ");
				interfaceDef_CDC.append(",\n    ");
				if (table.isMerge) {
					columnMergeList.append(",\n       ");
				}
				columnEncryptList.append(",\n       ");
			}
			if (column.isPk) {
				if (i != 0) {
					onClause.append(" AND ");
				}
				onClause.append("trim(A." + column.columnName + ") = " + (isEncrypt ? " trim(EncodeAES(B." + column.columnName + "))" : "trim(B." + column.columnName)+")");
			}
		}

		tmp = tmp.replaceAll("#<InterfaceDef>", interfaceDef.toString());
		tmp = tmp.replaceAll("#<InterfaceDef_CDC>", interfaceDef_CDC.toString());
		tmp = tmp.replaceAll("#<ColumnList>", columnList.toString());
		if (table.isMerge) {
			tmp = tmp.replaceAll("#<ColumnMergeList>", columnMergeList.toString());
		}
		tmp = tmp.replaceAll("#<OnClause>", onClause.toString());
		tmp = tmp.replaceAll("#<ColumnEncryptList>", columnEncryptList.toString());

		return tmp;
	}

	private ArrayList<Table> parseTable(Sheet[] sheets) {
		int maxRow = sheets[0].getRows();
		ArrayList<Table> tables = new ArrayList<Table>();

		Table table = null;
		String fullName = null;
		
		for (int rowIndex = 1; rowIndex < maxRow; rowIndex++) {
			if(!"".equals(sheets[0].getCell(0, rowIndex).getContents().trim())) {
				table = new Table();
				fullName = sheets[0].getCell(0, rowIndex).getContents().trim();
				table.tableName = fullName.substring(4);
				table.tableNameHead = fullName.substring(0, 3);
				table.templeteName = sheets[0].getCell(1, rowIndex).getContents().trim();
				table.delimeter =  "\\\\" + sheets[0].getCell(2, rowIndex).getContents().trim();
				table.hasEncrypt = "Y".equals(sheets[0].getCell(3, rowIndex).getContents().trim());
				table.primaryKey = new ArrayList<String>();
				table.columnList = parseColumn(findSheet(fullName, sheets), table);
				table.isMerge = "M".equals(sheets[0].getCell(4, rowIndex).getContents().trim());
				table.initTempleteName = sheets[0].getCell(5, rowIndex).getContents().trim();
				tables.add(table);
			}
		}

		return tables;
	}

	private Sheet findSheet(String sheetName, Sheet[] sheets) {
		Sheet target = null;
		for (int i = 1; i < sheets.length; i++) {

			if (sheetName.equals(sheets[i].getName().trim())) {
				target = sheets[i];
				break;
			}
		}
		return target;
	}

	private ArrayList<Column> parseColumn(Sheet sheet, Table table) {
		int maxRow = sheet.getRows();
		ArrayList<Column> columnList = new ArrayList<Column>();
		Column column = null;
		for (int rowIndex = 1; rowIndex < maxRow; rowIndex++) {
			if(!"".equals(sheet.getCell(0, rowIndex).getContents().trim())) {
				column = new Column();
				column.columnName = sheet.getCell(0, rowIndex).getContents().trim();
				column.columnDataType = sheet.getCell(1, rowIndex).getContents().trim();
				column.columnLength = sheet.getCell(2, rowIndex).getContents().trim();
				column.isPk = sheet.getCell(3, rowIndex).getContents().trim().equals("Y");
				column.columnComment = sheet.getCell(5, rowIndex).getContents().trim();
				if (column.isPk) {
					table.primaryKey.add(column.columnName);
				}
				column.isEncrypt = sheet.getCell(4, rowIndex).getContents().trim().equals("Y");
				
				columnList.add(column);
			}
		}
		return columnList;
	}

	private String readTemplete(String templeteName) throws Exception {
		StringBuffer templete = new StringBuffer();
		BufferedReader reader = null;

		String line = null;
		try {
			reader = new BufferedReader(new FileReader(templeteName));
			while ((line = reader.readLine()) != null) {
				templete.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return templete.toString();
	}

	private void writeQlFile(String qlFilePath, String tableName, String genSrc) throws IOException {
		FileWriter writer = null;
		try {
			//writer = new FileWriter(qlFilePath + "/" + tableName + ".ql");
			//writer = new FileWriter(qlFilePath + "/" + tableName + ".hql");
			writer = new FileWriter(qlFilePath + "/" + tableName + ".template");
			writer.write(genSrc);
			writer.flush();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					throw new IOException(e);
				}
			}
		}
	}
}
