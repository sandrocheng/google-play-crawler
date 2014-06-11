package com.akdeniz.googleplaycrawler.cli.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author sandro
 *
 */
public class Out {
	public static void print(String message){
		System.out.println(message);
	}
	
	public static void saveFile(String fileName,String content){
		File f = new File(fileName);
		if(f.exists()){
			f.delete();
		}
		OutputStream op = null;
		BufferedOutputStream bos = null;
		try {
			f.createNewFile();
			op = new FileOutputStream(f);
			bos = new BufferedOutputStream(op);
			
			bos.write(content.getBytes(), 0, content.getBytes().length);
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null){
				try {
					bos.close();
				} catch (IOException e) {
				}				
			}
			if(op != null){
				try {
					op.close();
				} catch (IOException e) {
				}
			}
		}		
	}
}
