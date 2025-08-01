## preface

### excerpt

1. 从主流为符号机器学习发展到主流为统计机器学习，反映了机器学习从纯粹的理论研究和模型研究发展到以解决现实生活中实际问题为目的的研究。
2. 深度学习在理论和技术上并没有太多创新，只不过由于硬件技术的革命，计算机的速度大幅提高，使得人们有可能采用原来复杂度很高的算法，从而得到比过去更精确的结果。深度学习是否又要取代统计学习了？

***

## symbolic machine learning

### what is

* This approach relies on explicit rules and logic to encode knowledge and make inferences. It focuses on manipulating symbols to represent concepts, relationships, and rules.
* Symbolic methods were some of the earliest machine learning techniques, such as *decision trees* and *rule-based systems*. They work by defining conditions or rules that guide the model’s predictions.
* Symbolic ML is highly interpretable and can be used for tasks requiring transparent, logical reasoning, but it tends to struggle with noisy data and unstructured inputs, like images or audio.

### example with decision trees

* predict whether a person will enjoy a movie.  simple facts about the person and the movie, such as:
  1. **Age** (e.g., child, teenager, adult)
  2. **Genre** (e.g., action, animation, drama)
  3. **Time Available** (e.g., weekend, weekday)

* decision trees

  ```bash
            [Is Adult?]
            /         \
         Yes           No
        /               \
  [Is Genre Drama?]   [Is Genre Animation?]
        |                 |
     Enjoy            Enjoy
  ```

* features

  1. **Each rule** in the tree represents a logical statement, like “If the genre is animation, then enjoy.”
  2. **Decision trees don’t rely on probabilities or statistical inferences**. Instead, they rely on rules and conditions based on the structure of the data.
  3. the **logic and reasoning process** is indeed clear and interpretable, allowing us to **trace and describe each decision** the model makes.

***

## statistical machine learning

### what is 

* It is a branch of machine learning that relies on **probabilistic and statistical methods** to recognize patterns in data and make predictions. Unlike symbolic machine learning, which uses clear, rule-based logic, statistical machine learning often works by **finding patterns in data** that may be hard for humans to interpret directly.
* **Deep learning** is indeed a **subset** of **statistical machine learning**.

### features

1. **Data-Driven Learning**:
   * It relies on large amounts of data to learn patterns rather than on predefined rules.
   * It doesn’t “know” anything explicitly about the relationships within the data at the beginning. Instead, it learns by **adjusting parameters** through repeated exposure to data, finding statistical correlations.
2. **Probabilistic Reasoning**:
   * Predictions are made based on probabilities rather than clear-cut rules. For example, a statistical ML model might say there is a 70% chance that a given email is spam rather than definitively classifying it as spam or not.
   * This probabilistic approach helps with uncertainty and variability in real-world data, making it adaptable to noisy or complex data.
3. **Black Box Nature**:
   * Many statistical ML models, especially complex ones like neural networks, are difficult to interpret because the learned patterns and relationships aren’t explicitly represented.
   * Instead, they use matrices of numbers (weights and biases) that are updated to minimize error, creating a “black box” that can perform well but isn’t always explainable.
   * The weights and mathematical transformations within the model are abstract, making it hard to interpret exactly how each feature affects the final prediction.



## introduction

### excerpt

1. 若我们欲预测的是离散值，例如“好瓜”“坏瓜”，此类学习任务称为“分类"(classification); 若欲预测的是连续值，例如西瓜成熟度0.95、0.37,此类学习任务称为“回归”(regression)。
2. 根据训练数据是否拥有标记信息，学习任务可大致划分为两大类：“监督学习“(supervised learning)和 “无监督学习"(unsupervised learning), 分类和回归是前者的代表, 而聚类则是后者的代表。

***

## three tasks

### classification

* feature
  1. The result is a discrete label or class
  2. supervised learning
* example
  1. Spam Detection: Classify emails as spam or not spam.
  2. Image Recognition: Identify objects in images, such as cats, dogs, or cars.
  3. Medical Diagnosis: Predict the presence or absence of a disease based on symptoms.

### regression

* feature
  1. Continuous Output: The output variable is a real number
  2. supervised learning
* example
  1. House Price Prediction: Estimate house prices based on attributes.
  2. Stock Price Prediction: Predict future prices based on historical trends.
  3. Weather Forecasting: Predict temperature or rainfall amounts.

### clustering

* feature
  1. Grouping Similar Data: Data points in the same cluster are more similar to each other than to those in other clusters.
  2. unsupervised learning
* example
  1. Customer Segmentation: Group customers with similar behavior for targeted marketing.
  2. Image Segmentation: Divide an image into different regions based on pixel similarity.
  3. Anomaly Detection: Identify unusual patterns by finding clusters of similar instances.

***



## NFL Theorem

### full name

* No Free Lunch (NFL) Theorem

### what is means

* It is a concept in machine learning and optimization that states there is **no single algorithm that performs best across all possible problems**. In other words, if an algorithm works exceptionally well on one set of problems, it is likely to perform poorly on other types of problems. The theorem, formally proposed by David Wolpert and William Macready, highlights the idea that **algorithm performance is problem-dependent**.

### in ML

* The NFL theorem suggests that **no one algorithm can guarantee the best performance on every possible dataset or task**.
* An algorithm that performs well on one type of data may not work as well on another.
* There’s always a trade-off between an algorithm’s performance on different problem types.

***

## model evaluation and selection

### excerpt

1. 有多种因素可能导致过拟合, 其中最常见的情况是由于学习能力过于强大, 以至于把训练样本所包含的不太一般的特性都学到了。
2. 很多学习器是为测试样本产生一个实值或概率预测，然后将这个预测值与一个分类阈值(threshold)进行比较，若大于阈值则分为正类，否则为反类.

### 查准率与查全率

* 是什么

  |          | 预测正例 | 预测反例 |
  | -------- | -------- | -------- |
  | 真实正例 | TP       | FN       |
  | 真实反例 | FP       | TN       |

  > 查准率P = TP / (TP + FP)
  >
  > 查全率R = TP / (TP + FN)

* 现状：查准率和查全率是一对矛盾的度量. 一般来说，查准率高时，查全率往往偏低；而查全率高时，查准率往往偏低。

### 代价敏感错误率

* 是什么？
  * 不同类型的错误所造成的后果不同.
* example
  * 在医疗诊断中，错误地把患者诊断为健康人与错误地把健康人诊断为患者, 看起来都是犯了 “一次错误”，但后者的影响是增加了进一步检查的麻烦，前者的后果却可能是丧失了拯救生命的最佳时机。
  * 门禁系统错误地把可通行人员拦在门外，将使得用户体验不佳,但错误地把陌生人放进门内，则会造成严重的安全事故。
  * 

***

## P versus NP

### what is?

* **P**: The general class of questions that some algorithm can answer in polynomial.
* **NP**: For some questions, there is no known way to find an answer quickly, but if provided with an answer, it can be verified quickly. The class of questions where an answer can be *verified* in polynomial time is NP, standing for "nondeterministic polynomial time".
* **NP-Complete**: the set of all problems `X` in NP for which it is possible to reduce any other NP problem `Y` to `X` in polynomial time.
* Informally: it asks whether every problem whose solution can be quickly verified can also be quickly solved.

### feature 

1. all P problems are NP problems.
2. but if all NP problems are P problems?
3. If we can at least solve one NP-Complete problem, then we can solve all NP problems.
4. there are even harder problems than NP-Complete.

### example

* 解一个数独很难，它是一个NP问题。但给定一个数独的答案，去验证它是否是正确的很简单，验证数独答案只事一个P问题。
* 机器学习面临的问题通常是NP难甚至更难，而有效的学习算法必然是在多项式时间内运行完成，若可彻底避免过拟合，则通过经验差最小化就能获最优解，这就意味着我们构造性地证明了 “P=NP”； 因此，只要相信 “P != NP” ，过拟合就不可避免。
* NP-Complete：数独问题，蛋白质结构问题

***

## linear model

### excerpt

1. 许多功能更为强大的非线性模型可在线性模型的基础上通过引入层级结构或高维映射而得。此外，由于w直观表达了各属性在预测中的重要性，因此线性模型有很好的可解释性。

2. 广义线性模型"(generalized linear model)
   $$
   y = g^{-1}(w^Tx + b)
   $$
   * 其中`g^(-1) = ln()`时为对数线性回归

3. 对数几率函数。可用于分类，因为其输出固定在(0, 1)之间
   $$
   y = \frac{1}{1 + e^{-x}}
   $$
   * 将对数几率函数作为`g^(-1)`代入得
   	$$
   	y = \frac{1}{1 + e^{-(w^tx + b)}}
   	$$


***

## decision tree

### excerpt

1. 一棵决策树包含一个根结点、若干个内部结点和若干个叶结点；叶结点对应于决策结果，其他每个结点则对应于一个属性测试；
2. 决策树学习的目的是为了产生一棵泛化能力强，即处理未见示例能力强的决策树。

### how

* 决策树算法的核心是如何产生划分，其中一种方式是计算`信息增益`(informationi gain)。其核心思想即在众多的属性中，计算哪一种属性对最终结果产生的影响更大，优先选择影响最大的属性作为划分。

  ```bash
  # 下面这种划分说明根据训练集数据，Is Adult属性的信息增益最大
  					[Is Adult?] 
            /         \
         Yes           No
        /               \
  [Is Genre Drama?]   [Is Genre Animation?]
        |                 |
     Enjoy            Enjoy
  ```

### overfit

* 决策树算法也可能会过拟合，剪枝(pruning)是一种剪枝算法，至于咋剪就有点复杂了，这里不再赘述。
* 怎么判断剪枝后的效果更好性能提升呢？可以预留一部分训练集的数据作为测试集来验证。

### continuous value

* 上述的算法只适合处理离散值，遇到连续值有点棘手。
* 可以采用二分法，将连续值离散化来处理。

***

## neural network

### excerpt

1. M-P神经元模型模型中，神经元接收到来自几个其他神经元传递过来的输入信号，这些输入信号通过带权重的连接(connection)进行传递，神经元接收到的总输入值将与神经元的阈值进行比较，然后通过“激活函数”(activation function)处理以产生神经元的输出。

### M-P neural model

#### what

* it  is a binary computational model that mimics how biological neurals process information.

#### components

1. Input Signals (x1, x2, …, xn):
2. Weights (w1, w2, …, wn):
3. Summation Function: computes a weighted sum of the inputs:
4. Threshold (θ): it determines whether the neural will “fire” (activate) or not. If the weighted sum S exceeds the threshold (S >= θ), the neural outputs 1; otherwise, it outputs 0.
5. Output (y): 1 or 0.

#### limitations

1. 输入必须转换为线性数组
2. 输出只能是1 or 0
3. The model does not have a learning algorithm, so weights and thresholds must be predefined.

### gradient descent

#### process

1. 选一个模型。那目的实际就是去找w和b的值
   $$
   y_{\text{pred}} = w \cdot x + b
   $$
   
2. 定义好loss function。这里选择用均方差Mean Squared Error
   $$
   L(w, b) = \frac{1}{N} \sum_{i=1}^N \left( y_i - (w \cdot x_i + b) \right)^2
   $$
   
3. 随机初始化参数，定义一个学习率
   $$
   w = 0.1, b = 0.5, 学习率(\eta) = 0.01
   $$

4. loss function中其实有两个变量分别是w和b。现在分别对w和b进行求导
   $$
   \frac{\partial L}{\partial w} = -\frac{2}{N} \sum_{i=1}^N x_i \cdot \left( y_i - (w \cdot x_i + b) \right)
   $$

   $$
   \frac{\partial L}{\partial b} = -\frac{2}{N} \sum_{i=1}^N \left( y_i - (w \cdot x_i + b) \right)
   $$

   

5. 我们的目标是让loss function的函数值尽量小，怎么样才能更小呢？就是w和b朝着斜率的方向去变换。变换的量是`学习率*求导值`
   $$
   w \leftarrow w - \eta \cdot \frac{\partial L}{\partial w}
   $$

   $$
   b \leftarrow b - \eta \cdot \frac{\partial L}{\partial b}
   $$

6. 通常当loss fuction小于某个threshold时训练停止；或达到最大的迭代次数时训练停止；当然还有一些其他的停止情况

#### learning rate

* A small learning rate means smaller updates to the model parameters, leading to **slower convergence** but potentially more accurate results.
* A large learning rate means bigger updates, which can speed up training but risks **overshooting** the optimal solution or even diverging.

***

## CNN

### what is?

* Convolutional Neural Networks 卷积神经网络

### components

1. Convolutional Layer: 不同的filter去寻找不同的特征
2. Pooling Layer: 分辨率降低并不影响一个物体的识别
3. Fully Connected Layer
4. Softmax: 将输出聚在一个范围内

### 拉直

* 图像是一个100 * 100 * 3的tensor，怎样才能让它作为CNN的输入呢？
* 直接把它拉直，成一个30000的tensor丢进CNN作为输入

### receptive field

* 理论上每一层都是Fully Connected Layer岂不是会更好？这样参数多，模型弹性大，能预测更复杂的模型。但100 * 100 * 3的图像如果经过一层有1000个neural的Fully Connected Layer，参数weight的数目是 3 * 10^7，是一个十分巨大的数字。参数太多训练难度过大 且overfitting的风险也很大
* 我们得知，识别图像分类往往不用看整张图像。因此一个neural不用感知100 * 100 * 3有30000个weight，它只需要明确感知一小块3 * 3 * 3的范围即可，这样weight数是27
* Each receptive field has a set of neurals(e.g. 64 neurals ). 同一块receptive field可能包含鸟嘴、鸟尾、天空等多种特征，一个neural只能识别一种特征。假设特征一共有64种，则同一块receptive field由64个neural来检查。 

### parameter sharing

* 100 * 100 * 3的图像在经典参数下共有98 * 98个3 * 3 * 3的receptive field。每一个receptive field由64个neural来进行观察。理论上weight的总数是98 * 98 * 3 * 3 * 3 * 64。但其实每98 * 98个neural干的事都是一样的，因此完全可以共享参数。因此最终的weight是3 * 3 * 3 * 64

### model bias

* 理论上Fully Connected Layer弹性最大，使用receptive field限制的了弹性的范围。等于是100 * 100 * 3的weight大多数都被强制设为了0
* parameter sharing又进一步限制了弹性，因为理论上weight从98 * 98 * 3 * 3 * 3 * 64降成了3 * 3 * 3 * 64。强制让很多weight取相同的值
* 因此CNN的model bias其实很大。但这不一定是坏事，它的overfitting的可能性就降低。且增大model bias的原理都是从实际图像分类中获得灵感，因此CNN处理图像有奇效，处理非图像可能不适用。
* pooling层没有参数，但使用pooling的原因也是从图片分类获得的灵感。因此在非图片任务中pooling不一定适用。

### convolutional layer

* 上面receptive field和parameter sharing的另一种解释方式。
* 100 * 100 * 3的图像，经过一个3 * 3 * 3的neural(filter)后，会产生98 * 98 * 1的feature map。注意输入是拉直的。
* parameter sharing实际就是同一个filter扫过一张图片，这个过程就叫Convolution
* 假设某一层Convolutional Layer有64个3 * 3 * 3filter，那weight总数是 3 * 3 * 3 * 64，输出的feature map是98 * 98 * 64。
* 假设以98 * 98 * 64的feature map作为新的一张图片输入，经过128个3 * 3 * 64的filter，则weight数是3 * 3 * 64 * 128。输出的feature map是96 * 96 * 128。

### feature

* 3 * 3的kennel size会不会太小？其实不会，因为在first Convolutional Layer确实只能看到 3 * 3的范围，但其实second Convolutional Layer中的3 * 3对应着原始图像5 * 5的范围。
* AlphaGo。下围棋和图像很像，可以抽象成图片分类问题，因此可以用CNN来下围棋。但不用pooling，因为和图像分类还是有点不一样的。
* CNN不能很好处理放大，缩小，旋转问题。因为放大后人眼看起来是一样的，但对同一个训练好的CNN来说，输入完全不一样。
* The number of filters in each convolutional layer of a CNN is a hyperparameter,
* the total number of layers, whether pooling is used, the type of layers following one another, and their configurations are all hyperparameters. 

### process

* Input: a 32 * 32 * 3 image of a dog.
* Convolution Layer: Apply 10 filters of size 3 * 3 * 3, Outputs 30 * 30 * 10 feature maps.
* Pooling Layer: Apply 2 * 2 max pooling, Outputs 15 * 15 * 10.
* Convolution Layer: Apply 20 filters of size 3 * 3 * 10, Outputs 13 * 13 * 10 feature maps.
* Fully Connected Layer: Flatten to a vector of size 3380 (from 13 * 13 * 20). output  A probability distribution (e.g., [0.95 ({dog}), 0.05 ({cat})]).

***

## KNN

### what is?

* K Nearest Neighbor algorithm

### feature

1. it is a kind of lazy training algorithm 

### how

1. choose K

   * A small K might be too sensitive to noise, while a large K can smooth over complex patterns.

2. Calculate Distances

   * Euclidean Distance
     $$
     d = \sqrt{\sum_{i=1}^n (x_i - y_i)^2}
     $$

   * Manhattan Distance
     $$
     d = \sum_{i=1}^n |x_i - y_i|
     $$

3. Sort the training points by their distance to the new point and select the K closest ones.

4. output

   1. For classification: Use majority voting among the K neighbors.
   2. For regression: Take the mean (or weighted mean) of the neighbors’ values.

***

## Self Attention

### vector set as input

* 一段话其实是一个向量set；一段语音其实是一个向量set
* 一个社交网络可以看作一个向量set；每一个节点node可以视为一个向量，这个向量里有年龄、性别、爱好等属性；整个社交网络就是向量set

### what is input

1. N -> N(Sequence Labeling): 比如POS tagging(词性标注)；社交网络输出每个人是否买某种东西
2. N -> 1: sentiment analysis；输入一个分子，输出它是否有毒性
3. N -> M: translation；ChatGpt, Voice Recognition, Translation

### why?

* 假如要做POS tagging，最直接的办法是N个输入向量丢进N个neural，由于neural干的是同一件事词性分析，因此可以parameter sharing，就完事了。
* 但是假设输入是`I saw a saw.`，则`saw`的输出是不同的，但上面的方式一定输出是相同的。所以问题本质是要让一个neural能感知到其他neural负责的输入。

### self attention layer

* 预期是输入向量set中的每一个向量都要感知到set中其他的向量。

* 因此方便理解的角度来看就是每个向量与其他向量做dot product，当然不是单纯地做dot product，而是一个向量生成n个矩阵，n个矩阵与其他向量生成的n矩阵相乘。

* 从最终矩阵运算的角度来看，假设self attention layer层超参neural的数量是3，则计算过程如下
  $$
  Q = W^qI \\
  K = W^kI \\
  V = W^vI \\
  A = K^tQ \\
  A^` = A 这一步经过softmax \\
  O = VA^` \\
  其中I是输入向量set，W^q,W^k,W^v是训练的weight，O是输出
  $$

### feature

* self attention不感知输入向量set中每个向量的位置关系，每一个向量都是平等的，都要和其他所有向量做dot product。但实际上有些问题是与position相关的，比如词性分析。因此我们可以给输入向量set的每一个向量带上一个位置向量后才做为输入。
* 声音相关的任务中，没有必要每一个向量都要感知其他所有向量，因为实际我们也知道声音想要确认含义往往后它附近的声音相关性比较大。因此可以设置超参只看周围的向量。
* 图像处理也可以用self attention，比如100 * 100 * 3的图像可以看作是10000个向量set，每个向量其实是一个像素点。
* 当Graph中使用Self attention时，每一个向量其实只有感知与它有连线的就可以了，全感知也没用与实际问题不符。而且这就叫做GNN (Graph Neural Netwok)
* Self Attension的计算量往往很大，因此都在想办法降级计算量，但降级计算量往往带来模型效果下降。

### CNN vs Self Attention

* 如果把100 * 100 * 3的图像看作是10000个向量set的话，每一个向量CNN每一个neroun只感知周围的8个向量，而每一个向量self attention的每一个neural要感知9999个向量。
* 因此self attention是更flexible的CNN，CNN是model bias更大的self attention。
* 因此也能得知，self attention的计算量偏大

### process

1. input: n tensor
2. self attention layer: calculate attention score, then soft max
3. full connected network 

### other model

* self attention常常仅仅作为其他模型的中间一层，比如transformer中间有self attention
* 当输入非常长时，整个模型计算过程的耗时会主要落在self attention上
* 因此常常在影像处理上会优化self attention来降低计算量

***

## RNN

### what is?

* Recurrent Neural Network

### how
* 一个向量set，从左到右，每一個向量輸出的結果都將作為下一個向量的輸入
* 当然也可以做成双向的，从右到左，每一个向量感知它右边的内容。

### RNN vs Self Attention

* RNN只能感知某一边，而SelfAttention天涯若比邻，再远都要感知到
* 由于RNN架构，只能从某一边串行计算，而self attention每一个向量的attention值都可以并行计算不耦合。
* RNN可以一定程度視爲简化的Self Attention.
* Self Attention在訓練時並行度可以很高，當輸入一個n長度的序列時，它可以並行計算出n個輸出序列，因此計算h(t)是不依賴h(t-1)的。但LSTM訓練並行度就很低，因為計算h(t)要依賴h(t-1).

### example applicagion

* slot filling
  * 找出一段语句中的表单关键信息。I would like to arrive **Taipei**(destination) on **November 2nd**(time of arrival).
* 前面的input可能会影响后面某一个input的输出
  1. arrive Taipei on November 2nd.
  2. leave Taipei on Novermber 2nd.
  3. 其中分别对应 place of arrival, time of arrival, place of depature, time of depature.

### Long Short-Term Memory

* [Understanding LSTM Networks](https://colah.github.io/posts/2015-08-Understanding-LSTMs/)
* RNN的問題
  * "the clouds are in the **sky**". 推理出"sky"其實只需要最近的一段信息即可，前面的不需要
  * "I grew up in France… I speak fluent **French**." 推理出"French"需要很久以前的信息，中間的信息反而不需要
  * RNNs don’t seem to be able to handling such “long-term dependencies.”
* 原理
  * Memory Cell: It can store information over time.
  * Forget Gate: decides which information to discard from the cell state.
  * Input Gate: decides what new information to store in the cell
  * Oupput Gate: decides what part of the cell state to output.
* process
  * input: cell(t - 1), h(t - 1), x(t)
  * 經過forget、input、output
  * output: cell(t), h(t) 

* 怎麼理解？
  * 假設輸入的x(t)內容很重要，那input門會激活，最終cell(t)內就存有與x()相關的東西
  * 假設輸入的x(t)內容不重要，那input門不會激活，x(t)的內容就不會存到cell(t)中；而且還可能forget門被激活，輸入x(t)後反而導致h(t-1)的部分內容不重要，被移除。

* 特點
  * Cell(0)可以是一個向量，也可以是一個很大的矩陣存很多東西
  * LSTM的参数量是同规格其他网络的四倍
  * 它的四个输入就是从一个输入异化而来的


## word embedding

### what?

* an embedding is a way to represent *discrete* data (like words, categories, or IDs) as *continuous* vectors (or points) in a multi-dimensional space.

###  1 of n encoding

* 假设有十万个词，那就建立一个10万维的向量。每一个词就是这个向量的某一维为1
* 这种表示方法没有办法表示词与词之间的关系。

### word embedding

* 假设有十万个词，将其映射到一个N dimension 的martix中，这个matrix中相邻附近的词会有某种关联。

### how

1. Embeddings are learned via machine learning algorithms.
2. count based: 如果两个词经常同时出现，那么这两个词彼此接近
3. prediction based: GPT模型第一层的结果作为word embedding.
   * 原理：如果两个词输入同一个GPT推导的下一个词是一样的，那这两个词必然相近。

### feature

1. Embeddings transform discrete data into dense, continuous vectors where each dimension of the vector represents a different attribute of the data.
2. Similar items are represented by similar vectors (i.e., vectors that are close in the embedding space).
3. it is an example of unsupervised learning.
4. 相似组合的词在N dimension空间中有相似的几何形状。比如`fall-fell-fallen`, `draw-drew-drawn`, `take-took-token`
5. 可以解决类似于 `Rome : Italy = Berlin : ?`. `? = V(Berlin) - V(Rome) + V(Italy)`

***

## Supervised Learning

### model complexity and dataset size

* 数据集中包含的数据点的变化范围越大，在不发生过拟合的前提下你可以使用的模型就越复杂。
* 仅仅复制数据集或找相似的数据集是无济于事的。
* 在现实世界中，你往往能够决定收集多少数据，这可能比模型调参更为有效。永远不要低估更多数据的力量！

***

## Transfomer

### feature

1. sequence to sequence model.
2. 很多問NLP題都可以轉換为QA問題，如翻譯、文章摘要、情緒分析、multi label classification；QA問題就可以用transformer來解
3. 文法分析也可以用transformer來做。將樹狀非線形的結構轉換成線形的結構來訓練
4. transformer基本結構。輸入 -> encoder -> decoder -> 輸出

### encoder

* input: a sequence

* output: a sequence with same length

* components

  1. multi head self attention

  2. positional encoding

  3. residual. real output = normal output + origin input

  4. layer normalization

  5. FC

### decoder

* input

  1. a token, and the starting token must be `begin`.
  2. the previos output

* output

  * a token. (输出一个N dimension向量，每一维的值代表每个token的可能性。)
  * 每次输出一个token，直到输出`end`结束

* components

  1. `masked` multi head self attention. 只看左边，不看右边，类似RNN

  2. positional encoding

  3. residual. real output = normal output + origin input

  4. layer normalization
  5. cross attention

* end

  * N dimension向量中有一维是`END`，当输出是`End`时推理结束。

***

## GAN

### what?

* Generative Adversarial Networks(生成对抗网络)

### IO

* input
  * x
  * z (sample from a simple distribution)
* output
  * y

### why distribution?

* 当同一个输入，预期它会有不同的输出时。就适合给输入加一个从简单distribution内sample出来的值。
* 这样输入其实是 x + simple distribution，输出其实是complicated distribution
* 比如：
  * input: 请画一个红眼睛的角色。 output: 很多角色都有红眼睛
  * chatbot

### discriminator

* 本身是一个network
* input: GAN输出的一个y
* output: scalar, larger means real while smaller means fake.

### Adversarial

1. 固定D，初始化G。从simple distribution中sample出n个输入，丢入G，得到n个图像。
2. 从训练集sample出n个真实图像，和上面n个图像混在一起成2n个图像集。
3. 固定G，训练D。训练D能够从2n个图像集中分辨出那部分是真实图像，即输入真图是输出高分，输入生成图时输出低分。
4. 固定D，训练G。训练G，让G输出的图像输入D后，也能产生高分。
5. 重复步骤1

***

## CoT

### what?

* **Chain of Thought** is a reasoning technique in LLM that encourages **step-by-step problem-solving** by explicitly generating intermediate steps in a task before arriving at the final answer. This method is particularly useful for tasks requiring complex reasoning, such as mathematical problem-solving, logical reasoning, and multi-hop question answering.

### process

1. **Prompting**: Explicitly instructing the model to “think step by step.”
2. **Reasoning Path**: The model generates a sequence of intermediate reasoning steps.
3. **Final Answer**: After reasoning through the steps, the model provides the solution.

### implementation

1. **Manual Prompting**: Add “Let’s think step by step” or a similar phrase in your prompts.
2. **Few-Shot Learning**: Provide examples of problems solved step-by-step to guide the model.
3. **Automatic CoT**: Use fine-tuned models trained explicitly to reason step-by-step.

### implicit prompt

1. Pre-training with Diverse and Rich Data
   * Many online resources, such as math solutions, logical proofs, or tutorials, naturally include CoT reasoning.
   * Educational materials and code comments often break problems into steps.
2. Supervised Fine-Tuning with Human-Generated Data
   * Fine-tuning datasets include tasks solved using CoT reasoning.
   * Data from instruction-following tasks (e.g., responding to “Explain how X works”) often naturally incorporates reasoning steps.
3. Reinforcement Learning with Human Feedback (RLHF)
   * When annotators evaluate model outputs, step-by-step reasoning is often rated higher for tasks requiring logic or problem-solving.
4. Implicit CoT via Few-Shot and Zero-Shot Learning
   * Recognizing when step-by-step reasoning is required based on the **context** or **nature of the query**.

### RL

* **Problem with SFT Alone:** A model trained only with SFT might generate CoT that *looks* right structurally but contains logical errors, calculation mistakes, or simply doesn't lead to the correct final answer. SFT optimizes for matching the *exact* target sequence, not necessarily for the *correctness* of the reasoning itself.
* **Enter RLHF/RLAIF:**
  - **Reward Model (RM):** First, a separate Reward Model is trained. This RM is shown pairs of LLM-generated responses (often including CoT reasoning) to the same prompt, and human labelers (or another powerful AI) indicate which response is better (e.g., more logical, more correct, more helpful). The RM learns to predict this preference score.
  - **RL Fine-Tuning Loop:** The LLM (the "policy" in RL terms) generates a CoT response to a prompt. This complete response (including all reasoning steps and the final answer) is then evaluated by the Reward Model, which gives it a score (the "reward").
  - **Optimization:** Using an RL algorithm (like PPO - Proximal Policy Optimization), the LLM's parameters are updated to maximize the expected reward from the RM.

### thought and result

* the generation process is **continuous**. The model generates the chain of thought tokens first. These generated thoughts then become part of the *context* or *prompt* for generating the subsequent tokens(result).

***

## prompt

### terms

1. prompt
2. inference
3. completion
4. context window
5. prompt engineering
6. in context learning(ICL)
7. zero-shot/few-shot inference

### example

```bash
Classify this review:
I loved this movie!
Setiment: positive. # this is one-shot
Classfify this review:
I dont like this chair!
Setiment: 
```

***

## generative configuration

### configuration

1. max new token
2. top K/P
3. temperature
   * higher temperature, higher randomness
   * use in final softmax layer

***

## AGI project lifecycle

### scope

* define use cases

### select

* choose an existing model or pretrain your own.

### adapt and align model

* c
* fine tuning
* align with human feedback (RLHF)
* evaluate

### app integration

* optimize and deploy model for inference
* augment model and build LLM-powerd applications.

***

## computational challenge 

### quantization

* 将FP32 换成 FP16, BF16, INT8等占用内容更小的type

***

## fine-tuning

### full fine tuning

* 占用内存和资源很大

### how

1. 丢训练case进LLM产生一个结果
2. 产生的结果和打标的预期结果做cross-entropy
3. 根据cross -entropy来更新LLM的参数weight
4. 用验证集和测试集看效果

### why

* 经过fine-tuning的模型对于训练集内的任务有很好的效果
* 往往只需要500 - 1000的训练case就能达到很好的效果

### catastrophic forgetting

* 经过full fine tuning的模型，在一个task上表现很好，但有可能在其他task上表现比fine-tuning前更差

### exmaple

```json
"samsum": [
    ("{dialogue}\n\nBriefly summarize that dialogue.", "{summary}"),
    ("Here is a dialogue:\n{dialogue}\n\nWrite a short summary!", "{summary}"),
    ("Dialogue:\n{dialogue}\n\nWhat is a summary of this dialogue?", "{summary}"),
    ("{dialogue}\n\nWhat was that dialogue about, in two sentences or less?", "{summary}"),
    ("Here is a dialogue:\n{dialogue}\n\nWhat were they talking about?", "{summary}"),
    ("Dialogue:\n{dialogue}\nWhat were the main points in that conversation?", "{summary}"),
    ("Dialogue:\n{dialogue}\nWhat was going on in that conversation?", "{summary}"),
    ("Write a dialog about anything you want", "{dialogue}"),
    ("Write a dialog based on this summary:\n{summary}.", "{dialogue}"),
    ("Write a dialog with this premise \"{summary}\".", "{dialogue}"),
]
```

* 有这样一个模版，然后自己填充"dialogue"和"summary"进行训练
* 常见用户可能在总结类这个问题上的prompt它都包含进去了，等于是不用自己写prompt，只需要准备真正的输入和输出结果即可。

## PEFT

### what

* parameter efficient fine-tuning

### feature

* 只更新一部份weight，大部份weight都保持不变
* 因为大部份保持不变，因此更不容易catastrophic forgetting
* 可以关注model的某一层参数，某一类型层参数，某一类型的参数
* 还可以给model外套一些层，model内部参数不变，训练外部层参数

### LoRA

* what: Low Rank Adaptiion of LLMs
* how
  * 在transformer的self attention层输出的参数计算加一点东西
  * rank数选得越小，越省资源，但效果不一定更好，但不意味着rank越大效果越好。
* feature
  * 往往只需要一个GPU就行，而不是分布式GPU系统

***

## prompt tuning

### how

* model的weight不变，只训练words embedding层的weight

### path methods

* 可以某个task prompt tuning输出一个对应的words embedding层weight，然后在inference的时候，根据不同类型的任务，读取不同的words embedding层weight来进行推理。
* 上面這種思路就叫做path methods

***

## Reinforcement Learning

### Challenge

* How do you train an LLM to be "helpful," "safe," or "perform complex multi-step reasoning correctly" *without explicitly writing down millions of perfectly reasoned, perfectly helpful examples*? There's no single "correct" token sequence for "helpfulness" or "safety." These are subjective qualities.

### process

1. 先把基礎模型經過SFT。This "wakes up" the model's ability to follow instructions and generate outputs in a desired format (e.g., CoT).
2. Training a Reward Model (RM)
   1. 數據集：SFT後的model對一個prompt輸出多個答案，人工對這些答案進行打分
   2. 損失函數(pairwise ranking loss)：Its loss function is designed to make it output a higher score for the preferred response and a lower score for the less preferred response. 
   3. 結果：輸入一段回答，給出一個分數
3. 開始強化學習
   * 目標：To make the LLM generate outputs that maximize the reward score given by the Reward Model.
   * 過程：收集一堆沒有在SFT階段和RM訓練階段使用過的prompt；當前的LLM對於這些prompt會生成一系列答案；把這些答案給RM會得到一個分數
   * 損失函數：Adjust the LLM's parameters so that it is more likely to generate sequences that receive high rewards from the RM.
   * 注意：KL-divergence penalty。硬追求高分數可能會過擬合，因此在調整weight的時候，不能和基礎的SFT相差太大

### CoT

* 只要在RM訓練階段，human給一步步思考，成體系的思維過程答案打高分即可
* 由於RM需要人類來判斷答案是否正確來打分，因此RL選擇的問題一般都是程式設計或數學等有明確答案的問題
* RL的作用是有決策能力，推理能力。但基礎的對現實的理解能力，是RL給不了的，因此還是需要預訓練

***

## RLHF

### what

* reinforcement learning from human feedback
* RLHF is a process used to fine-tune large language models, making them generate text that is not only grammatically correct and coherent, but also aligned with human intentions and values.

### for?

* 消除toxic language, aggressive responses, dangerous information. 总之就是LLM也要讲X性
* LLM的回答可能not helpful, not honest, not harmless

### feature

* RLHF can be done with both full fine-tuning and partial fine-tuning methods like LoRA, but full fine-tuning is more common in practice.

### component

* **Agent:** Language Model (LM)
* **Environment:** Text Generation Task & Human Feedback
* **States:** Prompt or Context
  * This is the information that the agent uses to decide what action to take. In RLHF, the "state" is the prompt or context given to the language model.
* **Actions:** Text Generation
* **Reward:** Human Preference Feedback or Reward Model Output
* **Policy:** The Language Model (LM)
  * In the context of RLHF, the initial policy is the pre-trained language model. Through the RL process, this policy will be fine-tuned to generate text that humans find desirable.

### example

* prompt: My house is too hot.
* completion
  1. You are too poor to buy an conditioner, and you deserve it.
  2. You can try open the window.

### reward hacking

* what?
  * prompt: This product is ...?
  * completion: Beatutiful love and world peace all around.
  * 上述的回复尽管很正面，能在reward中拿到高分。但过于正面已经不honest了，因此需要避免这个问题
* how
  * 加入一个reference model。具体就不细讲了

***

## constitutional AI 

### what？

* It is a method for training AI models, particularly large language models (LLMs), to be more *self-governing* and less reliant on extensive human feedback. Instead of relying solely on direct human ratings to steer the model's behavior, it uses a set of *principles* or rules, similar to a constitution, to guide its actions.

### components

* constitutional principles
* self-evaluation mechanism
* self-revision module
* reward model
* language model fine tuning

### example

* Prompt: "I'm feeling a little unwell, can you give me some advice?"
* Constitutional Principles:
  1. 
  2. Be helpful
  3. Do not provide harmful advice
  4. Only provide information based on reliable sources
  5. Avoid providing medical advice
  6. Encourage people to seek professional medical advice
* Initial Response (before self-evaluation): "You might have the flu. Take some over-the-counter flu medicine."
* Self-Evaluation:
  - Does this follow principle 1? - Yes
  - Does this follow principle 2? - Maybe, since it does not take into account user specific situations.
  - Does this follow principle 3? - Yes, since over-the-counter flu medicine is generally considered safe.
  - Does this follow principle 4? - No, it provides medical advice, albeit general advice.
  - Does this follow principle 5? - No.
* Self-Revised Response (after self-evaluation): "It sounds like you may be feeling under the weather. Please consult a healthcare professional for a proper diagnosis and treatment plan. They will be able to provide tailored advice for your specific situation. Please rest and drink plenty of fluids in the meantime."

***

## opt for deployment 

### distillation

* 用一个teacher model 去 训练一个student model
* student model有较小的体积，然后把student model部署在生产环境中，节约资源。

### PTO

* what: post training quantityzation
* 将weight用更小精度来表示，节省占用的体积

### Pruning

* remove model weights with values close or equal to zero 

***

## RAG

### model difficulties

* out of data
* wrong answer
* hallucination

### examples

* Program aided language model (PAL)
  * 一个问题，先用CoT拆解成步骤，把每个步骤的math转换为python script，丢给interpreter执行，返回结果然后输出。

***

## ReAct

### what?

* reasoning and acting
* It's a framework for training language models to combine *reasoning* with *action taking* to solve complex tasks. It moves beyond simple text generation to incorporate external knowledge, tools, and interaction with the environment.
* Acting: The model executes actions based on its reasoning, using external tools or knowledge sources. These actions allow the model to interact with the world and obtain information needed to solve the problem.

### components

* Reasoning Module
* Action Module
* Tool Integration
* Observation

### example

* **Prompt:** "I want to plan a trip to San Francisco for 3 days. Can you suggest some activities?"
* **Thought:** *I need to find some information about San Francisco. I will start by searching online for popular attractions.*
* **Action:** *Search Tool: search_query = "popular attractions in San Francisco"*
* **Observation:** *Results from Search Tool: "Golden Gate Bridge, Alcatraz Island, Fisherman's Wharf, etc..."*
* **Thought:** *Okay, now that I have a list of popular attractions, I need to gather information about them and see if they fit my preferences for 3-day trip. I also want to include local food recommendations in my trip plan.*
* **Action:** *Search Tool: search_query = "best restaurants San Francisco"*
* **Observation:** *Results from Search Tool: "Tadich Grill, Swan Oyster Depot, etc..."*
* **Thought:** *I have some ideas for attractions and food, I need to organize these into a day by day plan. I will assume the user is generally interested in touristy locations.*
* **Action:** *Trip Planner Tool: create_itinerary based on the results gathered previously and make a day-by-day plan*
* **Observation:** *Results from trip planner tool: Day 1: Visit Golden Gate Bridge, Day 2: Alcatraz Island, Day 3: Fisherman's Wharf. Food Recommendations: Tadich Grill, Swan Oyster Depot*
* **Final Response:** "Okay, here is a suggested 3-day itinerary for your trip to San Francisco: Day 1: Visit the Golden Gate Bridge. Day 2: Explore Alcatraz Island. Day 3: Experience Fisherman's Wharf. I also recommend trying the food at Tadich Grill and Swan Oyster Depot. Have a great time!"

### feature

* 不涉及model本身，是一种prompt engineering

***

## LangChain

### what

* LangChain is an open-source framework that makes it easier to develop applications that combine large language models (LLMs) with other tools, data sources, and functionalities. It provides a set of abstractions and pre-built components that can be used to create complex, customized language-based applications, such as chatbots, question-answering systems, and content generation tools.
* LangChain, at its core, is indeed a framework. It's not a fundamentally new algorithm or a completely novel approach to AI, but rather a powerful tool that consolidates and simplifies existing ideas, techniques, and best practices for building LLM applications.

### memory

* what: LangChain's memory modules are designed to extend the LLM's context window and give it a more persistent form of memory, beyond just a single turn. This allows the LLM to maintain state and remember information across multiple conversational turns or actions.
* purpose: LangChain's memory aims to solve the problem of the limited context window of LLMs. It helps create applications that feel more conversational by allowing LLMs to remember past interactions.
* manage: Unlike the context window, LangChain's memory is explicitly managed by the application. You, as the developer, decide how to store and retrieve information from the memory component.

***

## internal LLM

### process

* Data Collection and Preparation:
  - Sources: Internal documents (Word, PDF, etc.), Wikis and knowledge bases, Databases, Internal APIs, Transcripts of meetings or presentations, Emails, Other structured and unstructured data. 
  - Cleaning and Processing: This data is often unstructured and needs to be cleaned, processed, and converted into a format suitable for processing by an LLM, such as splitting into chunks and removing personally identifiable information.
* Data Indexing/Embedding (Vector Database):
  - Chunking: Text documents are typically broken down into smaller chunks to fit the LLM's context window and for better matching.
  - Embeddings: Each chunk is converted into a vector representation (embedding) using a pre-trained embedding model. This embedding captures the semantic meaning of the text.
  - Indexing: These embeddings are then stored in a vector database. Vector databases are optimized for efficient similarity searches based on these embeddings. Example databases are Pinecone, Chroma, FAISS, etc.
* The Retrieval Component:
  - Query Embedding: When a user asks a question, the question is also converted into an embedding vector.
  - Similarity Search: This query embedding is then used to search the vector database for the most similar embeddings based on some similarity metric.
  - Retrieval: The chunks of text corresponding to the most similar embeddings are then retrieved from the vector database.
* The LLM (Generation Component):
  - Context Injection: The original user query and the retrieved context from the vector database are combined to form a prompt for the LLM. The relevant information is thus "injected" into the context provided to the LLM.
  - Response Generation: The LLM uses this combined prompt to generate a relevant answer. The LLM thus has access to both its prior knowledge and the context provided by the retrieval step.
* Optional: Fine-Tuning or Prompt Engineering:
  - Fine-Tuning: In some cases, the LLM might also be fine-tuned on the company's internal data to further improve its performance and style, and make it more specific to the given company.
  - Prompt Engineering: Careful prompt engineering can optimize the LLM's performance for the specific task of answering questions about the company.

***

## AI compiler

### difference

| feature                | traditional compiler                                        | AI compiler                                                  |
| ---------------------- | ----------------------------------------------------------- | ------------------------------------------------------------ |
| **Target Workload**    | General-purpose programming languages                       | Machine learning models and computations                     |
| **Optimization Goal**  | General performance on CPUs                                 | High throughput, low latency, memory efficiency on specialized hardware |
| **Domain Knowledge**   | Limited                                                     | Deep understanding of ML operations and models               |
| **Optimization Level** | Low-level instruction optimization                          | High-level, domain-specific optimization                     |
| **Key Optimizations**  | Register allocation, instruction scheduling, loop unrolling | Operator fusion, data layout transformation, quantization, graph optimization, kernel generation |
| **Target Hardware**    | General-purpose CPUs                                        | GPUs, TPUs, NPUs, and sometimes CPUs                         |
| **Input**              | Source code files (e.g., .c, .cpp)                          | ML model representation (e.g., TensorFlow Graph, PyTorch model) |

## AI Agent

### process

* 抽象描述
  1. 人工输入一个goal
  2. agent分析environment得到一个observation(当前棋盘中所有棋子的位置)，将observation和goal作为输入，产生一个action
  3. 此action可能改变environment
  4. 重复步骤2
* 具体例子
  * goal - 下围棋要赢；agent - alpha go；environment - 棋盘，棋子；observation - 当前棋盘中所有棋子的位置；action - 将一颗棋落在棋盘上
* 分析
  * 第二步其实也是在做文字接龙，将observation和goal作为输入，文字接龙产生一个输出action

### llm agent

* 優勢
  1. 傳統的agent如alpha go所有的action其實也只有19 * 19種可能，但llm agent的action理論上有無數種可能
  2. 傳統的agent的reward需要開發者手動定義；但LLM可以直接將輸出又作為輸入再餵進去，其實不需要人工定義reward

### computer use/operator

* goal - 訂一張票
* agent - computer use/operator
* environment - 電腦屏幕
* observation - 當前電腦屏幕呈現的內容
* action - 移動鼠標、點擊鍵盤

### interact

* 有可能當agent還沒做出action時，environment就已經發生改變。這時理想的行為是放棄當前的action，立即分析environment得到observation，然後產生下一個action
* 比如說語音模型，支持打斷
  * 用戶突然說一句"ok"，其實沒改變environment，可以繼續使用當前的action
  * 但當用戶突然說一句"stop, I don't want to hear that."，這時的environment就發生了改變，要產生新的action來執行

### adjust actions based on experience

* 寫程序
  * goal: 你是軟件開發大師
  * observation 1:寫一個XX程序
  * action 1:寫了一段程序
  * environment：這段程序錯誤，輸出了一個錯誤報告
  * observation 2: 寫一個XX程序，錯誤報告
  * action 2: 寫了另一段程序
  * environment：程序正確
* 分析
  * 僅僅通過將錯誤報告重新作為輸入喂給LLM，它就有可能文字接龍出正確的程序
* 問題
  * 需要將每一個observation都記錄下來，但LLM的context window是有限的，它記不住那麼多東西
* 解決方案
  * agent維護一個long term memory，負責存有用的經驗
  * 有一個負責從LTM中retrieval的llm模塊，它的職責是根據當前的輸出，從LTM中找出需要的之前的經驗，作為產生下一個action的輸入。就是RAG的R
  * 有一個負責往LTM中write的llm模塊，它的職責是判斷每次observation中那些經驗是有用的，如果有用，就將其寫入LTM
  * 還有一個reflection模塊，它的職責是對LTM中的經驗進行整理和反思，得到更好的經驗和想法
* 和RAG的不同
  * 以上解決方案和RAG思路其實一模一樣，唯一不同是RAG的LTM中存的是別人的經驗；而Agent的LTM中存的是自己的經驗
* 結論
  * 在benchmark中，會根據之前的經驗修正下一次輸出的LLM比每次都從零開始解體的LLM能取得更高的正確率
  * 負面的例子對於LLM基本沒有幫助。與其告訴LLM不要做什麼，不如告訴LLM要做什麼。比如"請輸出較短的內容"比"不要輸出較長的內容"更有用
* chat gpt
  * chatgpt - setting - personalization - memory中可以看见记下的所有memory
  * 其中的memory如果不准确，可以删除。如果某个事项很重要，可以手动让chat gpt记下来，比如输入"记下来"

### use tool

* 基本流程
  * system prompt
    * 如何使用工具
      * 如果遇到根據你的知識無法回答的問題，請使用工具。把工具的指令放在<tool>和</tool>之間，使用完工具後你會得到輸出，放在<output>和</output>中間
    * 使用某個特定的工具
      * 現在你可以使用天氣查詢工具如下：查詢某地、某時的函數Temperature(location, time)，使用範例: Temperature("台北", "2025.02.22 14:26")
  * user prompt
    * 2025年3月18日那天14:00, 高雄的天氣如何？
  * system prompt + user prompt喂給負責分析使用tool的AI後的輸出，這個輸出先不給用戶看
    * <tool>Temperature("高雄", "2025.03.18 14:26")</tool>
  * agent開發者做好橋樑工作，去call function得到結果
    * <output>32</output>
  * system prompt + user prompt + <output>32</output> 都作為輸入，給AI做文字接龍
    * 2025年3月18日 14:00，高雄的溫度是32度。比較熱。
* 工具很多怎麼辦？
  * 工具很多了以後，光是system prompt就會非常多
  * 這時就可以將工具的使用說明書，全部放在LTM裡面
  * 有一個專門負責query tool的module，它能根據當前的observation決定需要使用那些工具
  * 然後將observation和query tool的輸出共同作為新的observation，讓Agent去產生Action
* 工具返回的結構可能有錯
  * 假設上面天氣function返回的溫度是1000度時，AI能意識到有錯。它最終做文字接龍的結果為"2025年3月18日 14:00，高雄的溫度是1000度。這個數值顯然不合常理，可能是工具輸出有誤。如需其他信息或查詢，請告訴我。"
  * 當工具獲取的知識結果，跟agent本身訓練的資料相差不大時，agent會相信工具的結果。但當相差太大時，模型會相信自己訓練資料的結果。
  * 同樣的內容，目前AI比較容易相信AI同類生成的話，不太相信人類產生的話。
* 使用工具於模型本身的平衡
  * 比如一個任務是計算 3 * 4等於多少。當然可以藉助工具拿出計算器算，但難道不是直接心算會更快嗎？

### do plan/reasoning

* 當一個Agent收到一個observation後，它不急於立刻給出回復。
* 有一個world model。它能夠模擬產生了一個action後environment會發生怎樣的變化
* 假設第一個agent有3種action，那麼world model就模擬產生3個observation，每個observation又有3個action，那最後就有9個observation。類似於tree search。
* 然後tree search的過程中進行剪枝，然後找出最可能達到action的路徑進行輸出。

## Mamba vs Transformer

### 現狀

1. transformer：訓練並行度高，推理計算attention計算量大，要保存前面輸入的token來計算attention內存壓力大
2. LSTM：訓練無法並行，推理計算量小，內存壓力小，要cell可以存記憶

### linear Attention

* 現狀
  * transform易於訓練，但不易於推理
  * LSTM不易於訓練，但易與推理
  * 那有沒有那種方式能夠集合兩個的優點？
  * 現在LLM都需要更大的context window，支持多模態視頻、照片對應的輸入越來越大
* 推理過程
  * 目前核心難點是LSTM在訓練時，h(t)的計算必須依賴h(t-1)，導致並行度不高
  * 那就數學展開，消除中間項，看下能否並行計算
  * 結果發現當去掉forget gate函數後，居然LSTM的訓練計算就和self attention一樣了，只不過少了soft max
* 結論
  1. Linear Attention是LSTM少了forget gate
  2. Linear Attention是Self-Attention少了soft-max
* 特點
  * Linear Attention記住一個東西後，無法進行忘記。從LSTM視角來看是因為少了forget gate；從Self-Attention視角來看，一個attention值算出來值是1很重要，最後soft-max後得到的值會很高，但假設以後有一個更重要的attention算出來是2，那soft-max後之前1對應的值就變小了，變相是進行忘記
* 注意
  * 無論是LSTM，還是Self-Attention。其記憶的大小都是有限的，Self-Attention的記憶大小取決於計算attention時，q,k向量的大小，當輸入序列變長時，理論上q, k記不下來所有

### Mamba

* 其實就是優化改進後的Linear Attention
* Linear Attention缺失了forget gate，那就給它加上，並且通過一系列數學變換，還能在訓練的時候轉化爲類self-attention

## Catastrophic Forgetting

### what?

* Fine-tuning Aligned Language Models Compromises Safety, Even When Users Do Not Intend To! 
* 在進行後訓練時，基礎模型的一部分能力通常會喪失，最容易喪失的能力是之前Alignment的能力

### RL

* 相比於SFT，RL可能是最不容易使LLM發生遺忘的一種後訓練方式
* 因為RL是讓模型自己去產生答案，正確的答案給出更高的分數然後再去更新參數；這裡的答案都是LLM自己產生的，pre-training後RL階段並沒有給新的訓練資料。因此不容器產生遺忘。

### how to solve?

* experience replay - 在SFT時，用5%之前的訓練資料作為補充，就能夠防止forgetting.
* 這也是為什麼要使用MOE模型的原因之一。
