/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentskip;

import java.util.concurrent.atomic.*;
import java.util.Iterator;


public class BucketList<K,V> {
    static final int WORD_SIZE = 24;
    static final int LO_MASK = 0x00000001;
    static final int HI_MASK = 0x00800000;
    static final int MASK = 0x00FFFFFF;
    Node head;
    
    public BucketList() {
        this.head = new Node(0);
        this.head.next =
            new AtomicMarkableReference<Node>(new Node(Integer.MAX_VALUE), false);
    }
      
    private BucketList(Node e) {
        this.head  = e;
    }
    
    public class Node {
        int key;
        Object value;
        AtomicMarkableReference<Node> next;
        Node(int key, Object value) {      
            this.key   = key;
            this.value = value;
            this.next  = new AtomicMarkableReference<Node>(null, false);
        }
        Node(int key) { // sentinel constructor
            this.key  = key;
            this.next = new AtomicMarkableReference<Node>(null, false);
        }

        Node getNext() {
            boolean[] cMarked = {false}; 
            boolean[] sMarked = {false}; 
            Node entry = this.next.get(cMarked);
            while (cMarked[0]) {
                Node succ = entry.next.get(sMarked);
                this.next.compareAndSet(entry, succ, true, sMarked[0]);
                entry = this.next.get(cMarked);
            }
            return entry;
        }
    }
    
    class Window {
        public Node pred;
        public Node curr;
        Window(Node pred, Node curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }
      
    public Window find(Node head, int key) {
        Node pred = head;
        Node curr = head.getNext();
        while (curr.key < key) {
          pred = curr;
          curr = pred.getNext();
        }
        return new Window(pred, curr);
    }
    
    public static int hashCode(Object x) {
        return x.hashCode() & MASK;
    }
    
    public boolean add(K p, V x) {
        int key = makeRegularKey(p);
        boolean splice;
        while (true) {
            Window window = find(head, key);
            Node pred = window.pred;
            Node curr = window.curr;

            if (curr.key == key) {
                return false;
            } else {
              Node entry = new Node(key, x);
              entry.next.set(curr, false);
              splice = pred.next.compareAndSet(curr, entry, false, false);
              if (splice)
                return true;
              else
                continue;
          }
        }
    }
    
    public boolean remove(K p) {
        int key = makeRegularKey(p);
        boolean snip;
        while (true) {
          Window window = find(head, key);
          Node pred = window.pred;
          Node curr = window.curr;
          if (curr.key != key) {
            return false;
          } else {
            snip = pred.next.attemptMark(curr, true);
            if (snip)
                return true;
            else
                 continue;
          }
        }
    }
    
      
    public boolean contains(K p) {
        int key = makeRegularKey(p);
        Window window = find(head, key);
        Node pred = window.pred;
        Node curr = window.curr;
        return (curr.key == key);
    }
    
    public V containsN(K p){
        int key = makeRegularKey(p);
        Window window = find(head, key);
        Node pred = window.pred;
        Node curr = window.curr;
            if (curr.key == key){
                return (V)curr.value;
            }else{
                return null;
            }
                
    }
      
    public BucketList<K,V> getSentinel(int index) {
        int key = makeSentinelKey(index);
        boolean splice;
        while (true) {
          Window window = find(head, key);
          Node pred = window.pred;
          Node curr = window.curr;
          if (curr.key == key) {
                return new BucketList<K,V>(curr);
          } else {
            Node entry = new Node(key);
            entry.next.set(pred.next.getReference(), false);
            splice = pred.next.compareAndSet(curr, entry, false, false);
            if (splice)
                 return new BucketList<K,V>(entry);
            else
                continue;
          }
        }
    }
      
    public static int reverse(int key) {
        int loMask = LO_MASK;
        int hiMask = HI_MASK;
        int result = 0;
        for (int i = 0; i < WORD_SIZE; i++) {
            if ((key & loMask) != 0) {  
                result |= hiMask;
            }
            loMask <<= 1;
            hiMask >>>= 1;  
        }
        return result;
    }
    
    public void compareAndDelete(K p, SkipTrie.TrieNode n){
        //int key = makeRegularKey(p);
        
        if(n.key == p){
            this.remove(p);
        }
    }
      
    public int makeRegularKey(K p) {
        int code = p.hashCode() & MASK; 
        return reverse(code | HI_MASK);
    }
      
    private int makeSentinelKey(int key) {
        return reverse(key & MASK);
    }
      // iterate over Set elements
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
      
    
}
