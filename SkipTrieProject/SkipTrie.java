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


public class SkipTrie {
    private final ConcurrentSkipListMap skipList;
    private final LockFreeHashSet prefixes;
    
    private final int TOP = 4;
    
    
    public SkipTrie(){
        skipList = new ConcurrentSkipListMap();
        prefixes = new LockFreeHashSet<Integer,TrieNode>((int)Math.pow(2,20));
        prefixes.add("", new TrieNode(""));
        TrieNode tn =(TrieNode)prefixes.lookup("");
        tn.pointers[0] = new ConcurrentSkipListMap.Index<>(null, null, null);
        tn.pointers[1] = new ConcurrentSkipListMap.Index<>(null, null, null);
    }
    
    
    public class TrieNode{
        String key;
        ConcurrentSkipListMap.Index<Integer, Boolean> pointers[];
        
        public TrieNode(String key){
            this.key = key;
            this.pointers = new ConcurrentSkipListMap.Index[2];
            this.pointers[0] = new ConcurrentSkipListMap.Index<>(null, null, null);
            this.pointers[1] = new ConcurrentSkipListMap.Index<>(null, null, null);
        }
        
    }
    
    public ConcurrentSkipListMap.Index<Integer, Boolean> xFastTriePred(int key){
        
        ConcurrentSkipListMap.Index<Integer, Boolean> curr = lowestAncestor(key);
        
        if(curr != null){
            while(getValidValue(curr) > key){
                if (curr.node.value == null){
                    curr = curr.prev;
                }
                else{
                    curr = curr.right;
                }
            }
        }
        
        return curr;
    }
    
    public ConcurrentSkipListMap.Index<Integer, Boolean> predecessor(int key){
        return skipList.skipListPred(key, xFastTriePred(key));
    }
    
    public ConcurrentSkipListMap.Index<Integer, Boolean> lowestAncestor(int key){
        
        String binaryString = String.format("%32s", Integer.toBinaryString(key)).replace(' ', '0');
        String common_prefix = "";
        ConcurrentSkipListMap.Index<Integer, Boolean> ancestor;
        
        // Find best ancestor from top node
        TrieNode tn = (TrieNode) prefixes.lookup(common_prefix);
        ancestor = tn.pointers[Character.getNumericValue(binaryString.charAt(31))];
        
        int start = 0;// The index of the first bit in the search window
        int size = 16; // The size of the search window
        while (size > 0){  
           String query = binaryString.substring(start, start + size - 1);
           int direction = Character.getNumericValue(binaryString.charAt(start + 1));
           
           TrieNode query_node = (TrieNode) prefixes.lookup(query);
           if(query_node != null){
               ConcurrentSkipListMap.Index<Integer, Boolean> candidate = query_node.pointers[direction];
               if(candidate != null){
                   
                   if(Math.abs(key - getValidValue(candidate)) <= Math.abs(key - getValidValue(ancestor))){
                       ancestor = candidate;
                   }
                   common_prefix = query;
                   start = start + size;
               }
           }
           size = size / 2;
        }
        //System.out.println("FOUND AN ANCESTOR! " + ancestor); /// Testing
        return ancestor;
    }

    public boolean insert(int key){

        ConcurrentSkipListMap.Index<Integer, Boolean> pred = this.xFastTriePred(key);
        
       
        if (getValidValue(pred) == key){
                return false;
        }
        
        ConcurrentSkipListMap.Index<Integer, Boolean> node = skipList.topLevelInsert(key, pred);
        if (node == null)
            return false;
        if (node.node.orig_height != TOP){
            
            return true;
        }
        
        String binaryString = String.format("%32s", Integer.toBinaryString(key)).replace(' ', '0');
 
        for(int i = 0; i < binaryString.length(); i++){
            
            String p = binaryString.substring(0, binaryString.length()- 1 - i);
            int direction = Character.getNumericValue(binaryString.charAt(binaryString.length()- 1 - i));
//            System.out.println("Run:" + i + "  " + p);
//            System.out.println("Direction:  " + direction);
            
            while (node.node.value != null){
               TrieNode tn = (TrieNode)  prefixes.lookup(p);
               if(tn == null){
//                   System.out.println("Adding prefixes to hash");
                    tn = new TrieNode(p);
                    tn.pointers[direction] = new ConcurrentSkipListMap.Index<>(node); 
                    if(prefixes.add(p, tn)){
//                        System.out.println("Adding prefix success");
                        break;
                    }
               }
               else if (tn.pointers[0] == null && tn.pointers[1] == null){
//                   System.out.println("Deleting node");
                    prefixes.compareAndDelete(p, tn);
               }
               else{
                   System.out.println("Trying CAS or Key BS");
                   ConcurrentSkipListMap.Index<Integer, Boolean> curr = tn.pointers[direction];
                   //tn.pointers[direction] = new ConcurrentSkipListMap.Index<>(node);
                   if((curr != null)){
                       if(curr.nodeA != null){
                            if(((direction == 0 && curr.nodeA.node.key >= key)||(direction == 1 && curr.nodeA.node.key<=key))){
                                System.out.println("Key BS");
                                break;
                            }
                       }
                    }
                       
                    
//                   System.out.println("curr  " + curr);
//                   System.out.println("curr.node "  + curr.nodeA);
                   //ConcurrentSkipListMap.Index<Integer, Boolean> next = node.right;
                   if(tn.pointers[direction].casNode(curr.nodeA, node)){
                       System.out.println("CAS SUCCESS!");
                       break;
                   }
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
        ConcurrentSkipListMap.Index<Integer,Boolean> pred = predecessor(key);
        ConcurrentSkipListMap.Pair<ConcurrentSkipListMap.Index<Integer,Boolean>, 
                ConcurrentSkipListMap.Index<Integer,Boolean>> pair = skipList.listSearch(key, pred);
        if(pair.right == null){
            System.out.println("I WANT FALSE");
            return false;
        }
        if(pair.right.node.orig_height != TOP){
            System.out.println("skip delete");
            return skipList.remove(key, Boolean.TRUE);
        }
        if(!skipList.topLevelDelete(pair.left, pair.right))
            return true;
        String binaryString = String.format("%32s", Integer.toBinaryString(key)).replace(' ', '0');
 
        for(int i = 0; i < binaryString.length(); i++){
            
            String p = binaryString.substring(0, binaryString.length()- 1 - i);
            int direction = Character.getNumericValue(binaryString.charAt(binaryString.length()- 1 - i));
            
            TrieNode tn = (TrieNode)  prefixes.lookup(p);
            
            if(tn == null){
                continue;
            }
            ConcurrentSkipListMap.Index<Integer,Boolean> curr = tn.pointers[direction];
            
            while(curr == pair.right){
                ConcurrentSkipListMap.Pair<ConcurrentSkipListMap.Index<Integer,Boolean>, 
                ConcurrentSkipListMap.Index<Integer,Boolean>> pair2 = skipList.listSearch(key, pair.left);
                if(direction == 0){
                    tn.pointers[direction].casNode(curr.nodeA, pair2.left);
                }
                else{
                    tn.pointers[direction].casNode(curr.nodeA, pair2.right);
                }
                curr = tn.pointers[direction];
            }
            if(!((p.length() < binaryString.length()) || p.equals(binaryString))){
                tn.pointers[direction].casNode(curr.nodeA, null);
            }
            if (tn.pointers[0] == null && tn.pointers[1] == null){
                    prefixes.compareAndDelete(p, tn);
            }
        }
        return true;
    }
    
    public Integer getValidValue(ConcurrentSkipListMap.Index<Integer,Boolean> index){
        if(index.node != null){
            System.out.println("[getValidValue] value found: " + index.node.key);
            return index.node.key;
        }
        else{
            return Integer.MIN_VALUE;
        }
    }
    


   
}