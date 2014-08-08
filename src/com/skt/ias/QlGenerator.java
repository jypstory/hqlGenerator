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
 * @author pjy0313@sk.com
 * @version
 */
public class QlGenerator {
	public static void main(String[] args) throws Exception {
		Generator generator = new Generator();

		System.out.println("start............");
		generator.generate("./itf/interfaceList_khb.xls", "./hql", "KHB");  //khub
		generator.generate("./itf/interfaceList_imc.xls", "./hql", "IMC");  //IMC 정보
		generator.generate("./itf/interfaceList_isc.xls", "./hql", "ISC");  //Istore coupon
		generator.generate("./itf/interfaceList_ipc.xls", "./hql", "IPC");  //Istore poc 정보
		generator.generate("./itf/interfaceList_mbf.xls", "./hql", "MBF");  //모바일전단지
		generator.generate("./itf/interfaceList_ocp.xls", "./hql", "OCP");  //OCB pass
		generator.generate("./itf/interfaceList_oct.xls", "./hql", "OCT");  //OCB Tagging
		generator.generate("./itf/interfaceList_soi.xls", "./hql", "SOI");  //SOI*/
		generator.generate("./itf/interfaceList_qrm.xls", "./hql", "QRM");  //QRMS
		generator.generate("./itf/interfaceList_tcp.xls", "./hql", "TCP");  //통합쿠폰
		generator.generate("./itf/interfaceList_gfc.xls", "./hql", "GFC");  //QRMS
		generator.generate("./itf/interfaceList_obc.xls", "./hql", "OBC");  //OCB.com
		generator.generate("./itf/interfaceList_adn.xls", "./hql", "ADN");  //애딩
		generator.generate("./itf/interfaceList_mlf.xls", "./hql", "MLF");  //모바일 전단
		
		
		System.out.println("finish...........");

		SimpleDateFormat dateFormat = null;
		Date date = null;
		try {
			dateFormat = new SimpleDateFormat("yyyyMMdd");
			date = dateFormat.parse("20140508");
			System.out.println("OK");
			//System.out.println(dateFormat.format(date));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR");
		}
		
	}
}
