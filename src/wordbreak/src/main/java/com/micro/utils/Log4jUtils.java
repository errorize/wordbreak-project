package com.micro.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.alibaba.fastjson.util.IOUtils;

public class Log4jUtils {
	
	/**
     * @see initialize log4j
     * @author Micro
     * @since 2019年10月18日 上午9:42:15
     * @param fileDir the file directory of log4j.properties
     * @return void
     */
    public static void initLog4j(String fileDir) {
        Properties prop = null;
        InputStream in =null;
        try {
            prop = new Properties();
            in = new BufferedInputStream(new FileInputStream(fileDir));
            prop.load(in);
            String key = "log4j.appender.File.File";
            String value = prop.getProperty(key);
            if (value != null) {
                File file = new File(value);
                if (!file.getParentFile().exists()) { file.getParentFile().mkdirs(); }
                if (!file.exists()) { file.createNewFile(); }
            }
            
            key = "log4j.appender.R.File";
            value = prop.getProperty(key);
            if (value != null) {
                File file = new File(value);
                if (!file.getParentFile().exists()) { file.getParentFile().mkdirs(); }
                if (!file.exists()) { file.createNewFile(); }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            IOUtils.close(in);
        }
    }
}
