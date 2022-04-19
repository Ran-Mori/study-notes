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
>   * 组合非排列：要求下一个元素必须 **大于等于** 上一个元素。
>     * `if (tempResult.size() > 0 && nums[i] < tempResult.get(tempResult.size() - 1))`
>     * **等于**是因为数组中有同一个元素出现两次及以上
>     * 不用担心加上等于会重复，因为`[-1, 0, 0, 1]`中的两个0是有顺序的，另一种会被 **continue**掉
>   * 元素只能用一次：维护Boolean数组。`if (isUsed[i])`
>   * 同一个元素剪枝：`if (i > 0 && nums[i] == nums[i - 1] && !isUsed[i - 1])`
>   * sum大于了后面直接剪枝：`if (nums[i] > 0 && sum + nums[i] > target)`。有一个`num[i] > 0`是因为`num[i]`可能为负数，即时大于了也有可能加上一个负数变小
>
> ### 代码
>
> ```java
> private List<List<Integer>> result;
> private List<Integer> tempResult;
> private boolean[] isUsed;
> private int target;
> private int sum;
> 
> public List<List<Integer>> fourSum(int[] nums, int target) {
>     result = new ArrayList<>();
>     tempResult = new ArrayList<>();
>     isUsed = new boolean[nums.length];
>     this.target = target;
>     sum = 0;
> 
>     Arrays.sort(nums);
> 
>     backTrack(nums,0);
> 
>     return result;
> }
> 
> private void backTrack(int[] nums,int level){
>     if (level == 4 && sum == target)
>         result.add(new ArrayList<>(tempResult));
>     else if (level != 4){
>         for (int i = 0;i < nums.length;i++){
>             if (nums[i] > 0 && sum + nums[i] > target)
>                 break;
>             if (isUsed[i])
>                 continue;
>             if (tempResult.size() > 0 && nums[i] < tempResult.get(tempResult.size() - 1))
>                 continue;
>             if (i > 0 && nums[i] == nums[i - 1] && !isUsed[i - 1])
>                 continue;
> 
>             isUsed[i] = true;
>             tempResult.add(nums[i]);
>             sum += nums[i];
>             backTrack(nums,level + 1);
>             sum -= nums[i];
>             tempResult.remove(level);
>             isUsed[i] = false;
>         }
>     }
> }
> ```
>
> 
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

## 92 反转链表

> ### 题目
>
> * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。
>
>
> ### 示例
>
> ```java
> 输入：head = [1,2,3,4,5], left = 2, right = 4
> 输出：[1,4,3,2,5
> ```
>
> ### 思路
>
> * 找到反转的首尾，进行反转
>
> ### 代码
>
> ```java
> public ListNode reverseBetween(ListNode head, int left, int right) {
>     int between = right - left;
> 
>     ListNode newHead = head;
>     ListNode beforeHead = head;
>     boolean isLeftEqualOne = left == 1 ? true : false;
>     if (left == 1)
>         newHead = head;
>     else {
>         while (left != 2){
>             beforeHead = beforeHead.next;
>             left--;
>         }
>         newHead = beforeHead.next;
>     }
> 
> 
>     ListNode newTail = newHead;
>     ListNode afterTail = null;
>     while (between != 0){
>         newTail = newTail.next;
>         between--;
>     }
>     afterTail = newTail.next;
> 
>     ListNode finalTail = newHead;
>     ListNode finalHead = newTail;
>     ListNode dump = null;
>     ListNode temp = null;
>     while (newHead != afterTail){
>         temp = newHead.next;
>         newHead.next = dump;
>         dump = newHead;
>         newHead = temp;
>     }
>     finalTail.next = afterTail;
>     if (isLeftEqualOne)
>         return finalHead;
>     beforeHead.next = finalHead;
>     return head;
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

## 242 有效的字母异位词

> ### 题目
>
> * 给定两个字符串 *s* 和 *t* ，编写一个函数来判断 *t* 是否是 *s* 的字母异位词。
> * 说明：你可以假设字符串只包含小写字母。
>
> ### 示例
>
> ```java
> 输入: s = "anagram", t = "nagaram"
> 输出: true
> ```
>
> ### 思路
>
> * 使用 `int[26]`来存放每个char出现的次数
>
> ### 代码
>
> ```java
> public boolean isAnagram(String s, String t) {
>     if (s.length() != t.length())
>         return false;
> 
>     int[] counts = new int[26];
> 
>     for (char temp : s.toCharArray())
>             counts[temp - 'a']++;
>     for (char temp : t.toCharArray())
>         counts[temp - 'a']--;
>     for (int temp : counts){
>         if (temp != 0)
>             return false;   
>     }
>     return true;
> }
> ```
>
> ****

## 257 二叉树的所有路径

> ### 题目
>
> * 给定一个二叉树，返回所有从根节点到叶子节点的路径。
> * **说明:** 叶子节点是指没有子节点的节点。
>
> ### 示例
>
> ```java
> 输入:
> 
>    1
>  /   \
> 2     3
>  \
>   5
> 
> 输出: ["1->2->5", "1->3"]
> ```
>
> ### 思路
>
> * **BFS** 
> * **DFS**
>
> ### 代码
>
> ```java
> public List<String> binaryTreePaths(TreeNode root) {
>     List<String> results = new ArrayList<String>();
>     StringBuilder builder= new StringBuilder();
>     dfs(results,builder,root);
>     return results;
> }
> 
> private void dfs(List<String> list,StringBuilder builder,TreeNode node){
>     if (node == null)
>         return;
>     else if (node.left == null && node.right == null){
>         builder.append(node.val);
>         list.add(builder.toString());
>         builder.delete(builder.lastIndexOf(">")+1,builder.length());
>     }
>     else {
>         builder.append(node.val);
>         builder.append("->");
>         int tempLength = builder.length();
>         dfs(list,builder,node.left);
>         builder.delete(tempLength,builder.length());
>         dfs(list,builder,node.right);
>         builder.delete(tempLength,builder.length());
>     }
> }
> ```
>
> ****

## 268 丢失的数字

> ### 题目
>
> * 给定一个包含 `[0, n]` 中 `n` 个数的数组 `nums` ，找出 `[0, n]` 这个范围内没有出现在数组中的那个数。
>
> ### 示例
>
> ```java
> 输入：nums = [3,0,1]
> 输出：2
> 解释：n = 3，因为有 3 个数字，所以所有的数字都在范围 [0,3] 内。2 是丢失的数字，因为它没有出现在 nums 中。
> ```
>
> ### 思路
>
> * **异或**
> * 初始异或数字为`num.length`
>
> ### 代码
>
> ```java
> public int missingNumber(int[] nums) {
>     int missing = nums.length;
>     for (int i = 0; i < nums.length; i++) {
>         missing ^= i ^ nums[i];
>     }
>     return missing;
> }
> ```
>
> ****

## 278 第一个错误的版本

> ### 题目
>
> * 假设你有 `n` 个版本 `[1, 2, ..., n]`，你想找出导致之后所有版本出错的第一个错误的版本。
> * 你可以通过调用 bool isBadVersion(version) 接口来判断版本号 version 是否在单元测试中出错。实现一个函数来查找第一个错误的版本。你应该尽量减少对调用 API 的次数。
>
>
> ### 示例
>
> ```java
> 给定 n = 5，并且 version = 4 是第一个错误的版本。
> 
> 调用 isBadVersion(3) -> false
> 调用 isBadVersion(4) -> true
> 调用 isBadVersion(5) -> true
> 
> 所以，4 是第一个错误的版本。
> ```
>
> ### 思路
>
> * 问题实质是找 **第一次出现的true**
> * 肯定得用**二分法**，但二分一些处理不同，具体看代码的边界
>
> ### 代码
>
> ```java
> public int firstBadVersion(int n) {
>     int i = 1;
>     int j = n;
>     int mid;
>     while (i < j){
>         mid = i + (j - i) / 2;
>         if (isBadVersion(mid))
>             j = mid;
>         else
>             i = ++mid;
>     }
>     return i;
> }
> ```
>
> ****

## 279 完全平方数

> ### 题目
>
> * 给定正整数 *n*，找到若干个完全平方数（比如 `1, 4, 9, 16, ...`）使得它们的和等于 *n*。你需要让组成和的完全平方数的个数最少。
> * 给你一个整数 `n` ，返回和为 `n` 的完全平方数的 **最少数量** 。
>
> ### 示例
>
> ```java
> 输入：n = 12
> 输出：3 
> 解释：12 = 4 + 4 + 4
> ```
>
> ### 思路
>
> * **回溯法**会报超时
> * 用 **动态规划**，但真不容易想到
>
> ### 代码
>
> ```java
> public int numSquares(int n) {
>     int[] dp = new int[n + 1]; // 默认初始化值都为0
>     for (int i = 1; i <= n; i++) {
>         dp[i] = i; // 最坏的情况就是每次+1
>         for (int j = 1; i - j * j >= 0; j++) {
>             dp[i] = Math.min(dp[i], dp[i - j * j] + 1); // 动态转移方程
>         }
>     }
>     return dp[n];
> }
> ```
>
> ****

## 283 移动零

> ### 题目
>
> * 给定一个数组 `nums`，编写一个函数将所有 `0` 移动到数组的末尾，同时保持非零元素的相对顺序。
>
> ### 示例
>
> ```java
> 输入: [0,1,0,3,12]
> 输出: [1,3,12,0,0]
> ```
>
> ### 思路
>
> * **双指针**
> * 维护一个 **左边暂时正确答案**
>
> ### 代码
>
> ```java
> public void moveZeroes(int[] nums) {
>     int length = nums.length;
>     int zeroNumberCount = 0;
>     int index = 0;
>     for (;index < length;index++){
>         if (nums[index] == 0)
>             zeroNumberCount++;
>         else if (zeroNumberCount != 0)
>             nums[index - zeroNumberCount] = nums[index];
>     }
>     index = length - 1;
>     while (zeroNumberCount != 0){
>         nums[index--] = 0;
>         zeroNumberCount--;
>     }
> }
> ```
>
> ****

## 287 寻找重复数

> ### 题目
>
> * 给定一个包含 `n + 1` 个整数的数组 `nums` ，其数字都在 `1` 到 `n` 之间（包括 `1` 和 `n`），可知至少存在一个重复的整数。
> * 假设 `nums` 只有 **一个重复的整数** ，找出 **这个重复的数** 。
>
> ### 示例
>
> ```java
> 输入：nums = [1,3,4,2,2]
> 输出：2
> ```
>
> ### 思路
>
> * **HashSet**
> * **排序**
> * **快慢指针成环**。即在找入环处结点
>
> ### 代码
>
> ```java
> public int findDuplicate(int[] nums) {
>     int slow = 0, fast = 0;
>     do {
>         slow = nums[slow];
>         fast = nums[nums[fast]];
>     } while (slow != fast);
>     slow = 0;
>     while (slow != fast) {
>         slow = nums[slow];
>         fast = nums[fast];
>     }
>     return slow;
> }
> ```
>
> ****

## 328 奇偶链表

> ### 题目
>
> * 给定一个单链表，把所有的奇数节点和偶数节点分别排在一起。请注意，这里的奇数节点和偶数节点指的是节点编号的奇偶性，而不是节点的值的奇偶性。
> * 请尝试使用原地算法完成。你的算法的空间复杂度应为 O(1)，时间复杂度应为 O(nodes)，nodes 为节点总数。
>
> ### 示例
>
> ```java
> 输入: 1->2->3->4->5->NULL
> 输出: 1->3->5->2->4->NULL
> ```
>
> ### 思路
>
> * 递归是递归不了的，只能 **强行遍历改指针**
>
> ### 代码
>
> ```java
> public ListNode oddEvenList(ListNode head) {
>     if (head == null) 
>         return head;
>     ListNode odd = head, even = head.next, evenHead = even;
>     while (even != null && even.next != null) {
>         odd.next = odd.next.next;
>         odd = odd.next;
>         even.next = even.next.next;
>         even = even.next;
>     }
>     odd.next = evenHead;
>     return head;
> }
> ```
>
> ****

## 345 反转元音字母

> ### 题目
>
> * 编写一个函数，以字符串作为输入，反转该字符串中的元音字母。
>
> ### 示例
>
> ```java
> 输入："leetcode"
> 输出："leotcede"
> ```
>
> ### 思路
>
> * **双指针**
>
> ### 代码
>
> ```java
> public String reverseVowels(String s) {
>     Set<Character> set = new HashSet<Character>(){{
>         add('a');
>         add('e');
>         add('i');
>         add('o');
>         add('u');
>         add('A');
>         add('E');
>         add('I');
>         add('O');
>         add('U');
>     }};
> 
>     StringBuilder builder = new StringBuilder(s);
> 
>     int i = 0;
>     int j = builder.length() - 1;
>     char temp = ' ';
>     while (i < j){
>         while (i < j && !set.contains(builder.charAt(i)))
>             i++;
>         while (i < j && !set.contains(builder.charAt(j)))
>             j--;
>         if (i<j){
>             temp = builder.charAt(i);
>             builder.setCharAt(i++, builder.charAt(j));
>             builder.setCharAt(j--,temp);
>         }
>     }
>     return builder.toString();
> }
> ```
>
> ****

## 392 判断子序列

> ### 题目
>
> * 给定字符串 **s** 和 **t** ，判断 **s** 是否为 **t** 的子序列。
> * 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
>
>
> ### 示例
>
> ```java
> 输入：s = "abc", t = "ahbgdc"
> 输出：true
> ```
>
> ### 思路
>
> * **双指针**
>
> ### 代码
>
> ```java
> public boolean isSubsequence(String s, String t) {
>     int lengthS = s.length();
>     int lengthT = t.length();
>     int i = 0;
>     int j = 0;
>     while (i < lengthS && j< lengthT){
>         if (s.charAt(i) == t.charAt(j)){
>             i++;
>             j++;
>         }else 
>             j++;
>     }
>     return i == lengthS;
> }
> ```
>
> ****

## 406 根据身高重建队列

> ### 题目
>
> * 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
> * 请你重新构造并返回输入数组 people 所表示的队列。返回的队列应该格式化为数组 queue ，其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
>
>
> ### 示例
>
> ```java
> 输入：people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
> 输出：[[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
> 解释：
> 编号为 0 的人身高为 5 ，没有身高更高或者相同的人排在他前面。
> 编号为 1 的人身高为 7 ，没有身高更高或者相同的人排在他前面。
> 编号为 2 的人身高为 5 ，有 2 个身高更高或者相同的人排在他前面，即编号为 0 和 1 的人。
> 编号为 3 的人身高为 6 ，有 1 个身高更高或者相同的人排在他前面，即编号为 1 的人。
> 编号为 4 的人身高为 4 ，有 4 个身高更高或者相同的人排在他前面，即编号为 0、1、2、3 的人。
> 编号为 5 的人身高为 7 ，有 1 个身高更高或者相同的人排在他前面，即编号为 1 的人。
> 因此 [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]] 是重新构造后的队列
> ```
>
> ### 思路
>
> * **贪心思想**
> * 按照 **身高从高到低，序号从低到高**排序，然后直接把编号当索引插入就行了
> * 原理是身高最高的排好后，无论是前面还是插入什么元素都对结果没有影响
>
> ### 代码
>
> ```java
> public int[][] reconstructQueue(int[][] people) {
>     Arrays.sort(people, new Comparator<int[]>() {
>         @Override
>         public int compare(int[] o1, int[] o2) {
>             if (o1[0] == o2[0])
>                 return o1[1] - o2[1];
>             else
>                 return o2[0] - o1[0];
>         }
>     });
> 
>     List<int[]> ans = new ArrayList<int[]>();
>     for (int[] person : people) {
>         ans.add(person[1], person);
>     }
>     return ans.toArray(new int[ans.size()][]);
> }
> ```
>
> ****

## 409 最长回文串

> ### 题目
>
> * 给定一个包含大写字母和小写字母的字符串，找到通过这些字母构造成的最长的回文串。
> * 在构造过程中，请注意区分大小写。比如 `"Aa"` 不能当做一个回文字符串。
>
> ### 示例
>
> ```java
> 输入: "abccccdd"
> 输出: 7
> 解释: 我们可以构造的最长的回文串是"dccaccd", 它的长度是 7
> ```
>
> ### 思路
>
> * 用`int[128]`存放每个字符出现的次数
> * 代码思路不是算最长有多长，而是计算 **最少需要删除多少个**
>
> ### 代码
>
> ```java
> public int longestPalindrome(String s) {
>     int[] arr = new int[128];
>     for(char c : s.toCharArray()) {
>         arr[c]++;
>     }
>     int count = 0;
>     for (int i : arr) {
>         count += (i % 2);
>     }
>     return count == 0 ? s.length() : (s.length() - count + 1);
> }
> ```
>
> ****

## 435 无重叠区间

> ### 题目
>
> * 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
>
> ### 示例
>
> ```java
> 输入: [ [1,2], [2,3], [3,4], [1,3] ]
> 输出: 1
> 解释: 移除 [1,3] 后，剩下的区间没有重叠
> ```
>
> ### 思路
>
> * **排序** + **贪心思想** 
>
> ### 代码
>
> ```java
> public int eraseOverlapIntervals(int[][] intervals) {
>     Arrays.sort(intervals, new Comparator<int[]>() {
>         @Override
>         public int compare(int[] o1, int[] o2) {
>             if(o1[1] > o2[1])
>                 return 1;
>             else if (o1[1] == o2[1])
>                 return 0;
>             else
>                 return -1;
>         }
>     });
> 
>     int length = intervals.length;
> 
>     if (length <= 1)
>         return 0;
>     if (length == 2)
>         return intervals[0][1] <= intervals[1][0] ? 0:1;
> 
>     int i = 0;
>     int j = 1;
>     int result = 0;
> 
>     while (j < length){
>         if (intervals[i][1] <= intervals[j][0]){
>             i = j;
>         }else {
>             result++;
>         }
>         j++;
>     }
>     return result;
> }
> ```
>
> ****

## 445 两数相加

> ### 题目
>
> * 给你两个 **非空** 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
> * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
>
> ### 示例
>
> ```java
> 输入：(7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
> 输出：7 -> 8 -> 0 -> 7
> ```
>
> ### 思路
>
> * **栈**
>
> ### 代码
>
> ```java
> public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
>     Stack<ListNode> stackL1 = new Stack<>();
>     Stack<ListNode> stackL2 = new Stack<>();
> 
>     while (l1 != null){
>         stackL1.push(l1);
>         l1 = l1.next;
>     }
>     while (l2 != null){
>         stackL2.push(l2);
>         l2 = l2.next;
>     }
>     ListNode result = null;
>     ListNode temp = null;
>     int remain = 0;
>     int sum;
>     int op1;
>     int op2;
>     while (!stackL1.isEmpty() || !stackL2.isEmpty()){
>         op1 = stackL1.isEmpty() ? 0 : stackL1.pop().val;
>         op2 = stackL2.isEmpty() ? 0 : stackL2.pop().val;
>         sum = (op1 + op2 + remain) % 10;
>         remain = (op1 + op2 + remain) / 10;
>         if (result == null)
>             result = new ListNode(sum,null);
>         else{
>             temp = new ListNode(sum,result);
>             result = temp;
>         }
>     }
>     if (remain != 0){
>         temp = new ListNode(remain,result);
>         result = temp;
>     }
>     return result;
> }
> ```
>
> ****

## 452 用最数量的箭引爆气球

> ### 示例
>
> ```java
> 输入：points = [[10,16],[2,8],[1,6],[7,12]]
> 输出：2
> 解释：对于该样例，x = 6 可以射爆 [2,8],[1,6] 两个气球，以及 x = 11 射爆另外两个气球
> ```
>
> ### 思路
>
> * **排序** + **贪心思想**
>
> ### 代码
>
> ```java
> public int findMinArrowShots(int[][] points) {
>     if (points.length == 0) {
>         return 0;
>     }
>     Arrays.sort(points, new Comparator<int[]>() {
>         public int compare(int[] point1, int[] point2) {
>             if (point1[1] > point2[1]) {
>                 return 1;
>             } else if (point1[1] < point2[1]) {
>                 return -1;
>             } else {
>                 return 0;
>             }
>         }
>     });
>     int pos = points[0][1];
>     int ans = 1;
>     for (int[] balloon: points) {
>         if (balloon[0] > pos) {
>             pos = balloon[1];
>             ++ans;
>         }
>     }
>     return ans;
> }
> ```
>
> ****

## 455 分发饼干

> ### 示例
>
> ```java
> 输入: g = [1,2,3], s = [1,1]
> 输出: 1
> 解释: 
> 你有三个孩子和两块小饼干，3个孩子的胃口值分别是：1,2,3。
> 虽然你有两块小饼干，由于他们的尺寸都是1，你只能让胃口值是1的孩子满足。
> 所以你应该输出1。
> ```
>
> ### 思路
>
> * **排序** + **贪心思想**
>
> ****

## 461 汉明距离

> ### 题目
>
> * 两个整数之间的 **汉明距离** 指的是这两个数字对应二进制位不同的位置的数目。
>
> ### 示例
>
> ```java
> 输入: x = 1, y = 4
> 输出: 2
> 解释:
> 1   (0 0 0 1)
> 4   (0 1 0 0)
>        ↑   ↑
> ```
>
> ### 思路
>
> * 直接 **模** 运算取低位看是否相同
>
> ### 代码
>
> ```java
> public int hammingDistance(int x, int y) {
>     int result = 0;
>     while (x != 0 || y != 0){
>         if (x % 2 != y % 2)
>             result++;
>         x >>= 1;
>         y >>= 1;
>     }
>     return result;
> }
> ```
> 
>****

## 485 最大连续1的个数

> ### 题目
>
> * 给定一个二进制数组， 计算其中最大连续 1 的个数。
>
> ### 示例
>
> ```java
> 输入：[1,1,0,1,1,1]
> 输出：3
> 解释：开头的两位和最后的三位都是连续 1 ，所以最大连续 1 的个数是 3.
> ```
>
> ### 思路
>
> * 维护一个当前最大值的变量
>
> ### 代码
>
> ```java
> public int findMaxConsecutiveOnes(int[] nums) {
>     int max = 0;
>     int temp = 0;
>     for (int num :nums){
>         if (num == 1)
>             temp++;
>         else {
>             max = Math.max(max,temp);
>             temp = 0;
>         }
>     }
>     return Math.max(max,temp);
> }
> ```
>
> ****

## 503 下一个更大的元素II

> ### 题目
>
> * 给定一个循环数组（最后一个元素的下一个元素是数组的第一个元素），输出每个元素的下一个更大元素。数字 x 的下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1。
>
>
> ### 示例
>
> ```java
> 输入: [1,2,1]
> 输出: [2,-1,2]
> 解释: 第一个 1 的下一个更大的数是 2；
> 数字 2 找不到下一个更大的数； 
> 第二个 1 的下一个最大的数需要循环搜索，结果也是 2。
> ```
>
> ### 思路
>
> * 维护一个**递减栈**
> * 第一遍正常遍历，并记录最大值
> * 第二遍当栈顶不是最大值时，将大于栈顶的元素压栈；当栈顶元素为最大值时，就全部出栈赋值为-1
> * 两遍遍历的巧妙做法是对index **取模**
>
> ### 代码
>
> ```java
> public int[] nextGreaterElements(int[] nums) {
>     int length = nums.length;
>     if(length == 0)
>         return new int[0];
>     Stack<Integer> stack = new Stack<>();
>     int max = nums[0];
>     int maxIndex = 0;
>     int currentNum = 0;
>     int peekIndex = 0;
>     int[] result = new int[length];
>     for (int i = 0;i < length;i++){
>         currentNum = nums[i];
>         if (currentNum > max){
>             max = currentNum;
>             maxIndex = i;
>         }
>         while (!stack.isEmpty() && currentNum > nums[stack.peek()]){
>             peekIndex = stack.pop();
>             result[peekIndex] = currentNum;
>         }
>         stack.push(i);
>     }
>     for (int i = 0;i < length;i++){
>         currentNum = nums[i];
>         if (currentNum <= nums[stack.peek()])
>             continue;
>         while (!stack.isEmpty() && currentNum > nums[stack.peek()]){
>             peekIndex = stack.pop();
>             result[peekIndex] = currentNum;
>         }
>         if (currentNum == max)
>             break;
>     }
>     while (!stack.isEmpty())
>         result[stack.pop()] = -1;
>     return result;
> }
> ```
>
> ****

## 513 找树左下角的值

> ### 题目
>
> * 给定一个二叉树，在树的最后一行找到最左边的值。
>
> ### 示例
>
> ```java
> 输入:
> 
>         1
>        / \
>       2   3
>      /   / \
>     4   5   6
>        /
>       7
> 
> 输出:
> 7
> ```
>
> ### 思路
>
> * **BFS** null指针作为层分界
> * **递归**
>
> ### 代码
>
> ```java
> //BFS
> public int findBottomLeftValue(TreeNode root) {
>     int result = root.val;
>     Queue<TreeNode> queue = new LinkedList<TreeNode>();
> 
>     queue.add(root);
>     queue.add(null);
>     TreeNode peek;
>     while (true){
>         if ((peek = queue.poll()) == null){
>             if (queue.isEmpty())
>                 break;
>             else {
>                 result = queue.peek().val;
>                 queue.add(null);
>             }
>         }else {
>             if (peek.left != null)
>                 queue.add(peek.left);
>             if (peek.right != null)
>                 queue.add(peek.right);
>         }
>     }
>     return result;
> }
> 
> //递归
> public int findBottomLeftValue(TreeNode root) {
>     if (root.left == null && root.right == null)
>         return root.val;
>     int leftLevel = treeLevel(root.left);
>     int rightLevel = treeLevel(root.right);
>     if (leftLevel >= rightLevel)
>         return findBottomLeftValue(root.left);
>     else
>         return findBottomLeftValue(root.right);
> }
> 
> private int treeLevel(TreeNode root){
>     if (root == null)
>         return 0;
>     else
>         return Math.max(treeLevel(root.left),treeLevel(root.right)) + 1;
> }
> ```
>
> ****

## 515 在每个树中找最大值

> ### 题目
>
> * 您需要在二叉树的每一行中找到最大的值。
>
> ### 示例
>
> ```java
> 输入: 
> 
>           1
>          / \
>         3   2
>        / \   \  
>       5   3   9 
> 
> 输出: [1, 3, 9]
> ```
>
> ### 思路
>
> * **BFS**
>
> ### 代码
>
> ```java
> public List<Integer> largestValues(TreeNode root) {
>     Queue<TreeNode> queue = new LinkedList<TreeNode>();
>     List<Integer> result = new ArrayList<Integer>();
> 
>     if (root == null)
>         return result;
> 
>     queue.add(root);
>     queue.add(null);
>     int tempMax = Integer.MIN_VALUE;
>     TreeNode currentTreeNode = null;
>     while (queue.peek() != null){
>         while ((currentTreeNode = queue.poll()) != null){
>             tempMax = Math.max(tempMax,currentTreeNode.val);
>             if (currentTreeNode.left != null)
>                 queue.add(currentTreeNode.left);
>             if (currentTreeNode.right != null)
>                 queue.add(currentTreeNode.right);
>         }
>         result.add(tempMax);
>         tempMax = Integer.MIN_VALUE;
>         queue.add(null);
>     }
>     return result;
> }
> ```
>
> ****

## 524 通过删除字符匹配到字典最长的单词

> ### 题目
>
> * 给定一个字符串和一个字符串字典，找到字典里面最长的字符串，该字符串可以通过删除给定字符串的某些字符来得到。如果答案不止一个，返回长度最长且字典顺序最小的字符串。如果答案不存在，则返回空字符串。
>
>
> ### 示例
>
> ```java
> 输入: s = "abpcplea", d = ["ale","apple","monkey","plea"]
> 输出: "apple"
> ```
>
> ### 思路
>
> * 按照 **长度递减，字典序递增** 的顺序排序
> * 然后顺序遍历，使用 **双指针** 返回
> * java里字典序比较方法是`compareTo()`
>
> ### 代码
>
> ```java
> public String findLongestWord(String s, List<String> dictionary) {
>     dictionary.sort(new Comparator<String>() {
>         @Override
>         public int compare(String o1, String o2) {
>             if (o1.length() > o2.length())
>                 return -1;
>             else if (o1.length() < o2.length())
>                 return 1;
>             else {
>                 return o1.compareTo(o2);
>             }
>         }
>     });
> 
>     int length = s.length();
>     for (int k = 0; k<dictionary.size();k++){
>         String current = dictionary.get(k);
>         int currentLength = current.length();
>         int i = 0;
>         int j = 0;
>         while (i < length && j < currentLength){
>             if (s.charAt(i) == current.charAt(j)){
>                 i++;
>                 j++;
>             }else
>                 i++;
>         }
>         if (j == currentLength)
>             return current;
>     }
>     return "";
> }
> ```
>
> ****

## 539 最小时间差

> ### 题目
>
> * 给定一个 24 小时制（小时:分钟 **"HH:MM"**）的时间列表，找出列表中任意两个时间的最小时间差并以分钟数表示。
>
> ### 示例
>
> ```java
> 输入：timePoints = ["23:59","00:00"]
> 输出：1
> ```
>
> ### 思路
>
> * **排序**，每一位的分钟权重数分别是`600、60、10、1`
> * 由于是 **循环数组** 还要考虑最后一位到第一位的间隔
>
> ### 代码
>
> ```java
> public int findMinDifference(List<String> timePoints) {
>     // 一天有 1440 分钟，如果 timePoints >= 1440 则表示有相等的时间，时间差为 0
>     if (timePoints.size() >= 1440) {
>         return 0;
>     }
>     // 用来存储每个时间的分钟
>     int[] array = new int[timePoints.size()];
>     for (int i = 0; i < timePoints.size(); i++) {
>         // 计算分钟
>         array[i] = minute(timePoints.get(i));
>     }
>     // 排序
>     Arrays.sort(array);
>     int min = Integer.MAX_VALUE;
>     for (int i = 1; i < array.length; i++) {
>         // 求出分钟差
>         min = Math.min(min, array[i] - array[i - 1]);
>         // 如果有最小分钟差，则直接返回
>         if (min == 0) {
>             return 0;
>         }
>     }
>     // 最大时间和最小时间的分钟差可能最小，需要判断一下
>     return Math.min(min, 1440 + array[0] - array[array.length - 1]);
> }
> 
> public int minute(String s) {
>     return s.charAt(0) * 600 + s.charAt(1) * 60 + s.charAt(3) * 10 + s.charAt(4);
> }
> ```
>
> ****

## 540 有序数组中的单一元素

> ### 题目
>
> * 给定一个只包含整数的有序数组，每个元素都会出现两次，唯有一个数只会出现一次，找出这个数。
>
> ### 示例
>
> ```java
> 输入: [1,1,2,3,3,4,4,8,8]
> 输出: 2
> ```
>
> ### 思路
>
> * **异或**
> * **二分**，但二分要考虑奇偶
>
> ### 代码
>
> ```java
> public int singleNonDuplicate(int[] nums) {
>     if (nums.length == 1)
>         return nums[0];
>     int i = 0;
>     int j = nums.length - 1;
>     int mid = 0;
>     boolean isOdd;
>     while (i < j){
>         mid = (i + j) / 2;
>         isOdd = (j - mid) % 2 == 1;
>         if (nums[mid] == nums[mid - 1]){
>             if (isOdd)
>                 i = mid + 1;
>             else
>                 j = mid;
>         }else if (nums[mid] == nums[mid + 1]){
>             if (isOdd)
>                 j = mid - 1;
>             else
>                 i = mid;
>         }else{
>             return nums[mid];
>         }
>     }
>     return nums[i];
> }
> ```
>
> ****

## 543 二叉树的直径

> ### 题目
>
> * 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
>
> ### 示例
>
> ```java
>           1
>          / \
>         2   3
>        / \     
>       4   5
> 返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
> ```
>
> ### 思路
>
> * **递归**
> * 递归思路看代码
>
> ### 代码
>
> ```java
> public int diameterOfBinaryTree(TreeNode root) {
>     if (root == null)
>         return 0;
>     else {
>         int current = maxLength(root.left) + maxLength(root.right);
>         int left = diameterOfBinaryTree(root.left);
>         int right = diameterOfBinaryTree(root.right);
>         return Math.max(current,Math.max(left,right));
>     }
> 
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

## 565 数组嵌套

> ### 题目
>
> * 索引从0开始长度为N的数组A，包含0到N - 1的所有整数。找到最大的集合S并返回其大小，其中 S[i] = {A[i], A[A[i]], A[A[A[i]]], ... }且遵守以下的规则。
> * 假设选择索引为i的元素A[i]为S的第一个元素，S的下一个元素应该是A[A[i]]，之后是A[A[A[i]]]... 以此类推，不断添加直到S出现重复的元素。
>
>
> ### 示例
>
> ```java
> 输入: A = [5,4,0,3,1,6,2]
> 输出: 4
> 解释: 
> A[0] = 5, A[1] = 4, A[2] = 0, A[3] = 3, A[4] = 1, A[5] = 6, A[6] = 2.
> 
> 其中一种最长的 S[K]:
> S[0] = {A[0], A[5], A[6], A[2]} = {5, 6, 2, 0}
> ```
>
> ### 思路
>
> * **快慢指针** ，此题类似于 **链表找环**
> * 维护一个`boolean[]`用于减少情况
>
> ### 代码
>
> ```java
> public int arrayNesting(int[] nums) {
>     int slow = 0;
>     int fast = 0;
>     int count = 0;
>     int result = 0;
>     boolean[] hasLooked = new boolean[nums.length];
>     for (int i = 0;i < nums.length;i++){
>         if (hasLooked[i])
>             continue;
>         hasLooked[i] = true;
>         slow = nums[i];
>         fast = nums[i];
>         count = 0;
>         do {
>             hasLooked[nums[slow]] = true;
>             hasLooked[nums[nums[fast]]] = true;
>             slow = nums[slow];
>             fast = nums[nums[fast]];
>             count++;
>         }while (slow != fast);
>         result = Math.max(result,count);
>     }
>     return result;
> }
> ```
>
> ****

## 566 重塑矩阵

> ### 题目
>
> * 
>
> ### 示例
>
> ```java
> 输入: 
> nums = [
>     [1,2],
>  	[3,4]
> ]
> r = 1, c = 4
> 输出: [[1,2,3,4]]
> 解释: 行遍历nums的结果是 [1,2,3,4]。新的矩阵是 1 * 4 矩阵, 用之前的元素值一行一行填充新矩阵。
> ```
>
> ### 思路
>
> * 用 **取模** 运算来递增结果矩阵的索引
>
> ### 代码
>
> ```java
> public int[][] matrixReshape(int[][] nums, int r, int c) {
>     if (nums == null || nums.length == 0 || nums[0].length == 0)
>         return nums;
> 
>     int row = nums.length;
>     int column = nums[0].length;
> 
>     if (r * c != row * column)
>         return nums;
> 
>     int[][] result = new int[r][c];
> 
>     int k = 0;
>     int l = 0;
>     for (int i = 0;i < row;i++)
>         for (int j = 0;j < column;j++){
>             result[k][l] = nums[i][j];
>             k += ++l / c;
>             l %= c;
>         }
>     return result;
> }
> ```
>
> ****

## 594 最长和谐子序列

> ### 题目
>
> * 和谐数组是指一个数组里元素的最大值和最小值之间的差别 **正好是 `1`** 。
> * 现在，给你一个整数数组 `nums` ，请你在所有可能的子序列中找到最长的和谐子序列的长度。
> * 数组的子序列是一个由数组派生出来的序列，它可以通过删除一些元素或不删除元素、且不改变其余元素的顺序而得到。
>
> ### 示例
>
> ```java
> 输入：nums = [1,3,2,2,5,2,3,7]
> 输出：5
> 解释：最长的和谐子序列是 [3,2,2,2,3]
>     
> 输入：nums = [1,2,3,4]
> 输出：2
> ```
>
> ### 思路
>
> * **HashMap** + **map.getOrDefault()**
>
> ### 代码
>
> ```java
> public int findLHS(int[] nums) {
>     Map<Integer, Integer> countForNum = new HashMap<>();
>     for (int num : nums)
>         countForNum.put(num, countForNum.getOrDefault(num, 0) + 1);
>     int longest = 0;
>     for (int num : countForNum.keySet()) 
>         if (countForNum.containsKey(num + 1))
>             longest = Math.max(longest, countForNum.get(num + 1) + countForNum.get(num));
>     return longest;
> }
> ```
>
> ****

## 605 种花问题

> ### 题目
>
> * 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
> * 给你一个整数数组  flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？能则返回 true ，不能则返回 false
>
>
> ### 示例
>
> ```java
> 输入：flowerbed = [1,0,0,0,1], n = 1
> 输出：true
> ```
>
> ### 思路
>
> * **贪心思想**
> * 但要注意开始的第一盆花
>
> ### 代码
>
> ```java
> public boolean canPlaceFlowers(int[] flowerbed, int n) {
>     int result = 0;
>     int length = flowerbed.length;
>     if (length == 1){
>         if (flowerbed[0] == 0)
>             result = 1;
>         return result >= n;
>     }
>     if (flowerbed[0] + flowerbed[1] == 0){
>         result++;
>         flowerbed[0] = 1;
>     }
>     int i = 1;
>     while (i < length -1){
>         if (flowerbed[i-1] + flowerbed[i] + flowerbed[i+1] == 0){
>             result++;
>             flowerbed[i] = 1;
>             i += 2;
>         }else {
>             i++;
>         }
>     }
>     if (flowerbed[length - 1] + flowerbed[length - 2] == 0)
>         result++;
>     return result >= n;
> }
> ```
>
> ****

## 633 平方数之和

> ### 题目
>
> * 给定一个非负整数 `c` ，你要判断是否存在两个整数 `a` 和 `b`，使得 `a * a + b * b = c` 。
>
> ### 示例
>
> ```java
> 输入：c = 5
> 输出：true
> 解释：1 * 1 + 2 * 2 = 5
> ```
>
> ### 思路
>
> * **双指针**
> * 右边界由 **Math.sqrt()** 获得
>
> ### 代码
>
> ```java
> public boolean judgeSquareSum(int c) {
>     int i = 0;
>     int j = (int) Math.sqrt(c);
>     int tempSum = 0;
>     while (i <= j){
>         tempSum = i * i + j * j;
>         if (tempSum == c)
>             return true;
>         else if (tempSum > c)
>             j--;
>         else 
>             i++;
>     }
>     return false;
> }
> ```
>
> ****

## 645 错误的集合

> ### 题目
>
> * 集合 s 包含从 1 到 n 的整数。不幸的是，因为数据错误，导致集合里面某一个数字复制了成了集合里面的另外一个数字的值，导致集合 丢失了一个数字 并且 有一个数字重复 。
>
> * 给定一个数组 `nums` 代表了集合 `S` 发生错误后的结果。
> * 请你找出重复出现的整数，再找到丢失的整数，将它们以数组的形式返回。
>
> ### 示例
>
> ```java
> 输入：nums = [1,2,2,4]
> 输出：[2,3]
> ```
>
> ### 思路
>
> * 使用 **HashSet**
>
> ### 代码
>
> ```java
> public int[] findErrorNums(int[] nums) {
>     int[] result = new int[2];
>     Set<Integer> set = new HashSet<Integer>();
>     for (int num : nums){
>         if (set.contains(num))
>             result[0] = num;
>         else
>             set.add(num);
>     }
>     for (int i = 1;i <= nums.length;i++){
>         if (!set.contains(i)){
>             result[1] = i;
>             return result;
>         }
>     }
>     return result;
> }
> ```
>
> ****

## 647 回文子串

> ### 题目
>
> * 给定一个字符串，你的任务是计算这个字符串中有多少个回文子串。
> * 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被视作不同的子串。
>
> ### 示例
>
> ```java
> 输入："aaa"
> 输出：6
> 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
> ```
>
> ### 思路
>
> * **每一个** 和 **每两个** 分别向两边遍历，外部维护一个全局结果
>
> ### 代码
>
> ```java
> private int cnt = 0;
> 
> public int countSubstrings(String s) {
>     for (int i = 0; i < s.length(); i++) {
>         extendSubstrings(s, i, i);     // 奇数长度
>         extendSubstrings(s, i, i + 1); // 偶数长度
>     }
>     return cnt;
> }
> 
> private void extendSubstrings(String s, int start, int end) {
>     while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
>         start--;
>         end++;
>         cnt++;
>     }
> }
> ```
>
> ****

## 680 验证回文字符串II

> ### 题目
>
> * 给定一个非空字符串 `s`，**最多**删除一个字符。判断是否能成为回文字符串。
>
> ### 示例
>
> ```java
> 输入: "abca"
> 输出: True
> 解释: 你可以删除c字符
> ```
>
> ### 思路
>
> * **双指针**从两边向中间遍历
> * 如果不相等则分两种情况讨论，分别删除左指针和删除右指针
>
> ### 代码
>
> ```java
> public boolean validPalindrome(String s) {
>     int i = 0;
>     int j = s.length() - 1;
> 
>     while (i < j){
>         if (s.charAt(i) == s.charAt(j)){
>             i++;
>             j--;
>         }else {
>             return valid(s,i,j-1) || valid(s,i+1,j);
>         }
>     }
>     return true;
> }
> 
> private boolean valid(String s, int left,int right){
>     while (left < right){
>         if (s.charAt(left) == s.charAt(right)){
>             left++;
>             right--;
>         }else {
>             return false;
>         }
>     }
>     return true;
> }
> ```
>
> ****

## 695 岛屿的最大面积

> ### 题目
>
> * 给定一个包含了一些 `0` 和 `1` 的非空二维数组 `grid` 。
> * 一个 岛屿 是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在水平或者竖直方向上相邻。你可以假设 grid 的四个边缘都被 0（代表水）包围着。
> * 找到给定的二维数组中最大的岛屿面积。(如果没有岛屿，则返回面积为 `0` 。)
>
> ### 示例
>
> ```java
> [[0,0,1,0,0,0,0,1,0,0,0,0,0],
>  [0,0,0,0,0,0,0,1,1,1,0,0,0],
>  [0,1,1,0,1,0,0,0,0,0,0,0,0],
>  [0,1,0,0,1,1,0,0,1,0,1,0,0],
>  [0,1,0,0,1,1,0,0,1,1,1,0,0],
>  [0,0,0,0,0,0,0,0,0,0,1,0,0],
>  [0,0,0,0,0,0,0,1,1,1,0,0,0],
>  [0,0,0,0,0,0,0,1,1,0,0,0,0]]
> 
> 对于上面这个给定矩阵应返回 6。注意答案不应该是 11 ，因为岛屿只能包含水平或垂直的四个方向的 1 
> ```
>
> ### 思路
>
> * **DFS**向四个方向遍历，遍历过了就将1设置成0
>
> ### 代码
>
> ```java
> private int m, n;
> private int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
> 
> public int maxAreaOfIsland(int[][] grid) {
>     if (grid == null || grid.length == 0) {
>         return 0;
>     }
>     m = grid.length;
>     n = grid[0].length;
>     int maxArea = 0;
>     for (int i = 0; i < m; i++) {
>         for (int j = 0; j < n; j++) {
>             maxArea = Math.max(maxArea, dfs(grid, i, j));
>         }
>     }
>     return maxArea;
> }
> 
> private int dfs(int[][] grid, int r, int c) {
>     if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
>         return 0;
>     }
>     grid[r][c] = 0;
>     int area = 1;
>     for (int[] d : direction) {
>         area += dfs(grid, r + d[0], c + d[1]);
>     }
>     return area;
> }
> ```
>
> ****

## 696 计算二进制子串

> ### 题目
>
> * 给定一个字符串 `s`，计算具有相同数量 0 和 1 的非空（连续）子字符串的数量，并且这些子字符串中的所有 0 和所有 1 都是连续的。
> * 重复出现的子串要计算它们出现的次数。
>
> ### 示例
>
> ```java
> 输入: "00110011"
> 输出: 6
> 解释: 有6个子串具有相同数量的连续1和0：“0011”，“01”，“1100”，“10”，“0011” 和 “01”。
> 请注意，一些重复出现的子串要计算它们出现的次数。
> 另外，“00110011”不是有效的子串，因为所有的0（和1）没有组合在一起。
> ```
>
> ### 思路
>
> * 每两个字符向**两边**遍历
>
> ### 代码
>
> ```java
> public int countBinarySubstrings(String s) {
>     int preLen = 0, curLen = 1, count = 0;
>     for (int i = 1; i < s.length(); i++) {
>         if (s.charAt(i) == s.charAt(i - 1)) {
>             curLen++;
>         } else {
>             preLen = curLen;
>             curLen = 1;
>         }
> 
>         if (preLen >= curLen) {
>             count++;
>         }
>     }
>     return count;
> }
> ```
>
> ****

## 696 数组的度

> ### 题目
>
> * 给定一个非空且只包含非负数的整数数组 `nums`，数组的度的定义是指数组里任一元素出现频数的最大值。
> * 你的任务是在 `nums` 中找到与 `nums` 拥有相同大小的度的最短连续子数组，返回其长度。
>
> ### 示例
>
> ```java
> 输入：[1, 2, 2, 3, 1]
> 输出：2
> 解释：
> 输入数组的度是2，因为元素1和2的出现频数最大，均为2.
> 连续子数组里面拥有相同度的有如下所示:
> [1, 2, 2, 3, 1], [1, 2, 2, 3], [2, 2, 3, 1], [1, 2, 2], [2, 2, 3], [2, 2]
> 最短连续子数组[2, 2]的长度为2，所以返回2.
> ```
>
> ### 思路
>
> * **HashMap**
> * 只不过map的value属性要设置成`int[]`存放 **起始**和 **终止**的索引值
>
> ### 代码
>
> ```java
> public int findShortestSubArray(int[] nums) {
>     Map<Integer, int[]> map = new HashMap<Integer, int[]>();
>     int n = nums.length;
>     for (int i = 0; i < n; i++) {
>         if (map.containsKey(nums[i])) {
>             map.get(nums[i])[0]++;
>             map.get(nums[i])[2] = i;
>         } else {
>             map.put(nums[i], new int[]{1, i, i});
>         }
>     }
>     int maxNum = 0, minLen = 0;
>     for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
>         int[] arr = entry.getValue();
>         if (maxNum < arr[0]) {
>             maxNum = arr[0];
>             minLen = arr[2] - arr[1] + 1;
>         } else if (maxNum == arr[0]) {
>             if (minLen > arr[2] - arr[1] + 1) {
>                 minLen = arr[2] - arr[1] + 1;
>             }
>         }
>     }
>     return minLen;
> }
> ```
>
> ****

## 725 分隔链表

> ### 题目
>
> * 给定一个头结点为 `root` 的链表, 编写一个函数以将链表分隔为 `k` 个连续的部分。
> * 每部分的长度应该尽可能的相等: 任意两部分的长度差距不能超过 1，也就是说可能有些部分为 null。
> * 这k个部分应该按照在链表中出现的顺序进行输出，并且排在前面的部分的长度应该大于或等于后面的长度。
>
> ### 示例
>
> ```java
> 输入: 
> root = [1, 2, 3], k = 5
> 输出: [[1],[2],[3],[],[]]
> ```
>
> ### 思路
>
> * 先计算出总长度，在计算出每部分的长度
> * 最后按照长度截断
>
> ### 代码
>
> ```java
> public ListNode[] splitListToParts(ListNode root, int k) {
>     int count = 0;
>     ListNode rootMark = root;
>     while (root != null){
>         count++;
>         root = root.next;
>     }
>     int quotient = count / k;
>     int remain = count % k;
>     int[] nums = new int[k];
>     for (int i = 0;i < k;i++)
>         nums[i] = remain-- > 0 ? quotient + 1 : quotient;
>     ListNode[] nodes = new ListNode[k];
> 
>     ListNode current = rootMark;
>     ListNode temp = null;
>     for (int i = 0;i < k;i++){
>         nodes[i] = current;
>         while (nums[i]-- > 1)
>             current = current.next;
>         if (current == null)
>             break;
>         temp = current;
>         current = current.next;
>         temp.next = null;
>     }
>     return nodes;
> }
> ```
>
> ****

## 739 每日温度

> ### 题目
>
> * 请根据每日 `气温` 列表，重新生成一个列表。对应位置的输出为：要想观测到更高的气温，至少需要等待的天数。如果气温在这之后都不会升高，请在该位置用 `0` 来代替。
>
> ### 示例
>
> ```java
> 输入：temperatures = [73, 74, 75, 71, 69, 72, 76, 73]
> 输出：[1, 1, 4, 2, 1, 1, 0, 0]
> ```
>
> ### 思路
>
> * 维护一个**递减栈**
> * 压栈的是 **索引**，温度的大小是 **索引 + 数组**求出来的
>
> ### 代码
>
> ```java
> public int[] dailyTemperatures(int[] t) {
>     int length = t.length;
>     int[] ans = new int[length];
>     Deque<Integer> stack = new LinkedList<Integer>();
>     int temperature;
>     int prevIndex;
>     for (int i = 0; i < length; i++) {
>         temperature = t[i];
>         while (!stack.isEmpty() && temperature > t[stack.peek()]) {
>             prevIndex = stack.pop();
>             ans[prevIndex] = i - prevIndex;
>         }
>         stack.push(i);
>     }
>     return ans;
> }
> ```
>
> ****

## 744 寻找比目标字母大的最小字母

> ### 题目
>
> * 给你一个排序后的字符列表 `letters` ，列表中只包含小写英文字母。另给出一个目标字母 `target`，请你寻找在这一有序列表里比目标字母大的最小字母
> * 在比较时，字母是依序循环出现的。
>
> ### 示例
>
> ```java
> 输入:
> letters = ["c", "f", "j"]
> target = "a"
> 输出: "c"
> ```
>
> ### 思路
>
> * **二分法**
>
> ### 代码
>
> ```java
> public char nextGreatestLetter(char[] letters, char target) {
>     int i = 0;
>     int j = letters.length - 1;
>     int mid;
>     char midChar;
>     while (i <= j){
>         mid = (i + j) / 2;
>         midChar = letters[mid];
>         if (midChar <= target)
>             i = ++mid;
>         else
>             j = --mid;
>     }
>     int tempMax = Math.max(i,j);
>     return tempMax >= letters.length ? letters[0] : letters[tempMax];
> }
> ```
>
> ****

## 763 划分字母区间

> ### 题目
>
> * 字符串 `S` 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
>
> ### 示例
>
> ```java
> 输入：S = "ababcbacadefegdehijhklij"
> 输出：[9,7,8]
> 解释：
> 划分结果为 "ababcbaca", "defegde", "hijhklij"。
> 每个字母最多出现在一个片段中。
> 像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
> ```
>
> ### 思路
>
> * `s.lastIndexof()`
>
> ### 代码
>
> ```java
> public List<Integer> partitionLabels(String s) {
>     int length = s.length();
>     int left = 0;
>     int right = 0;
>     int index = 0;
>     List<Integer> list = new ArrayList<Integer>();
>     while (left < length){
>         right = Math.max(right,s.lastIndexOf(s.charAt(left)));
>         if (left == right){
>             list.add(left + 1 -index);
>             index = ++left;
>             continue;
>         }
>         if (left < length)
>             left++;
>     }
>     return list;
> }
> ```
>
> ****

## 766 托普利茨矩阵

> ### 题目
>
> * 给你一个 `m x n` 的矩阵 `matrix` 。如果这个矩阵是托普利茨矩阵，返回 `true` ；否则，返回 `false` *。*
> * 如果矩阵上每一条由左上到右下的对角线上的元素都相同，那么这个矩阵是 **托普利茨矩阵** 。
>
> ### 示例
>
> ```java
> 输入：matrix = [
>     [1,2,3,4],
>     [5,1,2,3],
>     [9,5,1,2]
> ]
> 输出：true
> 解释：在上述矩阵中, 其对角线为: 
> "[9]", "[5, 5]", "[1, 1, 1]", "[2, 2, 2]", "[3, 3]", "[4]"。 
> 各条对角线上的所有元素均相同, 因此答案是 True 
> ```
>
> ### 思路
>
> * 顺序遍历一遍
>
> ### 代码
>
> ```java
> public boolean isToeplitzMatrix(int[][] matrix) {
>     int row = matrix.length;
>     int column = matrix[0].length;
> 
>     for (int i = 0;i < row;i++)
>         for (int j = 0;j < column;j++)
>             if (i > 0 && j > 0 && matrix[i][j] != matrix[i - 1][j - 1])
>                 return false;
>     return true;
> }
> ```
>
> ****

## 769 最多能完成排序的块

> ### 题目
>
> * 数组arr是[0, 1, ..., arr.length - 1]的一种排列，我们将这个数组分割成几个“块”，并将这些块分别进行排序。之后再连接起来，使得连接的结果和按升序排序后的原数组相同。
>
> * 我们最多能将数组分成多少块？
>
> ### 示例
>
> ```java
> 输入: arr = [1,0,2,3,4]
> 输出: 4
> 解释:
> 我们可以把它分成两块，例如 [1, 0], [2, 3, 4]。
> 然而，分成 [1, 0], [2], [3], [4] 可以得到最多的块数。
> ```
>
> ### 思路
>
> * **双指针**
>
> ### 代码
>
> ```java
> public int maxChunksToSorted(int[] arr) {
>     if (arr == null) return 0;
>     int ret = 0;
>     int right = arr[0];
>     for (int i = 0; i < arr.length; i++) {
>         right = Math.max(right, arr[i]);
>         if (right == i) ret++;
>     }
>     return ret;
> }
> ```
>
> ****

## 841 钥匙和房间

> ### 题目
>
> * 有`n`个房间，房间按从`0`到`n - 1`编号。最初，除`0`号房间外的其余所有房间都被锁住。你的目标是进入所有的房间。然而，你不能在没有获得钥匙的时候进入锁住的房间
> * 当你进入一个房间，你可能会在里面找到一套不同的钥匙，每把钥匙上都有对应的房间号，即表示钥匙可以打开的房间。你可以拿上所有钥匙去解锁其他房间
> * 给你一个数组 `rooms` 其中 `rooms[i]` 是你进入 `i` 号房间可以获得的钥匙集合。如果能进入**所有**房间返回 `true`，否则返回 `false`。
>
> ### 示例
>
> ```java
> 输入：rooms = [[1,3],[3,0,1],[2],[0]]
> 输出：false
> 解释：我们不能进入 2 号房间。
> ```
>
> ### 思路
>
> * 这道题是找是否存在一条路径，而且已经走过的房间就没有必要在走了，因此非常适合使用`BFS`遍历
>
> ### 代码
>
> ```java
> public boolean canVisitAllRooms(List<List<Integer>> rooms) {
>   int roomSize = rooms.size();
>   Queue<Integer> queue = new LinkedList<>();
>   queue.add(0);
> 
>   int visitedCount = 0;
>   boolean[] visited = new boolean[roomSize];
> 
>   while (!queue.isEmpty()) {
>     Integer pop = queue.poll();
>     if (visited[pop]) {
>       continue;
>     }
>     if (++visitedCount == roomSize) {
>       return true;
>     }
>     visited[pop] = true;
>     for (Integer value : rooms.get(pop)) {
>       queue.add(value);
>     }
>   }
>   return false;
> }
> ```
>
> ****

## 1047 删除字符串中所有相邻的重复项

> ### 题目
>
> * 给出由小写字母组成的字符串 `S`，**重复项删除操作**会选择两个相邻且相同的字母，并删除它们。
> * 在 S 上反复执行重复项删除操作，直到无法继续删除。
> * 在完成所有重复项删除操作后返回最终的字符串。答案保证唯一。
>
> ### 示例
>
> ```java
> 输入："abbaca"
> 输出："ca"
> 解释：
> 例如，在 "abbaca" 中，我们可以删除 "bb" 由于两字母相邻且相同，这是此时唯一可以执行删除操作的重复项。之后我们得到字符串 "aaca"，其中又只有 "aa" 可以执行重复项删除操作，所以最后的字符串为 "ca"。
> ```
>
> ### 思路
>
> * **栈**
>
> ### 代码
>
> ```java
> public String removeDuplicates(String s) {
>     int length = s.length();
>     StringBuilder builder = new StringBuilder(s);
> 
>     Stack<Character> stack = new Stack<>();
> 
>     for (int i = 0; i < s.length(); i++){
>         if (stack.isEmpty())
>             stack.push(builder.charAt(i));
>         else if (stack.peek().equals(builder.charAt(i)))
>             stack.pop();
>         else
>             stack.push(builder.charAt(i));
>     }
>     builder.replace(0,builder.length(),"");
>     while (!stack.isEmpty())
>         builder.append(stack.pop());
>     return builder.reverse().toString();
> }
> ```
>
> ****

## 1091 二进制矩阵中的最短路径

> ### 题目
>
> * 给你一个 `n x n` 的二进制矩阵 `grid` 中，返回矩阵中最短 **畅通路径** 的长度。如果不存在这样的路径，返回 `-1` 。
> * 二进制矩阵中的 畅通路径 是一条从 **左上角** 单元格（即，`(0, 0)`）到 **右下角** 单元格（即，`(n - 1, n - 1)`）的路径，该路径同时满足下述要求：
>   * 路径途经的所有单元格都的值都是 `0` 。
>   * 路径中所有相邻的单元格应当在 **8 个方向之一** 上连通
> * **畅通路径的长度** 是该路径途经的单元格总数。
>
> ### 示例
>
> ```java
> 输入：grid = [
>     [0,0,0],
>     [1,1,0],
>     [1,1,0]
> ]
> 输出：4
> ```
>
> ### 思路
>
> * **BFS**遍历
> * 求最短路径一般都是优先使用`BFS`。因为`BFS`遍历一旦找到就直接返回，其他一切都不用管
> * 为什么要将`grid[i][j] == 1`置为1？因为`BFS`不会走回头路
> * 为什么中间就可以直接返回，都不用比较的吗？因为广度就是最短路径，一旦找到就是最短
> * 此问题比较棘手的是如何记录每遍历一层使`++resultPathLength`
>
> ### BFS求最短路径
>
> * 核心思想是找到一个就是最短，不用继续找了。
> * 在每一层的末尾插入一个`null`，标志一层的结束，根据这个标志可以记住遍历了多少层
> * `BFS`不会走回头路，因此遍历完置成`visited`就行了
>
> ### 代码
>
> ```java
> //不知道为什么会超时
> public int shortestPathBinaryMatrix(int[][] grids) {
>     int result = 0;
>     int length = grids.length;
>     Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
> 
>     int[][] directions = new int[][] {
>       {-1, -1},
>       {0, -1},
>       {1, -1},
>       {1, 0},
>       {1, 1},
>       {0, 1},
>       {-1, 1},
>       {-1, 0}
>     };
> 
>     if (grids[0][0] == 1) {
>       return -1;
>     }
> 
>     queue.add(new Pair<>(0,0));
>     queue.add(null);
> 
>     while (queue.size() != 1) {
>       ++result;
>       while (queue.peek() != null) {
>         Pair<Integer, Integer> current = queue.poll();
>         int currentX = current.getKey();
>         int currentY = current.getValue();
>         if (currentX == length - 1 && currentY == length - 1) {
>           return result;
>         }
>         grids[currentX][currentY] = 1;
> 
>         for (int[] direction : directions) {
>           int dx = currentX + direction[0];
>           int dy = currentY + direction[1];
>           if (dx >= 0 && dx < length && dy >= 0 && dy < length && grids[dx][dy] == 0) {
>             queue.add(new Pair<>(dx,dy));
>           }
>         }
>       }
>       queue.add(queue.poll());
>     }
>     return -1;
> }
> ```
>
> ****

## 1513 仅含1的子串数

> ### 题目
>
> * 给你一个二进制字符串 `s`（仅由 '0' 和 '1' 组成的字符串）。
> * 返回所有字符都为 1 的子字符串的数目。
> * 由于答案可能很大，请你将它对 10^9 + 7 取模后返回。
>
> ### 示例
>
> ```java
> 输入：s = "0110111"
> 输出：9
> 解释：共有 9 个子字符串仅由 '1' 组成
> "1" -> 5 次
> "11" -> 3 次
> "111" -> 1 次
> ```
>
> ### 思路
>
> * 找到连续出现1的个数，使用等差数列公式求结果
> * 结果对题目要求数字取模
>
> ### 代码
>
> ```java
> public int numSub(String s) {
>     final int MODULO = 1000000007;
>     long total = 0;
>     int length = s.length();
>     long consecutive = 0;
>     for (int i = 0; i < length; i++) {
>         char c = s.charAt(i);
>         if (c == '0') {
>             total += consecutive * (consecutive + 1) / 2;
>             total %= MODULO;
>             consecutive = 0;
>         } else {
>             consecutive++;
>         }
>     }
>     total += consecutive * (consecutive + 1) / 2;
>     total %= MODULO;
>     return (int) total;
> }
> ```
>
> ****

## 1647 字符频次唯一的最小删除次数

> ### 题目
>
> * 如果字符串 `s` 中 **不存在** 两个不同字符 **频次** 相同的情况，就称 `s` 是 **优质字符串** 。
> * 给你一个字符串 `s`，返回使 `s` 成为 **优质字符串** 需要删除的 **最小** 字符数。
> * 字符串中字符的 **频次** 是该字符在字符串中的出现次数。例如，在字符串 `"aab"` 中，`'a'` 的频次是 `2`，而 `'b'` 的频次是 `1` 
> * 提示：字符串只包含小写字母
>
> ### 示例
>
> ```java
> 输入：s = "aaabbbcc"
> 输出：2
> 解释：可以删除两个 'b' , 得到优质字符串 "aaabcc" 。
> 另一种方式是删除一个 'b' 和一个 'c' ，得到优质字符串 "aaabbc" 
> ```
>
> ### 思路
>
> * 思路前提：因为只包含小写字母，因此使用`int[] counts = new int[26]`来存频率
> * 思路一 —— 排序
>   * 对`counts`进行排序
>   * 使用**贪心思想**尽量保留**高频**字符，然后对低频相同字符递删
>
> * 思路二 —— 不排序，去重
>   * 使用`hashset`来进行去重，核心思想是只遍历一遍
>   * 往`hashset`里面放之前的频率。如果一个待放入的频率已有，则待放入的频率递减后尝试在放入，然后`ans++`
>   * 最后的`ans`就是结果
>   * 核心思想也是`贪心`，和上面排序思路不同的是排序想尽量保持大的，但hash是先来就不用动了，其他人来递减
>
>
> ### 代码
>
> ```java
> //排序
> public int minDeletions(String s) {
>     int[] counts = new int[26];
>     for (char temp : s.toCharArray())
>         counts[temp - 'a']++;
> 
>     Arrays.sort(counts);
>     int currentMaxTime = counts[counts.length - 1] + 1;
>     int result = 0;
>     for (int i = counts.length - 1;i > -1 && counts[i] > 0;i--){
>         if (counts[i] < currentMaxTime)
>             currentMaxTime = counts[i];
>         else {
>             currentMaxTime = currentMaxTime > 0 ? currentMaxTime - 1 : 0;
>             result += counts[i] - currentMaxTime;
>         }
>     }
>     return result;
> }
> 
> //hash
> public int minDeletions(String s) {
>   int[] a = new int[26];
>   char[] cs = s.toCharArray();
>   for (char c : cs) a[c - 'a'] ++;// 统计字母个数
> 
>   Set<Integer> h = new HashSet<Integer>();
>   int res = 0;
>   for (int i : a) {
>     if (i != 0) {               // 有数目才进行判断
>       while (h.contains(i)) { // set已经包含就自减
>         i --;
>         res ++;
>       }
>       if (i != 0) h.add(i);   // 自减到0时，表示完全删除了某个字母，不能加入set中
>     }
>   }
>   return res;
> }
> ```
>
> ****

## 面试题 17.20 连续中值

> ### 题目
>
> * 就是找一串数字的中位数
>
> ### 示例
>
> ### 思路
>
> * 维护两个堆，分别是左边一个大根堆，右边一个小根堆
> * 对于一个新的元素。如果小于左边的最大值就放在左边，如果大于右边的最小值就放到右边。
> * 放好后在调节左右两个堆的元素个数，使其要么相等，要么相差1
>
>
> ### 代码
>
> ```java
> 
> ```
>
> ****

## 面试题 17.21 直方图的水量

> ### 题目
>
> * 给定一个直方图(也称柱状图)，假设有人从上面源源不断地倒水，最后直方图能存多少水量?直方图的宽度为 1
>
> ### 示例
>
> ![直方图示例](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2018/10/22/rainwatertrap.png "直方图示例")
>
> ```java
> 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
> 输出: 6
> ```
>
> ### 思路
>
> * 思路一 —— `每一列来看`
>   * 对于每一列来说，这一列能装多少水。取决于它的左边和右边最高的柱子。
>   * 即`volume = min(leftMax, rightMax) - selfHeight`
>   * 即问题转换成了求每个坐标左边和右边的最大高度
>   * 因此需要从左遍历一次，从右到左遍历一次。共遍历两次，中途用`动态规划`来求最大值
> * 思路二 —— `水量 = 总体积 - 柱子体积`
>   * 对于`level 1`第一层来说，体积是`11`；第二层是`8`，第三层是`1`
>   * 至于如何求体积，则是用双指针，从两边向中间遍历
>
> * 思路三 —— `单调栈`
>   * 存放内容：`下标`，用于计算水量
>   * 顺序依据：`柱高`，从栈底到栈顶柱子的高度依次减少
>   * 思路就是每增加一列柱子，能增加多少水量。使用单调栈只需要遍历一遍。
>   * 要想维成水量必须要有三根柱子，即当遍历到`i`时栈里肯定要有`2`个元素
>
>
> ### 代码
>
> ```java
> public int trap(int[] height) {
>   int ans = 0;
>   Deque<Integer> stack = new LinkedList<Integer>();
>   int n = height.length;
>   for (int i = 0; i < n; ++i) {
>     while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
>       int top = stack.pop();
>       if (stack.isEmpty()) {
>         break;
>       }
>       int left = stack.peek();
>       int currWidth = i - left - 1;
>       int currHeight = Math.min(height[left], height[i]) - height[top];
>       ans += currWidth * currHeight;
>     }
>     stack.push(i);
>   }
>   return ans;
> }
> ```
>
> ****

## 面试题 17.23 最大黑方阵

> ### 题目
>
> * 给定一个方阵，其中每个单元(像素)非黑即白。设计一个算法，找出 4 条边皆为黑色像素的最大子方阵
> * 返回一个数组`[r, c, size]`，其中`r, c`分别代表子方阵左上角的行号和列号，`size`是子方阵的边长。若有多个满足条件的子方阵，返回`r`最小的，若`r`相同，返回`c`最小的子方阵。若无满足条件的子方阵，返回空数组
>
> ### 示例
>
> ```java
> 输入:
> [
>    [1,0,1],
>    [0,0,1],
>    [0,0,1]
> ]
> 输出: [1,0,2]
> 解释: 输入中 0 代表黑色，1 代表白色，标粗的元素即为满足条件的最大子方阵
> ```
>
> ### 思路
>
> * 思路一
>   * 从左到右，从上到小进行遍历。`1`根本不管，只用看`0`。记录下此时当前最大的黑方阵`size`，尝试找一个更大的
>   * 找到更大的方法就是当前是`0`，依次判断四条边
>
> * 思路二
>   * 上面这个思路会导致同一个元素被多次遍历，因此有优化空间，优化空间就是减少遍历
>   * 思路是存起来。建立一个二维数组，数组的元素是一个`pair`，`first`表示从左到右最多连续有多少个零，`second`是从右到左最多连续有多少个零
>   * 这样一下就能判断两条边，另外两条边也能通过移动索引查看得到
>   * 而且遍历获得`pair`的时候可以采用从下到上，从右到左的方式。这样还能使用动态规划
>
>
> ### 代码
>
> ```java
> 
> ```
>
> ****

## 面试题 17.26 稀疏相似度

> ### 题目
>
> * 两个数组中有相同的元素则认为这两个数组有相似度，`相似度` = `交集元素个数`/`并集元素个数`
> * 现在有一个二维数组，列出所有相似度大于零的组合。并按`0,1: 0.2500`格式输出
>
> ### 示例
>
> ```java
> 输入: 
> [
>   [14, 15, 100, 9, 3],
>   [32, 1, 9, 3, 5],
>   [15, 29, 2, 6, 8, 7],
>   [7, 10]
> ]
> 输出:
> [
>   "0,1: 0.2500",
>   "0,2: 0.1000",
>   "2,3: 0.1429"
> ]
> ```
>
> ### 思路
>
> * 核心就是求任意两个数组的交集元素的个数，但是是任意两数组之间，一个数组会和其他所有数组进行比较
> * 思路一
>   * 在进行比较时遍历将一个数组`hash`化，再遍历另一个数组判断是否`contain`
>   * 如果有`n`个数组，会进行`n-1`次遍历哈希化，会进行`(n - 1)*n/2`次遍历判断`contain`
>
> * 思路二
>   * 将n个数组差不多每个遍历一遍排好序
>   * 使用双指针法任意两个数组之间求交集个数
>
> * 思路三
>   * 这道题的核心不是求任意两个数组之间有多少个相同元素。而是求当有相同元素时，它来自那两个组，不断累积两个组之间的相同答案就是结果
>   * 有一个map定义如下：`map<int, vector<int>>`，key是数组中的元素值，value是这个值来自那个数组
>   * 比较糟糕的思路是得到最终`map`，再n个数组每个遍历一遍找结果。其实完全可以中途记录结果，一步步把结果累积起来
>   * 即设置一个`vector<vector<int>>`来存结果，`vector[i][j]`表示两个数组的交集个数
>
>
> ### 代码
>
> ```java
> public List<String> computeSimilarities(int[][] docs) {
>   List<String> ans = new ArrayList<>();
>   Map<Integer, List<Integer>> map = new HashMap<>();
>   int[][] help = new int[docs.length][docs.length];
>   for (int i = 0; i < docs.length; i++) {
>     for (int j = 0; j < docs[i].length; j++) {
>       List<Integer> list = map.get(docs[i][j]);
>       if (list == null) {
>         list = new ArrayList<>();
>         map.put(docs[i][j], list);
>       } else {
>         for (Integer k : list) {
>           help[k][i]++;
>         }
>       }
>       list.add(i);
>     }
>   }
> 
>   for (int i = 0;i < docs.length;i++) {
>     for (int j = i + 1; j < docs.length;j++) {
>       if (help[i][j] != 0) {
>         ans.add(i + "," + j + ": " + String.format("%.4f", (double) help[i][j] / (docs[i].length + docs[j].length - help[i][j])));
>       }
>     }
>   }
>   return ans;
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

