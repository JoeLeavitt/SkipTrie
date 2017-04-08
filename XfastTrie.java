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

    // Gets lowest ancestor and makes sure its a real value
    public XfastTrieNode xFastTriePred(String binaryString){
        XfastTrieNode node = lowestAncestor(binaryString);

        if(node == null) {
            System.out.println("No pred found");
            return node;
        }

        if(node.isLeaf){
            node.isMarked = true;
            System.out.println("The prefix is indeed an ancestor");
        }
        else{
            System.out.println("The prefix is not an ancestor");
        }

        return node;
    }

    // Searches for lowest ancestor from data in the skiplist
    public XfastTrieNode lowestAncestor(String binaryString) {
        String prefix = "";
        XfastTrieNode node = null;

        for(int i = 1; i < binaryString.length(); i++){
            prefix = binaryString.substring(0, binaryString.length() - i);
            node = searchNode(prefix);
            if (node != null) break;
        }

        int base10 = Integer.parseInt(binaryString, 2);

        System.out.println("Possible ancestor for " + base10 + " is " + prefix);

        return node;
    }

    public XfastTrieNode successor(){
        return null;
    }

    public XfastTrieNode predecessor(){
        return null;
    }
}
