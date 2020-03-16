package com.linkallcloud.um.pc.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;


/**
 * Base64Util 
 *    Base64互转
 * 
 * @Author：luoam
 * @Date：    2017-10-17 17:27
 * @Description：
 *
 */
public class Base64Util {

	// BASE64转文本
    private static byte[] decode(final byte[] bytes) {  
        return Base64.decodeBase64(bytes);  
    }  
  
    // 文本转BASE64
    private static String encode(final byte[] bytes) {  
        return new String(Base64.encodeBase64(bytes));  
    }  
    
	/**
	 * 文本成Base64
	 * @throws UnsupportedEncodingException 
	 */
	public static String StrToBase64(String s) throws UnsupportedEncodingException  {
		String sRes="";
		if (s == null) return null;
		//sRes = encode(s.getBytes());
		sRes = encode(s.getBytes("UTF-8")); //2018-01-09, luwei查询省市区联动时出现乱码，增加参数后正常

		if ("/".equals(File.separator)) {
			sRes = sRes.replaceAll("\n", "");
		}else{
			sRes = sRes.replaceAll("\r\n", "");
		}
		
		
		return sRes;
	}

	// Base64转化文本
	public static String Base64ToStr(String s) {
		try {
			byte[] b = decode(s.getBytes());
			return new String(b, "UTF-8");
			//return new String(b, "GB2312");   UTF-8  "iso-8859"
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		String sTxt = "";
//		String sRes = "";
//		
//		sTxt = "This is a message of sms.";
//		sTxt = "由于您的固话号码已经升位或并网，对应的协同�?信帐号已同步变更，请重新输入";
//		
//		
//		sRes = StrToBase64(sTxt);
//		sTxt = Base64ToStr(sRes);
//		
//		String startTime="20120306 15:30:00";
//		sTxt = startTime.substring(0, 4)+"-"+startTime.substring(4, 6)+"-"+startTime.substring(6);
//		
//		sRes = "55S15L+h5a695bim6ICB5a6i5oi35Lus77ya5Y+y5LiK5pyA5LyY5oOg5pS/562W5p2l5LqG77yB5Yqg5YWlRTktMTI55aWX6aSQ5bCx54us5LqrNE3lhYnnuqTvvIzlrZgzMDDnvZHotLnov5jpgIHkuIDpg6g05a+45aSn5bGP5pm66IO95omL5py65ZOm77yM5YqgNDAw5YaN6YCBN+WvuOW5s+advw==";
//		sRes = "PFJlcXVlc3REYXRhIFRyYW5zYWN0aW9uSUQ9IkM0NTgyQUEzLTBEM0YtNDJiOC1BQ0JCLUUyRjM4MEU5MDY4MiIgUmVxdWVzdFRpbWU9IjIwMTIxMjE0MDgyNTU5IiBTZXJ2aWNlVHlwZT0iU01TQ29udGVudEdldCIgVmVyc2lvbj0iMi44LjEuMDAiIEFjY291bnQ9IjIwMTIxMzgzNjM2MzkzMjM3MzAzNTQwNjU2MzcwNkM2OTc2NjUyRTYzNkY2RDIxNDA4MjU1OSIgVGlja2V0PSI1OGJlYjk0NjM2NWFjZTRhYjc3ZDIwZjhjNmY5MGEzMyIgIENsaWVudEluZm89IjEuMCIgQ2xpZW50RGVzYz0iMjAxMjAxMjcuMC4wLjE4MDkxMTMwMDAiIFZlcnNpb25UeXBlPSIxMDQiPjxTTVNUeXBlPjEwMDwvU01TVHlwZT48U3RhcnRJbmRleD4xPC9TdGFydEluZGV4PjxFbmRJbmRleD40MDwvRW5kSW5kZXg+PFBob25lTnVtYmVyPjEzMTc5NjgwMzYwPC9QaG9uZU51bWJlcj48U3RhcnRUaW1lPjIwMTItMTItMDEgMDA6MDA6MDA8L1N0YXJ0VGltZT48RW5kVGltZT4yMDEzLTAxLTAxIDAwOjAwOjAwPC9FbmRUaW1lPjxBY2NvdW50UGhvbmU+MDU3NDg2NjkyNzA1PC9BY2NvdW50UGhvbmU+PC9SZXF1ZXN0RGF0YT4=";
//		sTxt = Base64ToStr(sRes);
		
		String str="5pa95a626Imv5oKo5aW9LOaCqOWcqDIwMTTlubQwNeaciDE25pel5omA5YGa55qE44CQ5aSn55Sf5YyWKOa1t++8ieOAkeajgOmqjOe7k+aenOWmguS4izrpl7TmjqXog4bnuqLntKDnu5Pmnpw6Ni42KOWPguiAgzoyLjB+MTUuMCk75L2O5a+G5bqm6ISC6JuL55m96IOG5Zu66YaH57uT5p6cOjIuMTco5Y+C6ICDOjAuMDB+My4xMik76auY";
		System.out.println();
		System.out.println("sr:"+Base64ToStr(str));
		System.out.println("sr:"+new String(Base64.decodeBase64(str)));
		System.out.println();
		
		String ss="施家良您好,您在2014年05月16日所做的【大生化(海）】检验结果如下:间接胆红素结果:6.6(参考:2.0~15.0);低密度脂蛋白胆固醇结果:2.17(参考:0.00~3.12);高密度脂蛋白胆固醇结果:0.99(参考:0.90~1.45);镁结果:1.02(参考:0.65~1.15);前白蛋白结果:117(参考:100~400);β羟丁酸结果:0.24(参考:0.03~0.30);a-L-岩藻糖甘酶结果:15(参考:0~40);腺苷脱氨酶结果:5.0(参考:0.0~15.0);总胆汁酸结果:4.4(参考:0.0~12.0);钙结果:2.33(参考:2.15~2.57);磷测定结果:1.36(参考:0.80~1.50);尿酸结果:338(参考:200~420);尿素氮结果:3.8(参考:2.8~7.2);脂肪酶结果:21.8(参考:5.6~51.3);氯结果:100.01(参考:96.00~106.00);肌酸激酶结果:182(参考:24~195);钾结果:4.16(参考:3.50~5.50);肌酸激酶同工酶结果:13(参考:0~25);钠结果:139.43(参考:135.00~145.00);C-反应蛋白结果:2.9(参考:0.0~10.0);乳酸脱氢酶结果:151(参考:100~240);载脂蛋白AI结果:1.27(参考:1.00~1.60);球蛋白结果:26.2(参考:20.0~30.0);肌酐结果:71(参考:50~110);甘油三酯结果:1.18(参考:0.40~1.80);总胆固醇结果:4.10(参考:3.00~6.00);载脂蛋白B结果:0.88(参考:0.60~1.10);碱性磷酸酶结果:45(参考:56~119);总胆红素结果:10.1(参考:3.0~20.0);丙氨酸氨基转移酶结果:13(参考:0~40);ALT/AST结果:1.00(参考:0.5~3.00);白蛋白结果:44.9(参考:35.0~55.0);γ-谷氨酰基转移酶结果:12(参考:11~61);白/球蛋白结果:1.7(参考:1.5~2.5);总蛋白结果:71.1(参考:55.0~85.0);葡萄糖结果:6.15(参考:3.89~6.11);天门冬氨酸氨基转移酶结果:13(参考:0~40);直接胆红素结果:3.5(参考:0.0~7.0); ";
		System.out.println(ss.length());
		String smsCont="dddd";
		int count= smsCont.getBytes().length/1000;
		System.out.println(count);
			
		String	sTxt = "这是文本短信。g==, |, ";  //\n
		System.out.println("原始信息: "+sTxt);
		String	sRes = StrToBase64(sTxt);
		System.out.println("转码信息: "+sRes);
		sTxt = Base64ToStr(sRes);
		System.out.println("还原信息: "+sTxt);

//			sTxt = "这是�?��短信。xml特殊字符�?, <, ', \", >";
//			System.out.println("原始信息: "+sTxt);
//			sRes = StrToBase64(sTxt);
//			System.out.println("转码信息: "+sRes);
//			sTxt = Base64ToStr(sRes);
//			System.out.println("还原信息: "+sTxt);
//			
//			sTxt = "诚信通浙江地�?388�?年，服务：全年提供买�?企业网站+无抵押贷款，详情回复1，阿里巴巴易有权";
//			System.out.println("原始信息: "+sTxt);
//			sRes = StrToBase64(sTxt);
//			System.out.println("转码信息: "+sRes+", "+sRes.length());
//			sTxt = Base64ToStr(sRes);
//			System.out.println("还原信息: "+sTxt);
//
//			sRes = "6K+a5L+h6YCa5rWZ5rGf5Zyw5Yy6MTM4OOWFgy/lubTvvIzmnI3liqHvvJrlhajlubTmj5DkvpvkubDlrrYr5LyB5Lia572R56uZK+aXoOaKteaKvOi0t+asvu+8jOivpuaDheWbnuWkjTHvvIzpmL/ph4zlt7Tlt7TmmJPmnInmnYM=";
//			sTxt = Base64ToStr(sRes);
//			System.out.println("还原信息: "+sTxt);
	}

}
