package com.micro.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.micro.structure.DataNode;
import com.micro.structure.SentenceTree;
import com.micro.structure.WordTree;

public class MyWordBreak {
	private WordTree wTree;
	
	public MyWordBreak(WordTree wTree) {
		this.wTree = wTree;
	}
	
	public MyWordBreak(String []strArray) {
		this.wTree = new WordTree(strArray);
	}

	// 获取字符数组，目的在于控制搜索过程中，不断new char[]
    private char[] getCharacterArraySpace(Integer key, Map<Integer, char[]> arraySpaceMap) {
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
    private void storeWord(WordTree wTree, SentenceTree<String> sTree, String str,
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
     * @see 从一个字符串中拆解所有可能的句子，并以List返回
     * @param str
     * @return
     */
    public List<String> findAllSentence(String str) {
    	if (str == null || str.isEmpty()) { return Collections.emptyList(); }
    	SentenceTree<String> sTree = new SentenceTree<String>(str.length());
        Map<Integer, char[]> arraySpaceMap = new HashMap<Integer, char[]>(str.length() + 1);
        int rootWordCount = 1;
        boolean isWord;
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
        
        return sTree.list();
    }
    
    /**
     * @see 用于增加自定义的单词
     * @param wordArray
     */
    public void addWords(String []wordArray) {
    	if (wordArray == null || wordArray.length == 0) { return; }
    	for (String word : wordArray) {
    		wTree.add(word);
    	}
    }
    
    public WordTree getWordTree() {
    	return this.wTree;
    }
    
    public void setWordTree(WordTree wTree) {
    	this.wTree = wTree;
    }
}
