package com.applex.main;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class VectorDrawableFileOpen extends JFileChooser{

	public VectorDrawableFileOpen() {
		super();
		setDialogTitle(DefaultValues.OPEN_FILE);
		setFileSelectionMode(FILES_ONLY);
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Android VectorDrawable File(*.xml)";
			}

			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				if (f.isDirectory()) {
				return true;
			}else	if (f.getName().toLowerCase().endsWith(".xml")) {
					return true;
				}
				return false;
			}
		});
	}

}
