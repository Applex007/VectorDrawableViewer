package com.applex.main;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import com.applex.logger.Logger;
import com.applex.main.MainFrame.FileOpenedCallback;
import com.applex.main.MainFrame.WinstateChangeCallback;
import com.applex.main.MouseActionsListener.DragCallback;
import com.applex.main.MouseZoomListener.ZoomCallback;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class Main implements FileOpenedCallback, ZoomCallback, DragCallback, WinstateChangeCallback{
	
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	
	private static Rectangle mScreenSize = ge.getMaximumWindowBounds();
	
	private MainFrame mMainFrame;

	private ViewPanel mViewPanel;

	private SizeLabel mSizeLabel;

	private boolean mIsFullScreen = false;
	
	MouseZoomListener mouseZoomListener;

	MouseActionsListener mouseActionsListener;

	private void init() {
		mMainFrame = new MainFrame();
		mMainFrame.setFileOpenedCallback(this);
		mMainFrame.	setVisible(true);
	}

	@Override
	public void openFile(String path) {
		// TODO Auto-generated method stub
		VectorDrawableOp vectorOp = new VectorDrawableOp();
		if (XMLReader.readXml(path, vectorOp)) {
			if (mSizeLabel != null) mMainFrame.remove(mSizeLabel);
			mSizeLabel = new SizeLabel(vectorOp);
			mMainFrame.add(mSizeLabel);
			setBottomRightLocation(mMainFrame, mSizeLabel, mIsFullScreen);

			if (mViewPanel != null) mMainFrame.remove(mViewPanel);
			mViewPanel = new ViewPanel(vectorOp);
			mMainFrame.add(mViewPanel);
			setCenterLocation(mMainFrame, mViewPanel, mIsFullScreen);

			mouseZoomListener = new MouseZoomListener();
			mouseActionsListener = new MouseActionsListener();
			mMainFrame.addMouseWheelListener(mouseZoomListener);
			mViewPanel.addMouseListener(mouseActionsListener);
			mViewPanel.addMouseMotionListener(mouseActionsListener);
			mouseZoomListener.setZoomCallback(this);
			mouseActionsListener.setDragCallback(this);
			mMainFrame.setWinstateChangeCallback(this);
		} else {
			mMainFrame.showOpenErrorDialog();
		}
	}

	@Override
	public void zoom(float change) {
		// TODO Auto-generated method stub
		if (mViewPanel != null) {
			float targetZoom = mViewPanel.zoom(change);
			setCenterLocation(mMainFrame, mViewPanel, mIsFullScreen);
			if (mSizeLabel != null) {
				mSizeLabel.showZoom(targetZoom);
				setBottomRightLocation(mMainFrame, mSizeLabel, mIsFullScreen);
			}
		}
	}

	@Override
	public void drag(int x_change, int y_change) {
		// TODO Auto-generated method stub
		dragInParent(mMainFrame, mViewPanel, x_change, y_change, mIsFullScreen);
	}

	@Override
	public void windowstateChange(boolean isFullScreen) {
		// TODO Auto-generated method stub
		mIsFullScreen = isFullScreen;
		setCenterLocation(mMainFrame, mViewPanel, mIsFullScreen);
		setBottomRightLocation(mMainFrame, mSizeLabel, mIsFullScreen);
	}

	private static void setCenterLocation(Component parent, Component child, boolean isFullScreen) {
		int parentWidth = isFullScreen ? mScreenSize.width : parent.getWidth();
		int parentHeight = isFullScreen ?  mScreenSize.height : parent.getHeight();
		int leftTopX = (parentWidth - child.getWidth()) / 2;
		int leftTopY = ((parentHeight - 50) - child.getHeight()) / 2; // subtract height of title bar
		child.setBounds(leftTopX, leftTopY, child.getWidth(), child.getHeight());
	}

	private static void setBottomRightLocation(Component parent, Component child, boolean isFullScreen) {
		int parentWidth = isFullScreen ? mScreenSize.width : parent.getWidth();
		int parentHeight = isFullScreen ?  mScreenSize.height : parent.getHeight();
		int leftTopX = parentWidth - child.getWidth();
		int leftTopY = (parentHeight - 50) - child.getHeight(); // subtract height of title bar
		Logger.out("leftTopX:" + leftTopX + " leftTopY:" + leftTopY);
		child.setBounds(leftTopX, leftTopY, child.getWidth(), child.getHeight());
	}

	public static void dragInParent(Component parent, Component child, int x_change, int y_change, boolean isFullScreen) {
		int parentWidth = isFullScreen ? mScreenSize.width : parent.getWidth();
		int parentHeight = isFullScreen ?  mScreenSize.height : parent.getHeight();
		
		if (child.getWidth() > parentWidth || child.getHeight() > parentHeight) {

			int viewWidht = parentWidth;
			int leftTopX = child.getX();
			int ViewHeigth = parentHeight;
			int leftTopY = child.getY();
			
			if (child.getWidth() > parentWidth) {
				if (x_change > 0) { // move right
					leftTopX = (leftTopX + x_change) < 0 ?  leftTopX + x_change : 0;
				} else {            //move left
					int rightBottomX = leftTopX + child.getWidth();
					rightBottomX = (rightBottomX + x_change) > viewWidht ? (rightBottomX + x_change) : viewWidht;
					leftTopX = rightBottomX - child.getWidth();
				}
			}
			if (child.getHeight() > parentHeight) {
				
				if (y_change < 0) { // move up
					int rightBottomY = leftTopY + child.getHeight();
					rightBottomY = (rightBottomY + y_change) > ViewHeigth ? (rightBottomY + y_change) : ViewHeigth;
					leftTopY = rightBottomY - child.getHeight();
				} else {            // move down
					leftTopY = (leftTopY + y_change) < 0 ?  (leftTopY + y_change) : 0;
				}
			}
			child.setBounds(leftTopX, leftTopY, child.getWidth(), child.getHeight());
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().init();
	}
	
}
