package com.applex.main;

import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;

import com.applex.logger.Logger;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class VectorDrawableOp {

	private static String INSET = "inset";
	
	private static String INSETLEFT = "android:insetLeft";
	
	private static String INSETRIGHT = "android:insetRight";

	
	private static String VECTOR = "vector";
	
	private static String WIDTH = "android:width";
	
	private static String HEIGHT = "android:height";
	
	private static String VP_WIDTH = "android:viewportWidth";
	
	private static String VP_HEIGHT = "android:viewportHeight";
	
	private static String ALPHA = "android:alpha"; 

	
	private static String PATH = "path";
	
	private static String FILLALPHA = "android:fillAlpha";
	
	private static String FILLCOLOR = "android:fillColor";
	
	private static String STORKEALPHA = "android:strokeAlpha";
	
	private static String STORKECOLOR = "android:strokeColor";
	
	private static String PATHDATA = "android:pathData";

	public float insetLeft, insetRight, width, height, vpWidht, vpHeigth, alpha;
	
	public Vector<String> pathDatas = new Vector<String>(); 
	public Vector<String> fillColors = new Vector<String>();
	public Vector<Float> fillAlphas = new Vector<Float>();
	public Vector<Float> storkeAlphas = new Vector<Float>();
	public Vector<String> storkeColors = new Vector<String>();
	
	public boolean readVector(XmlPullParser xpp) {
		String arg = null;
		boolean ret = false;
		if (INSET.equals(xpp.getName())) {
			arg = xpp.getAttributeValue(null, INSETLEFT);
			arg = FormatNumStr(arg);
			insetLeft = arg == null ? 0f : Float.valueOf(arg).floatValue();
			
			arg = xpp.getAttributeValue(null, INSETRIGHT).replaceAll("dp", "");
			arg = FormatNumStr(arg);
			insetRight = arg == null ? 0f :  Float.valueOf(arg).floatValue();

			ret = true;
			Logger.out("insetLeft = " + insetLeft);
			Logger.out("insetRight = " + insetRight);
		} 

		
		if (VECTOR.equals(xpp.getName())) {
			arg = xpp.getAttributeValue(null, WIDTH);
			arg = FormatNumStr(arg);
			width = arg == null ? 0f : Float.valueOf(arg).floatValue();
			
			arg = xpp.getAttributeValue(null, HEIGHT);
			arg = FormatNumStr(arg);
			height = arg == null ? 0f : Float.valueOf(arg).floatValue();

			arg = xpp.getAttributeValue(null, VP_WIDTH);
			vpWidht = arg == null ? 0f : Float.valueOf(arg).floatValue();
			
			arg = xpp.getAttributeValue(null, VP_HEIGHT);
			vpHeigth = arg == null ? 0f : Float.valueOf(arg).floatValue();
			
			arg = xpp.getAttributeValue(null,  ALPHA);
			alpha = arg == null ? 1f : Float.valueOf(arg).floatValue();
			
			ret = true;
			
			Logger.out("width = " + width);
			Logger.out("height = " + height);
			
			Logger.out("vpWidht = " + vpWidht);
			Logger.out("vpHeigth = " + vpHeigth);
			
			Logger.out("alpha = " + alpha);
		}
		
		
		if (PATH.equals(xpp.getName())) {
			arg = xpp.getAttributeValue(null, FILLCOLOR);
			String fillColor = arg == null ? "#FFFFFFFF" : arg; 
			
			arg = xpp.getAttributeValue(null, FILLALPHA);
			float fillAlpha = arg == null ? 1f : Float.valueOf(arg).floatValue();
			
			arg = xpp.getAttributeValue(null, STORKECOLOR);
			String storkeColor = arg == null ? "#FFFFFFFF" : arg; 
			
			arg = xpp.getAttributeValue(null, STORKEALPHA);
			float storkeAlpha = arg == null ? 1f : Float.valueOf(arg).floatValue();
			
			arg = xpp.getAttributeValue(null, PATHDATA);
			String pathData = arg == null ? "" : arg;

			fillColors.add(fillColor);
			fillAlphas.add(fillAlpha);
			storkeColors.add(storkeColor);
			storkeAlphas.add(storkeAlpha);
			pathDatas.add(pathData);
			
			ret = true;
			
			Logger.out("fillColor = " + fillColor);
			Logger.out("fillAlpha = " + fillColor);
			Logger.out("storkeColor = " + fillColor);
			Logger.out("storkeAlpha = " + fillColor);
			Logger.out("pathData = " + pathData);
		}	
		
		return ret;
	}	  
	
	public static String FormatNumStr(String numStr) {
		if (numStr == null) return null; 
		return numStr.replaceAll("dp", "")
					.replaceAll("px", "")
					.replaceAll("sp", "");
	}
	
}
