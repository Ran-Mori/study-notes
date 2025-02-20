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
             /                  \                  /                  \
     (hash_1, hash_2)    (hash_3, hash_4)    (hash_5 hash_6)     (hash_7 hash_8)
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

## btc协议

### double spending attack

* 央行使用私钥签名发行电子货币理论上是可行的没问题。但电子货币防伪性可以解决，但无法解决电子货币可以像文件一样复制。
* 一种解决方式是央行还要维护一个每一张电子货币由谁拥有的数据库

### transition

* 输入
  * 转账发起方的公钥
  * 转账发起方btc的来源hash pointer
  * 接受方的公钥
* 输出 - transition，含有如下内容
  * 转账发起方私钥的签名
  * 接受方的公钥 (用于下一次转账证明btc来源)

### coinbase transition

* 铸币权
* 直接授予A一定数量的btc
* 此block使用A的公钥来签名

### block

* block header
  * version
  * hash pointer of previous block header
  * merkle root hash
  * Unix Timestamp
  * target
  * nonce
* block body
  * transition merkle tree 
* 特点
  * 区块链实际指的是chain of block header。block body其实不在链表结构中

### distributed consensus

* 需要达成的共识：区块链上所有交易的内容
* 投票机制是解决分布式共识的一种方式
* sybil attack: 创建很多个实体，使自己控制能投票的实体的数量过半，达到操作分布式共识的目的
* btc consensus
  * 信任最长的合法链
  * 记账的实体可以记账，使链表长度加一。协议还规定每个记账实体可以得到一定数量的btc奖励
  * 因为有奖励，所有人都想成为记账者；协议规定成为记账者的前提是找到正确的nonce计算hash值使target符合要求；而这个计算过程目前无高效算法，只能硬试；因此简洁的结果就是算力最强的实体获得记账权，并获得btc奖励
  * 最后确实是投票机制，但是用的算力进行投票。你可以创建无数的账户，但算力与账户数量无关，因此解决了sybil attack

### Proof of Work, PoW

* 计算式：SHA256(SHA256(block\_header)) <= target
* process
  1. 初始化区块头信息，包括 previous block hash、Merkle root 等
  2. 调整 Nonce（从 0 开始递增），然后计算：SHA256(SHA256(block\_header))
  3. SHA256(SHA256(block\_header)) ≤ target  ? 则找到合法区块，并将其广播到比特币网络，获得奖励（目前 6.25 BTC）: 则调整 Nonce，重复步骤 2-3。
  4. 如果 Nonce 耗尽（32 位无符号整数），矿工可以调整 timestamp 或者 Merkle root（通过加入额外的交易），继续尝试。
* target
  * 比特币网络大约每 2016 个区块（约两周）调整一次 target，以保持 10 分钟出一个区块的目标，target 越小，挖矿越难（因为能满足 hash(block header) <= target 的哈希值更少）。

