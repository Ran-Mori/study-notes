# 算法设计与分析

## 算法的概念

### 概念

* 算法是解决问题的一种方法或一个过程，是一个由若干运算或指令组成的有穷序列

### 特点

* 输入和输出
* 确定性
* 可行性
* 有穷性

 ### 插入排序

 * 认为左边 **1...k** 个是有序的
 * 用一个 **key** 来存储 **k+1** 的值
 * k, k-1,k-2依次右移，选择适当位置插入key

***

## 算法的正确性

### 循环不变量

* 类似于数学归纳法

* 循环不变量举例
  * 插入排序前面 **1...k** 已经是排好序的
  * 找最大值max已经是前面 **1...k** 里面最大的
* 循环不变量核心
  * 第0个
  * 第k个
  * 第k+1个
  * 有穷性循环终止时第n+1个 

## 算法的效率

* 算法时间与空间中往往更关注 **时间效率**
* 算法计算的各种时间
  * 最优时间
  * 平均时间 —— 衡量时间效率
  * **最坏时间** —— 往往关注最坏时间，是运行
* 循环语句中外层语句执行次数会比内层语句执行次数加一
* 算法的时间效率取决于 **n的规模**
* 一个算法的最优，最坏，平均时间复杂度 **n的规模** 可能不同
  * 插入排序最优时间复杂度为 n的平方
  * 插入排序最坏时间复杂度为 n的线性
  * 插入排序平均时间复杂大为 n的平方
* 效率的比较
  * n的规模低，当语句次数很大几百万时可以看出规模决定时间
  * 计算机效率，编译器优劣等条件都不足以弥补n的规模

***

## 问题的下界

* 问题的下界
  * 任何一种算法解决一个问题所必须的最小运行时间
* 最优算法
  * 设问题下界为F(n)，当算法A的最坏时间复杂度W(n)=F(n)时则可以认为算法A是这个问题最优算法
* 排序算法的问题下界是 **(nlog2n)**

***

## 概率分析时复度

### 概率分析

* 用概率的方法分析平均时间复杂度

### 概率分析举例

* 线性搜索算法平均复杂度为 (n+1)/2，复杂度规模为n
* 插入排序算法平均时间复杂度为n的平方

### 雇佣问题

* T(n)=n\*c(面试)+m\*(雇佣)。其中m指雇佣的次数，最好一次，最坏n次

***

## 其他方法分析时复度

* ### 分摊分析

  * 估计每个的时间代价，最后乘总数

* ### 累计分析

  * 总体分析需要付出的代价

* ### 记账方法

  * 每个步骤分配一定的时间，如果未用完，就记着下面的步骤可以接着用

  * 举例：空栈执行n次进栈和出栈操作，其中出栈可以一次性出k个
    * 分摊分析： 最坏可能每次都n，因此是n*n
    * 累计分析： 只有进栈才会有机会出栈，因此总体最坏是n
    * 记账方法：进栈代价2，出栈代价0，多次出栈代价0

***

## 选择排序

* 从n个元素选出一张最小的
* 最小的放左边，右边是SelcectSort(n-1)
* T(n)=n+T(n-1)

***

## 生成排列

* n个元素全排列问题
  * T(n)=n*T(n-1)

***

## 递归方程的求解

### 递归方程概念

* 类似于数列的通项公式

### 公式法

* T(n)=aT(b/n)+F(n)；F(n)=D(n)+C(n)
* 解释：规模n的问题分解成a个规模为b/n的问题，并且加上分解和合并操作产生的复杂度
* 当F(n)=O(n的(logba-s)次方)时，说明aT(b/n)占主因素，则T(n)=O(n的(logba)次方)
* 当F(n)=O(n的(logba)次方)时，说明两者相当，则T(n)=O((n的(logba)次方)*lgn)
* 当F(n)=O(n的(logba-s)次方)时，说明F(n)占主因素，则T(n)=O(F(n))

### 猜想法

* 已知 T(n)=f(T(n-1))
* 猜想假设T(n)=g(n)
* 则T(n)=f(g(n-1))
* 当存在一个n>n0，满足T(n)=f(g(n-1))<=c*g(n)时则猜想成立

***

## 分治法

### 分治法总体思想

* 将想求解的较大规模的问题分割成多个小规模的子问题，小问题继续分割成更小的问题，将求出的小规模问题的解合并成更大规模的问题的解，自底向上求出原问题的解

### 分治法名字由来

* 分而治之，各个击破

### 分治法适用条件

* 缩小到一定规模可以解决
* 具有最优子结构性质
* 子问题的解可以合并成原问题的解
* 子问题相互独立，即子问题无公共子问题

### 分治法分割问题的一般规则

* 尽量分割的小问题规模大小相同，解法类似

***

## 二分搜索

### 天平找伪币问题

### 二分搜索

* 一个有序序列，一个目标查找值
* 和mid值进行比较，逐渐二分

```java
public int binaryDivideSearch(int[] nums,int target){
    return search(nums,0,nums.length-1,target);
}

public int search(int[] nums, int left, int right, int target){
    if (left > right)
        return -1;
    int mid = (left + right) / 2;
    if (nums[mid] == target)
        return mid;
    if (nums[mid] > target)
        return search(nums,left,mid-1,target);
    else
        return search(nums,mid+1,right,target);
}
```

* 高度

***

##  排序

### 冒泡排序

* 相邻交换，依次找到最大，次大...最小。就像泡沫一样冒出来

  ```java
  public int[] bubbleSort(int[] nums){
      int length = nums.length;
  
      int temp;
      for (int i = length -1;i > -1; i--)
          for (int j = 1; j <= i;j++){
              if(nums[j-1] > nums[j]){
                  temp = nums[j-1];
                  nums[j-1] = nums[j];
                  nums[j] = temp;
              }
          }
      return nums;
  }
  ```

### 快速排序

* 选一个哨兵元素

* 通过比较移动或交换元素，首先定下哨兵的最终位置，然后问题就被分解成了两个小问题

  ```java
  public int[] quickSort(int[] nums){
      sort(nums,0,nums.length-1);
      return nums;
  }
  
  public void sort(int[] nums,int left,int right){
      if (left < right){
          int key = nums[left];
          int leftStore = left;
          int rightStore = right;
          while (left < right){
              while (left < right && nums[right] > key)
                  right--;
              if (left < right){
                  nums[left] = nums[right];
                  left++;
              }
              while (left < right && nums[left] < key)
                  left++;
              if (left < right){
                  nums[right] = nums[left];
                  right--;
              }
          }
          nums[left] = key;
          sort(nums,leftStore,left-1);
          sort(nums,left+1,rightStore);
      }
  }
  ```

### 归并排序

* 先两个两个排，在四个四个排……最后完成排序

* 算法的核心是如何将两个已经排好序的数组合并成一个数组

  ```java
  public int[] mergeSort(int[] nums){
      sort(nums,0,nums.length-1);
      return nums;
  }
  
  public void sort(int[] nums,int left,int right){
      if (left < right){
          int mid = (left + right) / 2;
          sort(nums,left,mid);
          sort(nums,mid+1,right);
          merge(nums,left,right);
      }
  }
  public void merge(int[] nums,int left,int right){
      if (left == right)
          return;
      int mid = (left + right) / 2;
      int i = left;
      int j = mid + 1;
  
      int[] temp = new int[right - left + 1];
      int index = 0;
      while (i <= mid && j <= right){
          if (nums[i] < nums[j])
              temp[index++] = nums[i++];
          else
              temp[index++] = nums[j++];
      }
      while (i <= mid)
          temp[index++] = nums[i++];
      while (j <= right)
          temp[index++] = nums[j++];
      index = 0;
      while (left <= right)
          nums[left++] = temp[index++];
  }
  ```

### 选择排序

* 认为前面1...k个元素已经排好

* 在k+1 ... n中找一个最小的放在k+1位置上

  ```java
  public int[] selectSort(int[] nums){
      int length = nums.length;
      if (length <= 1)
          return nums;
  
      int smallestIndex = -1;
      int temp = -1;
      for (int i = 0; i < length;i++){
          smallestIndex = i;
          for (int j = i; j < length;j++){
              if (nums[j] < nums[smallestIndex])
                  smallestIndex = j;
          }
          temp = nums[i];
          nums[i] = nums[smallestIndex];
          nums[smallestIndex] = temp;
      }
      return nums;
  }
  ```

***

## 覆盖残缺棋盘

### 规则

* 棋盘大小为n*n，其中n是2的整数次幂
* 棋盘中有随机一块是残缺的
* 一共有(n*n-1)/3块L形格板
* 使用L形格板填满所有非残缺块，要求不重复不覆盖刚好填满

### 解法

* 分割棋盘
* 假设三个完整残缺棋盘临接处残缺
* 三个残缺处刚好可以被一个L形格板覆盖
* 问题完成

***

## 大整数乘法

### 问题描述

* 两个n位数相乘
* 把n位数分为两个 n/2 数，变成4个数的乘法
* T(n)=4T(n/2)+O(n)

### 核心

* 时间复杂度表达式基本都变不了
* 只能变 “4”
* 运用配方合并可以变成3个乘法
* 有加法有减法选择减法，因为减法能使乘数位数变少
* T(n)=3T(n/2)+O(n)

***

## 矩阵乘法

### 问题概述

* 两个n阶矩阵相乘
* 每个矩阵拆成4个n/2矩阵
* 最后每个四分之一矩阵是由两个矩阵乘法的和，一定是和
* T(n)=8T(n/2)+O(n\*n)  因为是矩阵加法，因此是O(n\*n)

### 核心

* 降低子问题的个数来降低时间复杂度

***

## 动态规划引言

### 引言

* DFS计算斐波那契数列有很多很多的重复

### 备忘录解决方法

* 设置一个长度为n的数组
* 数组每个元素分别表示F(n)的值
* 当DFS计算时首先判断数组内有无值
  * 无值情况： 计算出值后填入
  * 有值情况： 直接取出值来用

### 备忘录方法缺点

* 由于使用递归
* 当F(2)已知时仍然求F(2)仍然要使用函数调用
* 虽然不向下递归，但依然要分配空间等函数开销

### 备忘录缺点解决方法

* 递归转递推 **自顶向下为递归，自底向上为递推**
* 使用递推的方法构造求F(n)就不用递归，无重复调用函数开销
* 但仍然要建立备忘录

### 动态规划特点

* 通常会使用数组来保留子问题的解，用备忘录的思想

***

## 动态规划算法思想

* 动态规划基本步骤
* 找出最优解的性质并刻画其结构特征
* 递归地定义最优值
* 以 **自底向上** 方法计算出最优值
* 根据计算出的最优值时得到的信息构造最优解

***

## 矩阵连乘问题

### 概述

* 多个矩阵连续相乘，求乘法次数的最小值
* 矩阵的乘法满足结合律
* 不同的乘顺序与括号位置会导致最后乘法的次数不相同

### 解决思路

* 使用穷举法可以解决，但会发现很多乘法次数需要重新计算
* 涉及多次重复计算可以考虑动态规划
* 设置A[1,n]表示n个矩阵连乘
* 所有的值空间就是一个类似倒乘法表的倒三角
* n个矩阵相乘可以切断分成n-1中情况
* 原问题的解等于子问题解的和在加上两个子问题相乘的开销
* 最优值一定是这n-1种情况中的一种，目的就是找出最优的这一种
* 涉及3个及其3个以上的矩阵乘法就继续分类讨论切分
* 最后会发现很多计算是重复的

***

## 最优二叉查找树

### 二叉查找树概述

* 左子树一定都比结点小
* 右子树一定都比结点大

### 解决思路

* 和矩阵连乘类似
* 分类成n种情况，最优解一定在这n种之一
* 子问题继续分
* 子问题合并成大问题的最优值
* 重复的只用计算一次

### 使用自底向上而非自顶向下

***

## 最大子段和问题

### 问题概述

* n个整数乱序随机排列，且排列次序已定
* 从n个选择连续不大于n个整数求和，求最大值
* 当所有整数为负数，规定最大字段和为0
* {-2, 11, -4, 13, -5} 的最大字段和为11-4+13=20

### 解决思路

* 动态规划

  ```java
  public int packageProblem(int[] values,int[] weights,int loading){
      int length = values.length;
      int[][] temps = new int[length+1][loading+1];
      for (int i = 1;i <= length;i++)
          for (int j = 1;j <= loading;j++){
              if (j >= weights[i-1])
                  temps[i][j] = Math.max(temps[i - 1][j - weights[i - 1]] + values[i - 1],temps[i - 1][j]);
              else
                  temps[i][j] = temps[i - 1][j];
          }
      return temps[length][loading];
  }
  ```

***

## 装配线调度问题

### 问题概述

* 有两条装配线，两条都能完成装配任务
* 一次装配任务划分为很多个装配环节，两条线的划分完全相同
* 汽车可以随便选择任意装配环节在任意装配线装配
* 两条线的进线时间不同，出线时间不同，每个环节的装配时间也不一定相同
* 相邻两个环节同一条线无开销，切换线有开销
* 求最短装配时间
* 实际上就是一个 **最短路径问题**

### 解决思路

* 这个问题还不同于一般的最短路径问题
* 汽车必须经过两条线某个装配环节中的一个
* 起点到终点分解成两个小问题，最优值在两者中一个
  * 线路1终点+线路1出线时间
  * 线路2终点+线路2出线时间
* 线路1终点时间分为两个小问题
  * 线路1次终点时间+线路1终点环节装配时间
  * 线路2次终点时间+线路切换时间+线路1终点环节装配时间
* 使用自底向上填表法动态规划思想求解
* 用一个for循环填表

***

## 最长公共子序列

### 问题概述

* 有两个序列
* 求这两个序列的最长公共子序列
* 最优解长度唯一，但最长公共子序列内容不一定唯一

### 问题应用

* 论文抄袭造假
* APP抄袭造假

### 解决思路

* Xm指X这个序列，序列中有m个元素；Yn指Y这个序列，序列中有n个元素；C(m,n)指Xm和Yn最大公共序列
* 当Xm != Yn时，C(m,n)=max{C(m,n-1), C(m-1,n)}   不相等指X第m个元素与Y第m个元素不相等
* 当Xm == Ym时，C(m,n)=C(m-1,n-1)+1  相等指X第m个元素与Y第m个元素相等
* 然后填一个二维数组，最后就能动态规划出结果

***

## 背包问题

### 问题概述

* 小偷洗劫一家商场，商场有n件商品
* 第i件商品的价值是Vi，重量是Wi
* 小偷的背包最大承重是W重量
* 求问如何使小偷的收益最大

### 解决思路

* 不要被W重量限制死

* V[j, w]指前面j件物品在w限重下的最高价值

* 当wj > w时，肯定不要第j件物品。V[j, w]=V[j-1, w]

* 当wj <= w时。V[j, w]=max{V[j-1, w-wj]+vj,  V[j-1,w]}

* 填写一个n*w的矩阵表

  ```java
  public int packageProblem(int[] values,int[] weights,int loading){
     int length = values.length;
     int[][] temps = new int[length+1][loading+1];
     for (int i = 1;i <= length;i++)
         for (int j = 1;j <= loading;j++){
             if (j >= weights[i-1])
                 temps[i][j] = Math.max(temps[i - 1][j - weights[i - 1]] + values[i - 1],temps[i - 1][j]);
             else
                 temps[i][j] = temps[i - 1][j];
         }
     return temps[length][loading];
  }
  ```

***

## 动态规划总结

### 涉及的两个问题

* 当求解原问题时，需要用到多少个子问题
* 在确定那些子问题的解包含在原问题中时，有多少种选择

***

## 贪心算法思想

### 算法思想

* 局部每一个小问题都是最优解，那么大问题也是最优解
* 不是所有的问题都能用贪心思想。互不相关的最短路径可以用，但相关联的最短路径问题就不能用

### 贪心算法与动态规划的区别

* 动态规划一般是有多个子问题，解决这多个子问题，找一个最优解
* 贪心算法一般是只有一个子问题，这个子问题就是最优解，因此贪心算法往往只用解决一个子问题

*****************

## 任务选择问题

### 问题概述

* 有n个任务，n个任务占用同一个资源
* 第i个任务在时间段[si, fi) 占用资源，占用期间不得释放资源
* 求选择任务的个数，使资源尽量被占用

### 解决思路

* 首先让任务按照结束时间排序
* C(0, n+1) 指在完成任务0结束后和任务n+1开始前求解的原问题
* C(0,n+1)=max{C(0,k-1)+1+C(k+1,n)   0<k<n+1}

***

## 贪心算法基本步骤与要素

### 基本步骤

* 确定问题额最优子结构性质
* 定义递归解
* 证明递归的任意一步，最优选择就是贪心选择
* 证明做了贪心选择后，只留下一个非空子问题
* 设计一个实现贪心策略的递归算法
* 把递归算法转换成迭代算法

### 基本要素

* 贪心选择性质
* 最优子结构性质

### 特点

* 先做出一个贪心选择
* 解决由选择导出的子问题
* 我们做出的选择依赖于前面的选择，而不是子问题的解
* 自顶向下，问题逐步减少

***

## 贪心算法解小数背包问题

### 概述

* 整体类似于0-1背包问题
* 但允许取一部分出来，比如取一个物品的一半

### 解决思路

* 按照价值率最高来做贪心选择
* 一直做下去直到背包被装满

### 贪心策略

* 按照重量最低作为贪心策略，它是一种贪心但不是问题最优解的贪心
* 按照价值率最高作为贪心，它是一种贪心且是贪心的最优解

>**贪心策略有很多，贪心策略不一定会导向最优解**

***

## 哈夫曼编码问题

### 概述

* 在所有的前缀编码中找出编码代价最小的编码方案
* 编码代价：求和(每个码的频率*树深度)

### 前缀编码

* 没有一个编码是另一个编码的码制

### 贪心策略

* 每次从森林中选择两个频度最低的树构成一棵树

***

## 最小生成树问题

### 概述

* 对一个无向连通图，找出一个边无向连通子集T，其连通了图所有的顶点且权值最小

### Kruskal算法贪心策略

* 每次从所有边中选择一个权值最小的
* 如果没有构成环就加入，如果构成就重新取次小边

***

## 最大流问题

### 问题概述

* 给定一个有向图G。每一条边的权值代表此路径能通过的最大流量
* 有向图只有一个入结点和一个出结点
* 流必须从入结点流入，从出结点流出，且任意时刻某路径上流量不能超过规定最大值
* 求稳定后有向图的最大流量

### 剩余网络

* 在图流了一部分后求剩下的路径能流过的流量
* 剩余流量=初始流量 - 流过的流量
* 最开始没有的边默认权值是0，剩余流量 = 0 - 一个负数 = 正数

### 增广路径

* 在剩余网络中选择一条从入结点到出结点的有向路径，作为一个流量路径
* 流量大小等于路径上面的最大流量的最小值

### 解决思路

* 在原有向图的基础上不断找增广路径，得到剩余网络
* 剩余网络继续找增广路径，得到剩余网络
* 当剩余网络中找不到增广路径时，就已经是最大流了

### 如何求增广路径

* 使用BFS或者DFS进行遍历

***

## 回溯算法

### 回溯法思想特点

* 系统性：在包含问题所有解的解空间树中，按照DFS的方法从根节点出发搜素
* 跳跃性：如果判断出某结点及其子树肯定不包含问题的解，则跳过此结点且不DFS向下搜索它的子树。
* 剪枝
* 特点：回溯思想是对穷举法的改进
* 解通常由向量表示

### 回溯法一般步骤

* 确定解空间
* 组织好解空间以便使搜索更容易
* 以为DFS搜索解空间，利用剪枝函数减少搜索

### 解空间树结点种类

* 拓展结点： 一个正在产生儿子的结点
* 活结点：一个自己已经生成但儿子还没全部生成的结点
* 死结点：一个所有儿子已经产生的结点

***

## 货箱装载问题

### 问题概述

* 有n个货箱，第 i 个货箱的重量为 wi ，船最大载重为W，求在船不翻的前提下装载尽可能多的货物

### 解决思路

* 因为是选择装和不装，因此问题的解空间肯定是一个子集树

* 约束函数： 装了第 i 个物品后总重量不能超过W

* 剪枝：如果重量超了，就可以不用继续往下搜索了，直接剪枝

* 限界函数 

* 此题找载重量的最大值因此存在一个下界

* 限界函数为此时的重量+剩下所有全部装入的质量

* 如果限界函数的值比现在最优向量解的值还小，就可以把这个枝全部剪掉

  > 0-1背包问题解决方法与此问题类似，唯一不同是限界函数

***

## 着色问题

### 概述

* 用四种不同的颜色对地图进行着色
* 要求相邻的国家颜色不能相同 

### 四色原理

* 对地图或球面进行着色，要求不相邻，四种颜色是必须的
* 这是第一个主要由计算机证明的理论

### 解决思路

* 将地图转化为一个邻接表
* 使用回溯法
* 解空间也是一个子集树
* 树的高度为地图块的树木
* 解集向量为每个地图块涂的颜色
* 子集树一定是一颗满二叉树

***

## 分支限界思想

### 特点

* 和回溯法一样，采用解空间搜索
* 使用排列树而不是子集树
* 使用BFS而不是DFS

### FIFO队列

* 分支限界算法不使用普通的队列而使用FIFO队列
* 当搜索 **最小花费** 解时就使用 **最小堆** 来表示活结点
* 当搜索 **最大收益** 解时就使用 **最大堆** 来表示活结点
* 始终使最大或最小的结点优先遍历搜索

### 注意事项

* 如何确定上界(最大值问题)
* 如何确定下界(最小值问题)
* 怎样为下一个分支操作选择一个结点
* 怎样将解空间扩展成一棵树
* 怎样利用约束函数和限界函数缩小搜索范围

***

## 货箱装载问题

### 概述

* 前面有概述

### 解决思路

* 解空间还是使用的子集树
* 使用BFS进行遍历，遍历过程中结合限界和剪枝
* 最开始压队一个 **-1** ，-1出队时又重新压入，代表遍历一层

### 最大程度剪枝

* 用上约束函数
* 用上限界函数
* 货箱重物按照重量进行排序不随机
* 同一高度入队层次遍历顺序不按入队顺序而按重量优先级

***

## 旅行商问题

### 问题概述

* 有n个点的网络图(无向或者有向)，找一条包含所有顶点一次且仅一次的最小代价回路
* 回路：从起点出发又最后回到起点

### 排列树

* 因为要经过所有的顶点且只经过一次
* 解空间可以看成是首尾都是 x1 的 x2到xn 的全排列，形成一颗排列树

### 解决思路

* 根据排列树进行搜索求解 

***

## P和NP

### P

* 多项式问题

### NP

* 非多项式问题

***

## 最优问题与判定问题

### 最优问题

* 在众多的问题可行解中寻找一个最优解

### 判定问题

* 给定限制条件，问在这个限制条件下是否存在一个解。
* 判定问题只有 true 和 false 两种答案

### 两者关系

* 最优问题可以向判定问题进行转化

### 约简

* 求一个新问题的解，可以把新问题的一个 **实例** 在多项式时间复杂度内转换为旧问题的一个实例
* 这样只要能够转换且知道旧问题的解，就能求出新问题的解

***