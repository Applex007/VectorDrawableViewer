package com.applex.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import com.applex.logger.Logger;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class ViewPanel extends JPanel{

	private VectorDrawableOp mVop;
	
	private int mWidth, mHeight;
	
	private float mZoom = 1f;
	
	private boolean isSmooth = true;
	
	public ViewPanel(VectorDrawableOp vop) {
		super();
		mVop = vop;
		mWidth = Math.round(mVop.width);
		mHeight = Math.round(mVop.height);
		updateSize();
	}
	
	private void updateSize() {
		int cw = (int)(mWidth * mZoom);
		int ch = (int)(mHeight * mZoom);
		Logger.out("cw = " + cw + " ch = " + ch);
		setSize(cw, ch);	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		float scaleX = mWidth / mVop.vpWidht;
		float scaleY = mHeight / mVop.vpHeigth;
		scaleX *= mZoom;
		scaleY *= mZoom;
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		for (String pathData : mVop.pathDatas) {
			VectorDrawer.drawVector((Graphics2D)g, isSmooth, pathData, scaleX, scaleY);
		}
	}

	public float zoom(float change) {
		// TODO Auto-generated method stub
		float targetZoom = mZoom;
		if (mZoom <= 1 && change < 0) {
			// if zoom less or equals 1 and change less 1, targetZoom = zoom - |change| * 0.75
			targetZoom += change * 0.25f; 
		} else if (mZoom < 1 && change > 0) {
			// if zoom less 1 and change greater 1, targetZoom = zoom - |change| * 0.75
			targetZoom += change * 0.25f;
		} else {
			targetZoom = mZoom + change;
		}
		float targetWidht = mWidth * targetZoom;
		float targetHeight = mHeight * targetZoom;
		
		if (targetWidht < 10 || targetHeight < 10 || targetWidht > 1000 || targetHeight > 1000){
			return DefaultValues.UNCHANGED_ZOOM;
		}
		mZoom = targetZoom;
		updateSize();
		return targetZoom;
	}
}
