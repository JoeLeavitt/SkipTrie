/**
 *
 * @author haroldmarcial
 */

import concurrentskip.ConcurrentSkipListMap.Node;
import java.io.*;
import java.util.*;

public class Test {

    public static void main(String [] args){
        ConcurrentSkipListMap<Integer, Boolean> skiplist = new ConcurrentSkipListMap<>();

        skiplist.add(1);
        skiplist.add(3);
        skiplist.add(2);
        skiplist.add(4);
        skiplist.add(5);
        skiplist.add(6);
        skiplist.add(7);


        ConcurrentSkipListMap.Pair<Node<Integer, Boolean>,Node<Integer, Boolean>> pair;// = new ConcurrentSkipListMap.Node<>(null, null, null);

        pair = skiplist.listSearch(5, skiplist.findNode(3));

        for(int i = 0; i < skiplist.size(); i++){
            System.out.println(skiplist.findPredecessor(i+1).key);
        }

        System.out.println(pair.left.key + " " + pair.right.key);
        System.out.println(skiplist.containsKey(2));
    }

}
