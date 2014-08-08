package com.skt.ias.model;

/**
 * <pre>
 * <p>Title: com.skt.ias.model.Column</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012. 5. 16.</p>
 * <p>Company: SK Planet</p>
 * </pre>
 * 
 * @author kdblue11@naver.com
 * @version
 */
public class Column {
	
	public String columnName = null;
	public String columnDataType = null;
	public String columnLength = null;
	
	public boolean isPk = false;
	public boolean isEncrypt = false;
	
	public String columnComment = null;
}
