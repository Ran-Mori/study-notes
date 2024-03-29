# 数字图像处理

## 第一节课

### 图形和图像的区别

* 图形是计算机画出来的。计算机图形学主要研究图形
* 图像是像素矩阵。数字图像处理主要研究图像。

### 视频与图像的关系

* 视频是图像加了一个时间维度

### 数字图像处理的三个维度

* 初级   图像->图像
* 中级   图像->特征  
* 高级   图像->理解 （涉及计算机视觉，AI）

### 什么是数字图像处理

* 各种各样的处理都是数字图像处理
* 数字处理的不一定有标准的评判标准

### 色彩模型

* 三原色是 **红绿蓝**
* 255是白，0是黑
* 印刷使用的是色彩相减模型。因为是白光吸收了某一种光后反射的光才是人眼看到的光，刚好是相减。



## 第一章 ：绪论

### 数字图像定义

* 像素坐标和灰度值都是离散值的图像

### 数字图像处理定义

* 借助计算机来处理数字图像

### 数字图像的来源

* 所有频率的电磁波都可以成像，并非只有可见光才行

### 处理层次的定义

* 低级
  * 对图像的降噪，增强，锐化等


* 中级
  * 如分割图像，提取图像的特征


* 高级
  * 理解图像——计算机视觉

### 数字图像处理基本步骤

* 图像增强概念

  * 对图像进行某些操作，使图像操作后的结果在 **特定** 的应用中比原始图像更容易处理

  * 是一种 **主观性** 处理


* 图像复原概念

  * 对图像进行某些操作，使图像退化到原来的状态

  * 是一种 **客观性** 处理


* 图像分割

  * 将图像划分成它的组成部分或者目标

  * 分割越准确，识别越成功


* 图像识别
  * 基与目标描述给目标赋予标志的过程


* 知识库

  * 先验知识

  * 和数字图像处理各步骤是 **双箭头** 关系

## 第二章：数字图像基础

### 人眼结构

* 锥状体

  * 集中分布于中央凹

  * **对色彩高度敏感**

  * 白昼视觉


* 杆状体

  * 分散分布于视网膜

  * **无彩色感觉，对低照明敏感**

  * 暗视觉


* 亮度适应

  * 暗适应 —— **慢**

  * 亮适应 —— **快**

***

### 采样量化

* 概念
  * 分别是对坐标和灰度值的 **离散化**


* 空间分辨率
  * **PPI** 和 **DPI** 的概念


* 灰度分辨率
  * 灰度值占用的二进制 **位数**

***

### 插值算法

* 最近邻插值
* 双线性插值

### 动态范围

* 图像中灰度值差的最大值
* 和对比度差不多

### 图像的表示

* 使用矩阵
* 以左上角为原点，一排一排向下扫描，每一排在从左到右扫描

***

### 像素间的基本关系

* 邻域

  * **种类**：分为四邻域和八邻域

  * **定义**：位置符合要求且 **灰度值在规定集合内**


* 通路

  * 能够邻接连通就是通路

  * 首尾相同就是闭合通路


* 连通集
  * 通路上的所有像素点构成的集合


* 区域
  * 连通集就是区域


* 边界
  * 特殊的区域是边界

*************

### 像素距离

* 欧式距离
  * 就是欧几里得几何距离


* 街区距离
  * 四领域距离


* 棋盘距离
  * 八领域距离

**************

### 图像代数运算

* 加法
  * 对于含有K个均值为0的噪声的图像可以进行降噪


* 减法
  * 增强图像之间的差


* 乘法
  * 给一个模板做图像的局部显示

************************

## 第三章：灰度变换和空间滤波

### 灰度变换

* 处理方法
  * 点处理


* 处理域
  * 空域


* 举例
  * 对数函数可以提升动态范围，使灰度分布得更均匀

### 直方图均衡化

* 列出 **rk** 代表灰度级别， **其中8个灰度级别除数为7**
* 算出每个灰度级别所占的比例 **p( rk )**
* 对 p(rk) 进行累加得到 **累积**
* 对累积根据 **k/7** 进行最近舍入
* 舍入的结果是那一个灰度级就代表转换到那个灰度级

### 直方图规定化

* 列出 **rk** 代表灰度级别， **其中8个灰度级别除数为7**
* 算出每个灰度级别所占的比例 **p( rk )**
* 对 p(rk) 进行累加得到 **累积**
* 对目标直方图进行累加，**只用写出目标p(rk)不为零的灰度级别累加就行**
* 单映射规则
  * 对累积根据目标累积进行舍入
  * 舍入的结果是那一个灰度级就代表转换到那个灰度级
* 组映射规则
  * 在累积中找出k个最接近目标累积的
  * 找到的累积 **往上** 所有都归入目标累积所属的灰度值等级

### 均衡化和规定化特点

* 均衡化

  * 增大动态范围，可以看到更多的细节

  * 自动增强

  * 效果不易控制

  * 总得到全图增强的效果


* 规定化

  * 有选择的增强

  * 效果容易控制

### 空间滤波

#### 处理方法

* 模板滤波处理

#### 处理域

* 空域

#### 滤波器

* 一个邻域加预定义的操作

#### 分类

* 平滑和锐化
* 线性和非线性

#### 滤波含义

* 模板内每个像素设置一个 **权值**
* 结果等于所有像素灰度值与权值积的和

#### 平滑滤波特点

* 使用均值滤波器
* 噪声减少但图像模糊

#### 非线性平滑滤波

* 最大值滤波：找最大值
* 最小值滤波：找最小值
* 中值滤波：既能消除噪声，又不那么模糊。专治 **椒盐噪声**

#### 锐化滤波特点

* 突出图像的细节

#### 一阶滤波 - Sobel算子

| -1   | -2   | -1   |
| ---- | ---- | ---- |
| 0    | 0    | 0    |
| -1   | -2   | -1   |



| -1   | 0    | 1    |
| ---- | ---- | ---- |
| -2   | 0    | 2    |
| -1   | 0    | 1    |



#### 二阶滤波 - 拉普拉斯算子

| 0    | -1   | 0    |
| ---- | ---- | ---- |
| -1   | 5    | -1   |
| 0    | -1   | 9    |



| -1   | -1   | -1   |
| ---- | ---- | ---- |
| -1   | 9    | -1   |
| -1   | -1   | -1   |

***

## 第四章：频率变换

### 傅里叶变换

* 把图像在空域和频域间转换 
* 频域图像乘以基图像等于空域图像

### 频谱特性

* 中心点代表频率最低
* 低频集中大部分能量
* 高频对应 **边缘和噪声** 

### 理想低通滤波器

* 截止频率和百分功率
* 会有振铃效应

### 产生振铃现象的原因

* 一个点图像对应的傅里叶时域图像是一个正弦函数，这个函数尾巴会有振荡
* 当卷积之后的图像就会有振铃效应

### Bufferworth低通滤波器

* 一阶滤波不会出现振铃效应
* 阶数越大，振铃效应越明显
* 截止频率可以调整

### 高斯低通滤波器

* 没有振铃效应
* 截止频率可以调整

### 高通滤波器

* 就是低通滤波器的反
* 理想高通还是会产生振铃现象

### 高频增强滤波

* 对结果乘以一个大于1的常数在加上另外一个常数c

### 带通带阻滤波器

*******************

## 第五章：图像的复原与重建

### 图像退化

* 指在图像的形成，存储，传输过程中由于成像系统，传输介质和设备的不完善造成图像质量变坏

### 图像复原

* 指在研究图形退化原因的基础上，以已经退化的图像为依据，根据一定的先验知识建立一个图像退化模型，然后进行逆运算恢复图像

### 图像复原过程

* 估计退化原因
* 建立退化模型
* 进行逆运算
* 得到恢复图像

### 算术关系

* 原图像在退化函数作用下在加上噪声等于退化后的图像

### 空域上的卷积等于频域上的相乘

### 噪声模型

* 统计特征分类

  * 平稳： 不随时间的变化而变化

  * 非平稳：随着时间的变化而变化


* 按直方图划分

  * 高斯噪声

  * 瑞丽噪声

  * 伽马噪声

  * 均匀噪声

  * 椒盐噪声

  * 指数噪声


* 周期噪声

  * 被不同频率的正弦波函数干扰

  * 频谱特点是很多个点构成一个园


* PDF估计

  * 取一小部分计算它的直方图分布

  * 估计噪声的参数

  * 统计的均值是平均灰度

  * 统计的方差是平均对比度

****************

### 噪声滤除

* 空域滤波器

  * 各种均值滤波器

  * 各种统计滤波器

  * 各种自适应滤波器


* 频域滤波器

  * 带通带阻滤波器

  * 陷波滤波器


* 均值滤波器

  * 算术，几何，谐波，逆谐波滤波器

  * 算术均值适合高斯噪声和均匀噪声

  * 谐波均值适合椒盐噪声


* 统计滤波器

  * 中值滤波： 适合椒盐噪声

  * 最大值：适合椒噪声

  * 最小值：适合盐噪声

  * 中值滤波：适合高斯，均匀噪声


* 自适应滤波器
  * 不同地方用不同的滤波器，类似于局部均衡化


* 带通带阻频域滤波器

  * 带阻常用于消除噪声

  * 带通常用于提取噪声模型

  * 陷波滤波器用于消除某一频率周围的频率

### 退化函数估计

* 注意
  * 退化函数不是噪声


* 方法

  * 图像观察法：从局部观察整体

  * 试验估计法：相同设备环境获取差别来估计

  * 模型估计法：根据物理数学模型来估计，比如车辆运动模型

### 逆滤波

* 退化函数的逆
* 如大雾环境退化的逆

********************

## 第六章：彩色处理

### RGB彩色模型

* 黑色为最小值：(0,0,0)
* 白色为最大值：(255,255,255)

### CMY彩色模型

* 定义
  * (C, M, Y)= (1, 1, 1) - (R, G, B)


* K
  * CMYK中的K指的是黑色

### HSI彩色模型

* 字母含义

  * H —— 色调

  * S —— 饱和度

  * I  —— 强度


* 特点

  * HS构成彩色信息

  * I与彩色信息无关

  * 饱和度为0则无颜色


* 与RGB的对应关系

  * I 是RGB的均值

  * H 同一个三角面上的所有点具有相同的色调，因此色调是360度的园
  * S 是点到中轴线上的距离长度

### 伪彩色图像处理

* 定义
  * 根据一定准则对灰度值赋予彩色的处理过程


* 理由

  * 人类对色彩远比对灰度更加敏感

  * 做此映射后方便人类识别


* 如何进行

  * 灰度强度分层

  * 灰度转彩色


* 如何强度分层

  * 用M个平面去切

  * 对M+1个范围的灰度强度赋予M+1个颜色


* 如何灰度到彩色的转换
  * 输入一个灰度值，输出三个独立的RGB分量值

### 全彩色图像处理

* 分类

  * 对每个分量进行处理，然后合成

  * 直接对彩色图形进行处理

### 彩色变换

* 特点

  * 和灰度变换类似，只不过是在三个分量上做操作

  * 灰度图像是单通道，彩色通道是三通道


* 补色
  * (255,255,255) - (R,G,B)


* 彩色分层

  * 目的：突出图像中特殊彩色区域，以便分离出目标物

  * 实质：将感兴趣之外的其他颜色映射成不突出的颜色，这样就能突出主体

***

## 第八章：图像视频压缩

### 冗余

* 时间冗余：前后帧之间重复的部分
* 空间冗余：同一帧之间相同的部分
* 视觉冗余：使用更高的规格但人眼看起来是一模一样的

### 压缩评价指标

* 压缩率
* 保真度
* 计算复杂度

### 压缩技术发展

* 摩尔斯电码
* 香农编码
* 哈夫曼编码
* 傅里叶变换
* DCT变换
* 运动矢量预测
* 视频编码框架

### 游程编码

* 一个数据项d出现几次就用nd替换，一般会加一个提示字符如@
* 如 'lll' 被编码成 '@3l'

### 熵编码

* 简介
  * 利用概率压榨完二进制编码


* 要求

  * 短码赋给频率高的

  * 遵循前缀性


* 分类

  * 哈夫曼编码

  * 香农-菲诺编码

  * 算术编码

### 哈夫曼编码

* 注意：为了使方差尽量小，尽量使用还没用用过的，尽量避免使用合成的

### 算术编码

* 特点：无限 0 - 1 划分
* 左闭右开
* 最后编码结果取右边开复杂的那个
* 解码取左边闭这个，当涉及一直为0无法判断终止时，可以在结尾加个终止符号

****************

## 第九章：形态学处理

### 膨胀

* 符号：圆圈里面一个加号
* 让正方形的中心点在被膨胀中
* 正方形随便动
* 所有正方形覆盖的部分就是膨胀部分

### 腐蚀

* 符号：圆圈里面一个减号
* 让正方形永远在被腐蚀之中
* 正方形随便动
* 正方形中点经过的部分就是被腐蚀的部分

### 开操作

* 符号：空心点
* 体现分开
* 小东西在里面滑动
* 中点覆盖的是开操作结果
* 类似于腐蚀

### 闭操作

* 符号：实心点
* 体现联结
* 小东西沿着边缘在外面滑动
* 整个小东西包围的面积都是闭操作的结果

*************

## 第十章：图像分割

***



