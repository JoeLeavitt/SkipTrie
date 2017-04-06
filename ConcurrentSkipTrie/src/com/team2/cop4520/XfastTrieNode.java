/**
 * Created by JL on 4/5/17.
 */
package com.team2.cop4520;

import java.util.*;

public class XfastTrieNode {
    int bits;
    HashMap<Integer, XfastTrieNode> children = new HashMap<Integer, XfastTrieNode>();
    boolean isLeaf;

    public XfastTrieNode() { }

    public XfastTrieNode(int bits){
        this.bits = bits;

    }
}
