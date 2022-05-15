# OS

## system call

### exit

* `int exit(int status)`
* no return
* status reported to `wait()`.
* status `0` to indicate success and `1` to indicate failure.

### wait

* `int wait(int *status)`
* returns the PID of an exited (or killed) child of the current process and copies the exit status of the child to the address passed to wait
* if none of the caller’s children has exited, wait waits for one to do so. 
* If the caller has no children, wait immediately returns -1. 
* If the parent doesn’t care about the exit status of a child, it can pass a 0 address to wait.

### exec

* `int exec(char *file, char *argv[])`
* only returns if error.
* replaces the calling process’s memory with a new memory image loaded from a file stored in the file system but preserves its file table.
* when success, the instructions loaded from the file start executing at the entry point declared in the ELF header.

### fork

* `int fork()`
* copy the parent’s file descriptor table along with its memory.
* file descriptors share an offset

### dup

* `int dup(int fd)`
* duplicate an existing file descriptor, returning a new one that refers to the same underlying I/O object.
* file descriptors share an offset

### mknod

* `int mknod(char *path, int, int);`
* creates a special file that refers to a device.
* Associated with a device file are the major and minor device numbers (the two arguments to mknod), which uniquely identify a kernel device.
* When a process later opens a device file, the kernel diverts read and write system calls to the kernel device implementation instead of passing them to the file system.

### fstat

* `int fstat(int fd, struct stat *st)`
* retrieves information from the inode that a file descriptor refers to.

### link

* `int link(char *file1, char *file2)`

* creates another file system name referring to the same inode as an exist-

  ing file.

### unlink

* `int unlink(char *file)`
* removes a name from the file system.
* The file’s inode and the disk space holding its content are only freed when the file’s link count is zero and no file descriptors refer to it.

***

## Unix Utils

### cat

```c
void cat(int fd)
{
  int n;

  while((n = read(fd, buf, sizeof(buf))) > 0) {
    if (write(1, buf, n) != n) {
      fprintf(2, "cat: write error\n");
      exit(1);
    }
  }
  if(n < 0){
    fprintf(2, "cat: read error\n");
    exit(1);
  }
}
```

### echo

```c
int main(int argc, char *argv[])
{
  int i;

  for(i = 1; i < argc; i++){
    write(1, argv[i], strlen(argv[i]));
    if(i + 1 < argc){
      write(1, " ", 1);
    } else {
      write(1, "\n", 1);
    }
  }
  exit(0);
}
```

### `|`

```c
int p[2];
pcmd = (struct pipecmd*)cmd;
if(pipe(p) < 0)
  panic("pipe");
if(fork1() == 0){
  close(1);
  dup(p[1]);
  close(p[0]);
  close(p[1]);
  runcmd(pcmd->left); // redirect stdout to write end of pipe
}
if(fork1() == 0){
  close(0);
  dup(p[0]);
  close(p[0]);
  close(p[1]);
  runcmd(pcmd->right); // redirect stdin to read end of pipe
}
close(p[0]);
close(p[1]);
wait(0);
wait(0);
```





***

## Operating system interfaces

### what os does

* shares a computer among multiple programs and to provide a more useful set of services than the hardware alone supports.
* manages and abstracts the low-level hardwares.
* shares the hardware among multiple programs so that they run (or appear to run) at the same time.
* provide controlled ways for programs to interact.

### difficultity of interface design

* two confict sides
  * simple and narrow because that makes it easier to get the implementation.
  * offer many sophisticated features to applications
* solution
  * design interfaces that rely on a few mechanisms that can be combined to provide much generality.
* Unix’s internal design
  * Unix provides a narrow interface whose mechanisms combine well, offering a surprising degree of generality.

### kenel

* what is it
  * a special program that provides services to running programs.
* feature
  * A given computer typically has many processes but only a single kernel.
  * Kernel executes with the hardware privileges while user programs execute without those.
* system call
  * When a user program invokes a system call, the hardware raises the privilege level and starts executing a pre-arranged function in the kernel.
  * The collection of system calls that a kernel provides is the interface that user programs see.

### shell

* what is it 
  * Unix’s command-line user interface.
* feature
  * a user program, and not part of the kernel.
  * there is nothing special about the shell, and shell is easy to replace.
* how it works
  * The main loop reads a line of input from the user
  * Then it calls fork, which creates a copy of the shell process.
  * The parent calls wait, while the child runs the command.
  * child runs exec, I fexec succeeds then the child will execute instructions from the new program.
  * At some point new program will call exit, which will cause the parent to return from wait in main.

### file descriptor

* what is it 
  * a small integer representing a kernel-managed object that a process may read from or write to.
* feature
  * A newly allocated file descriptor is always the lowest- numbered unused descriptor of the current process.

### I/O redirection with fork, exec

```c
char *argv[2];
argv[0] = "cat";
argv[1] = 0;
if(fork() == 0) {
  close(0);
  open("input.txt", O_RDONLY); // fd is 0 as 0 is the lowest
  exec("cat", argv); // redirec 0 to 1
}
```

### pipe

* what is it 
  * a small kernel buffer exposed to processes as a pair of file descriptors, one for reading and one for writing.
* feature
  * If no data is available, a read on a pipe waits for either data to be written or for all file descriptors referring to the write end to be closed.
  * The fact that read blocks until it is impossible for new data to arrive is one reason that it’s important for the child to close the write end of the pipe before exit.
* `|`
  * `(((a | b) | c) | d)`
  * The shell may create a tree of processes. The leaves of this tree are commands and the interior nodes are processes that wait until the left and right children complete.
* advantages over temp files
  * pipes automatically clean themselves up.
  * pipes can pass arbitrarily long streams of data.
  * pipes allow for parallel execution of pipeline stages.
  * pipes’ blocking reads and writes are more efficient than the non-blocking semantics of files.

### file and inode

* feature
  * A file’s name is distinct from the file itself
  * the same underlying file, called an *inode*, can have multiple names, called *links*.
  * Each link consists of an entry in a directory. the entry contains a file name and a reference to an inode.
  * An inode holds *metadata* about a file

***

## Operating system organization

### requirements

* multiplexing
* isolation
* interaction

### abstract

* Some operating systems for embedded devices or real-time systems implement the system calls as a library. Applications could directly interact with hardware resources.
* Unix processes use exec to build up their memory image, instead of directly interacting with physical memory.
* The Unix interface is not the only way to abstract resources, but it has proven to be a very good one.

### mode,  system calls

* modes
  * CPUs provide hardware support for modes.
  * The software running in kernel space is called the *kernel*.
* system call
  * CPUs provide **a special instruction** that switches the CPU from user mode to supervisor mode and enters the kernel at an entry point specified by the kernel.

### kernel organization

* monolithic kernel
  * what is it 
    * the entire operating system resides in the kernel, so that the implementations of all system calls run in supervisor mode.
  * advantages
    *  the entire operating system runs with full hardware privilege.
    * it is convenient because the OS designer doesn’t have to decide which part of the operating system doesn’t need full hardware privilege.
    * it is easier for different parts of the op- erating system to cooperate.
  * disadvantages
    * the interfaces between different parts of the operating system are often complex
    * a mistake is fatal, because an error in supervisor mode will often cause the kernel to fail.
* microkernel
  * what is it
    *  minimize the amount of operating system code that runs in supervisor mode, and execute the bulk of the operating system in user mode.
  * feature
    * OS services running as processes are called servers.(eg. file system service)
    * To allow applications to interact with the file server, the kernel provides an inter-process communication mechanism to send messages from one user-mode process to another.
    * it is relatively simple, as most of the operating system resides in user-level servers.
* linux
  * Linux has a monolithic kernel, although some OS functions run as user-level servers (e.g., the windowing system)
  * Linux delivers high performance to OS-intensive applications
* diffirences
  * faster performance
  * smaller code size
  * reliability of the kernel
  * reliability of the complete operating system (inclusing user-level services)

### process overview

*  mechanisms to implement processes
  * user/supervisor mode flag
  * address spaces
  * time-slicing of threads
  * etc
* page tables
  * translates (or “maps”) a *virtual address*  to a *physical address*
  * access page table in xv6: `p->pagetable`
* pointers
  * pointers on the RISC-V are 64 bits wide; the hardware only uses the low 39 bits
* kernel space
  * a page for a trampoline
  * a page mapping the process’s trapframe
  * Xv6 uses these two pages to transition into the kernel and back
* user/kernel stack
  * When executing user instructions, only its user stack is in use, and its kernel stack is empty.
  * When the process enters the kernel (for a system call or interrupt), the kernel code executes on the process’s kernel stack
* make system call
  * A process can make a system call by executing the RISC-V ecall instruction.This instruction raises the hardware privilege level and changes the program counter to a kernel-defined entry point.
  * When the system call completes, the kernel switches back to the user stack and returns to user space by calling the sret instruction,
* summary
  * an address space to give a process the illusion of its own memory
  *  a thread gives the process the illusion of its own CPU
  * In xv6, a process consists of one address space and one thread. In real operating systems a process may have more than one thread to take advantage of multiple CPUs.

***

## lab

reference [NebulorDang/xv6-lab-2021](https://github.com/NebulorDang/xv6-lab-2021)

### sleep

1. obtain the command-line arguments passed to a program.
2. use the system call `sleep`

### pingpong

1. use `fork()` to create process
2. use `read/write`
3. use `int pipe(int p[])` to create `pipe`
4. way to use `pipe`, close one end, read/write one end, close one end
5. use `wait()` to sync process

### primes

1. recursive `fork`.
2. write many into one end of a pipe, then read many from the other end of a pipe.
