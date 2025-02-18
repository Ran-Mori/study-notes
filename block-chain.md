# block chain

## btc密码学原理

### collision resistance

* 哈希碰撞是客观存在的
* 但目前很难通过简单的方式来实现给定hash值的碰撞
* 没有数学理论能证明某个hash是collision resistance的，只能依靠经验和时间来说某一个函数是collision resistance的

### puzzle friendly

* 哈希值不可预测。即只看输入，无法预测它的hash输出可能是什么

### public key

* 可以使用RSA生成公钥私钥

  * 随机选取两个特别大的质数 p, q
  * public key is (e, n)；private key is (d, n).

* 公私钥都是基于p和q算出来的，因此它们是有关联的是一对。

* 用了分解大数很难的数学性质，确保了**one-way mathematical relationship**

* 公私钥其实真正的表达形式是分为了两部分(e, n), (d, n)。但传输过程中会使用**PEM (Privacy-Enhanced Mail)**和**Base64**进行编码成统一格式

  ```bash
  # 原始格式
  e = 65537
  n = 234792374923847982374923874923847923
  
  # 编码后格式
  MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn
  ```

## btc数据结构

### hash pointer

* 不仅存放了内容的地址
* 还存放了内容的hash值，用于检测内容是否被篡改

### blocked chain

* 结构

  ```bash
  genesis_block <-- block_a <-- block_b <-- block_c <-- recent_block
  ```

* 特点

  * 由哈希指针构成的一条链

  * 头节点由系统产生，被称为genesis block
  * 每一个节点的内容
    1. 自己节点的content
    2. 指向前一个节点的hash pointer
  * 一个hash_pointer如何计算？ `hash(前节点的content + 前节点存有的指向前前节点的hash pointer)
  * 系统存着指向recent_block的hash_pointer
  * tamper evident log - 只需要系统记住的hash pointer，就能验证整条链上所有的记录是否被篡改

### merkle tree

* 特点

  * 叶子节点包内容是交易数据；非叶子节点不包含数据，内容是各个child的hash pointer
  * 根节点内容只有一个hash指针，被称为root hash
  * 不能成环，因为每一个节点来自child的hash，环的话会导致所有hash无法计算出来

* 结构

  ```bash
                                    (root_hash)
                                         |
                                 (hash_13 hash_14)
                                /                  \
               (hash_9 hash_10)                     (hash_11 hash_12)
             /                  \                 /                   \
     (hash_1, hash_2)   (hash_3, hash_4)   (hash_5 hash_6)      (hash_7 hash_8)
      /           \         /       \          /        \           /       \
  (node_a)     (node_b)  (node_c) (node_e)   (node_f) (node_g)   (node_h) (node_i)
  ```

* merkle proof

  * 意图：证明某个叶子节点代表的交易是未被篡改合法的。
  * 材料
    * root_hash：已知且其是正确的
    * node_c: 已知，待证明其确定性
    * hash_4, hash_9, hash_14: 已知，但不知道其正确性
  * 证明过程
    * 计算node_c的hash值，依次使用hash_4, hash_9, hash_14一步步算出最后的root_hash。用计算出来的root_hash和已知的root_hash比较，如果一致则证明正确。
  * 为什么hash_4, hash_9, hash_14不用验其正确性？
    * 因为假设node_c被修改导致hash_3值不一样，想通过修改hash_4的值来达到hash(hash_3, hash_4) = hash_10是人为制造hash碰撞，目前是不可能的。