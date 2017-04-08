/**
 * Created by JL on 4/5/17.
 */

import java.util.*;

public class SkipTrie {
    private XfastTrie top;
    private ConcurrentSkipListMap bottom;

    public SkipTrie(){
        top = new XfastTrie();
        bottom = new ConcurrentSkipListMap();
    }

    public void insert(int value){
        /*
            Insert into skiplist first

            If top level then
                Insert into x-fast

            **** Still need the coin flipping in the skip list
         */
    }

    public void findPred(int x){
        /*
            Find pred in x-fast first
            then find in the skip-list
         */
    }

    public void delete(int x){
        /*
            First delete from skiplist

            If it was a top level node then
                delete from x-fast trie as well
         */
    }
}
