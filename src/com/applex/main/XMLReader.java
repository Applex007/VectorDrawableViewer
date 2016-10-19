package com.applex.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.applex.logger.Logger;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class XMLReader {

	public static boolean readXml(String fileName, VectorDrawableOp vectorOp) {
		File file = new File(fileName); 
		XmlPullParser xpp = null;  
		FileReader fr = null;
		boolean ret = false;
		try {  
			fr = new FileReader(file);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
			xpp = factory.newPullParser();  
			xpp.setInput(fr);  
			int type = xpp.getEventType();  
			StringBuilder sb = new StringBuilder();  
			while(type != XmlPullParser.END_DOCUMENT){  
				switch(type){  
				case XmlPullParser.START_DOCUMENT:
					System.out.println("Start document");  
					break;  
				case XmlPullParser.START_TAG:  
					ret = vectorOp.readVector(xpp);
					break;  
				case XmlPullParser.END_TAG:  
					break;  
				case XmlPullParser.TEXT:  
					break;  
				default:break;  
				}  
				sb.delete(0, sb.length());  
				xpp.next();  
				type = xpp.getEventType();  
			}  
			System.out.println("End document");
			Logger.out("ret0=" + ret);
		}catch (FileNotFoundException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
			ret = false;
		}catch (XmlPullParserException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();
			ret = false;
		} catch (IOException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();
			ret = false;
		} finally {  
			try {  
				if(fr!=null){  
					fr.close();  
				}  
			} catch (IOException e) {  
				// TODO Auto-generated catch block  
				e.printStackTrace();
				ret = false;
			}     
		}     
		Logger.out("ret=" + ret);
		return ret;
	}  
}
