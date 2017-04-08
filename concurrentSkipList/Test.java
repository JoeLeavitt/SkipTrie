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
    
//          for(int i = 0; i< 20; i++){
//              skiplist.add(i+1);
//          }
        skiplist.add(1);
        skiplist.add(3);
        skiplist.add(2);
        skiplist.add(4);
        skiplist.add(5);
        skiplist.add(6);
        skiplist.add(15);
        
        
        ConcurrentSkipListMap.Pair<Node<Integer, Boolean>,Node<Integer, Boolean>> pair;// = new ConcurrentSkipListMap.Node<>(null, null, null);
        
        pair = skiplist.listSearch(9, skiplist.findNode(6));
        
//        for(int i = 0; i < skiplist.size(); i++){
//            System.out.println(skiplist.findPredecessor(i+1).key);
//        }

        //skiplist.fixPrev(skiplist.findPredecessor(5), skiplist.findNode(2));
        
        
        
        System.out.println(pair.left.key + " " + pair.right.key);
        System.out.println(pair.left.orig_height + " " + pair.right.orig_height);
        System.out.println(skiplist.add(7).orig_height);
        System.out.println(skiplist.add(7));
    }
    
}
