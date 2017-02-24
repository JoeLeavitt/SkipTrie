# The SkipTrie
## Low Depth Concurrent Search Without Rebalancing

* Background
    * The SkipTrie is a probabilistically-balanced version of a Y-Fast Trie. The Y-Fast Trie is the combination of an X-Fast Trie that's linked to a series of balanced
      binary search trees. The SkipTrie distinguishes itself from the Y-Fast Trie by forgoing the balanced binary search trees and replacing it with a single skiplist.
      By eliminating the need for rebalancing, the SkipTrie is essentially a lock-free linearizable implementation of the Y-fast trie.

## The Data Structures

* The Skip List
    *

* The X-Fast Trie
    *

* The Hash Table
    *
