/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentskip;

/**
 *
 * @author haroldmarcial
 */

import concurrentskip.ConcurrentSkipListMap.Node;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Test {
    
    public static void main(String [] args){
        
        ConcurrentSkipListMap<Integer, Boolean> skiplist = new ConcurrentSkipListMap<>();
        SkipTrie skipTrie = new SkipTrie();
        
        for(int i = 1; i < 10; i++){
            System.out.println(skipTrie.insert(i));
        }
        
        //ConcurrentSkipListMap.Index<Integer,Boolean> node = skipTrie.lowestAncestor(2);

    }
    
}
