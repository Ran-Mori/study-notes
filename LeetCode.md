# LeetCode

## 1 两数之和

> ### 题目
>
> * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值的那两个整数，并返回它们的数组下标。
>
>   你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
>
>   你可以按任意顺序返回答案
>
> ### 思路
>
> * 使用**HashMap**，其中**key**为**target - num[i]**，**value**为当前**index**值**i**
> * 将期待两个数加起来的和等于target转换成已找到一个数期待另一个数，维护一个期待集合
>
> ***

## 2 两数相加

> ### 题目
>
> * 给你两非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
>
>   请你将两个数相加，并以相同形式返回一个表示和的链表。
>
>   你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
>
> #### 思路
>
> * 维护一个余数**remainder**
> * 每次的和等于 **remainder + op1  +op2**
> * 不用判断每个是否为 **null**，只要其中一个不为 **null** 就行，是null就取0值
> * 最后如果 **remainder不为零** 则还要多创建一个节点
>
> ***

## 3 无重复最长子串

> ### 题目
>
> * 给定一个字符串，请你找出其中不含有重复字符的 **最长子串** 的长度。
>
> ### 思路
>
> * 从头到尾**遍历**，一旦发现有相同的字符就直接**隔断**，表明结果不可能同时包含这两个相同的字符
> * 维护一个**left**索引，表示从此开始向右寻找答案
> * 判断是否存在相同 使用HashMap，**key**用来判断子串是否存在相同字符，**value**用来存索引用来计算长度
>
> ### 代码
>
> ```java
> public int lengthOfLongestSubstring(String s) {
>     HashMap<Character,Integer> map = new HashMap<>();
>     int result = 0;
>     int left = 0;
>     for (int i =0;i<s.length();i++){
>         if (map.containsKey(s.charAt(i)))
>             left = Math.max(left,map.get(s.charAt(i)) + 1);
>         map.put(s.charAt(i),i);
>         result = Math.max(result,i - left + 1);
>     }
>     return result;
> }
> ```
> ****

## 4 寻找两个正序数组的中位数

> ### 题目
>
> * 给定两个大小分别为 `m` 和 `n` 的正序（从小到大）数组 `nums1` 和 `nums2`。请你找出并返回这两个正序数组的 **中位数** 。
>
> ### 思路
>
> * 不用真正完整排序，只用找到中间的 **一个**或 **两个** 数就可以了
> * 偶数需要 第**(m + n) / 2** 和 第**(m + n) / 2 + 1**两个值，奇数需要 **(m + n) / 2 + 1** 一个值
> * 因此 `for (int k = 0; k < (m + n) / 2 + 1;k++)` .其中遍历的数组长度是 `(m + n) / 2 + 1`
> * 维护一个 **temp** 来代表最后的 **(m + n) / 2**
> * 最后根据奇偶性来返回，判断奇偶性可以直接判断 **位操作的最后一位**
>
> ### 代码
>
> ```java
> public double findMedianSortedArrays(int[] nums1, int[] nums2) {
>     int temp = 0;
>     int current = 0;
>     int i = 0;
>     int j = 0;
>     int lengthA = nums1.length;
>     int lengthB = nums2.length;
>     int length = lengthA + lengthB;
>     for (int k = 0; k < (length + 2) / 2;k++){
>         temp = current;
>         if (i < lengthA && (j >= lengthB || nums1[i] < nums2[j]))
>             current = nums1[i++];
>         else if (j < lengthB && (i >= lengthA || nums1[i] >= nums2[j]))
>             current = nums2[j++];
>     }
>     if ((length & 1) == 0)
>         return (temp + current) / 2.0;
>     return current;
> }
> ```
>
> ***



