package com.micro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;

public class WordBreakProblem {

	// set to hold dictionary values 
    private static Set<String> dictionary = new HashSet<>(); 
      
    public static void main(String []args) 
    { 
          
        // array of strings to be added in dictionary set. 
        String temp_dictionary[] = {"mobile","samsung","sam","sung",  
                            "man","mango","icecream","and",  
                            "go","i","like","ice","cream"}; 
        
        temp_dictionary = new String[] {"i", "like", "li", "ke", "il", "ike", "ilike"};
                              
        // loop to add all strings in dictionary set  
        for (String temp :temp_dictionary) 
        { 
            dictionary.add(temp); 
        } 
        String s = "ilike";
        List<String> ll = wordBreak(s, dictionary);
        s = JSON.toJSONString(ll);
        System.err.println(s);
    } 
      
    public static List<String> wordBreak(String s, Set<String> dict) {
        //create an array of ArrayList<String>
        List<String> dp[] = new ArrayList[s.length()+1];
        dp[0] = new ArrayList<String>();
     
        for(int i=0; i<s.length(); i++){
            if( dp[i] == null ) {
                continue; 
            }
     
            for(String word:dict){
                int len = word.length();
                int end = i+len;
                if(end > s.length()) {
                    continue;
                }
                String tmp = s.substring(i,end);
                if(tmp.equals(word)){
                    if(dp[end] == null){
                        dp[end] = new ArrayList<String>();
                    }
                    dp[end].add(word);
                }
            }
        }
     
        List<String> result = new LinkedList<String>();
        if(dp[s.length()] == null) 
            return result; 
     
        ArrayList<String> temp = new ArrayList<String>();
        dfs(dp, s.length(), result, temp);
     
        return result;
    }
     
    public static void dfs(List<String> dp[],int end,List<String> result, ArrayList<String> tmp){
        if(end <= 0){
            String path = tmp.get(tmp.size()-1);
            for(int i=tmp.size()-2; i>=0; i--){
                path += " " + tmp.get(i) ;
            }
     
            result.add(path);
            return;
        }
     
        for(String str : dp[end]){
            tmp.add(str);
            dfs(dp, end-str.length(), result, tmp);
            tmp.remove(tmp.size()-1);
        }
    }
}
