package com.applex.main;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class MouseZoomListener implements MouseWheelListener{

	public interface ZoomCallback {
		public void zoom(float change);
	}
	
	private ZoomCallback mZoomCallback;
	private float mZoomChange = 1f;
	
	public void setZoomCallback(ZoomCallback callback) {
		mZoomCallback = callback;
	}
	
	public void mouseWheelMoved(MouseWheelEvent wheelEvent) {
		// TODO Auto-generated method stub
		if (wheelEvent.getWheelRotation() == 1) {
//			Logger.out("zoom out!");
			zoom(-mZoomChange);
			
		} else if (wheelEvent.getWheelRotation() == -1) {
//			Logger.out("zoom in!");
			zoom(mZoomChange);
		}
	}
	
	private void zoom(float zoomChange) {
		if (mZoomCallback != null) {
			mZoomCallback.zoom(zoomChange);
		}
	}
	
}
