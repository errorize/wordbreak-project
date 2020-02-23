package com.micro.utils;

import java.io.File;

public class MyFileUtils {

	private MyFileUtils() {
		
	}
	
	public static String getFilePath(String fileName) {
		String directpry = "C:\\Users\\micro\\Desktop\\wordbreak-project\\src\\wordbreak";
        String filePath = directpry + File.separator + fileName;
        if (!(new File(filePath).exists())) {
            filePath = System.getProperty("user.dir") + File.separator + fileName;
        }
        return filePath;
	}

}
