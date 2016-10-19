package com.applex.main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import com.applex.logger.Logger;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class VectorDrawer {
	public static class Actions {
		public static final char M = 'M';
		public static final char L = 'L';
		public static final char H = 'H';
		public static final char V = 'V';
		public static final char C = 'C';
		public static final char S = 'S';
		public static final char Q = 'Q';
		public static final char T = 'T';
		public static final char A = 'A';
		public static final char Z = 'Z';
		public static final char m = 'm';
		public static final char l = 'l';
		public static final char h = 'h';
		public static final char v = 'v';
		public static final char c = 'c';
		public static final char s = 's';
		public static final char q = 'q';
		public static final char t = 't';
		public static final char a = 'a';
		public static final char z = 'z';		

		public static boolean isActionChar(char ch) {
			return ch == M || ch == m
					|| ch == L || ch == l
					|| ch == H || ch == h
					|| ch == V || ch == v
					|| ch == C || ch == c
					|| ch == S || ch == s
					|| ch == Q || ch == q
					|| ch == T || ch == t
					|| ch == A || ch == a
					|| ch == Z || ch == z;
		}

	};

	public static class VPoint{
		double x;
		double y;

		public VPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public VPoint(VPoint vpoint) {
			this.x = vpoint.x;
			this.y = vpoint.y;
		}
	}

	public static void drawVector(Graphics2D g, boolean isSmooth, String pathData, float scaleX, float scaleY) {
		// smooth process
		if (isSmooth) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// correct scale value
		if (scaleX < 0) scaleX = 1;
		if (scaleY < 0) scaleY = 1;
		VPoint startPoint = new VPoint(0, 0);
		VPoint curPaintLocation = new VPoint(0, 0);
		VPoint lastControlPoint = new VPoint(curPaintLocation);
		char previousAction = Actions.M;
		try {
			if (pathData == null) {
				Logger.out("pathData error! It's null!");
				return;
			}
			int length = pathData.length();
			if (length <= 0) {
				Logger.out("pathData error! There is no data!");
				return;
			} 
			if (pathData.charAt(0) != Actions.M && pathData.charAt(0) != Actions.m) {
				Logger.out("The first char is not 'M' or 'm', use the start point defalut: (0, 0)!!");
			}
			
			length = pathData.length();
			// start index, end index
			int s,e; 
			s = 0;
			e = s + 1;

			while(e <= length) {
				boolean isLastIndex = e == length;

				char ech = pathData.charAt(isLastIndex ? length-1 : e);
				// got an action
				if (Actions.isActionChar(ech)){
					String[] vars = isLastIndex ? new String[]{} : pathData.substring(s+1, e).trim().split(" |,");
					int len = vars.length;
					if (len > 0 || isLastIndex) {
						switch (pathData.charAt(s)) {
						case Actions.M:
							for (int n=0; n<len/2; n++){
								double Mx = Double.valueOf(vars[0+n*2]).doubleValue();
								double My = Double.valueOf(vars[1+n*2]).doubleValue();	
								curPaintLocation = new VPoint(Mx, My);
								startPoint = new VPoint(curPaintLocation);
							}
							break;
						case Actions.L:
							for (int n=0; n<len/2; n++) {
								double Lx = Double.valueOf(vars[0+n*2]).doubleValue();
								double Ly = Double.valueOf(vars[1+n*2]).doubleValue();
								drawLine(curPaintLocation.x, curPaintLocation.y, Lx, Ly, g, scaleX, scaleY);
								curPaintLocation = new VPoint(Lx, Ly);
							}
							break;
						case Actions.H:
							for (int n=0; n<len; n++) {
								double Hx = Double.valueOf(vars[n]).doubleValue();
								drawLine(curPaintLocation.x, curPaintLocation.y, Hx, curPaintLocation.y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(Hx, curPaintLocation.y);
							}
							break;
						case Actions.V:
							for (int n=0; n<len; n++) {
								double Vy = Double.valueOf(vars[n]).doubleValue();
								drawLine(curPaintLocation.x, curPaintLocation.y, curPaintLocation.x, Vy, g, scaleX, scaleY);
								curPaintLocation = new VPoint(curPaintLocation.x, Vy);
							}
							break;
						case Actions.C:
							for (int n=0; n<len/6; n++) {
								double C1x = Double.valueOf(vars[0+n*6]).doubleValue();
								double C1y = Double.valueOf(vars[1+n*6]).doubleValue();
								double C2x = Double.valueOf(vars[2+n*6]).doubleValue();
								double C2y = Double.valueOf(vars[3+n*6]).doubleValue();
								double C3x = Double.valueOf(vars[4+n*6]).doubleValue();
								double C3y = Double.valueOf(vars[5+n*6]).doubleValue();
								drawCubicCurve(curPaintLocation.x, curPaintLocation.y, C1x, C1y, C2x, C2y, C3x, C3y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(C3x, C3y);
								lastControlPoint = new VPoint(C2x, C2y);
							}
							break;
						case Actions.S:
							for (int n=0; n<len/4; n++){
								double S2x = Double.valueOf(vars[0+n*4]).doubleValue();
								double S2y = Double.valueOf(vars[1+n*4]).doubleValue();
								double S3x = Double.valueOf(vars[2+n*4]).doubleValue();
								double S3y = Double.valueOf(vars[3+n*4]).doubleValue();
								// S1 is the mirror of the last control point based the current point
								double S1x = curPaintLocation.x;
								double S1y = curPaintLocation.y;
								// 'S/s' need after 'C/c' or 'S/s'
								if (previousAction == Actions.C || previousAction == Actions.c
										|| previousAction == Actions.S || previousAction == Actions.s) {
									S1x = curPaintLocation.x * 2 - lastControlPoint.x;
									S1y = curPaintLocation.y * 2 - lastControlPoint.y;
								}
								drawCubicCurve(curPaintLocation.x, curPaintLocation.y,S1x, S1y, S2x, S2y, S3x, S3y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(S3x, S3y);
								lastControlPoint = new VPoint(S2x, S2y);
							}
							break;
						case Actions.Q:
							for (int n=0; n<len/4; n++) {
								double Q1x = Double.valueOf(vars[0+n*4]).doubleValue();
								double Q1y = Double.valueOf(vars[1+n*4]).doubleValue();
								double Q2x = Double.valueOf(vars[2+n*4]).doubleValue();
								double Q2y = Double.valueOf(vars[3+n*4]).doubleValue();
								drawQuadCurve(curPaintLocation.x, curPaintLocation.y, Q1x, Q1y, Q2x, Q2y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(Q2x, Q2y);
								lastControlPoint = new VPoint(Q1x, Q1y);
							}
							break;
						case Actions.T:
							for (int n=0; n<len/2; n++) {
								double T2x = Double.valueOf(vars[0+n*2]).doubleValue();
								double T2y = Double.valueOf(vars[1+n*2]).doubleValue();
								// T1 is the mirror of the last control point based the current point
								double T1x = curPaintLocation.x;
								double T1y = curPaintLocation.y;
								// 'T/t' need after 'Q/q' or 'T/t'
								if (previousAction == Actions.Q || previousAction == Actions.q
										|| previousAction == Actions.T || previousAction == Actions.t) {
									T1x = curPaintLocation.x * 2 - lastControlPoint.x;
									T1y = curPaintLocation.y * 2 - lastControlPoint.y;
								}
								drawQuadCurve(curPaintLocation.x, curPaintLocation.y, T1x, T1y, T2x, T2y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(T2x, T2y);
								lastControlPoint = new VPoint(T1x, T1y);
							}
							break;
						case Actions.A:
							for (int n=0; n<len/7; n++) {
								// radii of the ellipse
								double Arx = Double.valueOf(vars[0+n*7]).doubleValue();
								double Ary = Double.valueOf(vars[1+n*7]).doubleValue();
								// angle from the x-axis of the current coordinate system to the x-axi
								double Aϕ = Double.valueOf(vars[2+n*7]).doubleValue(); 
								// large arc flag
								double Afa = Double.valueOf(vars[3+n*7]).doubleValue();
								// sweep flag
								double Afs = Double.valueOf(vars[4+n*7]).doubleValue();
								// end point
								double Ax2 = Double.valueOf(vars[5+n*7]).doubleValue();
								double Ay2 = Double.valueOf(vars[6+n*7]).doubleValue();
								// start point
								double Ax1 = curPaintLocation.x;
								double Ay1 = curPaintLocation.y;
								drawArc(Ax1, Ay1, Ax2, Ay2, Arx, Ary, Afa, Afs, Aϕ, g, scaleX, scaleY);
								curPaintLocation = new VPoint(Ax2, Ay2);
								lastControlPoint = new VPoint(curPaintLocation);
							}
							break;
						case Actions.m:
							for (int n=0; n<len/2; n++){
								double mx = Double.valueOf(vars[0+n*2]).doubleValue();
								double my = Double.valueOf(vars[1+n*2]).doubleValue();
								mx += curPaintLocation.x;
								mx += curPaintLocation.y;
								curPaintLocation = new VPoint(mx, my);
								startPoint = new VPoint(curPaintLocation);
							}
							break;
						case Actions.l:
							for (int n=0; n<len/2; n++){
								double lx = Double.valueOf(vars[0+n*2]).doubleValue();
								double ly = Double.valueOf(vars[1+n*2]).doubleValue();
								lx += curPaintLocation.x;
								ly += curPaintLocation.y;
								drawLine(curPaintLocation.x, curPaintLocation.y, lx, ly, g, scaleX, scaleY);
								curPaintLocation = new VPoint(lx, ly);
							}
							break;
						case Actions.h:
							for (int n=0; n<len; n++) {
								double hx = Double.valueOf(vars[n]).doubleValue();
								hx += curPaintLocation.y;
								drawLine(curPaintLocation.x, curPaintLocation.y, hx, curPaintLocation.y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(hx, curPaintLocation.y);
							}
							break;
						case Actions.v:
							for (int n=0; n<len; n++) {
								double vy = Double.valueOf(vars[n]).doubleValue();
								vy += curPaintLocation.y;
								drawLine(curPaintLocation.x, curPaintLocation.y, curPaintLocation.x, vy, g, scaleX, scaleY);
								curPaintLocation = new VPoint(curPaintLocation.x, vy);
							}
							break;
						case Actions.c:
							for (int n=0; n<vars.length/6; n++){
								double c1x = Double.valueOf(vars[0+n*6]).doubleValue();
								double c1y = Double.valueOf(vars[1+n*6]).doubleValue();
								double c2x = Double.valueOf(vars[2+n*6]).doubleValue();
								double c2y = Double.valueOf(vars[3+n*6]).doubleValue();
								double c3x = Double.valueOf(vars[4+n*6]).doubleValue();
								double c3y = Double.valueOf(vars[5+n*6]).doubleValue();
								c1x += curPaintLocation.x;
								c1y += curPaintLocation.y;
								c2x += curPaintLocation.x;
								c2y += curPaintLocation.y;
								c3x += curPaintLocation.x;
								c3y += curPaintLocation.y;
								drawCubicCurve(curPaintLocation.x, curPaintLocation.y, c1x, c1y, c2x, c2y, c3x, c3y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(c3x, c3y);
								lastControlPoint = new VPoint(c2x, c2y);
							}
							break;
						case Actions.s:
							for (int n=0; n<len/4; n++) {
								double s2x = Double.valueOf(vars[0+n*4]).doubleValue();
								double s2y = Double.valueOf(vars[1+n*4]).doubleValue();
								double s3x = Double.valueOf(vars[2+n*4]).doubleValue();
								double s3y = Double.valueOf(vars[3+n*4]).doubleValue();
								s2x += curPaintLocation.x;
								s2y += curPaintLocation.y;
								s3x += curPaintLocation.x;
								s3y += curPaintLocation.y;
								// S1 is the mirror of the last control point based the current point
								double s1x = curPaintLocation.x;
								double s1y = curPaintLocation.y;
								if (previousAction == Actions.C || previousAction == Actions.c
										|| previousAction == Actions.S || previousAction == Actions.s) {
									s1x = curPaintLocation.x * 2 - lastControlPoint.x;
									s1y = curPaintLocation.y * 2 - lastControlPoint.y;
								}
								drawCubicCurve(curPaintLocation.x, curPaintLocation.y,s1x, s1y, s2x, s2y, s3x, s3y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(s3x, s3y);
								lastControlPoint = new VPoint(s2x, s2y);
							}
							break;
						case Actions.q:
							for (int n=0; n<len/4; n++) {
								double q1x = Double.valueOf(vars[0+n*4]).doubleValue();
								double q1y = Double.valueOf(vars[1+n*4]).doubleValue();
								double q2x = Double.valueOf(vars[2+n*4]).doubleValue();
								double q2y = Double.valueOf(vars[3+n*4]).doubleValue();
								drawQuadCurve(curPaintLocation.x, curPaintLocation.y, q1x, q1y, q2x, q2y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(q2x, q2y);
								lastControlPoint = new VPoint(q1x, q1y);
							}
							break;
						case Actions.t:
							for (int n=0; n<len/2; n++) {
								double t2x = Double.valueOf(vars[0+n*2]).doubleValue();
								double t2y = Double.valueOf(vars[1+n*2]).doubleValue();
								t2x += curPaintLocation.x;
								t2y += curPaintLocation.y;
								// T1 is the mirror of the last control point based the current point
								double t1x = curPaintLocation.x;
								double t1y = curPaintLocation.y;
								if (previousAction == Actions.Q || previousAction == Actions.q
										|| previousAction == Actions.T || previousAction == Actions.t) {
									t1x = curPaintLocation.x * 2 - lastControlPoint.x;
									t1y = curPaintLocation.y * 2 - lastControlPoint.y;
								}
								drawQuadCurve(curPaintLocation.x, curPaintLocation.y, t1x, t1y, t2x, t2y, g, scaleX, scaleY);
								curPaintLocation = new VPoint(t2x, t2y);
								lastControlPoint = new VPoint(t1x, t1y);
							}
							break;
						case Actions.a:
							for (int n=0; n<len/7; n++) {
								// radii of the ellipse
								double arx = Double.valueOf(vars[0+n*7]).doubleValue();
								double ary = Double.valueOf(vars[1+n*7]).doubleValue();
								// angle from the x-axis of the current coordinate system to the x-axi
								double aϕ = Double.valueOf(vars[2+n*7]).doubleValue(); 
								// large arc flag
								double afa = Double.valueOf(vars[3+n*7]).doubleValue();
								// sweep flag
								double afs = Double.valueOf(vars[4+n*7]).doubleValue();
								// end point
								double ax2 = Double.valueOf(vars[5+n*7]).doubleValue();
								double ay2 = Double.valueOf(vars[6+n*7]).doubleValue();
								ax2 += curPaintLocation.x;
								ax2 += curPaintLocation.y;
								// start point
								double ax1 = curPaintLocation.x;
								double ay1 = curPaintLocation.y;
								drawArc(ax1, ay1, ax2, ay2, arx, ary, afa, afs, aϕ, g, scaleX, scaleY);
								curPaintLocation = new VPoint(ax2, ay2);
								lastControlPoint = new VPoint(curPaintLocation);
							}
							break;
						case Actions.Z:
						case Actions.z:
							drawLine(curPaintLocation.x, curPaintLocation.y, startPoint.x, startPoint.y, g, scaleX, scaleY);
							curPaintLocation = new VPoint(startPoint);
							lastControlPoint = new VPoint(curPaintLocation);
							break;
						}
					}		

					s = e;
					e = s + 1;
					previousAction = pathData.charAt(isLastIndex ? length -1 : s);
				} else {
					e ++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void drawLine(
			double L0x, double L0y, 
			double L1x, double L1y,
			Graphics2D g,
			float scaleX, float scaleY) {
		L0x *= scaleX;
		L1x *= scaleX;
		L0y *= scaleY;
		L1y *= scaleY;
		Line2D line = new Line2D.Double(L0x, L0y, L1x, L1y);
		g.draw(line);
	}

	private static void drawCubicCurve(
			double C0x, double C0y, 
			double C1x, double C1y,
			double C2x, double C2y, 
			double C3x, double C3y,
			Graphics2D g,
			float scaleX, float scaleY) {
		C0x *= scaleX;
		C1x *= scaleX;
		C2x *= scaleX;
		C3x *= scaleX;
		C0y *= scaleY;
		C1y *= scaleY;
		C2y *= scaleY;
		C3y *= scaleY;
		CubicCurve2D Ccurve =  new	CubicCurve2D.Double(C0x, C0y, C1x, C1y, C2x, C2y, C3x, C3y);
		g.draw(Ccurve);
	}

	private static void drawQuadCurve(
			double Q0x, double Q0y,
			double Q1x, double Q1y, 
			double Q2x, double Q2y, 
			Graphics2D g,
			float scaleX, float scaleY) {
		Q0x *= scaleX;
		Q1x *= scaleX;
		Q2x *= scaleX;
		Q0y *= scaleY;
		Q1y *= scaleY;
		Q2y *= scaleY;
		QuadCurve2D Qcurve = new QuadCurve2D.Double(Q0x, Q0y, Q1x, Q1y, Q2x, Q2y);
		g.draw(Qcurve);
	}

	private static void drawArc(
			double Ax1, double Ay1, // start point
			double Ax2, double Ay2, // end point
			double Arx, double Ary, // radii of the ellipse
			double Afa,             // large arc flag
			double Afs,             // sweep flag
			double Aϕ,               // angle from the x-axis of the current coordinate system to the x-axi
			Graphics2D g,
			float scaleX, float scaleY) {
		Ax1 *= scaleX;
		Ax2 *= scaleX;
		Ay1 *= scaleY;
		Ay2 *= scaleY;
		Arx *= scaleX;
		Ary *= scaleY;
		// Ensure endpoints (x1, y1) and (x2, y2) are not identica
		if (Ax1 == Ax2 && Ay1 == Ay2) {
			return;
		}
		// Ensure radii are non-zero
		if (Arx == 0 || Ary == 0) {
			// if radii have zero value
			// then draw line from start point to end point
			Line2D l = new Line2D.Double(Ax1, Ay1, Ax2, Ay2);
			g.draw(l);
			return;
		}

		// convert degrees to radians
		Aϕ = Math.toRadians(Aϕ);

		// F6.5.1
		double sinϕ = Math.sin(Aϕ);
		double cosϕ = Math.cos(Aϕ);
		double half_diff_x = (Ax1 - Ax2) / 2;
		double half_diff_y = (Ay1 - Ay2) / 2;
		double Ax1_temp = cosϕ * half_diff_x + sinϕ * half_diff_y;
		double Ay1_temp = -sinϕ * half_diff_x + cosϕ * half_diff_y;
		// Ensure radii are positive
		Arx = Math.abs(Arx);
		Ary = Math.abs(Ary);
		// Ensure radii are large enough
		double Aadjust = Math.pow(Ax1_temp / Arx, 2) + Math.pow(Ay1_temp / Ary, 2);
		if (Aadjust > 1) {
			double Ascale = Math.sqrt(Aadjust);
			Arx *= Ascale;
			Ary *= Ascale;
		}
		// F6.5.2
		double Arxry = Arx * Ary;
		double Arxy1_temp = Arx * Ay1_temp;
		double Aryx1_temp = Ary * Ax1_temp;
		double AleftArg = Math.sqrt(
				(Math.pow(Arxry, 2) - Math.pow(Arxy1_temp, 2) - Math.pow(Aryx1_temp, 2))
				/ (Math.pow(Arxy1_temp, 2) + Math.pow(Aryx1_temp, 2)));
		if (Afa == Afs) AleftArg = -AleftArg;
		double Acx_temp = AleftArg * (Arxy1_temp / Ary);
		double Acy_temp = AleftArg * (-Aryx1_temp / Arx);
		// F6.5.3
		double half_sum_x = (Ax1 + Ax2) / 2;
		double half_sum_y = (Ay1 + Ay2) / 2;
		double Acx = cosϕ * Acx_temp - sinϕ * Acy_temp + half_sum_x;
		double Acy = sinϕ * Acx_temp + cosϕ * Acy_temp + half_sum_y;

		double Astartθ = getRadian(1, 0, 
				(Ax1_temp - Acx_temp) / Arx, (Ay1_temp - Acy_temp) / Ary);

		double Adiffθ = getRadian(
				(Ax1_temp - Acx_temp) / Arx, (Ay1_temp - Acy_temp) / Ary,
				(-Ax1_temp - Acx_temp) / Arx, (-Ay1_temp - Acy_temp) / Ary);

		Adiffθ %= Math.PI * 2;
		if (Afs == 0 && Adiffθ > 0) Adiffθ -= Math.PI * 2;
		if (Afs == 1 && Adiffθ < 0) Adiffθ += Math.PI * 2;

		// left-top point
		double Altx = Acx - Arx;
		double Alty = Acy - Ary;

		// convert radians to degrees
		Astartθ = Math.toDegrees(Astartθ);
		Adiffθ = Math.toDegrees(Adiffθ);

		Logger.out("Astartθ = " + Astartθ);
		Logger.out("Adiffθ = " + Adiffθ);

		// in Arc2D, clockwise is negative
		Arc2D arc = new Arc2D.Double(Altx, Alty, Arx*2, Ary*2, -Astartθ, -Adiffθ, Arc2D.OPEN);
		g.draw(arc);
	}

	private static double getRadian(
			/*vector u*/double ux, double uy, /*vector v*/double vx, double vy) {
		double dot = ux * vx + uy * vy;
		double module = Math.sqrt(
				(Math.pow(ux, 2) + Math.pow(uy, 2)) * (Math.pow(vx, 2) + Math.pow(vy, 2)));

		Logger.out("dot=" + dot + " m0dule=" + module);

		double θ = Math.acos(dot / module);
		if (ux * vy - uy * vx < 0) θ = -θ;
		return θ;
	}
}
