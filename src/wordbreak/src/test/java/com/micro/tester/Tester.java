/*
 * JDK's License:
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * The program's License:
 * Since the program obeys to Apache License. Please see more details about Apache License.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.micro.tester;

import java.io.File;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.micro.core.MyWordBreak;
import com.micro.core.WordBreak;
import com.micro.structure.DataNode;
import com.micro.structure.SentenceTree;
import com.micro.structure.WordTree;
import com.micro.utils.Log4jUtils;

/**
 * @see 由于该Java项目规模不大，所以所有需要测试的算法都可以集中在这个测试类做Junit测试
 * 
 */
public class Tester {
//	自定义测试数据
	String strArray[] = { "i", "like",  //"liker", "lik",
			"sam", "sung", "samsung",
			"mobile", "ice", "cream", "man", "go" 
			};
	
    @Before
    public void before() {
//    	该处用于初始化log4j日志
    	try {
            String filePath = "C:\\Users\\micro\\Desktop\\wordbreak-project\\src\\wordbreak\\log4j.properties";
            if (!(new File(filePath).exists())) {
                filePath = System.getProperty("user.dir") + File.separator + "log4j.properties";
            }
            Log4jUtils.initLog4j(filePath);
            BasicConfigurator.configure();
            PropertyConfigurator.configure(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // 用于测试获取Object的长度，其中String和数组比较特殊，其他类型一概转成String再获取长度
    static <T> int getLength(T value) {
		if (value != null) {
			if (value instanceof String) {
				return ((String) value).length();
			} else if (value.getClass().isArray()) {
				return Array.getLength(value);
			}
			return String.valueOf(value).length();
		}
		return 0;
	}
    
    // 用于测试获取Object的长度
    @Test
    public void testFunction() {
    	char ch[] = {'A', 'B'};
    	int r = getLength(ch);
    	System.err.println(r);
    }

    /**
     * 测试单词索引树，树结构的数据可以快速寻找单词，复杂度为log(n)
     */
    @Test
    public void testWordTree() {
    	WordTree wTree = new WordTree(strArray);
    	String s = JSON.toJSONString(wTree.getRoots());
    	System.err.println(s);
    }
    
    /**
     * 用于加载句子树的数据，用于测试数据是否正确
     */
    void loadData(SentenceTree<String> sTree, String mark) {
    	DataNode<String> root = sTree.createRoot("我" + mark);
    	DataNode<String> n21 = sTree.add(root, "喜欢" + mark, 1);
    	DataNode<String> n22 = sTree.add(root, "爱" + mark, 1);
    	
    	sTree.add(n21, "您" + mark, 2);
    	DataNode<String> n31 = sTree.add(n22, "人民" + mark, 2);
    	sTree.add(n31, "币" + mark, 3);
    }
    /**
     * 测试句子树
     */
    @Test
    public void testSentenceTree() {
    	SentenceTree<String> sTree = new SentenceTree<String>();
    	loadData(sTree, ".1");
    	loadData(sTree, ".2");
    	
    	List<String> list = sTree.list();
    	String s = JSON.toJSONString(list);
    	System.err.println(s);
    }
    
    // 获取字符数组，目的在于控制搜索过程中，不断new char[]
    char[] getCharacterArraySpace(Integer key, Map<Integer, char[]> arraySpaceMap) {
    	char ch[] = arraySpaceMap.get(key);
    	if (ch == null) {
    		ch = new char[key];
    		arraySpaceMap.put(key, ch);
    	}
    	return ch;
    }
    /**
     * @see 将发现的单词存储到句子树中，该方法包含递归，分别有纵向递归和横向递归
     * @param wTree  单词索引树
     * @param sTree  句子树
     * @param str    需要拆解为若干个句子的字符串
     * @param node   父节点，如果有合法的单词节点生成，将保存到该节点的子节点
     * @param wordCount  该节点的字符数量
     * @param startIdx   该节点从字符串哪个位置开始截取字符
     */
    void storeWord(WordTree wTree, SentenceTree<String> sTree, String str,
    		DataNode<String> node, int wordCount, int startIdx, Map<Integer, char[]> arraySpaceMap) {
    	if ((startIdx + wordCount) > str.length()) {
    		// 当横向搜索完毕，即将进入纵向递归，就是一个句子下，寻找下一个单词
    		Map<String, DataNode<String>> childrenMap = node.getChildren();
    		if (childrenMap != null && childrenMap.size() > 0) {
        		wordCount = 1;
        		int temp = 0;
    			for (Map.Entry<String, DataNode<String>> entry : childrenMap.entrySet()) {
    				// 以当前的子节点为父节点，也就是说横向递归完毕，这些子节点就成年了，该做父亲了
    				DataNode<String> node2 = entry.getValue();
    				temp = node2.wordCount + node2.startIndex;
    				if (temp == str.length()) {
    					// 因为该节点属于句子树中的叶子节点了，没必要再纵向递归了，节省性能
    					continue;
    				}
    				// 纵向递归：一个句子下，寻找下一个单词
    				storeWord(wTree, sTree, str, node2, wordCount, temp, arraySpaceMap);
    			}
    		}
    		return;
    	}
    	// 说明整个句子当作头一个单词也不是
    	if (startIdx == str.length()) {
    		return;
    	}
    	// 获取截取字符的空间
    	char []charArray = this.getCharacterArraySpace(wordCount, arraySpaceMap);
    	
    	// 截取字符到数组charArray
    	str.getChars(startIdx, startIdx + charArray.length, charArray, 0);
    	
    	// 将这些字符放到单词索引树搜索
    	boolean isWord = wTree.isWordInDictionary(charArray);
    	if (isWord) {
    		sTree.add(node, new String(charArray), startIdx);
    	}
    	++wordCount;
    	// 横向递归：搜索同一层次所有可能单词
    	storeWord(wTree, sTree, str, node, wordCount, startIdx, arraySpaceMap);
    }
    
    
    
    /**
     * 测试匹配单词在索引里面出现
     */
    @Test
    public void testMatcher() {
        String str = "ilikesamsungmobile";
        WordTree wTree = new WordTree(strArray);
        boolean isWord = wTree.isWordInDictionary(new char[] {
        		'l', 'i', 'k', 'e', 'r'
        		});
        System.err.println(isWord);
        
        str = "ilikeApple";
        strArray = new String[] {"i", "like", "li", "ke", "il", "ike", "ilike"};
        wTree = new WordTree(strArray);
        
        String s = JSON.toJSONString(wTree.getRoots());
    	System.err.println(s);

        // ilike  1. i like 2. i li ke 3. il ike  4 il i ke  5 ilike
        
        SentenceTree<String> sTree = new SentenceTree<String>();
        Map<Integer, char[]> arraySpaceMap = new HashMap<Integer, char[]>(str.length() + 1);
        int rootWordCount = 1;
        while (rootWordCount <= str.length()) {
        	// 不断把句子树的根节点找出来
        	char []charArray = this.getCharacterArraySpace(rootWordCount, arraySpaceMap);
        	str.getChars(0, charArray.length, charArray, 0);
        	isWord = wTree.isWordInDictionary(charArray);
        	if (isWord) {
        		DataNode<String> root = sTree.createRoot(new String(charArray));
        		storeWord(wTree, sTree, str, root, 1, rootWordCount, arraySpaceMap);
        	}
        	++rootWordCount;
        }
        
        s = JSON.toJSONString(sTree.getRoots());
    	System.err.println(s);
//    	
        List<String> list = sTree.list();
        s = JSON.toJSONString(list);
    	System.err.println(s);
    	
    	MyWordBreak mwb = new MyWordBreak(wTree);
    	list = mwb.findAllSentence(str);
        s = JSON.toJSONString(list);
    	System.err.println(s);
    	
        Set<String> dict = new HashSet<String>(strArray.length);
        for (String elem : strArray) {
        	dict.add(elem);
        }
    	WordBreak wb = new WordBreak();
    	list = wb.wordBreak(str, dict);
    	s = JSON.toJSONString(list);
    	System.err.println(s);
    }
}
