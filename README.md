# The SkipTrie
## Low Depth Concurrent Search Without Rebalancing

* Background
    * The SkipTrie is a probabilistically-balanced version of a Y-Fast Trie. The Y-Fast Trie is the combination of an X-Fast Trie that's linked to a series of balanced
      binary search trees. The SkipTrie distinguishes itself from the Y-Fast Trie by forgoing the balanced binary search trees and replacing it with a single skiplist.
      By eliminating the need for rebalancing, the SkipTrie is essentially a lock-free linearizable implementation of the Y-fast trie.

## The Data Structures

* The Skip List
    * In progress

* The X-Fast Trie
    * Our implementation of the x-fast trie is essentially a pseudo-bitwise trie with a doubly linked list built on top of the 
    leaf row in the trie. Each node has a concurrent hash map of its children.

* The Hash Table
    * In progress

## Bibliography
http://groups.csail.mit.edu/mag/oshman-shavit-podc13.pdf
http://web.stanford.edu/class/archive/cs/cs166/cs166.1146/lectures/15/Small15.pdf
https://en.wikipedia.org/wiki/Skip_list
https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentSkipListSet.html
https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentSkipListMap.html
