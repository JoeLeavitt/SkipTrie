/**
 * Created by JL on 4/5/17.
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class XfastTrieNode {
    int bits;
    ConcurrentHashMap<Integer, XfastTrieNode> children = new ConcurrentHashMap<Integer, XfastTrieNode>();

    boolean isLeaf;
    boolean isMarked;
    XfastTrieNode left;
    XfastTrieNode right;
    XfastTrieNode back;

    public XfastTrieNode() { }

    public XfastTrieNode(int bits){
        this.bits = bits;
    }
}
