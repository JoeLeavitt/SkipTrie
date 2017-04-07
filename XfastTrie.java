/**
 * Created by JL on 4/5/17.
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class XfastTrie {
    private XfastTrieNode root;
    private ConcurrentDoublyLinkedList<XfastTrieNode> DLL;

    public XfastTrie(){
        root = new XfastTrieNode();
        DLL = new ConcurrentDoublyLinkedList<XfastTrieNode>();
    }

    public void insert(int base10){
        String binaryString = Integer.toBinaryString(base10);
        ConcurrentHashMap<Integer, XfastTrieNode> children = root.children;

        for(int i = 0; i < binaryString.length(); i++){
            int n = Character.getNumericValue(binaryString.charAt(i));

            XfastTrieNode node;

            if(children.containsKey(n)){
                node = children.get(n);
            }
            else{
                node = new XfastTrieNode(n);
                children.put(n, node);
            }

            children = node.children;

            //set leaf node
            if(i==binaryString.length() - 1){
                node.isLeaf = true;
                DLL.add(node);
            }
        }
    }

    public void delete(int base10){

    }

    // check if bitstring is in trie
    public boolean containsBitstring(String binaryString){
        XfastTrieNode node = searchNode(binaryString);

        if(node != null && node.isLeaf) return true;
        else return false;
    }

    // check if any bitstring in the trie starts with a given prefix
    public boolean containsPrefix(String prefix) {
       if(searchNode(prefix) == null) return false;
        else return true;
    }

    public XfastTrieNode searchNode(String binaryString){
        ConcurrentHashMap<Integer, XfastTrieNode> children = root.children;
        XfastTrieNode node = null;

        for(int i = 0; i < binaryString.length(); i++){
            int n = Character.getNumericValue(binaryString.charAt(i));

            if(children.containsKey(n)){
                node = children.get(n);
                children = node.children;
            }
            else{
                return null;
            }
        }
        return node;
    }

    public XfastTrieNode successor(){
        return null;
    }

    public XfastTrieNode predecessor(){
        return null;
    }
}
