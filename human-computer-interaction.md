# 人机交互

## Info

> ### What is this course about?
>
> * build well designed **interactive systems**
> * the systems need to be not only working but **interactive**
> * emphasis on **fieldwork**, rapid **prototyping**
> * learn to design. prototype. evaluate interfaces
> * this course is about **process** not implmentation
>
> ### what is HCI
>
> * HCI is the study of how people interact with computers and to what extent computers are or are not developed for successful interactions with human beings
>
> ### three parts of HCI 
>
> * UI Design
> * UX Design
> * Interactive Design
>
> ***

## Design Principles

> ### discoverability
>
> * Define: the design should make all needed options and materials for a given task visible without distracting the user with extraneous or redundant information.
> * Examples
>   * 显示最近打开过的文件
>   * 选择字体要把字体样式显示出来，不要只是‘黑体’、‘宋体’
>   * 界面要清晰明了，一看就知道是发邮件、打电话页面
>
> ### simplicity
>
> * Define: use the design is easy to understand, regardless of user's experience,  knowledge, language skills, or current concentration level.
> * Examples
>   * 只有用户鼠标放上的时候才会hover
>   * 界面不要太复杂，甚至需要专业知识才能使用
>
> ### affordances
>
> * Explain: 功能可见性
> * Define: an affordance is a relationship between ther properties of an object and the capabilities of the agent that determine just how the object could be possibly useds
> * Dependable:
>   * 简单性要求不依赖其他东西如知识、经验等而能让用户明白
>   * 功能可见性可以依赖如知识，经验，文化等。比如文化上开关上下就是不一样的
> * Examples:
>   * 玻璃能够透视，玻璃易碎
>   * 门把手的样式决定了是采用外拉还是内推的方式打开
>   * 键盘一看就是敲，屏幕一看就是看，鼠标一看就是点
>   * 控件有八角一看就知道是可以缩放调整
>
> ### mapping
>
> * Define: Mapping is a technical term ... meaning the relationship between the elements of two sets of things
> * Examples:
>   * 方向盘向左打就是向左转，如果向左打却向右转就太反人类了
>   * 苹果鼠标向上滑动，页面内容更多。和windows是反的
>   * 设计中按钮和表盘要一一对应
>
> ### perceptibility
>
> * Explain: (especially of a slight movement or change of state) able to be seen or noticed. 易感知的
> * Define: 
>   * the design communicates necessary information efficetively to the user. regardless of ambient conditions or the user's sensory abilities.
>   * Feedback must be immediate. Feedback must also be informative... poor feedback can be even worse than no feedback at all. becasue it  is distracting, uninformative, and in many cases irritating and anxiety-provoking.
>
> * Examples:
>   * 进度要有估计时间，不然用户盯着屏幕傻等
>   * PS图标被选中了图标下沉，不然用户根本不知道选了哪一个
>   * 信息要明确。比如保存，保存成什么格式，保存到那里，大概要多久，都得明确
>   * 超过一定时间的等待必须提示反馈给用户。不然10s等待无反馈真的难顶
>
> ### consistency
>
> * Define: The design should resuse internal and external components and behaviors, maintaining consistency with purpose rather than arbitrary consistency, thus reducing the need for users to rethink and remember.
> * Examples:
>   * 要么全用'ok, cacel'，要么全用'accept, dismiss'。不要混用
>   * 不要把label的空间用成可以点击的形式，让用户觉得可以点击结果却是一个label
>   * 撤销都是ctrl + z, 就不要自己突发脑洞想另外一个快捷键
>
> ### flexibility
>
> * Define: the design accommodates a wide range of individual preferences and abilities.
> * Examples:
>   * 快捷方式，快捷键，首页设置
>   * 最常用功能直接单击或者双击而不是右键选择然后单击
>
> ### equity
>
> * Define: provide the same means of use for all users: identical whenever possible; equivalent when not 
>
> ### ease,  comfort
>
> * Define: the design can be use efficiently and comfotalbly and with a minimum of fatigue.
>
> ### structure
>
> * Define: Design should organize the user interface purposefully, in meaningful and useful ways base on clear, consistent models that are apparent and recognizable to users, putting related things together and separating unrelated things, differentiating dissimilar things and make similar things resemble one annother.
>
> ### constraints
>
> * Define: constaints are powerful clues, limitting the set of possible actions. The thoughtful use of constants in design lets people readily determine the proper course of action, even in a novel situation.
> * Examples:
>   * .jpeg修改成.png出警告提示
>   * 起始日期居然在终止日期之后，提示用户输入错误
>   * 新密码不符合要求提示用户
>
> ### tolerance
>
> * Explain:容错性
> * Define: The design should be flexible and tolerant, reducing the cost of mistakes and misuse by allowing undoing and redoing, while also preventing errors wherever possible.
> * Examples
>   * 提供取消按钮，终止按钮，回退操作
>
> ### feedback
>
> * Explain: Feedback must be immediate. Feedback must also be informative... poor feedback can be even worse than no feedback at all. becasue it  is distracting, uninformative, and in many cases irritating and anxiety-provoking.
> * Examples:
>   * 不要给错误码，要给具体那里错了，最好给出推荐的解决方式
>   * windows那种关机并更新强制更新真的是反人类
>
> ### documentation
>
> * Define: Event though it is better if the system can be used without documentation, it may be necessary to provide help and documentation. And such information should be easy to search, focused on user's task, list concrete steps to be carried out, and not to be large.
> * Examples:
>   * 要精简，最好提供电子版本，最好要随时用随时可以上网搜索到
>
> ***

## Heuristic Evaluation

> ### Nielsen的10条经验性准则
>
> * 简洁而自然的对话
> * 使用用户的语言
> * 将用户的记忆负担减到最小
> * 一致性
> * 反馈
> * 清晰地标志退出
> * 快捷方式
> * 好的出错信息
> * 避免出错
> * 帮助和文档
>
> ### process
>
> * pre evaluation traiining 
> * evaluation 
>   * Individual evaluate interface then aggregate results
>   * First focus the whole scope, and then focus on specific problems
>   * each evaluator produces list of problems
> * Severity rating
> * debriefing 
>
> ***

## Design Cycle

> ### Interface design cycle
>
> * design
> * prototype
> * evaluate
>
> ### the design process
>
> * acceptance
> * analysis
> * definition
> * ideation
> * idea selection
> * implementation
> * evaluation
>
> ### acceptance
>
> * set motivation
> * set deadline
> * set responsibility
>
> ### analysis
>
> * understand users and tasks
>
> ### difinition
>
> * focus on problem
>
> ### ideation
>
> * brainstorming
>
> ### idea selection
>
> * define importance of each idea
> * rank ideas according to your criteria
> * pick top N
>
> ### implementation
>
> * scale up low --> high fidelity
>
> ### evaluation
>
> *  type of evaluation chosen depends on the level of implementation
>
> ***

## Design Research

> 
