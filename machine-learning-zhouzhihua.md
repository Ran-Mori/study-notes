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

## todo

1. 符号机器学习？统计机器学习？ 深度机器学习？问下chatgpt



#### 