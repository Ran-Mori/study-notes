## make

### 概念

> Make (or rather a Makefile) is a buildsystem - it drives the compiler and other build tools to build your code. But cmake is a generator of buildsystems.

### 几个关键指令

1. `configure`

   * 概念

     * 是一个`shell script`


   * 作用

     > make sure all of the dependencies
     >
     > produces a customised `Makefile` specific to your system from a template file called `Makefile.in`


   * `configure script file`来源

     > run `autoconf` to turn  `configure.ac` into a `configure` script

2. `make`

   > This runs a series of tasks defined in a `Makefile` to build the finished program from its source code.

   * `MakeFile.in`来源

     > Run `automake` to turn  `Makefile.am` into a `Makefile.in`

3. `make install`

   > The `make install` command will copy the built program, and its libraries and documentation, to the correct locations.
   >
   > It does do nothing less than running the install function/task in `MakeFile`

## cmake

### 概念

* 解释

  > CMake is a generator of buildsystems. It can produce Makefiles, it can produce Ninja build files, it can produce KDEvelop or Xcode projects, it can produce Visual Studio solutions.

* `cmake`和`make`的区别

  > Make (or rather a Makefile) is a buildsystem - it drives the compiler and other build tools to build your code. But cmake is a generator of buildsystems.
  >
  > 即make由cmake构建而来，cmake是构建工具的构建工具。make和nija是平级关系

### 使用

* 经常使用的两个命令

  ```cmake
  cmake -S . -B ./build
  # -S <path-to-source> = Explicitly specify a source directory.
  # -B <path-to-build>  = Explicitly specify a build directory.
  cmake --build ./build
  # --build <dir> = Build a CMake-generated project binary tree.
  # path = the path to be built
  cmake --install ./build
  # --install <dir> = Install a CMake-generated project binary tree.
  # path = the path to be installed
  ```

### 语法

1. `include_directories(/opt/homebrew/Cellar/glfw/3.4/include)`

   * 标准格式

     ```cmake
     include_directories([AFTER|BEFORE] [SYSTEM] dir1 [dir2 ...])
     ```

   * 标准解释

     > Add the given directories to those the compiler uses to search for include files. Relative paths are interpreted as relative to the current source directory.

   * 通俗示例

     * 在CmakeLists.txt中加这样一句

       ```cmake
       include_directories(/opt/homebrew/Cellar/glfw/3.4/include)

     * 等于如下效果

       ```bash
       clang -cc1  -I /opt/homebrew/Cellar/glfw/3.4/include
       ```

2. `add_execuable(main.out main.cpp other.cpp)`

   * 标准格式

     ```cmake
     add_executable(<name> [WIN32] [MACOSX_BUNDLE]
                    [EXCLUDE_FROM_ALL]
                    [source1] [source2 ...])
     ```
   
   
      * 标准解释
   
        > Add an executable to the project using the specified source files.
   
   
   
      * 示范
   
        ```cmake
        add_executable(3_hello_triangle main.cpp ~/Environment/glad/src/glad.c)
        # glad.c文件不是链接库，不能用link_libraries
        # 这种方式本质是将glad.c编译成glad.o，最后链接
        
        # 所以也可以执行下面两条执行
        add_library(glad STATIC ~/Environment/glad/src/glad.c)
        link_libraries(glad)
        add_executable(3_hello_triangle main.cpp)
        ```
   
2. `add_library(mainlib SHARED main.cpp other.cpp)`

   * 标准格式

     ```cmake
     add_library(<name> [STATIC | SHARED | MODULE]
                 [EXCLUDE_FROM_ALL]
                 [<source>...])
     ```
   
   
      * 标准解释
   
        > Add a library to the project using the specified source files.
   
   
      * 通俗解释
   
        > 将mian.cpp和other.cpp源文件编译成一个命名为mainlib的共享库
   
3. `link_libraries(/opt/homebrew/Cellar/glfw/3.4/lib/libglfw.3.dylib)`

   * 标准格式

     ```cmake
     link_libraries([item1 [item2 [...]]]
                    [[debug|optimized|general] <item>] ...)
     ```

   * 标准解释

     > Specify libraries or flags to use when linking any targets created later in the current directory or below by commands such as add_executable() or add_library().

   * 通俗示例

     * 在CmakeLists.txt中加这样一句

       ```cmake
       link_libraries(/opt/homebrew/Cellar/glfw/3.4/lib/libglfw.3.dylib)
       ```

     * 等于如下效果

       ```bash
       ld -o 1_create_a_window CMakeFiles/1_create_a_window.dir/main.cpp.o /opt/homebrew/Cellar/glfw/3.4/lib/libglfw.3.dylib
       ```

4. `target_link_libraries(main.out PUBLIC hellolib)`

   * 标准格式

     ```cmake
     target_link_libraries(<target>
                           <PRIVATE|PUBLIC|INTERFACE> <item>...
                          [<PRIVATE|PUBLIC|INTERFACE> <item>...]...)
     ```
   
   
      * 标准解释
   
        > Specify libraries or flags to use when linking a given target and/or its dependents.
   
   
      * 示范
   
        ```cmake
        add_library(glad STATIC ~/Environment/glad/src/glad.c)
        add_executable(3_hello_triangle main.cpp)
        target_link_libraries(3_hello_triangle PUBLIC glad)
        ```
   
4. `add_subdirectory(fmt)`

   * 标准格式

     ```cmake
     add_subdirectory(source_dir [binary_dir] [EXCLUDE_FROM_ALL])
     ```
   
   * 标准解释
   
     > Add a subdirectory to the build.

5. `target_include_directories(hellolib PUBLIC .)`

     * 标准格式

       ```cmake
       target_include_directories(<target> [SYSTEM] [AFTER|BEFORE]
         <INTERFACE|PUBLIC|PRIVATE> [items1...]
         [<INTERFACE|PUBLIC|PRIVATE> [items2...] ...])
       ```

     * 标准解释

       > Add include directories to a target.

     * 通俗解释

       > 在构建hellolib搜索头文件时，将当前目录纳入头文件搜索目录，并且可以向下传递

### 其他

* `[PRIVATE|PUBLIC]`区别

  * 是否`link、include`等操作时进行`传染`
  * 如`target_link_libraries(myexec PRIVATE hellolib)`表示另一个`library`在链接时需要`myexec`库时会自动可以添加`hellolib`进行链接，否则不能进行链接

* C++声明的必要性

  * 声明即引入头文件或在顶部做一个函数等声明如`void hello();`
  * 不声明编译器无法知道名称的含义。如`vector<MyClass> a`可能会把`vector、MyClass`看作是变量，把`<、>`看作时运算符

* 递归引入头文件

  * 当递归引入头文件时会报错无法通过编译

  * 防止递归引入的两种办法

    * `#pragma once`

    * 宏定义

      ```c++
      #ifndef INC_01_HEADER_NAME_H
      #define INC_01_HEADER_NAME_H
      
      #endif //INC_01_HEADER_NAME_H
      ```

* 第三方库引入

  * 纯头文件引入
    * 直接`target_include_directories()`时将包含头文件的文件夹填入即可
    * 通常`#include <xxx.h>`时需要定义一个`xxx.h`定义的宏，因为要防止重复引入
  * 子模块引入
    * 即作为`cmake`的子模块引入，通过`add_subdirectories()`和`target_link_libraries()`即可
  * 引用系统中预安装的第三方库
    * 最麻烦，通常通过`find_package()`
    * 不推荐使用

* 包管理器

  * `C++`没有包管理器，`vcpkg`太多问题简直没法用


***

## 编译

### 参数

1. CMAKE_CXX_FLAGS

   * 含义: 编译器执行时加的参数

   * 用法: 

     ```cmake
     set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -v") # 输出详细的编译日志
     ```

2. CMAKE_EXPORT_COMPILE_COMMANDS

   * 将编译指令输出到compile_commands.json文件

3. CMAKE_VERBOSE_MAKEFILE

   * 让编译日志尽可能详细