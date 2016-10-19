package com.applex.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class SizeLabel extends JLabel{
	
	private int mWidthValue;
	
	private int mHeightValue;
	
	public SizeLabel(VectorDrawableOp vop) {
		super();
		Font f1 = new Font("Dialog", 0, 12);
		setFont(f1);
		setSize(200, 50);
		setForeground(Color.white);
		mWidthValue = Math.round(vop.width);
		mHeightValue = Math.round(vop.height);
		setText("default: " + mWidthValue + " x " + mHeightValue);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
	}

	public void showZoom(float zoom) {
		if (zoom == DefaultValues.UNCHANGED_ZOOM) return;
		setText("default: " + mWidthValue + " x " + mHeightValue + " zoom: " + zoom);
	}

	@Override
	protected void paintComponent(Graphics gs) {
		// TODO Auto-generated method stub
		Graphics2D g = (Graphics2D)gs;
   g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		String text = getText();
		g.setColor(Color.black);
		g.drawString(text, 1, 31);
		g.setColor(Color.white);
		g.drawString(text,0, 30);
	}
}
