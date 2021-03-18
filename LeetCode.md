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

## 5 最长回文字符串

> ### 题目
>
> * 给你一个字符串 `s`，找到 `s` 中最长的回文子串。
>
> ### 示例
>
> ```java
> 输入：s = "babad"
> 输出："bab"
> 解释："aba" 同样是符合题意的答案。
> ```
>
> ### 思路
>
> * **动态规划**
> * `d[i][j] = d[i + 1][j - 1] when s[i] =s[j]`
> * 初始化要从 **between** 为零，即 `i = j`初始化
> * **左范围**是for循环的变量， **右范围**是左范围和between算出来的
>
> ### 代码
>
> ```java
> public String longestPalindrome(String s) {
>     int length = s.length();
>     boolean[][] isPalindrome = new boolean[length][length];
>     String result = "";
>     int j = -1;
>     boolean temp;
>     for (int between = 0; between<length;between++){
>         for (int i = 0; i<length - between; i++){
>             j = i + between;
>             temp = s.charAt(i) == s.charAt(j);
>             if (between == 0)
>                 isPalindrome[i][j] = true;
>             else if (between == 1)
>                 isPalindrome[i][j] = temp;
>             else
>                 isPalindrome[i][j] = isPalindrome[i+1][j-1] && temp;
>             if (isPalindrome[i][j] && result.length()<(j-i+1))
>                 result = s.substring(i,j+1);
>         }
>     }
>     return result;
> }
> ```
>
> ***

## 6 Z字形变换

> ### 题目
>
> * 将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。
>
> ### 示例
>
> ```javascript
> 比如输入字符串为 "PAYPALISHIRING" 行数为 3 时，排列如下：
> P   A   H   N
> A P L S I I G
> Y   I   R
> ```
>
> ### 思路
>
> * 纯找规律问题。**一下一斜上为一组**
> * 每组包含元素为`row << 1 -2`，一共`length / k + 1`组，且最后一组可能某些元素index越界
> * 分完组后从 **从左到右，从上到下** 遍历
> * 第一行和第尾行每一组只有 **1** 个元素，其他行每一组有 **2** 个元素
>
> ### 代码
>
> ```java
> public String convert(String s, int numRows) {
>     if (numRows == 1)
>         return s;
>     int length = s.length();
>     int ontCount = (numRows + numRows - 2);
>     int times = length / ontCount+ 1;
>     StringBuilder builder = new StringBuilder();
>     for (int i = 0;i<numRows;i++){
>         for (int j = 0;j < times;j++){
>             if ((i + j*ontCount) >= length)
>                 continue;
>             builder.append(s.charAt(i + j*ontCount));
>             if (i != 0 && i != numRows-1 && ontCount - i + j*ontCount < length)
>                 builder.append(s.charAt(ontCount - i + j*ontCount));
>         }
>     }
>     return builder.toString();
> }
> ```
>
> ***

## 7 整数反转

> ### 题目
>
> * 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
>
>   如果反转后整数超过 32 位的有符号整数的范围 ，就返回 0。
>
>   假设环境不允许存储 64 位整数（有符号或无符号）
>
>
> ### 示例
>
> ```java
> 输入：x = 123
> 输出：321
> ```
>
> ### 思路
>
> * 由于要判断**是否越界**，所以难度比不判断越界大
> * 反转最好做法是 `result = result * 10 + n % 10`，不要试图直接把最高位通过各种操作移到低位，理论上行得通但越界无法判断
> * 上面做法还**不用区分正负数**，循环条件直接是`while(x != 0)`，只用在最开始记录下结果的正负性用于判断越界。因为 `n % 10`的结果是带正负性的
> * 判断越界 **先乘法后加法**。
>
> ### 代码
>
> ```java
> int reverse(int x) {
>     int rev = 0;
>     while (x != 0) {
>         int pop = x % 10;
>         x /= 10;
>         if (rev > INT_MAX/10 || (rev == INT_MAX / 10 && pop > 7)) return 0;
>         if (rev < INT_MIN/10 || (rev == INT_MIN / 10 && pop < -8)) return 0;
>         rev = rev * 10 + pop;
>     }
>     return rev;
> }
> ```
>
> ***

## 8 字符串转整数(atoi)

> ### 题目
>
> * 请你来实现一个 `myAtoi(string s)` 函数，使其能将字符串转换成一个 32 位有符号整数（类似 C/C++ 中的 `atoi` 函数）
> * 算法要求如下
>   * 读入字符串并丢弃无用的前导空格
>   * 检查下一个字符（假设还未到字符末尾）为正还是负号，读取该字符（如果有）。 确定最终结果是负数还是正数。 如果两者都不存在，则假定结果为正。
>   * 读入下一个字符，直到到达下一个非数字字符或到达输入的结尾。字符串的其余部分将被忽略。
>   * 将前面步骤读入的这些数字转换为整数（即，"123" -> 123， "0032" -> 32）。如果没有读入数字，则整数为 0 。必要时更改符号（从步骤 2 开始）。
>   * 如果整数数超过 32 位有符号整数范围 [−231,  231 − 1] ，需要截断这个整数，使其保持在这个范围内。具体来说，小于 −231 的整数应该被固定为 −231 ，大于 231 − 1 的整数应该被固定为 231 − 1 。
>   * 返回整数作为最终结果。
>
> ### 示例
>
> ```java
> 输入：s = "   -42"
> 输出：-42
> ```
>
> ### 思路
>
> * 严格按照要求一步步写就行了，不要乱跳步骤想当然地写
> * 判断越界的思路和上面那道题一模一样
>
> ### 代码
>
> ```java
> public int myAtoi(String s) {
>     int length = s.length();
>     int index = 0;
> 
>     //去除前导空格
>     while (index < length && s.charAt(index) == ' ')
>         index++;
>     if (index == length)
>         return 0;
> 
>     //判断正负号
>     boolean isPositive = true;
>     if (s.charAt(index) == '-'){
>         isPositive = false;
>         index++;
>     }else if (s.charAt(index) == '+')
>         index++;
> 
>     //解析数字
>     int result = 0;
>     int remainder = 0;
>     while (index < length && s.charAt(index) >= '0' && s.charAt(index) <= '9'){
>         remainder = isPositive ? Integer.valueOf(s.charAt(index)+"") : -Integer.valueOf(s.charAt(index)+"");
>         if (isPositive && result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && remainder >7))
>             return Integer.MAX_VALUE;
>         if (!isPositive && result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && remainder < -8))
>             return Integer.MIN_VALUE;
>         result = result*10 + remainder;
>         index++;
>     }
>     return result;
> }
> ```
>
> ***

## 9 回文数

> ### 题目
>
> * 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
>
> * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。例如，121 是回文，而 123 不是。
>
> ### 示例
>
> ```java
> 输入：x = -121
> 输出：false
>     
> 输入：x = 121
> 输出：true
> ```
>
> ### 思路
>
> * 简单思路直接变成字符串然后 **reverse**判断是否相同
> * 高级思路就是 **第七题的思路**，把后部份数字反转过来
>
> ### 高级思路解释
>
> ```java
> 输入 x = 1 2 3 2 1
>    
> 一: x = 1 2 3 2 1; y = 0;
> 二: x = 1 2 3 2;   y = 1;
> 三: x = 1 2 3;     y = 1 2;
> 然后根据限定条件就可以判断了
> ```
>
> ### 代码
>
> ```java
> public boolean isPalindrome(int x) {
>     if (x < 0 || (x % 10 == 0 && x != 0))
>         return false;
>     int revertedNumber = 0;
>     while (x > revertedNumber) {
>         revertedNumber = revertedNumber * 10 + x % 10;
>         x /= 10;
>     }
>     return x == revertedNumber || x == revertedNumber / 10;
> }
> ```
>
> ****

## 11 盛最多水的容器

> ### 题目
>
> * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
>
>
> ### 思路
>
> * **双指针** + **贪心思想**
> * 对任意两根木板，一旦围成容器，**则短木板与其他任何长度木板围成容器的体积都不会超过当前值**
>
> ### 代码
>
> ```java
> public int maxArea(int[] height) {
>     int i = 0, j = height.length - 1, result = 0;
>     while(i < j){
>         result = height[i] < height[j] ?
>             Math.max(result, (j - i) * height[i++]):
>         Math.max(result, (j - i) * height[j--]);
>     }
>     return result;
> }
> ```
>
> ****

## 12 整数转罗马数字

> ### 题目
>
> * 罗马数字包含以下七种字符： `I`， `V`， `X`， `L`，`C`，`D` 和 `M`
>
>   ```java
>   字符          数值
>   I             1
>   V             5
>   X             10
>   L             50
>   C             100
>   D             500
>   M             1000
>   ```
>
> * 例如， 罗马数字 2 写做 II ，即为两个并列的 1。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
>
> * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
>
>   * `I` 可以放在 `V` (5) 和 `X` (10) 的左边，来表示 4 和 9
>   * `X` 可以放在 `L` (50) 和 `C` (100) 的左边，来表示 40 和 90
>   * `C` 可以放在 `D` (500) 和 `M` (1000) 的左边，来表示 400 和 900
>
> ### 思路
>
> * 可以把例外也认为是一种映射规则，将此例外添加到规则中
> * 这样就只有规则没有例外
> * 由于要顺序遍历，因此不能用 **HashMap**
>
> ### 代码
>
> ```java
> public String intToRoman(int num) {
>     int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};    
>     String[] symbols = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
>     StringBuilder sb = new StringBuilder();
>     for (int i = 0; i < values.length && num >= 0; i++) {
>         while (values[i] <= num) {
>             num -= values[i];
>             sb.append(symbols[i]);
>         }
>     }
>     return sb.toString();
> }
> ```
>
> ****

## 13 罗马数字转整数

> ### 题目
>
> * 同上，只不过反过来
>
> ### 示例
>
> ```java
> 
> ```
>
> ### 思路
>
> * 先判断是否包含两个罗马数字的，在判断包含一个字母的
>
> ### 代码
>
> ```java
> public int romanToInt(String s) {
>     HashMap<String, Integer> map = new HashMap<>(){{
>         put("I", 1);
>         put("IV", 4);
>         put("V", 5);
>         put("IX", 9);
>         put("X", 10);
>         put("XL", 40);
>         put("L", 50);
>         put("XC", 90);
>         put("C", 100);
>         put("CD", 400);
>         put("D", 500);
>         put("CM", 900);
>         put("M", 1000);
>     }};
> 
>     int ans = 0;
>     for(int i = 0;i < s.length();) {
>         if(i + 1 < s.length() && map.containsKey(s.substring(i, i+2))) {
>             ans += map.get(s.substring(i, i+2));
>             i += 2;
>         } else {
>             ans += map.get(s.substring(i, i+1));
>             i ++;
>         }
>     }
>     return ans;
> }
> ```
>
> ****

## 14 最长公共前缀

> ### 题目
>
> * 编写一个函数来查找字符串数组中的最长公共前缀。
> * 如果不存在公共前缀，返回空字符串 `""`。
>
> ### 示例
>
> ```java
> 输入：strs = ["flower","flow","flight"]
> 输出："fl"
> ```
>
> ### 思路
>
> * 横向扫描
>   * 先默认最长公共前缀是第一个元素，然后每次用当前最长公共前缀和下一个元素求最长公共前缀
>   * 一旦中途为`""`就退出
> * 纵向扫描
>   * 依次比较每个元素的第i位
>   * 一旦不相等或某元素无第i位就截断返回结果
> * 分治
>   * 将数组一分为二
>   * 分别求两个数组的最长公共前缀，在求两个公共前缀的最长公共前缀
>
> ### 代码
>
> ```java
> public String longestCommonPrefix(String[] strs) {
>     if (strs == null || strs.length == 0) {
>         return "";
>     } else {
>         return longestCommonPrefix(strs, 0, strs.length - 1);
>     }
> }
> 
> public String longestCommonPrefix(String[] strs, int start, int end) {
>     if (start == end) {
>         return strs[start];
>     } else {
>         int mid = (end - start) / 2 + start;
>         String lcpLeft = longestCommonPrefix(strs, start, mid);
>         String lcpRight = longestCommonPrefix(strs, mid + 1, end);
>         return commonPrefix(lcpLeft, lcpRight);
>     }
> }
> 
> public String commonPrefix(String lcpLeft, String lcpRight) {
>     int minLength = Math.min(lcpLeft.length(), lcpRight.length());       
>     for (int i = 0; i < minLength; i++) {
>         if (lcpLeft.charAt(i) != lcpRight.charAt(i)) {
>             return lcpLeft.substring(0, i);
>         }
>     }
>     return lcpLeft.substring(0, minLength);
> }
> ```
>
> ****

## 15 三数之和

> ### 题目
>
> * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
> * **注意：**答案中不可以包含重复的三元组。
>
> ### 示例
>
> ```java
> 输入：nums = [-1,0,1,2,-1,-4]
> 输出：[[-1,-1,2],[-1,0,1]]
> ```
>
> ### 思路
>
> * **回溯法**
>   * 不重复三元组(**即组合非排列**)：只允许下一个元素 **大于等于** 下一个元素
>     * **等于**是因为例题中有两个`-1`
>     * 有两个`-1`也不会出现两个`[-1,-1,0],[-1,-1,0]`的情况，因为第二个会被剪枝
>   * 剪枝：和大于0就break；相同元素就continue
>   * 元素只能用一次：维护一个Boolean数组
> * **贪心思想** + **双指针**
>   * 首先进行排序
>   * 认定第一个元素，然后在 **n - 1 **个元素中找两个值等于target
>   * 由于排好了序，和大就只能右指针左移动，和小就只能左指针右移
>   * 可以 **剪枝** 排除两个相同的情况
>
> ### 代码
>
> ```java
> public List<List<Integer>> threeSum(int[] nums) {
>     List<List<Integer>> result = new ArrayList<>();
>     int length = nums.length;
>     if (length <= 2)
>         return result;
>     Arrays.sort(nums);
> 
>     int first =0;
>     int second = 1;
>     int third = length - 1;
>     int target = 0;
>     for (;first<length;first++){
>         if (first > 0 && nums[first] == nums[first-1])
>             continue;
>         target = -nums[first];
>         second = first + 1;
>         for (;second < length;second++){
>             if (second > first+1 && nums[second] == nums[second -1])
>                 continue;
>             while (second < third && nums[second] + nums[third] > target)
>                 third--;
>             if (second == third){
>                 third = length -1;
>                 break;
>             }
>             if (nums[second] + nums[third] == target){
>                 List list = new ArrayList();
>                 list.add(nums[first]);
>                 list.add(nums[second]);
>                 list.add(nums[third]);
>                 result.add(list);
>             }
>             third = length - 1;
>         }
>     }
>     return result;
> }
> ```
>
> ****

## 16 最接近的三数之和

> ### 题目
>
> * 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
>
>
> ### 示例
>
> ```java
> 输入：nums = [-1,2,1,-4], target = 1
> 输出：2
> 解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。
> ```
>
> ### 思路
>
> * 和三数之和的贪心思想解法一模一样
> * 只不过把相等改成了绝对值
>
> ****

## 17 电话号码的字母组合

> ### 题目
>
> * 给定一个仅包含数字 `2-9` 的字符串，返回所有它能表示的字母组合。答案可以按 **任意顺序** 返回。
>
> ### 示例
>
> ```java
> 输入：digits = "23"
> 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
> ```
>
> ### 思路
>
> * **回溯法**，因为所有的答案可以构成一棵解集树
> * **直接法**，维护一个中间List来存`n - 1`个数字组成的结果，最后在每个append上新的字母添加入集合。此方法频繁clear和add列表，时间度应该较差
>
> ### 代码
>
> ```java
> public List<String> letterCombinations(String digits) {
>     List<String> combinations = new ArrayList<String>();
>     if (digits.length() == 0) {
>         return combinations;
>     }
>     Map<Character, String> phoneMap = new HashMap<Character, String>() {{
>         put('2', "abc");
>         put('3', "def");
>         put('4', "ghi");
>         put('5', "jkl");
>         put('6', "mno");
>         put('7', "pqrs");
>         put('8', "tuv");
>         put('9', "wxyz");
>     }};
>     backtrack(combinations, phoneMap, digits, 0, new StringBuffer());
>     return combinations;
> }
> 
> public void backtrack(List<String> combinations, Map<Character, String> phoneMap, String digits, int index, StringBuffer combination) {
>     if (index == digits.length()) {
>         combinations.add(combination.toString());
>     } else {
>         char digit = digits.charAt(index);
>         String letters = phoneMap.get(digit);
>         int lettersCount = letters.length();
>         for (int i = 0; i < lettersCount; i++) {
>             combination.append(letters.charAt(i));
>             backtrack(combinations, phoneMap, digits, index + 1, combination);
>             combination.deleteCharAt(index);
>         }
>     }
> }
> ```
>
> ****

## 18 四数之和

> ### 题目
>
> * 给定一个包含 n 个整数的数组 nums 和一个目标值 target，判断 nums 中是否存在四个元素 a，b，c 和 d ，使得 a + b + c + d 的值与 target 相等？找出所有满足条件且不重复的四元组。
> * **注意：**答案中不可以包含重复的四元组。
>
> ### 示例
>
> ```java
> 输入：nums = [1,0,-1,0,-2,2], target = 0
> 输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
> ```
>
> ### 思路
>
> * **回溯法**
>   * 组合非排列：要求下一个元素必须 **大于等于** 上一个元素
>     * **等于**是因为数组中有同一个元素出现两次及以上
>     * 不用担心加上等于会重复，因为`[-1, 0, 0, 1]`中的两个0是有顺序的，另一种会被 **continue**掉
>   * 元素只能用一次：维护Boolean数组
>   * 剪枝：大于0就break，相同的元素如两个0就continue一个
>
> ****

## 19 删除链表倒数第n个结点

> ### 题目
>
> * 给你一个链表，删除链表的倒数第 `n` 个结点，并且返回链表的头结点
> * **进阶：**你能尝试使用一趟扫描实现吗？
>
> ### 示例
>
> ```java
> 输入：head = [1,2,3,4,5], n = 2
> 输出：[1,2,3,5]
> ```
>
> ### 思路
>
> * **容器存放法**
>   * 用一个容器存放结点，因为容器可以访问index就很容易了
>   * 最好使用Stack并把栈弹完，不然无法GC可能会导致其他问题
> * **快慢指针**
>   * 让两个指针的距离相差为 `n`，当第二个指针为null是第一个指针就是要删除元素
>
> ### 代码
>
> ```java
> public ListNode removeNthFromEnd(ListNode head, int n) {
>     ListNode dummy = new ListNode(0, head);
>     ListNode first = head;
>     ListNode second = dummy;
>     for (int i = 0; i < n; ++i) {
>         first = first.next;
>     }
>     while (first != null) {
>         first = first.next;
>         second = second.next;
>     }
>     second.next = second.next.next;
>     ListNode ans = dummy.next;
>     return ans;
> }
> ```
>
> ****

## 20 有效的括号

> 太简单了，略
>
> ***

## 21 合并两个有序链表

> ### 题目
>
> * 将两个升序链表合并为一个新的 **升序** 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。 
>
> ### 示例
>
> ```java
> 输入：l1 = [1,2,4], l2 = [1,3,4]
> 输出：[1,1,2,3,4,4]
> ```
>
> ### 思路
>
> * 当一个链表为空时，另一个链表直接 **接上**就可以了，不用额外判断
> * 创建一个 **dump** 结点，用于做头结点
> * 操作完成后最好将 **尾指针** 设置成 **null**，不然可能出现链表比预期长的情况
>
> ### 代码
>
> ```java
> public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
>     ListNode dumb = new ListNode(0);
>     ListNode current = dumb;
>     while (l1 != null && l2 != null){
>         if (l1.val > l2.val){
>             current.next = l2;
>             l2 = l2.next;
>             current = current.next;
>         }else {
>             current.next = l1;
>             l1 = l1.next;
>             current = current.next;
>         }
>     }
>     current.next = l1==null ? l2 : l1;
>     return dumb.next;
> }
> ```
>
> ****

## 22 括号生成

> ### 题目
>
> * 数字 `n` 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 **有效的** 括号组合。
>
> ### 示例
>
> ```java
> 输入：n = 3
> 输出：["((()))","(()())","(())()","()(())","()()()"]
> ```
>
> ### 思路
>
> * 解集树的 **分叉** 是此步到底放左括号还是放右括号
> * 而到底放左括号还是放右括号取决于当前剩余左右括号 **分别数量的关系**
>
> ### 代码
>
> ```java
> public List<String> generateParenthesis(int n) {
>     List<String> res = new ArrayList<>();
>     StringBuilder builder = new StringBuilder();
>     getParenthesis(res,builder,n,n);
>     return res;
> }
> 
> private void getParenthesis(List<String> res,StringBuilder builder,int left, int right) {
>     if(left == 0 && right == 0 ){
>         res.add(builder.toString());
>         return;
>     }
>     if (left != 0){
>         builder.append("(");
>         getParenthesis(res,builder,left-1,right);
>         builder.deleteCharAt(builder.length()-1);
>     }
>     if (right > left){
>         builder.append(")");
>         getParenthesis(res,builder,left,right-1);
>         builder.deleteCharAt(builder.length()-1);
>     }
> }
> ```
>
> ****

## 23 合并k个升序链表

> ### 题目
>
> * 给你一个链表数组，每个链表都已经按升序排列。
> * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
>
> ### 示例
>
> ```java
> 输入：lists = [[1,4,5],[1,3,4],[2,6]]
> 输出：[1,1,2,3,4,4,5,6]
> ```
>
> ### 思路
>
> * **分治法**
> * 由于会两个链表合并，因此不断 **mid** 分治成一个或者两个链表的合并
>
> ### 代码
>
> ```java
> public ListNode mergeKLists(ListNode[] lists) {
>     int k = lists.length;
>     if (k == 0)
>         return null;
>     else 
>         return merge(lists,0, lists.length - 1);
> }
> 
> private ListNode merge(ListNode[] lists,int start,int end){
>     if (start == end)
>         return lists[start];
>     else if (end - start == 1)
>         return mergeTwoList(lists[start],lists[end]);
>     else {
>         int mid = (start + end) / 2;
>         return mergeTwoList(merge(lists,start,mid),merge(lists,mid + 1,end));
>     }
> }
> private ListNode mergeTwoList(ListNode nodeA, ListNode nodeB){
>     ListNode result = new ListNode(0);
>     ListNode dump = result;
>     while (nodeA != null && nodeB != null){
>         if (nodeA.val > nodeB.val){
>             dump.next = nodeB;
>             nodeB = nodeB.next;
>         }else{
>             dump.next = nodeA;
>             nodeA = nodeA.next;
>         }
>         dump = dump.next;
>     }
>     if (nodeA != null)
>         dump.next = nodeA;
>     if (nodeB != null)
>         dump.next = nodeB;
>     return result.next;
> }
> ```
>
> ****

## 24 两两交换链表中的节点

> ### 题目
>
> * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
> * **你不能只是单纯的改变节点内部的值**，而是需要实际的进行节点交换。
>
> ### 示例
>
> ```java
> 输入：head = [1,2,3,4]
> 输出：[2,1,4,3]
> ```
>
> ### 思路
>
> * **递归**
> * **强行遍历迭代**
>
> ### 代码
>
> ```java
> public ListNode swapPairs(ListNode head) {
>     if (head == null || head.next == null)
>         return head;
>     ListNode newHead = head.next;
>     head.next = swapPairs(newHead.next);
>     newHead.next = head;
>     return newHead;
> }
> 
> public ListNode swapPairs(ListNode head) {
>     ListNode dummyHead = new ListNode(0);
>     dummyHead.next = head;
>     ListNode temp = dummyHead;
>     while (temp.next != null && temp.next.next != null) {
>         ListNode node1 = temp.next;
>         ListNode node2 = temp.next.next;
>         temp.next = node2;
>         node1.next = node2.next;
>         node2.next = node1;
>         temp = node1;
>     }
>     return dummyHead.next;
> }
> ```
>
> ****

## 25 K个一组反转链表

> ### 题目
>
> * 给你一个链表，每 *k* 个节点一组进行翻转，请你返回翻转后的链表。
> * *k* 是一个正整数，它的值小于或等于链表的长度。
> * 如果节点总数不是 *k* 的整数倍，那么请将最后剩余的节点保持原有顺序。
>
> ### 示例
>
> ```java
> 输入：head = [1,2,3,4,5], k = 3
> 输出：[3,2,1,4,5]
> ```
>
> ### 思路
>
> * **栈**
>   * 详细过程直接看代码
>   * 注意每次操作完最好将 **尾指针** 设置成 **null**
> * **每次向前看k个**
>   * 维护预期长度为k的链表的 **head 和 tail指针**
>   * 有k个就翻转，无就直接返回
>
> ### 代码
>
> ```java
> //方法一
> public ListNode reverseKGroup(ListNode head, int k) {
>     Stack<ListNode> stack = new Stack<>();
>     Queue<ListNode> queue = new LinkedList<>();
> 
>     int count;
>     while (head != null){
>         count = 0;
>         while (head != null && count != k){
>             stack.push(head);
>             head = head.next;
>             count++;
>         }
>         if (count != k)
>             break;
>         while (!stack.isEmpty())
>             queue.add(stack.pop());
>     }
> 
>     ListNode result = new ListNode(0,null);
>     ListNode dump = result;
>     while (!queue.isEmpty()){
>         dump.next = queue.poll();
>         dump = dump.next;
>     }
>     dump.next = null;
>     if (!stack.isEmpty())
>         dump.next = stack.get(0);
>     return result.next;
> }
> 
> //方法二
> public ListNode reverseKGroup(ListNode head, int k) {
>     ListNode hair = new ListNode(0);
>     hair.next = head;
>     ListNode pre = hair;
> 
>     while (head != null) {
>         ListNode tail = pre;
>         // 查看剩余部分长度是否大于等于 k
>         for (int i = 0; i < k; ++i) {
>             tail = tail.next;
>             if (tail == null) {
>                 return hair.next;
>             }
>         }
>         ListNode nex = tail.next;
>         ListNode[] reverse = myReverse(head, tail);
>         head = reverse[0];
>         tail = reverse[1];
>         // 把子链表重新接回原链表
>         pre.next = head;
>         tail.next = nex;
>         pre = tail;
>         head = tail.next;
>     }
> 
>     return hair.next;
> }
> 
> public ListNode[] myReverse(ListNode head, ListNode tail) {
>     ListNode prev = tail.next;
>     ListNode p = head;
>     while (prev != tail) {
>         ListNode nex = p.next;
>         p.next = prev;
>         prev = p;
>         p = nex;
>     }
>     return new ListNode[]{tail, head};
> }
> ```
>
> ****

## 26 删除有序数组中的重复项

> ### 题目
>
> * 给你一个有序数组 `nums` ，请你**原地**删除重复出现的元素，使每个元素 **只出现一次** ，返回删除后数组的新长度。
> * 不要使用额外的数组空间，你必须在 **原地**修改输入数组并在使用 O(1) 额外空间的条件下完成。
>
> ### 示例
>
> ```java
> 输入：nums = [1,1,2]
> 输出：2, nums = [1,2]
> ```
>
> ### 思路
>
> * 类似于选择排序，维护一个 **最左边**的非重复数组
>
> ### 代码
>
> ```java
> public int removeDuplicates(int[] nums) {
>     int length = nums.length;
>     int left = 0;
>     for (int i = 1;i<length;i++){
>         if (nums[i] != nums[left]){
>             left++;
>             nums[left] = nums[i];
>         }
>     }
>     return left+1;
> }
> ```
>
> ****

## 27 移除元素

> ### 题目
>
> * 给你一个数组 `nums` 和一个值 `val`，你需**原地**移除所有数值等于 `val` 的元素，并返回移除后数组的新长度。
> * 不要使用额外的数组空间，你必须仅使用 `O(1)` 额外空间并 **原地**修改输入数组。
> * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
>
> ### 示例
>
> ```java
> 输入：nums = [3,2,2,3], val = 3
> 输出：2, nums = [2,2]
> ```
>
> ### 思路
>
> * 和上题一样，维护一个 **最左边** 的正确结果
>
> ### 代码
>
> ```java
> public int removeElement(int[] nums, int val) {
>     int length = nums.length;
>     int left = -1;
>     for (int i = 0;i<length;i++){
>         if (nums[i] != val){
>             left++;
>             nums[left] = nums[i];
>         }
>     }
>     return ++left;
> }
> ```
>
> ****

## 28 实现strStr()

> ### 题目
>
> * 实现 strStr() 函数。
> * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
>
>
> ### 示例
>
> ```java
> 输入: haystack = "hello", needle = "ll"
> 输出: 2
> ```
>
> ### 思路
>
> * **BF**
>   * 第一位相等且`s.length() - i >= t.length()`才继续判断
>   * 否则回溯判断下一位
> * **KMP**
>
> ****

## 29 两数相除

> ### 题目
>
> * 给定两个整数，被除数 `dividend` 和除数 `divisor`。将两数相除，要求不使用乘法、除法和 mod 运算符。
> * 返回被除数 `dividend` 除以除数 `divisor` 得到的商。
> * 整数除法的结果应当截去（`truncate`）其小数部分
> * 本题中，如果除法结果溢出，则返回`1 << 31 - 1`。
>
> ### 示例
>
> ```java
> 输入: dividend = 7, divisor = -3
> 输出: -2
> ```
>
> ### 思路
>
> * 除法溢出就一种情况 `Integer.MIN_VALUE / -1`
> * 不能一个个++判断，每次将结果自增1，这样时间复杂度太高
> * 每次进行除数移位，这样一次多判断一点，但移位就涉及溢出判断
> * 把除数和被除数换成负数，这样不会溢出
> * 移位操作换成`while(dividend <= divisor)`就可以不用判断溢出
>
> ### 代码
>
> ```java
> public int divide(int dividend, int divisor) {
>     if (dividend == Integer.MIN_VALUE && divisor == -1)
>         return Integer.MAX_VALUE;
>     boolean isNeg = (dividend > 0 ) ^ (divisor > 0);
>     dividend = dividend > 0 ? -dividend : dividend;
>     divisor = divisor > 0 ? -divisor :divisor;
> 
>     int result = 0;
>     int temp = 0;
>     int i = 0;
>     while(dividend <= divisor) {
>         temp = divisor;
>         i = 1;
>         while(dividend - temp <= temp) {
>             temp = temp << 1;
>             i <<= 1;
>         }
>         dividend -= temp;
>         result += i;
>     }
> 
>     return isNeg ? -result:result;
> }
> ```
>
> ****

## 31 下一个排列

> ### 题目
>
> * 实现获取 **下一个排列** 的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
> * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）
> * 必须**原地**修改，只允许使用额外常数空间。
>
> ### 字典序
>
> * 字符串比大小的方式
> * 从第一位开始依次比较
>
> ### 示例
>
> ```java
> 输入：nums = [1,2,3]
> 输出：[1,3,2]
> ```
>
> ### 思路
>
> * 从尾向前遍历，如果所有元素是依次增加，就代表是最大字典序，返回最小排列值
> * 一旦`numb[i - 1] < num[i]`，就在`i...lenth - 1`中找大于`num[i - 1]`但最小的元素将两者进行交换，然后对`i...length - 1`进行升序排序
>
> ### 代码
>
> ```java
> public void nextPermutation(int[] nums) {
>     int length = nums.length;
>     if (length == 1)
>         return;
> 
>     boolean hasChanged = false;
>     int i = length - 2;
>     int j = length - 1;
>     for (; i > -1 ;i--){
>         if (nums[i] >= nums[j])
>             j--;
>         else
>             break;
>     }
>     int temp;
>     if (i == -1 && j == 0){
>         i = 0;
>         j = length - 1;
>         while (i <= j){
>             temp = nums[j];
>             nums[j] = nums[i];
>             nums[i] = temp;
>             i++;
>             j--;
>         }
>     }
>     else {
>         Arrays.sort(nums,j,length);
>         for (;j<length;j++){
>             if (nums[j] > nums[i]){
>                 temp = nums[i];
>                 nums[i] = nums[j];
>                 nums[j] = temp;
>                 return;
>             }
>         }
>     }
> }
> ```
>
> ****

## 34 在排序数组中查找元素的第一个和最后一个位置

> ### 题目
>
> * 给定一个按照升序排列的整数数组 `nums`，和一个目标值 `target`。找出给定目标值在数组中的开始位置和结束位置。
> * 如果数组中不存在目标值 `target`，返回 `[-1, -1]`。
> * 进阶：你可以设计并实现时间复杂度为 `O(log n)` 的算法解决此问题吗？
>
> ### 示例
>
> ```java
> 输入：nums = [5,7,7,8,8,10], target = 8
> 输出：[3,4]
> ```
>
> ### 思路
>
> * **二分分治法**
>
> ### 代码
>
> ```java
> public int[] searchRange(int[] nums, int target) {
>     int length = nums.length;
>     int i = 0;
>     int j = length - 1;
>     int[] result = new int[2];
>     int mid = 0;
>     while (i <= j){
>         mid = i + (j - i) / 2;
>         if (nums[mid] == target)
>             break;
>         else if (nums[mid] > target)
>             j = --mid;
>         else
>             i = ++mid;
>     }
>     if (i > j){
>         result[0] = -1;
>         result[1] = -1;
>     }
>     else {
>         i = mid - 1;
>         j = mid + 1;
>         while (i > -1){
>             if (nums[i] != target)
>                 break;
>             else
>                 i--;
>         }
>         while (j < length){
>             if (nums[j] != target)
>                 break;
>             else
>                 j++;
>         }
>         result[0] = ++i;
>         result[1] = --j;
>     }
>     return result;
> }
> ```
>
> ****

## 39 组合总数

> ### 题目
>
> * 给定一个**无重复元素**的数组 `candidates` 和一个目标数 `target` ，找出 `candidates` 中所有可以使数字和为 `target` 的组合。
> * `candidates` 中的数字可以无限制重复被选取。
>
> ### 示例
>
> ```java
> 输入：candidates = [2,3,5], target = 8,
> 所求解集为：
> [
>   [2,2,2,2],
>   [2,3,3],
>   [3,5]
> ]
> ```
>
> ### 思路
>
> * **回朔剪枝**
>
> ### 代码
>
> ```java
> public List<List<Integer>> combinationSum(int[] candidates, int target) {
>     List<List<Integer>> result = new ArrayList<List<Integer>>();
>     List<Integer> tempList = new ArrayList<>();
> 
>     Arrays.sort(candidates);
> 
>     backTrack(result,tempList,0,candidates,target,0);
>     return result;
> }
> 
> private void backTrack(List<List<Integer>> result,List<Integer> tempList,int tempResult,int[] candidate,int target,int index){
>     if (tempResult == target)
>         result.add(new ArrayList<>(tempList));
>     else {
>         for (int i = 0;i <candidate.length;i++){
>             if (tempResult + candidate[i] > target)
>                 break;
>             if (tempList.size() > 0 && candidate[i] < tempList.get(tempList.size()-1))
>                 continue;
>             tempList.add(candidate[i]);
>             tempResult += candidate[i];
>             backTrack(result,tempList,tempResult,candidate,target,index+1);
>             tempResult -= candidate[i];
>             tempList.remove(index);
>         }
>     }
> }
> ```
>
> ****

## 40 组合总数II

> ### 题目
>
> * 给定一个数组 `candidates` 和一个目标数 `target` ，找出 `candidates` 中所有可以使数字和为 `target` 的组合。
> * `candidates` 中的每个数字在每个组合中只能使用**一次**。
>
> ### 示例
>
> ```java
> 输入: candidates = [10,1,2,7,6,1,5], target = 8,
> 所求解集为:
> [
>   [1, 7],
>   [1, 2, 5],
>   [2, 6],
>   [1, 1, 6]
> ]
> ```
>
> ### 思路
>
> * **回溯+剪枝**
> * 重点：`if (i > 0 && candidate[i] == candidate[i-1] && !isUsed[i-1])`
>
> ### 代码
>
> ```java
> public List<List<Integer>> combinationSum2(int[] candidates, int target) {
>     List<List<Integer>> result = new ArrayList<List<Integer>>();
>     List<Integer> tempList = new ArrayList<>();
>     boolean[] isUsed = new boolean[candidates.length];
> 
>     Arrays.sort(candidates);
> 
>     backTrack(result,tempList,0,candidates,target,0,isUsed);
>     return result;
> }
> 
> private void backTrack(List<List<Integer>> result,List<Integer> tempList,int tempResult,int[] candidate,int target,int index,boolean[] isUsed){
>     if (tempResult == target)
>         result.add(new ArrayList<>(tempList));
>     else {
>         for (int i = 0;i <candidate.length;i++){
>             if (tempResult + candidate[i] > target)
>                 break;
>             if (tempList.size() > 0 && candidate[i] < tempList.get(tempList.size()-1) || isUsed[i])
>                 continue;
>             if (i > 0 && candidate[i] == candidate[i-1] && !isUsed[i-1])
>                 continue;
> 
>             tempList.add(candidate[i]);
>             tempResult += candidate[i];
>             isUsed[i] = true;
>             backTrack(result,tempList,tempResult,candidate,target,index+1,isUsed);
>             isUsed[i] = false;
>             tempResult -= candidate[i];
>             tempList.remove(index);
>         }
>     }
> }
> ```
>
> ****

## 46 全排列

> ### 题目
>
> * 给定一个 **没有重复** 数字的序列，返回其所有可能的全排列。
>
> ### 示例
>
> ```java
> 输入: [1,2,3]
> 输出:
> [
>   [1,2,3],
>   [1,3,2],
>   [2,1,3],
>   [2,3,1],
>   [3,1,2],
>   [3,2,1]
> ]
> ```
>
> ### 思路
>
> * **排列树 + 回溯**
>
> ### 代码
>
> ```java
> public List<List<Integer>> permute(int[] nums) {
>     List<List<Integer>> result = new ArrayList<List<Integer>>();
>     List<Integer> temp = new ArrayList<>();
> 
>     if (nums.length == 0)
>         return result;
> 
>     backTrack(result,temp,0,nums);
>     return result;
> }
> 
> public void backTrack(List<List<Integer>> list,List<Integer> temp,int index,int[] nums){
>     if (index == nums.length)
>         list.add(new ArrayList<Integer>(temp));
>     else {
>         for (int i = 0;i <nums.length;i++){
>             if (temp.contains(nums[i]))
>                 continue;
>             temp.add(nums[i]);
>             backTrack(list,temp,index+1,nums);
>             temp.remove(index);
>         }
>     }
> }
> ```
>
> ****

## 47 全排列II

> ### 题目
>
> * 给定一个可包含重复数字的序列 `nums` ，**按任意顺序** 返回所有不重复的全排列。
>
> ### 示例
>
> ```java
> 输入：nums = [1,1,2]
> 输出：
> [[1,1,2], [1,2,1], [2,1,1]]
> ```
>
> ### 思路
>
> * **回溯 + 剪枝**
>
> ### 代码
>
> ```java
> public List<List<Integer>> permuteUnique(int[] nums) {
>     List<List<Integer>> result = new ArrayList<List<Integer>>();
>     List<Integer> temp = new ArrayList<>();
>     HashSet<Integer> set = new HashSet<>();
> 
>     Arrays.sort(nums);
> 
>     boolean[] isUsed = new boolean[nums.length];
> 
>     backTrack(result,temp,0,nums,isUsed);
>     for (boolean is: isUsed) {
>         System.out.print(is);
>     }
>     return result;
> }
> 
> public void backTrack(List<List<Integer>> list, List<Integer> temp, int index, int[] nums, boolean[] isUsed){
>     if (index == nums.length )
>         list.add(new ArrayList<Integer>(temp));
>     else {
>         for (int i = 0;i <nums.length;i++){
>             if (isUsed[i])
>                 continue;
>             if (i > 0 && nums[i] == nums[i-1] && !isUsed[i - 1])
>                 continue;
>             temp.add(nums[i]);
>             isUsed[i] = true;
>             backTrack(list,temp,index+1,nums,isUsed);
>             isUsed[i] = false;
>             temp.remove(index);
>         }
>     }
> }
> ```
>
> ****

## 53 最大连续子序和

> ### 题目
>
> * 给定一个整数数组 `nums` ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
>
> ### 示例
>
> ```java
> 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
> 输出：6
> ```
>
> ### 思路
>
> * 维护一个 **当前sum** 值，维护一个 **当前max值**
> * 如果sum为负数，且当前值为正数就直接更新sum
>
> ### 代码
>
> ```java
> public int maxSubArray(int[] nums) {
>     int length = nums.length;
>     int result = nums[0];
>     int sum = nums[0];
>     for (int i = 1;i < length;i++){
>         if (nums[i] >= 0){
>             if (sum <= 0)
>                 sum = nums[i];
>             else 
>                 sum += nums[i];
>         }else {
>             if (sum < nums[i])
>                 sum = nums[i];
>             else 
>                 sum += nums[i];
>         }
>         result = Math.max(result,sum);
>     }
>     return result;
> }
> ```
>
> ****

## 64 最小路径和

> ### 题目
>
> * 给定一个包含非负整数的 `*m* x *n*` 网格 `grid` ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
> * **说明：**每次只能向下或者向右移动一步。
>
> ### 示例
>
> ```java
> 输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
> 输出：7
> 解释：因为路径 1→3→1→1→1 的总和最小
> ```
>
> ### 思路
>
> * **动态规划**
> * `d[i][j] = Math.min(d[i-1][j]+ grid[i][j], d[i][j-1]+ grid[i][j])`
> * 由于只能 **向下** 和 **向右** 走，因此`d[0][j]`和`d[i][0]`可以直接初始化
>
> ```java
> public int minPathSum(int[][] grid) {
>     int row = grid.length;
>     int column = grid[0].length;
> 
>     int[][] temps = new int[row][column];
> 
>     temps[0][0] = grid[0][0];
>     for (int i = 1;i<row;i++)
>         temps[i][0] = temps[i-1][0]+grid[i][0];
>     for (int j = 1;j<column;j++)
>         temps[0][j] = temps[0][j-1]+grid[0][j];
> 
>     for (int i=1;i<row;i++)
>         for (int j=1;j<column;j++)
>             temps[i][j] = Math.min(temps[i-1][j]+ grid[i][j],temps[i][j-1]+ grid[i][j]);
> 
>     return temps[row-1][column-1];
> }
> ```
>
> ****

## 69 x的平方根

> ### 题目
>
> * 实现 `int sqrt(int x)` 函数。
> * 计算并返回 *x* 的平方根，其中 *x* 是非负整数。
> * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
>
> ### 示例
>
> ```java
> 输入: 8
> 输出: 2
> 说明: 8 的平方根是 2.82842..., 
>      由于返回类型是整数，小数部分将被舍去。
> ```
>
> ### 思路
>
> * 左边界是 **0**，右边界是 **x**
> * 使用 **双指针 **和 **二分搜索**
> * 判断的时候不用乘法，而用模运算`xDivide = x / mid; if (mid == xDivide)`
>
> ### 代码
>
> ```java
> public int mySqrt(int x) {
>     if (x <= 1)
>         return x;
>     int i = 1;
>     int j = x;
>     int mid;
>     int xDivide;
>     while (i <= j){
>         mid = (i + j) >> 1;
>         xDivide = x / mid;
>         if (mid == xDivide)
>             return mid;
>         else if (mid < xDivide)
>             i = ++mid;
>         else
>             j = --mid;
>     }
>     return Math.min(i,j);
> }
> ```
>
> ****

## 70 爬楼梯

> ### 题目
>
> * 假设你正在爬楼梯。需要 *n* 阶你才能到达楼顶。
> * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
>
> ### 示例
>
> ```java
> 输入： 3
> 输出： 3
> ```
>
> ### 思路
>
> * **动态规划**
> * `d[i] = d[i - 1] + d[i - 2]`
>
> ### 代码
>
> ```java
> public int climbStairs(int n) {
>     if (n==1)
>         return 1;
>     if (n==2)
>         return 2;
>     int[] temps = new int[n];
>     temps[0] = 1;
>     temps[1] = 2;
> 
>     for (int i = 2; i < n;i++)
>         temps[i] += (temps[i-1]+temps[i-2]);
> 
>     return temps[n-1];
> }
> ```
>
> ****

## 78 子集

> ### 题目
>
> * 给你一个整数数组 `nums` ，数组中的元素 **互不相同** 。返回该数组所有可能的子集（幂集）。
> * 解集 **不能** 包含重复的子集。你可以按 **任意顺序** 返回解集。
>
> ### 示例
>
> ```java
> 输入：nums = [1,2,3]
> 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
> ```
>
> ### 思路
>
> * **回溯法**
>
> ### 代码
>
> ```java
> public List<List<Integer>> subsets(int[] nums) {
>     List<List<Integer>> result = new ArrayList<List<Integer>>();
>     List<Integer> tempList = new ArrayList<>();
>     boolean[] isUsed = new boolean[nums.length];
> 
>     Arrays.sort(nums);
>     backTrack(result,tempList,nums,isUsed,0);
>     result.add(new ArrayList<>());
>     return result;
> }
> 
> private void backTrack(List<List<Integer>> result,List<Integer> tempResult,int[] nums,boolean[] isUsed,int index){
>     if (index ！= nums.length)
>         for (int i = 0;i<nums.length;i++){
>             if (tempResult.size() >0 && nums[i] < tempResult.get(tempResult.size()-1))
>                 continue;
>             if (isUsed[i])
>                 continue;
> 
>             isUsed[i] = true;
>             tempResult.add(nums[i]);
>             result.add(new ArrayList<>(tempResult));
>             backTrack(result,tempResult,nums,isUsed,index+1);
>             tempResult.remove(index);
>             isUsed[i] = false;
>         }
> }
> ```
>
> ****

## 83 删除排序链表中的重复元素

> ### 题目
>
> * 给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。
>
> ### 示例
>
> ```java
> 1->1->2->3->3
> 输出: 1->2->3
> ```
>
> ### 思路
>
> * **递归**
> * **强行遍历变指针**
>
> ### 代码
>
> ```java
> public ListNode deleteDuplicates(ListNode head) {
>     if (head == null || head.next == null) return head;
>     head.next = deleteDuplicates(head.next);
>     return head.val == head.next.val ? head.next : head;
> }
> ```
>
> ****

## 88 合并两个有序数组

> ### 题目
>
> * 给你两个有序整数数组 `nums1` 和 `nums2`，请你将 `nums2` 合并到 `nums1` 中*，*使 `nums1` 成为一个有序数组。
> * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。你可以假设 nums1 的空间大小等于 m + n，这样它就有足够的空间保存来自 nums2 的元素。
>
>
> ### 示例
>
> ```java
> 输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
> 输出：[1,2,2,3,5,6
> ```
>
> ### 思路
>
> * **从尾到头** 逆序合并
> * 最后`i != 0`无所谓，是已经排好了。但`j != 0`就有问题，要进行处理
>
> ### 代码
>
> ```java
> public void merge(int[] nums1, int m, int[] nums2, int n) {
>     int i = m - 1;
>     int j = n - 1;
>     int index = m + n - 1;
>     while (i > -1 && j > -1){
>         if (nums1[i] > nums2[j]){
>             nums1[index] = nums1[i];
>             i--;
>         }else {
>             nums1[index] = nums2[j];
>             j--;
>         }
>         index--;
>     }
>     while (j > -1){
>         nums1[index] = nums2[j];
>         index--;
>         j--;
>     }
> }
> ```
>
> ****

## 95 不同的二叉搜索树

> ### 题目
>
> * 给定一个整数 *n*，生成所有由 1 ... *n* 为节点所组成的 **二叉搜索树** 。
>
> ### 示例
>
> ```java
> 输入：3
> 输出：
> [
>   [1,null,3,2],
>   [3,2,null,1],
>   [3,1,null,null,2],
>   [2,1,3],
>   [1,null,2,null,3]
> ]
> 解释：
> 以上的输出对应以下 5 种不同结构的二叉搜索树：
> 
>    1         3     3      2      1
>     \       /     /      / \      \
>      3     2     1      1   3      2
>     /     /       \                 \
>    2     1         2                 3
> ```
>
> ### 思路
>
> * **递归**
> * 一个for循环遍历每一个结点，此结点左边做左子树，右边做右子树
>
> ### 代码
>
> ```java
> public List<TreeNode> generateTrees(int n) {
>     return tree(1,n);
> }
> private List<TreeNode> tree(int start,int end){
>     List<TreeNode> results = new ArrayList<TreeNode>();
>     if (start == end){
>         results.add(new TreeNode(start,null,null));
>     }else if (start > end)
>         results.add(null);
>     else {
>         for (int i = start;i <= end;i++){
>             List<TreeNode> lefts = tree(start,i - 1);
>             List<TreeNode> rights = tree(i+1,end);
>             for (TreeNode leftTree : lefts){
>                 for (TreeNode rightTree :rights){
>                     results.add(new TreeNode(i,leftTree,rightTree));
>                 }
>             }
>         }
>     }
>     return results;
> }
> ```
>
> ****

## 104 二叉树的最大深度

> ### 题目
>
> * 给定一个二叉树，找出其最大深度。
>
> ### 示例
>
> ```java
> 给定二叉树 [3,9,20,null,null,15,7]，
>     3
>    / \
>   9  20
>     /  \
>    15   7
> 它的最大深度 3 。
> ```
>
> ### 思路
>
> * **递归**
> * **BFS**。每次在队列中加入个 **null** 代表遍历到一层
>
> ### 代码
>
> ```java
> public int maxDepth(TreeNode root) {
>     if (root == null)
>         return 0;
>     else
>         return Math.max(maxDepth(root.left),maxDepth(root.right)) + 1;
> }
> ```
>
> ****

## 110 平衡二叉树

> ### 题目
>
> * 给定一个二叉树，判断它是否是高度平衡的二叉树。
>
> ### 平衡二叉树
>
> * 一个二叉树*每个节点* 的左右两个子树的高度差的绝对值不超过 1 
>
> ### 示例
>
> ```java
> 输入：root = [3,9,20,null,null,15,7]
> 输出：true
> ```
>
> ### 思路
>
> * **递归**。要使用到树深的函数
>
> ### 代码
>
> ```java
> public boolean isBalanced(TreeNode root) {
>     if (root == null)
>         return true;
>     else if (Math.abs(maxLength(root.left) - maxLength(root.right)) > 1)
>         return false;
>     else 
>         return isBalanced(root.left) && isBalanced(root.right);
> }
> 
> private int maxLength(TreeNode root){
>     if (root == null)
>         return 0;
>     else
>         return Math.max(maxLength(root.left),maxLength(root.right)) + 1;
> }
> ```
>
> ****

## 115 不同的子序列

> ### 题目
>
> * 给定一个字符串 `s` 和一个字符串 `t` ，计算在 `s` 的子序列中 `t` 出现的个数。
> * 字符串的一个 子序列 是指，通过删除一些（也可以不删除）字符且不干扰剩余字符相对位置所组成的新字符串。（例如，"ACE" 是 "ABCDE" 的一个子序列，而 "AEC" 不是）
> * 题目数据保证答案符合 32 位带符号整数范围。
>
> ### 示例
>
> ```java
> 输入：s = "babgbag", t = "bag"
> 输出：5
> 解释：
> 如下图所示, 有 5 种可以从 s 中得到 "bag" 的方案。 
> (上箭头符号 ^ 表示选取的字母)
> babgbag
> ^^ ^
> babgbag
> ^^    ^
> babgbag
> ^    ^^
> babgbag
>   ^  ^^
> babgbag
>     ^^^
> ```
>
> ### 思路
>
> * **动态规划**
> * `d[i][j] = d[i + 1][j + 1] when s[i] == t[j]`
> * `d[i][j] =d[i + 1][j] +  d[i + 1][j + 1] when s[i] != t[j]`
>
> ### 代码
>
> ```java
> public int numDistinct(String s, String t) {
>     int m = s.length(), n = t.length();
>     if (m < n) {
>         return 0;
>     }
>     int[][] dp = new int[m + 1][n + 1];
>     for (int i = 0; i <= m; i++) {
>         dp[i][n] = 1;
>     }
>     for (int i = m - 1; i >= 0; i--) {
>         char sChar = s.charAt(i);
>         for (int j = n - 1; j >= 0; j--) {
>             char tChar = t.charAt(j);
>             if (sChar == tChar) {
>                 dp[i][j] = dp[i + 1][j + 1] + dp[i + 1][j];
>             } else {
>                 dp[i][j] = dp[i + 1][j];
>             }
>         }
>     }
>     return dp[0][0];
> }
> ```
>
> ****

## 121 买股票的最佳时机

> ### 题目
>
> * 给定一个数组 `prices` ，它的第 `i` 个元素 `prices[i]` 表示一支给定股票第 `i` 天的价格。
> * 你只能选择 **某一天** 买入这只股票，并选择在 **未来的某一个不同的日子** 卖出该股票。设计一个算法来计算你所能获取的最大利润。
> * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 `0` 。
>
> ### 示例
>
> ```java
> 输入：[7,1,5,3,6,4]
> 输出：5
> ```
>
> ### 思路
>
> * **贪心思想**
> * `currentMaxProfile = currentPrice - currentMinPrice`
>
> ### 代码
>
> ```java
> public int maxProfit(int[] prices) {
>     int length = prices.length;
> 
>     if (length == 1)
>         return 0;
> 
>     int currentMin = prices[0];
>     int result = 0;
>     for (int i = 1;i < length;i++){
>         result = Math.max(result,prices[i] - currentMin);
>         if (prices[i] < currentMin)
>             currentMin = prices[i];
>     }
>     return result;
> }
> ```
>
> ****

## 122 买卖股票的最佳时机II

> ### 题目
>
> * 给定一个数组，它的第 *i* 个元素是一支给定股票第 *i* 天的价格。
> * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
> * **注意：**你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
>
> ### 示例
>
> ```java
> 输入: [7,1,5,3,6,4]
> 输出: 7
> ```
>
> ### 思路
>
> * **贪心思想**
> * 把所有上升段都算作利润
>
> ### 代码
>
> ```java
> public int maxProfit(int[] prices) {
>     int profit = 0;
>     for (int i = 1; i < prices.length; i++) {
>         if (prices[i] > prices[i - 1]) {
>             profit += (prices[i] - prices[i - 1]);
>         }
>     }
>     return profit;
> }
> ```
>
> ****

## 128 最长连续序列

> ### 题目
>
> * 给定一个未排序的整数数组 `nums` ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
> * **进阶：**你可以设计并实现时间复杂度为 `O(n)` 的解决方案吗？
>
> ### 示例
>
> ```java
> 输入：nums = [100,4,200,1,3,2]
> 输出：4
> 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4
> ```
>
> ### 思路
>
> * **HashSet** + **O(n)遍历** + **每次向上或向下isContain()相邻**
> * **O(2n)**也是O(n)
>
> ### 代码
>
> ```java
> public int longestConsecutive(int[] nums) {
>     Set<Integer> num_set = new HashSet<Integer>();
>     for (int num : nums)
>         num_set.add(num);
> 
>     int longestStreak = 0;
> 
>     for (int num : num_set) 
>         if (!num_set.contains(num - 1)) {
>             int currentNum = num;
>             int currentStreak = 1;
>             while (num_set.contains(currentNum + 1)) {
>                 currentNum += 1;
>                 currentStreak += 1;
>             }
>             longestStreak = Math.max(longestStreak, currentStreak);
>         }
>     return longestStreak;
> }
> ```
>
> ****

## 136 只出现了一次的数字

> ### 题目
>
> * 给定一个**非空**整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素
>
> ### 示例
>
> ```java
> 输入: [2,2,1]
> 输出: 
> ```
>
> ### 思路
>
> * **异或**
>
> ### 代码
>
> ```java
> public int singleNumber(int[] nums) {
>     int result = 0;
>     for (int num : nums)
>         result ^= num;
>     return result;
> }
> ```
>
> ****

## 141 环形链表

> ### 题目
>
> * 给定一个链表，判断链表中是否有环。
> * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。
> * 如果链表中存在环，则返回 `true` 。 否则，返回 `false` 。
>
> ### 示例
>
> ```java
> 输入：head = [3,2,0,-4], pos = 1
> 输出：true
> 解释：链表中有一个环，其尾部连接到第二个节点
> ```
>
> ### 思路
>
> * **HashSet**法
> * **快慢指针法**
>
> ### 代码
>
> ```java
> //方法一
> public boolean hasCycle(ListNode head) {
>     Set<ListNode> seen = new HashSet<ListNode>();
>     while (head != null) {
>         if (!seen.add(head)) {
>             return true;
>         }
>         head = head.next;
>     }
>     return false;
> }
> 
> //方法二
> public boolean hasCycle(ListNode head) {
>     if (head == null || head.next == null)
>         return false;
> 
>     ListNode slow = head;
>     ListNode fast = head.next;
>     while (slow != fast){
>         if (fast.next == null || fast.next.next == null)
>             return false;
>         slow = slow.next;
>         fast = fast.next.next;
>     }
>     return true;
> }
> ```
>
> ****

## 142 环形链表II

> ### 题目
>
> * 和上一题一样，只不过要返回入环处结点
>
> ### 示例
>
> ```java
> 输入：head = [3,2,0,-4], pos = 1
> 输出：返回索引为 1 的链表节点
> 解释：链表中有一个环，其尾部连接到第二个节点
> ```
>
> ### 思路
>
> * **快慢指针** + **数学推理**
> * 追上了如果一个指针从head开始走会和慢指针相遇
>
> ### 代码
>
> ```java
> public ListNode detectCycle(ListNode head) {
>     if (head == null) {
>         return null;
>     }
>     ListNode slow = head, fast = head;
>     while (fast != null) {
>         slow = slow.next;
>         if (fast.next != null) {
>             fast = fast.next.next;
>         } else {
>             return null;
>         }
>         if (fast == slow) {
>             ListNode ptr = head;
>             while (ptr != slow) {
>                 ptr = ptr.next;
>                 slow = slow.next;
>             }
>             return ptr;
>         }
>     }
>     return null;
> }
> ```
>
> ****

## 153 寻找旋转排序数组中的最小值

> ### 题目
>
> * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。例如，数组 `[0,1,2,4,5,6,7]` 可能变为 `[4,5,6,7,0,1,2]` 。
> * 请找出其中最小的元素
>
> ### 示例
>
> ```java
> 输入：nums = [3,4,5,1,2]
> 输出：1
> ```
>
> ### 思路
>
> * **二分搜索**
>
> ### 代码
>
> ```java
> public int findMin(int[] nums) {
>     int i = 0;
>     int j = nums.length - 1;
>     int mid;
>     while (i < j){
>         mid = i + (j - i) / 2;
>         if (nums[mid] > nums[j]){
>             i = ++mid;
>         }else {
>             j = mid;
>         }
>     }
>     return nums[i];
> }
> ```
>
> ****

## 155 最小栈

> ### 题目
>
> * 设计一个支持 `push` ，`pop` ，`top` 操作，并能在常数时间内检索到最小元素的栈。
>
> ### 思路
>
> * 唯一难点在常数时间获取到栈中的最小值
> * 因此必须在push和pop操作时就把最小值计算出来并存好
> * 使用**辅助栈**
>   * 每次压栈都压一个最小值
>  * 每次出栈就出一个最小值
>   * 栈顶值就是当前栈的最小值
> 
> ****

## 167 两数之和II - 输入有序数组

> ### 题目
>
> * 给定一个已按照 **升序排列** 的整数数组 `numbers` ，请你从数组中找出两个数满足相加之和等于目标数 `target` 。
> * 函数应该以长度为 2 的整数数组的形式返回这两个数的下标值。numbers 的下标 从 1 开始计数 ，所以答案数组应当满足 1 <= answer[0] < answer[1] <= numbers.length 。
>
> * 你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素
>
> ### 示例
>
> ```java
> 输入：numbers = [2,7,11,15], target = 9
> 输出：[1,2]
> 解释：2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 
> ```
>
> ### 思路
>
> * **双指针**
>
> ### 代码
>
> ```java
> public int[] twoSum(int[] numbers, int target) {
>     int i = 0;
>     int j = numbers.length - 1;
>     int tempSum = 0;
>     while (i < j){
>         tempSum = numbers[i] + numbers[j];
>         if (tempSum == target)
>             return new int[]{++i,++j};
>         else if (tempSum > target)
>             j--;
>         else 
>             i++;
>     }
>     return new int[]{-1,-1};
> }
> ```
>
> ****

## 198 打劫家舍

> ### 题目
>
> * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，**如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警**。
>
> * 给定一个代表每个房屋存放金额的非负整数数组，计算你 **不触动警报装置的情况下** ，一夜之内能够偷窃到的最高金额。
>
> ### 示例
>
> ```java
> 输入：[1,2,3,1]
> 输出：4
> 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
>      偷窃到的最高金额 = 1 + 3 = 4 
> ```
>
> ### 思路
>
> * **动态规划**
> * `d[i] = Math.max(value[i] + d[i - 2], d[i - 1])`
>
> ### 代码
>
> ```java
> public int rob(int[] nums) {
>     if (nums.length == 0)
>         return 0;
>     if (nums.length == 1)
>         return nums[0];
> 
>     int[] value = new int[nums.length];
>     value[0] = nums[0];
>     value[1] = Math.max(nums[1],nums[0]);
>     int length = nums.length;
> 
>     for (int i = 2;i<length;i++){
>         value[i] = Math.max(nums[i]+value[i-2],value[i-1]);
>     }
>     return value[length-1];
> }
> ```
>
> ****

## 205 同构字符串

> ### 题目
>
> * 给定两个字符串 ***s*** 和 ***t***，判断它们是否是同构的。
> * 如果 ***s*** 中的字符可以按某种映射关系替换得到 ***t*** ，那么这两个字符串是同构的。
> * 每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上，字符可以映射到自己本身。
>
> ### 示例
>
> ```java
> 输入：s = "egg", t = "add"
> 输出：true
> ```
>
> ### 思路
>
> * 维护两个`int[128]`的数组，初始为0默认为true
> * 后面每判断一个`char`都提前判断是否相等，如果不等直接为false
>
> ### 代码
>
> ```java
> public boolean isIsomorphic(String s, String t) {
>     int[] preIndexOfS = new int[128];
>     int[] preIndexOfT = new int[128];
>     for (int i = 0; i < s.length(); i++) {
>         char sc = s.charAt(i), tc = t.charAt(i);
>         if (preIndexOfS[sc] != preIndexOfT[tc])
>             return false;
>         preIndexOfS[sc] = i + 1;
>         preIndexOfT[tc] = i + 1;
>     }
>     return true;
> }
> ```
>
> ****

## 213 打劫家舍II

> ### 题目
>
> * 题目和打劫家舍I一样，只不过多了 **首尾相连接**
>
> ```java
> 输入：nums = [2,3,2]
> 输出：3
> 解释：你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。
> ```
>
> ### 思路
>
> * 唯一的不同就是首尾相邻
> * 分为两种情况
>   * 不偷第一家，则结果为`1...length - 1`的打劫家舍
>   * 偷第一家，则结果为`0...length - 2`的打劫家舍
>
> ### 代码
>
> ```java
> public int rob(int[] nums) {
>     int length = nums.length;
> 
>     if (length == 1)
>         return nums[0];
>     if (length == 2)
>         return Math.max(nums[1],nums[0]);
> 
> 
>     int[] value= new int[length-1];
>     value[0] = nums[0];
>     value[1] = Math.max(nums[1],nums[0]);
> 
>     for (int i = 2;i<length-1;i++)
>         value[i] = Math.max(nums[i] + value[i-2],value[i-1]);
> 
>     int tempResult = value[length-2];
> 
>     value[0] = nums[1];
>     value[1] = Math.max(nums[2],nums[1]);
>     for (int i = 2;i<length-1;i++)
>         value[i] = Math.max(nums[i+1] + value[i-2],value[i-1]);
>     return Math.max(value[length-2],tempResult);
> }
> ```
>
> ****

## 216 组合总数III

> ### 题目
>
> * 找出所有相加之和为 ***n*** 的 **k** 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
> * 说明
>   * 所有数字都是正整数。
>   * 解集不能包含重复的组合。 
>
> ### 示例
>
> ```java
> 输入: k = 3, n = 9
> 输出: [[1,2,6], [1,3,5], [2,3,4]]
> ```
>
> ### 思路
>
> * **回溯** + **剪枝**
>
> ### 代码
>
> ```java
> public List<List<Integer>> combinationSum3(int k, int n) {
>     List<List<Integer>> results = new ArrayList<List<Integer>>();
>     List<Integer> temp = new ArrayList<Integer>();
>     int tempResult = 0;
>     boolean[] haveUsed = new boolean[10];
>     backTrack(results,temp,n,tempResult,k,0,haveUsed);
>     return results;
> }
> 
> private void backTrack(List<List<Integer>> results,
>                        List<Integer> temp,
>                        int target,
>                        int tempResult,
>                        int k,
>                        int index,
>                        boolean[] haveUsed){
>     if (index == k && target == tempResult){
>         results.add(new ArrayList<Integer>(temp));
>     }else {
>         for (int i = 1;i <= 9;i++){
>             if (tempResult + i > target)
>                 break;
>             if (temp.size() > 0 && i < temp.get(temp.size() - 1) || haveUsed[i])
>                 continue;
>             haveUsed[i] = true;
>             temp.add(i);
>             tempResult += i;
>             backTrack(results,temp,target,tempResult,k,index + 1,haveUsed);
>             tempResult -= i;
>             temp.remove(index);
>             haveUsed[i] = false;
>         }
>     }
> }
> ```
>
> ****

## 217 重复的元素

> ### 题目
>
> * 给定一个整数数组，判断是否存在重复元素。
> * 如果存在一值在数组中出现至少两次，函数返回 `true` 。如果数组中每个元素都不相同，则返回 `false` 。
>
> ### 示例
>
> ```java
> 输入: [1,2,3,1]
> 输出: true
> ```
>
> ### 思路
>
> * **HashSet**
> * **排序遍历**
> * 
>
> ### 代码
>
> ```java
> 
> ```
>
> ****

## 225 用队列实现栈

> ### 题目
>
> * 请你仅使用两个队列实现一个后入先出（LIFO）的栈，并支持普通队列的全部四种操作（`push`、`top`、`pop` 和 `empty`）。
>
> ### 思路
>
> * 唯一的难点在 **add** 操作
> * 做法是在push操作时，将A队列所有元素poll在add到B队列，然后A队列中add此元素，最后在将B队列的元素移动A队列
>
> ### 代码
>
> ```java
> public void push(int x) {
>     while (!pushQueue.isEmpty())
>         popQueue.add(pushQueue.poll());
>     pushQueue.add(x);
>     while (!popQueue.isEmpty())
>         pushQueue.add(popQueue.poll());
> }
> ```
>
> ****

## 232 用栈实现队列

> ### 题目
>
> * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（`push`、`pop`、`peek`、`empty`）
>
> ### 思路
>
> * 唯一的难点是 **poll** 操作
> * 做法是在进行poll操作时，将A栈所有元素pop然后push到B栈中，将B栈的栈顶出栈作为返回结果，B栈剩下的元素重新压回A栈中
>
> ### 代码
>
> ```java
> public int pop() {
>     while (!pushStack.isEmpty())
>         popStack.push(pushStack.pop());
>     int result = popStack.pop();
>     while (!popStack.isEmpty())
>         pushStack.push(popStack.pop());
>     return result;
> }
> ```
>
> ****

## 236 二叉树的最近公共祖先

> ### 题目
>
> * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
> * 共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
>
>
> ### 示例
>
> ```java
> 输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
> 输出：3
> 解释：节点 5 和节点 1 的最近公共祖先是节点 3 。
> ```
>
> ### 思路
>
> * **递归**
> * 自己的递归思路太复杂了，详细过程看代码
> * 代码的逻辑其实和自己一样，都是在判断左右子树中是否包含p 或 q，如果两边各包含一个那肯定此时的结点就是结果，如果一边为空则结果就一定是另外一边
>
> ### 递归思路核心
>
> * 用递归时要无条件相信此递归是正确的，即时自己现在还没有把它写出来
>
> ### 代码
>
> ```java
> public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
>     if (root == null || root == p || root == q) return root;
>     TreeNode left = lowestCommonAncestor(root.left, p, q);
>     TreeNode right = lowestCommonAncestor(root.right, p, q);
>     if(left == null) return right;
>     if(right == null) return left;
>     return root
> }
> ```
>
> ****

## 240 搜索二维矩阵II

> ### 题目
>
> * 编写一个高效的算法来搜索 `*m* x *n*` 矩阵 `matrix` 中的一个目标值 `target` 。
> * 该矩阵具有以下特性：
>   * 每行的元素从左到右升序排列。
>   * 每列的元素从上到下升序排列。
>
> ### 示例
>
> ```java
> 输入：matrix = [
>     [1 ,4 ,7 ,11,15],
>     [2 ,5 ,8 ,12,19],
>     [3 ,6 ,9 ,16,22],
>     [10,13,14,17,24],
>     [18,21,23,26,30]], target = 5
> 输出：true
> ```
>
> ### 思路
>
> * 从左上角和右下角开始进行搜索都无法进行判断
> * 但是是 **左下角** 和 **右下角** 进行搜索就能很好使用 **类似双指针** 的思想，迅速缩小解空间范围
>
> ### 代码
>
> ```java
> public boolean searchMatrix(int[][] matrix, int target) {
>     int row = matrix.length;
>     int column = matrix[0].length;
>     int i = row - 1;
>     int j = 0;
>     while (i > -1 && j < column) {
>         if (target == matrix[i][j]) return true;
>         else if (target < matrix[i][j]) i--;
>         else j++;
>     }
>     return false;
> }
> ```
>
> ****

## 241 为运算表达式设置优先级

> ### 题目
>
> * 给定一个含有数字和运算符的字符串，为表达式添加括号，改变其运算优先级以求出不同的结果。你需要给出所有可能的组合的结果。有效的运算符号包含 +, - 以及 * 。
>
>
> ### 示例
>
> ```java
> 输入: "2*3-4*5"
> 输出: [-34, -14, -10, -10, 10]
> 解释: 
> (2*(3-(4*5))) = -34 
> ((2*3)-(4*5)) = -14 
> ((2*(3-4))*5) = -10 
> (2*((3-4)*5)) = -10 
> (((2*3)-4)*5) = 10
> ```
>
> ### 思路
>
> * **递归**
> * 每一个操作符号的左右完全是和原问题相同的问题
>
> ### 代码
>
> ```java
> public List<Integer> diffWaysToCompute(String input) {
>     List<Integer> ways = new ArrayList<>();
>     for (int i = 0; i < input.length(); i++) {
>         char c = input.charAt(i);
>         if (c == '+' || c == '-' || c == '*') {
>             List<Integer> left = diffWaysToCompute(input.substring(0, i));
>             List<Integer> right = diffWaysToCompute(input.substring(i + 1));
>             for (int l : left) {
>                 for (int r : right) {
>                     switch (c) {
>                         case '+':
>                             ways.add(l + r);
>                             break;
>                         case '-':
>                             ways.add(l - r);
>                             break;
>                         case '*':
>                             ways.add(l * r);
>                             break;
>                     }
>                 }
>             }
>         }
>     }
>     if (ways.size() == 0) {
>         ways.add(Integer.valueOf(input));
>     }
>     return ways;
> }
> ```
>
> ****



**********



> ### 题目
>
> * 
>
> ### 示例
>
> ```java
> 
> ```
>
> ### 思路
>
> * 
>
> ### 代码
>
> ```java
> 
> ```
>
> ****

