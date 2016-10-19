package com.applex.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * 
 * @author applex 2016-09-29 
 *
 */

public class MainFrame extends JFrame implements ActionListener, WindowStateListener{
	
	public interface FileOpenedCallback {
		public void openFile(String path);
	}

	public interface WinstateChangeCallback {
		public void windowstateChange(boolean isFullScreen);
	} 
	
	private int mDefaultWidth = 640;
	
	private int mDefaultHeight = 480;
	
	private JMenuBar mBar = new JMenuBar();

	private JMenu mOpenFile = new JMenu(DefaultValues.OPEN_FILE);
	
	private JMenu mAbout = new JMenu(DefaultValues.ABOUT);
	
	private JMenuItem mOpenFileItem = new JMenuItem(DefaultValues.OPEN_FILE);
	
	private JMenuItem mAboutItem = new JMenuItem(DefaultValues.ABOUT);
	
	private VectorDrawableFileOpen mFileOpen = new VectorDrawableFileOpen(); 
	
	private FileOpenedCallback mFileOpenedCallback;
	
	private WinstateChangeCallback mWinstateChangeCallback;
	
	private boolean mIsFullScreen;
	
	public MainFrame() {
		super();
		setTitle(DefaultValues.TITLE);
		setDefaultSize();
		setLocationRelativeTo(null);
		setLayout(null);
		getContentPane().setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowStateListener(this);
		
		mOpenFileItem.addActionListener(this);
		mAboutItem.addActionListener(this);
		mOpenFile.add(mOpenFileItem);
		mAbout.add(mAboutItem);
		mBar.add(mOpenFile);
		mBar.add(mAbout);
		setJMenuBar(mBar);
	}

	public void setDefaultSize() {
		setSize(mDefaultWidth, mDefaultHeight);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == mOpenFileItem) {
			int result = mFileOpen.showOpenDialog(this);
			if (result == VectorDrawableFileOpen.APPROVE_OPTION) {
				String path = mFileOpen.getSelectedFile().getAbsolutePath();
				openFile(path);
			}
		} 
		else if (e.getSource() == mAboutItem) {
			showAboutDialog();
		} 
		
	}
	
	public void setFileOpenedCallback(FileOpenedCallback callback) {
		mFileOpenedCallback = callback;
	}
	
	public void setWinstateChangeCallback(WinstateChangeCallback callback) {
		mWinstateChangeCallback = callback;
	}
	
	private void openFile(String path) {
		if (mFileOpenedCallback != null) {
			mFileOpenedCallback.openFile(path);
		}
	}
	
	public void windowStateChange(boolean isFullScreen) {
		if (mWinstateChangeCallback != null) {
			mWinstateChangeCallback.windowstateChange(isFullScreen);
		}
	}
	
	public void showOpenErrorDialog() {
		Dialog.showOpenFileErrorDialog(this);
	}
	
	private void showAboutDialog() {
		Dialog.showAboutDialog(this);
	}

	@Override
	public void windowStateChanged(WindowEvent state) {
		// TODO Auto-generated method stub
		if (state.getNewState() == 6) {
			mIsFullScreen = true;
		} else if (state.getNewState() == 0) {
			mIsFullScreen = false;
		}
		windowStateChange(mIsFullScreen);
	}
	
	public boolean isFullScreen() {
		return mIsFullScreen;
	}

}
