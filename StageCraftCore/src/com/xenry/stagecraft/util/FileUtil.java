package com.xenry.stagecraft.util;
import java.io.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class FileUtil {
	
	private FileUtil(){}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void copy(File src, File dest) throws IOException, NullPointerException {
		if(!src.exists()) return;
		if(src.isDirectory()){
			if(!dest.exists())
				dest.mkdir();
			for(String f : src.list())
				copy(new File(src, f), new File(dest, f));
		}else{
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int l;
			while((l = in.read(buffer)) > 0)
				out.write(buffer, 0, l);
			in.close();
			out.close();
			System.out.println("Copied file '" + src + "' to '" + dest + "'");
		}
	}
	
}
