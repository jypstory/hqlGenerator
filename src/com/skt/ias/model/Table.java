package com.skt.ias.model;

import java.util.ArrayList;

/**
 * <pre>
 * <p>Title: com.skt.ias.model.Table</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012. 5. 16.</p>
 * <p>Company: SK Planet</p>
 * </pre>
 * 
 * @author kdblue11@naver.com
 * @version
 */
public class Table {
	public String tableName = null;
	public String tableNameHead = null;
	public ArrayList<String> primaryKey = null;
	
	public ArrayList<Column> columnList = null;
	public String templeteName = null;
	public String initTempleteName = null;
	public String delimeter = ","; //디폴트 ,
	public boolean hasEncrypt = false;
	public boolean isMerge = false;
}
