package com.micro;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.util.IOUtils;
import com.micro.core.MyWordBreak;
import com.micro.structure.WordTree;
import com.micro.utils.Log4jUtils;
import com.micro.utils.MyFileUtils;

public class MainFrame {
    public static final Logger logger = Logger.getLogger(MainFrame.class);
    private static final String CMD_QUIT = "q!";
    private static final String CMD_NEW_DICTIONARY = "d2!";
    private static final String CMD_ADD_DICTIONARY = "d3!";
    private static boolean USE_LOG4J = true;
    
    static {
        try {
        	// 初始化Log4j
            String filePath = MyFileUtils.getFilePath("log4j.properties");
            Log4jUtils.initLog4j(filePath);
            BasicConfigurator.configure();
            PropertyConfigurator.configure(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            USE_LOG4J = false;
        }
    }
    
    public static void log(String content) {
    	if (USE_LOG4J) {
    		
    	}
    }
    
    public static void addToWordTree(WordTree wTree, String line) {
    	line = StringUtils.remove(line, ' ');
    	if (line.length() == 0) { return; }
    	String words[] = StringUtils.split(line, ',');
    	for (String word : words) {
    		if (word.length() == 0) { continue; }
    		wTree.add(word.toLowerCase());
    	}
    }
    
    public static WordTree createWordTree(String line) {
    	WordTree wTree = new WordTree();
    	addToWordTree(wTree, line);
    	return wTree;
    }
    
    public static void print(List<String> sentences) {
    	if (sentences == null || sentences.size() == 0) {
    		System.out.println("以目前字典的单词数量，不可拆解该字符串！");
    		return;
    	}
    	boolean startFlag = true;
    	for (String sentence : sentences) {
    		if (startFlag) {
    			System.out.println("output: " + sentence);
    			startFlag = false;
    		} else {
    			System.out.println("\t" + sentence);
    		}
    	}
    }
    
	public static void main(String[] args) throws Exception {
		System.out.println("正在初始化单词索引树……");
		// 获取系统默认的单词字典
        String filePath = MyFileUtils.getFilePath("system.properties");
        List<String> lines = FileUtils.readLines(new File(filePath), "UTF-8");
        WordTree wTree = new WordTree();
        for (String line : lines) {
        	addToWordTree(wTree, line);
        }
//        String s = JSON.toJSONString(wTree.getRoots());
//    	System.err.println(s);
        System.out.println("初始化单词索引树完毕！");
        
//        if (args == null || args.length == 0) { return; }
        
		System.out.println(
				"请按照提示进行操作：\r\n"+
				"    1.如果想退出，请输入\"q!\"；\r\n" +
				"    2.如果想构建新字典（单词索引树），并且程序只会搜索输入的单词，请输入\"d2!\"；\r\n" +
				"    3.如果想增加其他额外的单词到字典（单词索引树），并且程序只会搜索输入的单词，请输入\"d3!\"；\r\n"
				);
		String inputTip1 = "请输入需要拆解的字符串（例如，ilikesamsungmobile），也可以输入q!、d2!、d3!：";
		String inputTip2 = "请输入字典（以逗号隔开，例如，this, is, my, test）：";
		Scanner scanner = new Scanner(System.in);
		
		MyWordBreak wordBreak = new MyWordBreak(wTree);
		try {
			do {
				System.out.println(inputTip1);
				String str = scanner.next();
				if (CMD_QUIT.equals(str)) { break; }
				if (CMD_NEW_DICTIONARY.equals(str)) {
					System.out.println(inputTip2);
					str = scanner.next();
					WordTree wTree2 = createWordTree(str);
					System.out.println(inputTip1);
					str = scanner.next();
					wordBreak.setWordTree(wTree2);
					List<String> sentences = wordBreak.findAllSentence(str);
					print(sentences);
					wordBreak.setWordTree(wTree);
					continue;
				} else if (CMD_ADD_DICTIONARY.equals(str)) {
					System.out.println(inputTip2);
					str = scanner.next();
					addToWordTree(wTree, str);
					System.out.println(inputTip1);
					str = scanner.next();
					List<String> sentences = wordBreak.findAllSentence(str);
					print(sentences);
					continue;
				}
				List<String> sentences = wordBreak.findAllSentence(str);
				print(sentences);
			} while (true);
		} finally {
			IOUtils.close(scanner);
		}
	}
}
