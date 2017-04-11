/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentskip;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 *
 * @author haroldmarcial
 */


public class SkipTrie {
    private final ConcurrentSkipListMap skipList;
    private final LockFreeHashSet prefixes;
    private final TrieNode root;
    private final int TOP = 4;
    private boolean reachedTop;
    
    
    public SkipTrie(){
        skipList = new ConcurrentSkipListMap();
        prefixes = new LockFreeHashSet<Integer,TrieNode>((int)Math.pow(2,20));
        prefixes.add("", new TrieNode(""));
        root = (TrieNode)prefixes.lookup("");
    }
    
    
    public class TrieNode{
        String key;
        volatile ConcurrentSkipListMap.Index<Integer, Boolean> left;
        volatile ConcurrentSkipListMap.Index<Integer, Boolean> right;
        
        public TrieNode(String key){
            this.key = key;
        }
        
        
        /** Updater for right pointer */
        final AtomicReferenceFieldUpdater<TrieNode, ConcurrentSkipListMap.Index>
        leftUpdater = AtomicReferenceFieldUpdater.newUpdater
        (TrieNode.class, ConcurrentSkipListMap.Index.class, "left");

        /** compareAndSet left field */
        boolean cas0(ConcurrentSkipListMap.Index<Integer,Boolean> cmp, ConcurrentSkipListMap.Index<Integer,Boolean> val) {
                return leftUpdater.compareAndSet(this, cmp, val);
        }
        
        /** Updater for right pointer */
        final AtomicReferenceFieldUpdater<TrieNode, ConcurrentSkipListMap.Index>
        rightUpdater = AtomicReferenceFieldUpdater.newUpdater
        (TrieNode.class, ConcurrentSkipListMap.Index.class, "right");

        /** compareAndSet right field */
        boolean cas1(ConcurrentSkipListMap.Index<Integer,Boolean> cmp, ConcurrentSkipListMap.Index<Integer,Boolean> val) {
                return rightUpdater.compareAndSet(this, cmp, val);
        }
        
    }
    
    /**
     * @param key
     * @return the node in the DLL <= key OR NULL if there is no such node yet
     */
    public ConcurrentSkipListMap.Index<Integer, Boolean> xFastTriePred(int key){
        
        ConcurrentSkipListMap.Index<Integer, Boolean> curr = lowestAncestor(key);
        
        while (curr != null && curr.node.key > key) {
//            if (curr.marked)        // TODO CHECK WHERE MARKIGN OCCURS AND IMPLEMENT BOTH THESE FIELDS
//                curr = curr.back;
//            else
                curr = curr.prev;
        }
        return curr;
    }
    
    public ConcurrentSkipListMap.Index<Integer, Boolean> predecessor(int key){
        return skipList.skipListPred(key, xFastTriePred(key));
    }
    
    /**
     * @param key
     * @return dll node closest to key OR NULL if there is no such node yet 
     *  ARE WE SURE DESCENDENT POITNERS SHOULD BE NULL???
     */
    public ConcurrentSkipListMap.Index<Integer, Boolean> lowestAncestor(int key) {
        ConcurrentSkipListMap.Index<Integer, Boolean> ancestor;
        ConcurrentSkipListMap.Index<Integer, Boolean> candidate;
        String binaryString = String.format("%32s", Integer.toBinaryString(key)).replace(' ', '0');
        String common_prefix = "";
        
        // Find best ancestor from top node
        TrieNode root = (TrieNode) prefixes.lookup(common_prefix);        
        ancestor = (Character.getNumericValue(binaryString.charAt(31)) == 0) ? 
                root.left : root.right;
        
        int start = 0;// The index of the first bit in the search window
        int size = 16; // The size of the search window //// is it log (u/2) or (log u)/2 ??????
        while (size > 0){  
           String query = binaryString.substring(start, start + size);  // should be range [start, start + size -1]
           int direction = Character.getNumericValue(binaryString.charAt(start + 1));
           
           TrieNode query_node = (TrieNode) prefixes.lookup(query);
           if(query_node != null){
               candidate = (direction == 0) ? query_node.left : query_node.right;
               
               if(candidate != null) {
                   if (ancestor == null)
                       return null;
                   String candKey = Integer.toBinaryString(getValidValue(candidate)).replace(' ', '0');
                   
                   if (substring(query, candKey)){
                       
                        if(Math.abs(key - getValidValue(candidate)) <= Math.abs(key - getValidValue(ancestor)))
                            ancestor = candidate;
                                                
                        common_prefix = query;
                        start = start + size;
                    }
               }
           }
           size = size / 2;
        }
        return ancestor;
    }

    public boolean insert(int key){

        ConcurrentSkipListMap.Index<Integer, Boolean> pred = this.xFastTriePred(key);
        

        if (pred != null && getValidValue(pred) == key) 
            return false;
        
        // Note that pred may be null
        ConcurrentSkipListMap.Index<Integer, Boolean> index = skipList.topLevelInsert(key, pred);
        if (index == null)
            return false;
        if (index.node.orig_height != TOP){
            return true;
        }
        
        // Indicate that we have a top level node
        System.out.print("TOP ");
        reachedTop = true;
        
        
        return xFastInsert(key, index);
    }
    
    private boolean xFastInsert (Integer key, ConcurrentSkipListMap.Index<Integer, Boolean> index) {
        String binaryString = String.format("%32s", Integer.toBinaryString(key)).replace(' ', '0');
 
        for(int i = 0; i < binaryString.length(); i++){
            
            String p = binaryString.substring(0, binaryString.length()- 1 - i);
            int direction = Character.getNumericValue(binaryString.charAt(binaryString.length()- 1 - i));
            
            while (index.node.value != null){
                TrieNode tn = (TrieNode)  prefixes.lookup(p);
                if(tn == null){
                    // Create a new trienode and point it to the dll node
                     tn = new TrieNode(p);
                     if (direction == 0)
                         tn.left = index;
                     else
                         tn.right = index;

                     if(prefixes.add(p, tn)){
                         break;
                     }
                }
                else if (tn != this.root && tn.left == null && tn.right == null){  // p is being deleted
                    prefixes.compareAndDelete(p, tn);
                }
                else{
                    ConcurrentSkipListMap.Index<Integer, Boolean> curr;
                    curr = (direction == 0) ? tn.left : tn.right;
                    
                    if (curr != null && 
                            ((direction == 0 && curr.node.key >= key) ||
                            (direction == 1 && curr.node.key <= key)) )
                        break;
                    
                    if (direction == 0 && tn.cas0(curr, index) )
                        break;
                    if (direction == 1 && tn.cas1(curr, index) )
                        break;
                    
                }
            }
        }
        return true;
    }
        
    public boolean delete(int key){
        /*
            First delete from skiplist
            If it was a top level node then
                delete from x-fast trie as well
         */
        if (skipList.findNode(key).orig_height != TOP) {
            System.out.println("skip delete");
            return skipList.remove(key, Boolean.TRUE);
        }
        
        ConcurrentSkipListMap.Index<Integer,Boolean> pred = predecessor(key);
        ConcurrentSkipListMap.Pair<ConcurrentSkipListMap.Index<Integer,Boolean>, 
                ConcurrentSkipListMap.Index<Integer,Boolean>> pair = skipList.listSearch(key, pred);
        
        if(pair.right == null){     // pair.right should return our Index!!!!!!!!!!!!!!!!!
            return false;
        }
        
        
        if(!skipList.topLevelDelete(pair.left, pair.right))
            return false;

        return xFastDelete(key, pair);
    }
    
    private boolean xFastDelete(Integer key, ConcurrentSkipListMap.Pair<ConcurrentSkipListMap.Index<Integer,Boolean>, 
                ConcurrentSkipListMap.Index<Integer,Boolean>> pair) {
        
        ConcurrentSkipListMap.Index<Integer,Boolean> index = pair.right;
        
        String binaryString = String.format("%32s", Integer.toBinaryString(key)).replace(' ', '0');
 
        for(int i = 0; i < binaryString.length(); i++){
            
            String p = binaryString.substring(0, binaryString.length()- 1 - i);
            int direction = Character.getNumericValue(binaryString.charAt(binaryString.length()- 1 - i));
            
            TrieNode tn = (TrieNode)  prefixes.lookup(p);
            
            if(tn == null){
                continue;
            }
            ConcurrentSkipListMap.Index<Integer,Boolean> curr;
            curr = (direction == 0) ? tn.left : tn.right;
            
            while(curr == index){
                pair = skipList.listSearch(key, pair.left);
                if(direction == 0){
                    tn.cas0(curr, pair.left);
                }
                else{
                    ///makeDone(pair.left, pair.right); ??????????
                    tn.cas1(curr, pair.right);
                }
                curr = (direction == 0) ? tn.left : tn.right;
            }
            
            if(!substring(p, binaryString)){  //if !(p subpre curr.key) then
                if (direction == 0)
                    tn.cas0(curr, null);
                else
                    tn.cas1(curr, null);
            }
            if (tn.left == null && tn.right == null){
                    prefixes.compareAndDelete(p, tn);
            }
        }
        return true;
    }
    
    private boolean substring(String sub, String key) {
        
        if (sub.length() > key.length())
            return false;
        
        for (int i = 0; i < sub.length(); i ++) {
            if (sub.charAt(i) != key.charAt(i))
                return false;
        }
        
        return true;
    }
    
    public Integer getValidValue(ConcurrentSkipListMap.Index<Integer,Boolean> index){
        if (index.node == null)
            return Integer.MIN_VALUE;
        return index.node.key;
    }
    


   
}