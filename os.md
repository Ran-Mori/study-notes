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

* creates another file system name referring to the same inode as an existing file.


### unlink

* `int unlink(char *file)`
* removes a name from the file system.
* The file’s inode and the disk space holding its content are only freed when the file’s link count is zero and no file descriptors refer to it.

### ioctl

* `int ioctl(int fd, unsigned long request, ...);`
* references
  * [Linux manual page](https://man7.org/linux/man-pages/man2/ioctl.2.html)
  * [stackoverflow](https://stackoverflow.com/questions/15807846/ioctl-linux-device-driver)
* The `ioctl` function is useful for implementing a device driver to set the configuration on the device.
* The ioctl() system call manipulates the underlying device parameters of special files.

### mmap

* `void *mmap(void *addr, size_t length, int prot, int flags, int fd, off_t offset);`
  * addr - The starting address for the new mapping is specified
  * fd - This is the file descriptor which has to be mapped.
  * offset - This is offset from where the file mapping started. In simple terms, the mapping connects to (offset) to (offset+length-1)  bytes for the file open on filedes descriptor.
* url
  * [Linux manual page](https://man7.org/linux/man-pages/man2/mmap.2.html)
  * [When should I use mmap for file access](https://stackoverflow.com/questions/258091/when-should-i-use-mmap-for-file-access)
  * [How to use mmap function in C language?](https://linuxhint.com/using_mmap_function_linux/)
    * Memory allocation (Example1.c)
    * Reading file (Example2.c)
    * Writing file (Example3.c)
    * Interprocess communication (Example4.c)
* what is for
  * map or unmap files or `devices` into memory.

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

## operating system interfaces

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
  * child runs exec, if exec succeeds then the child will execute instructions from the new program.
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

## operating system organization

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
    * it is easier for different parts of the operating system to cooperate.
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
*  page tables
  * translates (or “maps”) a *virtual address*  to a *physical address*
  * access page table in xv6: `p->pagetable`
*  pointers
  * pointers on the RISC-V are 64 bits wide; the hardware only uses the low 39 bits
*  kernel space
  * a page for a trampoline
  * a page mapping the process’s trapframe
  * Xv6 uses these two pages to transition into the kernel and back
*  user/kernel stack
  * When executing user instructions, only its user stack is in use, and its kernel stack is empty.
  * When the process enters the kernel (for a system call or interrupt), the kernel code executes on the process’s kernel stack
*  make system call
  * A process can make a system call by executing the RISC-V ecall instruction.This instruction raises the hardware privilege level and changes the program counter to a kernel-defined entry point.
  * When the system call completes, the kernel switches back to the user stack and returns to user space by calling the sret instruction,
*  summary
  * an address space to give a process the illusion of its own memory
  *  a thread gives the process the illusion of its own CPU
  * In xv6, a process consists of one address space and one thread. In real operating systems a process may have more than one thread to take advantage of multiple CPUs.

***

## Traps and system calls

### three kinds of event

* system call
* an exception
* device interrupt


## Interrupts and device drivers

### functions of driver

* configures the device hardware
* tells the device to perform operations
* handles the resulting interrupts
* interacts with processes that may be waiting for I/O from the device

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

***

## tcp/ip(v4)

### what is?

* They are part of the main [protocols](https://en.wikipedia.org/wiki/Communications_protocol) of the [Internet protocol suite](https://en.wikipedia.org/wiki/Internet_protocol_suite)
* They are a group of interfaces in fact.

### what relation?

* OS implements tcp/ip. 
* TCP/IP software usually resides in the operating system(kernel code).

***

## socket

### reference

* [wiki network socket](https://en.wikipedia.org/wiki/Network_socket)
* [wike berkeley sockets](https://en.wikipedia.org/wiki/Berkeley_sockets)

### diffrent meanings

* network/internet socket - it is a software structure within a network node of a computer network that serves as an endpoint for sending and receiving data across the network.
* socket descriptor - it is a handle created by the network protocol stack api for each socket(internet socket) created by an application. In Unix-like operating systems, this descriptor is a type of file descriptor.
* socket addresses - it is is the triad of transport protocol, IP address, and port number. Transport protocol usually is tcp, but it doesn't have to, for example udp instead.
* socket api - The application programming interface (API) that programs use to communicate with the protocol stack, using network sockets, is called a socket API. Internet socket APIs are usually based on the Berkeley sockets standard.

### diffrent types

* datagram sockets - Connectionless sockets, which use User Datagram Protocol(UDP).
* stream sockets - Connection-oriented sockets, which use TCP, SCTP or DCCP. 
* raw sockets - Allow direct sending and receiving of IP packets without any protocol-specific transport layer formatting. 

### what relation?

* OS has implemented the netword protocol stack, it offer a set of socket api, usually Berkeley sockets api. 

### AF_UNIX vs AF_INET

* reference
  * [example to explain unix domain socket - AF_INET vs AF_UNIX](https://stackoverflow.com/questions/21032562/example-to-explain-unix-domain-socket-af-inet-vs-af-unix)
  * [wiki Unix domain socket](https://en.wikipedia.org/wiki/Unix_domain_socket)
* what is?
  * It is a `socket address familiy` defined in [socket.h](https://github.com/openbsd/src/blob/master/sys/sys/socket.h)
* AF_UNIX
  * It is a data communications endpoint for exchanging data between processes executing on the **same** host operating system. 
  * The API for Unix domain sockets is similar to that of an Internet socket, but rather than using an underlying network protocol, all communication occurs entirely within the operating system kernel.
  * The Unix domain socket facility is a standard component of [POSIX](https://en.wikipedia.org/wiki/POSIX) [operating systems](https://en.wikipedia.org/wiki/Operating_system).
* AF_NET
  * AF_INET sockets sit at the top of a full TCP/IP stack. 

## Berkeley sockets api

### what is? 

*  it is an application programming interface(API) for Internet sockets and Unix domain sockets, used for inter-process communication. 
* It is commonly implemented as a library of linkable modules.

### feature

* The Berkeley sockets API represents network socket as a file descriptor (file handle) in the Unix philosophy that provides a common interface for input and output to streams of data.


### defination - [socket.h](https://github.com/openbsd/src/blob/master/sys/sys/socket.h)

```c++
/* Types */
#define	SOCK_STREAM	1		/* stream socket */
#define	SOCK_DGRAM	2		/* datagram socket */
#define	SOCK_RAW	3		/* raw-protocol interface */
#define	SOCK_RDM	4		/* reliably-delivered message */
#endif

#ifndef _TIMEVAL_DECLARED
#define _TIMEVAL_DECLARED
struct timeval {
	time_t		tv_sec;		/* seconds */
	suseconds_t	tv_usec;	/* and microseconds */
};
#endif

/*
 * Address families.
 */
#define	AF_UNSPEC	0		/* unspecified */
#define	AF_UNIX		1		/* local to host */
#define	AF_LOCAL	AF_UNIX		/* draft POSIX compatibility */
#define	AF_INET		2		/* internetwork: UDP, TCP, etc. */

struct sockaddr {
	__uint8_t    sa_len;		/* total length */
	sa_family_t sa_family;		/* address family */
	char	    sa_data[14];	/* actually longer; address value */
};

int	socket(int, int, int);
int	bind(int, const struct sockaddr *, socklen_t);
int	listen(int, int);
int	accept(int, struct sockaddr *, socklen_t *);
int	connect(int, const struct sockaddr *, socklen_t);
```

### purpose of each api

* nonblocking api
  * `socket()` - This function is used to create a new socket. It does not block and returns a file descriptor for the socket.
  * `bind()` - This function is used to bind a socket to a specific address and port. It does not block and returns immediately after it is called.
* blocking api
  * `listen()` - This function is used to set up a socket to listen for incoming client connections. It puts the socket in a passive listener state and blocks until a connection is established.
  * `accept()` - This function is used to accept an incoming client connection on a server socket. It blocks until a client connection is established and returns a new socket object to handle the communication with the client.
  * `connect()` - This function is used to establish a connection to a remote server. It blocks until the connection is established or an error occurs.
