# The SkipTrie_
## Low Depth Concurrent Search Without Rebalancing

* Background
    * The SkipTrie is a probabilistically-balanced version of a Y-fast trie. The Y-Fast trie is the combination of an X-Fast trie that's linked to a series of balanced
      binary search trees. The SkipTrie distinguishes itself from the Y-fast trie by forgoing the balanced binary search trees with a single skiplist.
      By eliminating the need for rebalancing, the SkipTrie is essentially a lock-free linearizable implementation of the Y-fast trie.
