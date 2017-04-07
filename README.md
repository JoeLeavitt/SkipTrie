# The SkipTrie
## Low Depth Concurrent Search Without Rebalancing

* Background
    * The SkipTrie is a probabilistically-balanced version of a Y-Fast Trie. The Y-Fast Trie is the combination of an X-Fast Trie that's linked to a series of balanced
      binary search trees. The SkipTrie distinguishes itself from the Y-Fast Trie by forgoing the balanced binary search trees and replacing it with a single skiplist.
      By eliminating the need for rebalancing, the SkipTrie is essentially a lock-free linearizable implementation of the Y-fast trie.

## The Data Structures

* The Skip List
    * The skip list used in this project is Doug Lea's implementation of his skip list. We modified his skip list structure by adding list search
      and the coin fipping, which is needed for the skip trie structure. We also modified the add method.

* The X-Fast Trie
    * Our implementation of the x-fast trie is essentially a pseudo-bitwise trie with a doubly linked list built on top of the
    leaf row in the trie. Each node has a concurrent hash map of its children.

* The Hash Table
    * The hashing thats done in the x-fast trie is done with Java's ConcurrentHashMap class.

* The Doubly Linked List
    * The doubly linked list is a direct port from Doug Lea's implementation of a concurrent doubly linked list.

## The final product: The SkipTrie

* By combining all of the components in the Data Structures section we build an extremely fast concurrent search structure, the skip trie.
    * TODO: Description of how the search structure works

## The Team
Joseph Leavitt, Joseph Landry, Jason Berk, Harold Marcial

## Bibliography
* http://groups.csail.mit.edu/mag/oshman-shavit-podc13.pdf
* http://web.stanford.edu/class/archive/cs/cs166/cs166.1146/lectures/15/Small15.pdf
* https://en.wikipedia.org/wiki/Skip_list
* http://opendatastructures.org/versions/edition-0.1c/ods-java/node66.html
