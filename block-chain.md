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

## btc实现

### utxo

* unspent transition output
* 每一个全节点的内存中维护那些btc没有花，用于防double spending
* 理论上可以遍历整条链手动计算出来，但每次都遍历成本高，不如一次性建好一个表然后每次验证时快速查表

### transition/account based

* btc是transition based，它没有维护一个account的概念。每个account账户剩余的金额要从链上去计算
* 以太坊是account based

### bernoulli trial

* 无论之前尝试计算失败多少次，都不会影响下一次能够正确挖到矿的概率
* 就像投硬币10次正面，第11次是反面的概率不会高

### btc总数

$$
y = 210000 * 50 * (1 + \frac{1}{2} + \frac{1}{4} + \frac{1}{8} + \frac{1}{16} + \frac{1}{32} + ...) \\
y = = 210000 * 50 * 2 \\ 
y = 21000000 \\
$$

### 确认

* 一个交易在写入区块链后，最好等后面有6个区块后(1h)才能确认这笔交易会永久写入。因为可能出现fork attack
* 但一个人/组织想对抗整个网络的算力还是有难度，因此基于算力投票且多数算力是好节点的前提，从概率上保证了安全有效，但无法从数学证明上给出。

## btc网络

* 没咋搞懂
* network layer is P2P overlay network。但这个概念不太懂
* 去中心化，全靠诚实节点转发

## btc调整挖矿难度

### 出块时间

* 时间不是越短越好。

  1. 太短容易多分叉，目前一般主要是二分叉
  2. 分叉多了后整体算力被分散，在某一个路径上的分支相对会更少。这样更容易进行fork attack

* 如何调整？

  * 每2016个区块调整一次，大概是2周

  $$
  new \hspace{2mm} target = old \hspace{2mm} target * \frac{actual \hspace{2mm} time}{expected \hspace{2mm} time}
  $$

  * 调整的代码是写在btc源码里面的，到了数量自己调。当然代码开源你也可以不调，但你算出来的新区块就会因为`nbits`域的值不对而不被接收。

## btc挖矿

### 节点

* 全节点
  1. 一直在线
  2. 磁盘中维护完整的区块链信息
  3. 内存中维护完整的UTXO表方便查询
  4. 监听比特币网络中的TX，验证TX的合法性
  5. 决定那些TX会被打进下一个区块里
  6. 监听别的全节点发布新的区块，验证其合法性
  7. 决定沿着那一条链挖下去
  8. 当出现等长分叉时，决定沿着那一个分支往下挖
* 非全节点
  1. 不用一直在线
  2. 磁盘不用保存完整的区块链，只保留区块头即可
  3. 不用维护全局的UTXO，只要知道自己相关的交易即可
  4. 无法验证大多数TX的合法性，只能检验与自己相关交易的合法性
  5. 无法检测网上发布的区块是否是正确的
  6. 可以验证挖矿的难度
  7. 只能检测那一个是最长链，但无法知道那一个是最长合法链

### 不同算力

* cpu：浪费。cpu大部分硬件没用到，硬盘资源没用到，内存资源没用到
* gpu：也浪费，但比cpu好一点。比如说浮点运算性能就完全没用到
* ASIC: application-specificated-intergrated-chip：专为挖矿设计的芯片，榨干所有硬件。

### 矿池

* 矿池头
  1. 要做全节点所有的工作
  2. 将构建好的区块(区块奖励是自己)发给矿工们，让它们并行尝试计算hash
  3. 收集并记录每一个矿工的PoW。让每个矿工上传难度比实际难度低的区块。这些区块只能作为每个矿工的PoW，除此之外别无他用。另外矿工们都用的ASIC，除了算hash什么也干不了。
  4. 收集矿工上交的区块，并验证出块奖励收款人是自己。按照PoW给每个矿工分赃。
* 矿工
  1. 接受矿池头分发的任务，无脑计算hash
  2. 如果计算出PoW直接上报给矿池头，最为真正出块时分赃比例
  3. 如果计算出真正的区块，也得上交。除非自己是别的矿池的间谍。自己提交给区块链没用，因为区块奖励收款人不是自己，结果竹篮打水一场空。

### 风险

* 矿池的算力如果超过50，风险很大。可以等交易确认6个区块后依旧强行进行forking attack；也可以靠算力强行让某个账户无法交易(只要交易了就进行forking attack)
* 目前风险不可控，因为每个矿池中的每个矿工只负责算hash，根本不知道自己的矿主集结了多少算力，是否在进行forking attack等其他攻击。

## btc分叉

### hard fork

* 只有更新的软件，才会认可新链是合法的。比如将block size从 1M -> 4M

### soft fork

* 无论是否更新软件，都认为新链是合法的。比如将block size从 1M -> 0,5M

## btc匿名性

### 与现实世界联系

* 如果交易全发生在比特币内部，那么匿名性还是可以保障的
* 一旦btc要和实体世界发生关联，那就有可能会泄漏身份

### 网络层

* 使用洋葱网络TOR，即多次转发的方式实现匿名

### 应用层

* coin mixing。将手上的btc完全换一个地址。最极端情况都是两个人两个账户各有5个btc，则两人直接交换账户，让交易记录链中断无法进行查询

## btc思考

### hash指针

* 实际上只有hash没有指针，因为hash只在这台电脑此时的内存中生效。换台电脑或者重啟電腦內存的值都不一樣。所以其實並沒有指針，真正的做法是全節點維護了一個key-value的數據庫，其中key是hash值，value是一個block。通過這個數據庫實現了區塊鏈

### 區塊戀

* 兩個人分別持有私鑰的一半其實是不安全的。因為理論上暴力破解需要嘗試2^256次，但現在只需要嘗試2^128次，這兩個數是相差很大很大的。
* 正確的做法是使用btc提供的多重簽名
* 而且這種一旦分手，錢取不出來，就永遠存在UTXO裡，也是不太好的。

### 分布式共識

* 理論上的分佈式共識是無法達成的，但它需要限定的條件。
* 比如理論上我連不上一台服務器，我無法區分出是因為服務器關機了或者是網路延遲連不上。這在分佈式理論中能證明是無法區分出來的。但現實中，我只需要打一個電話給服務器人員，讓他去看一眼就知道了。實際的這個場景其實打破了分佈式理論無法達成共識的限制條件。因為現實情況往往和理論是不同的。
* btc的共識也是和理論上有不同，雖然有各種各樣無法達成共識的問題比如forking attack，但總體運行下來是沒有問題的

### 量子計算

* 量子計算距離真正的實用還有一段距離，目前來看btc還是安全的
* btc用公私鑰密碼學體系保障了安全性，另外帳戶地址其實不是公鑰本身，而是公鑰hash後的一個值。在hash + 公私鑰的雙重作用下，量子計算機要破解也有難度。
* 另外最好的做法其實是不要使用同一個帳戶多次取錢，而是一個帳戶只取一次錢，並把剩餘的錢轉移到另一個新的帳戶。這樣能增加安全性。

## ETH概述

### name

* eth全稱為"Ethereum"。這個名字來源於之前被認為充滿整個空間無處不在的"以太 - ether"。

### smart contract

* 只要是人制訂的合約就有不遵守的可能，而smart contract只要發布到鏈上，任何人無法篡改
* 實現了contact decentralized

## ETH帳戶

### 特點

* 和银行帐户很像
* 天生防double spending
* 不用维护utxo
* btc一次需要把帳戶內所有錢的轉走，即使有剩餘也要轉入另一個帳戶(或者本身帳戶也可以)。而ETH不需要

### relay attack

* 和double spending attack很像，指的是一個區塊已經發布到區塊鏈上了，但收款方不誠實，將這個區塊再次發布到網上，希望給自己轉兩次帳扣別人兩次錢。
* 解決方式是每一個帳戶除了有balance字段屬性外，還有nonce(其實含義是count)屬性，每進行一次交易就將nonce自增。然後nonce作為區塊的一部分整體用私鑰簽名

### 帳戶類型

* external owned account
  * 就是和btc類似的帳戶，只需要生成一對公私鑰就是一個帳戶
  * 關鍵字段：balance、nonce
* smart contact account
  * 希望簽訂合約的個體保持一致性，不能簽完合約後像btc一樣更換身份，因此增加此類型帳戶
  * 關鍵字段：code、storage

## ETH state trie

### 基礎

1. EHT中帳戶地址格式 - 160 bit - 20 byte - 40個十六進制數
2. Merkel tree的葉子節點才是數據，非葉子節點都是hash pointer

### 要求

1. 設計一個數據結構，能夠存儲所有帳戶的狀態
2. 對於給定的一個帳戶全集列表，輸出的數據結構必須要是一樣的。即需要排序
3. 新增和更改一個帳戶，盡量只影響一小部分內容，而不是整個數據結構都要重新構造，不然增改成本高
4. 查詢起來效率要足夠高

### 嘗試

* 不能直接使用hash table - 
  * 本身查詢和更改效率很高，但每次都要將整個hash table構造成一顆Merkel tree的成本太高了
* 不能直接使用merkel tree - 
  * 沒有提供高效的查詢方法
  * 每發佈一個區塊，要將整個更新後的Merkel tree發佈到區塊網絡上(讓別人知道最新區塊對應的status tree)成本很大，一般都是只發佈交易，status tree自己本地維護(但要保證不同的全節點維護的是同一個)
  * 即使排序了，能部分解決上面兩個問題。但如果新增一個帳戶，很可能大部分的Merkel tree都要重建重新計算hash，其實和直接使用hash table就是一樣的了

### merkel patricia trie

* merkel
  * 說明是hash pointer而不是普通的pointer，最後整顆樹會有一個Merkel proof
* trie
  * 本質是排序了的
  * 查找效率取決於樹深，比如説ETH帳戶帳戶統一是40位十六進制樹，那樹深就是40
  * 更新局部性還可以
* patricia
  * 為什麼能壓縮？ - 因為理論上ETH帳戶的全集數量是2^160，有交易的帳戶數量肯定遠遠小於這個數。因此注定字典樹很多非葉子節點其實只有一個child，因此進行壓縮可以節省存儲，加速查找

## ETH receipt, tx trie

### tx tree

* 和BTC区块的交易树其实一样，只不过数据结构是MPT

### receipt tree

* 其实可以不需要tx tree，但是有smart contract，有收据树會更方便一些

### bloom  filter

* 是什麼？
  * 如何去快速查一個元素是否在某個集合中，並且這個集合的存儲還非常大？
  * 可以將集合中的元素算一遍hash，存在一個集合中。當需要查詢某個元素時，只需要對這個元素進行hash計算，然後查一下新的集合中是否有這個hash即可。
* hash碰撞
  * 由於有hash碰撞的存在，因此可能會出現一個元素不在集合中，但它的hash在新集合中的情況。這種情況叫做`false positive`，但不會出現`false negative`
  * 正是因為有碰撞，因此集合刪除元素的時候，不能直接刪新集合。不然就會出現`false negative`
* 用法
  * ETH中區塊頭存有bloom filter，用於快速過濾查詢TX
  * 由於要用這個方式來查詢TX，因此`false positive`無所謂，但`false negative`必須要避免

## ETH ghost

### why

* ETH的出塊時間設置為10s，這很快很快
* ETH網絡由於各種原因(這塊原因涉及網絡，還沒去看)，一個區塊發布到網絡上到被其他節點收到可能就要10s
* 因此ETH中即使不是出於惡意，出現分叉也是非常常見的事情
* 所以需要設置一個機制，當出現分叉的時候，盡可能維護一條主鏈
* 另外在BTC中，未在最長合法鏈上的區塊無法獲得出塊獎勵；但ETH中，太容易出現分叉，就會導致很多正常挖的區塊最後不是最長合法鏈，會有很大一部分出塊無法獲得獎勵

### ghost

* 目的
  * 當出現分叉的時候，盡可能維護一條主鏈
  * 讓即使最後沒合入最長鏈的區塊，也得到一定的出塊獎勵
* 怎麼做
  * 一個區塊，如果帶上uncle區塊，uncle區塊可以獲得7/8的獎勵，這個區塊可以獲得1/32的獎勵
* 規則
  * uncle不是嚴格的uncle，可以理解成uncle鏈和當前鏈的公共節點在當前鏈上
  * 一個區塊最多只能帶兩個uncle區塊，因為uncle區塊的獎勵其實也不少，這樣可以防止ETH供給太大
  * 最多只能往前找6個uncle區塊，且獎勵從7/8遞減成2/8。
    1. 這樣全節點就只用維護最多7個區塊，不用再往前維護更多
    2. 這樣全節點也會盡快把uncle節點給帶上，因為越早帶上得到的獎勵越多，如果這次不帶，下一個節點也會帶獲得6/8獎勵。
  * uncle的交易不算數，只有再發佈到網上，等下一個區塊打包交易
    1. 因為uncle的交易可能和parent的交易是衝突的
    2. 當前區塊只要增加uncle主人的出塊獎勵，只改動state tree的一條記錄，也不用去check uncle節點所有交易的合法性，減少計算量
    3. 只有最長合法鏈的交易才算數，這樣可以降低複雜度
  * uncle只有第一個區塊才可能獲得獎勵，uncle後面接的什麼都沒有
    1. 防分叉攻擊，攻擊成功了就可以回滾某一個交易；攻擊失敗也有一些出塊獎勵拿
    2. 降低複雜度，不然某一個交易發現自己後面都跟了10個區塊了，以為交易成功。結果最後發現自己不是主鏈，交易不算數，就特別坑



