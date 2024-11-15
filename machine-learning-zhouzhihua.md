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

3. 对数几率函数。可用于分类
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

### M-P neuron model

#### what

* it  is a binary computational model that mimics how biological neurons process information.

#### components

1. Input Signals (x1, x2, …, xn):
2. Weights (w1, w2, …, wn):
3. Summation Function: computes a weighted sum of the inputs:
4. Threshold (θ): it determines whether the neuron will “fire” (activate) or not. If the weighted sum S exceeds the threshold (S >= θ), the neuron outputs 1; otherwise, it outputs 0.
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



## todo