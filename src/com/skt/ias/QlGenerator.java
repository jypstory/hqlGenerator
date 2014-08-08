package com.skt.ias;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.skt.ias.control.Generator;

/**
 * <pre>
 * <p>Title: com.skt.ias.QlGenerator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014. 5. 21.</p>
 * <p>Company: SK C&C</p>
 * </pre>
 * 
 * @author jyp
 * @version
 */
public class QlGenerator {
	public static void main(String[] args) throws Exception {
		Generator generator = new Generator();

		System.out.println("start............");
		generator.generate("./itf/interfaceList_khb.xls", "./hql", "KHB"); 
//		generator.generate("./itf/interfaceList_imc.xls", "./hql", "IMC");  
//		generator.generate("./itf/interfaceList_isc.xls", "./hql", "ISC"); 
//		generator.generate("./itf/interfaceList_ipc.xls", "./hql", "IPC"); 
//		generator.generate("./itf/interfaceList_mbf.xls", "./hql", "MBF");  
//		generator.generate("./itf/interfaceList_ocp.xls", "./hql", "OCP"); 
//		generator.generate("./itf/interfaceList_oct.xls", "./hql", "OCT");  
//		generator.generate("./itf/interfaceList_soi.xls", "./hql", "SOI");  
//		generator.generate("./itf/interfaceList_qrm.xls", "./hql", "QRM");  
//		generator.generate("./itf/interfaceList_tcp.xls", "./hql", "TCP"); 
//		generator.generate("./itf/interfaceList_gfc.xls", "./hql", "GFC"); 
//		generator.generate("./itf/interfaceList_obc.xls", "./hql", "OBC"); 
//		generator.generate("./itf/interfaceList_adn.xls", "./hql", "ADN"); 
//		generator.generate("./itf/interfaceList_mlf.xls", "./hql", "MLF"); 
		
		
		System.out.println("finish...........");

		SimpleDateFormat dateFormat = null;
		Date date = null;
		try {
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			System.out.println("OK");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR");
		}
		
	}
}
