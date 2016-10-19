package com.applex.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseActionsListener implements MouseMotionListener,MouseListener  {

	public interface DragCallback {
		public void drag(int x_change, int y_change);
	}
	
	private DragCallback mDragCallback;
	
	private int mStartX;
	
	private int mStartY;
	
	private int mDragedX;
	
	private int mDragedY;
	
//	private long mLastClickTime = 0;
	
	public void setDragCallback(DragCallback callback) {
		mDragCallback = callback;
	}
	
	private void drag(int x_change, int y_change) {
		if (mDragCallback != null) {
			mDragCallback.drag(x_change, y_change);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		mDragedX = e.getX() - mStartX;
		mDragedY = e.getY() - mStartY;
		drag(mDragedX, mDragedY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		if (System.currentTimeMillis() - mLastClickTime < 400) {
//			mLastClickTime = 0;
//				doubleClick();
//		} else {
//			mLastClickTime = System.currentTimeMillis();
//		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mStartX = e.getX();
		mStartY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		mStartX = mStartY = mDragedX = mDragedY = 0;
	}


}
