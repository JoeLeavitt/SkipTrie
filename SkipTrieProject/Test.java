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
        
        for(int i = 0; i < 200; i++){
            System.out.println("insert: "+ i + " " + skipTrie.insert(i));
        }
        
        skiplist.dllTest();
        
        for(int i =0; i < 200; i++){
            System.out.println("delete: "+ i + " " +skipTrie.delete(i));
        }
        
/***************
 
//        for(int i =0; i < 200; i++){
//            System.out.println("delete: "+ i + " " +skipTrie.delete(i));
//        }
//        System.out.println("add" + skipTrie.insert(2));
        System.out.println("insert 200" + skipTrie.insert(200));
         System.out.println("delete 200" + skipTrie.delete(200));
        System.out.println("predecessor " + skipTrie.predecessor(5));
        
//        ConcurrentSkipListMap.Index<Integer,Boolean> node = skipTrie.lowestAncestor(2);
//        
//        System.out.println(node);
        
        System.out.println();
        System.out.println();
        System.out.println("SkipList Tests");
        System.out.println();
    
          for(int i = 0; i< 200; i++){
              skiplist.add(i+1);
             // System.out.println((i+1));
          }
        skiplist.add(1);
        //skiplist.topLevelInsert(Integer.SIZE, pred)
        skiplist.add(3);
        skiplist.add(2);
        skiplist.add(4);
        skiplist.add(5);
        skiplist.add(6);
        skiplist.add(15);
        
        
        ConcurrentSkipListMap.Pair<ConcurrentSkipListMap.Index<Integer, Boolean>,ConcurrentSkipListMap.Index<Integer, Boolean>> pair;
        
        pair = skiplist.listSearch(-2, skiplist.skipListPred(0, null));
        
//        for(int i = 0; i < skiplist.size(); i++){
//            System.out.println(skiplist.findPredecessor(i+1).key);
//        }

       // skiplist.fixPrev(skiplist.skipListPred(5,null), skiplist.skipListPred(6, null));
        
        
        
        System.out.println(pair.left.node.key + " " + pair.right.node.key);
        System.out.println(pair.left.node.orig_height + " " + pair.right.node.orig_height);
        System.out.println(skiplist.add(202));
        System.out.println(skiplist.add(7));
        System.out.println(skiplist.add(7));
        System.out.println(skiplist.containsKey(202));
        
        System.out.println();
        System.out.println();
        System.out.println("Hash Test");
        System.out.println();
        
        LockFreeHashSet hash = new LockFreeHashSet(300);
//        SkipTrie.TrieNode tn = new SkipTrie.TrieNode;
        
               
        hash.add(3, 3);
        hash.add(2, 2);
        hash.add(1, 1);
        hash.remove(3);
        
        System.out.println(hash.lookup(1));
        
        
//        String binaryString = String.format("%32s", Integer.toBinaryString(13)).replace(' ', '0');
//        
//        for(int i = 0; i < binaryString.length(); i++){
//            int direction = Character.getNumericValue(binaryString.charAt(binaryString.length()- 1 - i));
//            String p = binaryString.substring(0, binaryString.length() - 1 - i);
//            System.out.println("dropped" + " " + direction);
//            System.out.println(p);
//        }
    ***************/
    }
    
}
