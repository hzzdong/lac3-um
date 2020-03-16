package test.com.linkallcloud.um.pc;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.um.kh.utils.Base64Util;

public class Base64Test {

    private static Log log = Logs.get();
    
	public static void main(String[] args) {
		String sBase = "data:application/vnd.android.package-archive;base64,UEsDBBQACAAIAMIYx0xtlXFauE0AAN/";
		log.info("SBase: "+sBase);

		if (sBase.indexOf("base64,")>0) sBase = sBase.substring(sBase.indexOf("base64,")+7);
		String bin = Base64Util.Base64ToStr(sBase);
		log.info("SBase: "+sBase);
		log.info("SBase: "+bin.length());
	}

}
