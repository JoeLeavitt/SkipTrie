/**
 * Created by JL on 4/6/17.
 */

import java.util.*;

public class XfastTrieHandler {
    public static void main(String[] args){
        XfastTrie trie = new XfastTrie();

        trie.insert("100");
        trie.insert("101");
        trie.insert("001");

        /*

                   0
                 /   \
                0     1
               /     /
              00    10
             /    /   \
          001<->100<->101

         */

        System.out.println(trie.containsPrefix("00"));
        System.out.println(trie.containsBitstring("101"));
        System.out.println(trie.searchNode("101"));
        System.out.println(trie.searchNode("001"));
        System.out.println(trie.searchNode("00"));
    }

}
