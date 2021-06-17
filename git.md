## git高阶命令指南

### 默认合并原则

* `git config --global pull.rebase false` 会设置默认的pull原则为有冲突，总之此Mac下默认的合并原则是暴力合并。如在`zhengyi-izumi-dev`下执行`git pull https://github.com/IzumiSakai-zy/VariousKindsLearning.git master `会暴力合并。这是很不可取的，因此设置成pull时默认有冲突

### 合并别人的pull request

* `git branch zhengyi-izumi-dev`：在master下执行此指令，创建一个分支，分支名已经表示了是处理那里的pull request
* `git checkout zhengyi-izumi-dev`：切换分支为zhengyi-izumi-dev
* `git pull https://github/zhengyi-izumi/VariousKindsLearning.git dev` 一定注意拉的代码分支，有时不一定都是master。此时一定会有冲突，下面进行冲突合并
* `vim Bytedance.md`  
* `git add Bytedance.md`
* `git commit -m"解决冲突"` 注意解决冲突时commit不用加文件的名字
* `git checkout master` 切换到mater，**由于之间没有对master进行过操作**，因此master的内容还是之前的，**本地暂存区是不会进行共享的**一定牢记
* `git merge zhengyi-izumi-dev` 合并之前解决了冲突的分支。此时一般都是`fast forward`无冲突，由于之间已经有commit信息了，因此此处是不用写commit信息的
* `git push -u origin master` 完成pull request

### 申请 pull request

* 首先在项目处点击fork
* 然后clone项目`https://github.com/zhengyi-izumi/VariousKindsLearning.git`到本地，然后修改提交到上面这个地址
* 然后点击原主人地址`https://github.com/IzumiSakai-zy/VariousKindsLearning`，就有pull request提示了

### git push origin master : master解析

* `orgigin` 是一个url地址，可以通过`git remote -v`进行查看
* `master ：master`左边的master是本地分支名，右边的master是远程分支名，两者一样可以只写一个

### 删除远程分支

* `git push origin --delete branchname`

### 切换一台电脑上不同的git账号

* 假设现在的全局配置账号名密码是`zhengyi.izumi`，且密码也是`zhengyi.izumi`的账号密码
* 现在去克隆`https://github.com/IzumiSakai-zy/VariousKindsLearning.git`，然后尝试进行一次push会报错，因此此时系统默认的账户还是`zhengyi.izumi`
* 此时需要去Mac的密码链下删除记住的`zhengyi.izumi`的密码
* 然后在进行push就可以了

### IDEA下不用git账号的配置

* IDEA的设置中可以通过`token`的方式添加多个GitHub账号
* 在commit的过程中因为只涉及到本地库的修改，因此与账号无关
* 在push的时候可以进行账号原则，进行账号选择就可以进行push

### git未提交前的各种撤销

* 在修改了文件内容但还没有`git add`时，执行`git checkout -- filename`来进行撤销
* 在`git add`过后但还没有`git commit`时，执行`git reset HEAD filename`来进行撤销
* 在`git commit`过后但还没有`git push`时，执行`git reset HEAD^`来进行撤销
* 核心就是只有在`git commit`过后才会增加一个新的版本，`git add`是不会增加新的版本的
* 但是要注意`git reset HEAD^`会使自己修改的内容全部消失，如果只是`git commit`的信息填写错误就要谨慎执行该操作，它会使修改全部丢失，超级危险。
* 而`git reset HEAD filename`并不会清除修改的内容，非常的方便

### git合并提交

* 现在已经有了3条commit记录。分别是`commit 1`、`commit 2`、`commit 3`

* `git rebase -i HEAD~3`。执行此条指令，代表处理最近的3条commit记录。会弹出一个vim编辑界面，内容如下

  ```bash
  p cacc52da commit 1
  s f072ef48 commit 2
  s 4e84901a commit 3
  ```

  * `p`: 代表使用这条commit，原封不动
  * `s`：代表使用这条commit，但是commit的信息以及记录和上一次进行合并(即commit 1不能使用s，只能使用p)

* `git rebase --continue`。如果上面的修改没有问题，那么这一步应该是成功的

* `git log`。就可以查看刚才的合并，3条记录已经变成一条了

### git 修改commit信息

* 以最新的一条commit信息为例

* `git rebase -i HEAD~1`。执行此条指令，会进入一个vim界面，内容大概如下

  ```bash
  r cacc52da 待修改的commit信息
  ```

  * 保存后会又弹出一个`git commit filename`但未填写commit信息的vim界面
  * 填写完commit信息后，保存，即完成修改

