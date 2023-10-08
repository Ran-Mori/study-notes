# Network

## Introduction

* Routers are the building blocks of WANs. The largest WAN today is the Internet. Many companies build their own WANs and these private WANs may or may not be connected to the Internet.

***

## A Simple Daytime Client

### sa_family

* `AF_INET` is an address family that is used to designate the type of addresses that your socket can communicate with (in this case, Internet Protocol v4 addresses). 
* `AF_UNIX`、`AF_IMPLINK`、`AF_PUP`……

### differences of the term socket

* the API that we are using is called the sockets API
* a function named socket that is part of the sockets API
* a TCP socket, which is synonymous with a TCP endpoint.

### inet_pton function

* to convert an ASCII dotted-decimal string into the correct format

### read

* `while ( (n = read(sockfd, recvline, MAXLINE)) > 0) { //... }`
* Normally, a single segment containing all 26 bytes of data is returned, but with larger data sizes, we cannot assume that the server's reply will be returned by a single read.

### record boundaries

* TCP  is a *byte-stream* protocol with no record boundaries.
* In this example, the end of the record is being denoted by the server closing the connection.This technique is also used by version 1.0 of HTTP.
* Other techniques are available. For example, the SMTP marks the end of a record with the two-byte sequence of an ASCII carriage return followed by an ASCII linefeed.
* The important concept here is that TCP itself provides no record markers: If an application wants to delineate the ends of records, it must do so itself and there are a few common ways to accomplish this.

### terminate program

* Unix always closes all open descriptors when a process terminates, so our TCP socket is now closed.

### Protocol-Dependen

* Our program in Figure 1.5 is *protocol-dependent* on IPv4. We allocate and initialize a sockaddr_in structure, we set the family of this structure to AF_INET, and we specify the first argument to socket as AF_INET.
* To modify the program to work under IPv6, we must change the code.

### A Simple Daytime Server

* Bind server's well-known port to socket
  * `Bind(listenfd, (SA *) &servaddr, sizeof(servaddr));`
* Convert socket to listening socket
  * By calling listen, the socket is converted into a listening socket

***

## OSI Model

* The sockets programming interfaces described in this book are interfaces from the upper three layers (the application) into the transport layer. Or the upper one layer in five layers network models.
* TCP/IP are implemented in kernel. And the data will be copied between kernel and user process.

### layer

* application
* tcp/udp
* ipv4/ipv6

### raw socket

* an application to bypass tcp and use IPv4 or IPv6 directly

### focus of this book

* how to write applications using sockets that use either TCP or UDP

### reason for the interface of socket

* application knows little about the communication details. The lower four layers know little about the application, but handle all the communication details:ending data, waiting for acknowledgments, sequencing data that arrives out of order, calculating and verifying checksums, and so on.
* application what is called a *user process* while the lower four layers are normally provided as part of the operating system (OS) kernel.

***

## I/O Models

### five I/O models

1. blocking I/O

2. nonblocking I/O

3. I/O multiplexing

   1. select

   2. poll

   3. epoll or kqueue

4. signal driven I/O (SIGIO)

5. asynchronous I/O (the POSIX aio_functions)

### blocking I/O

* the process calls the system call (read, write), and it does not return until the data arrives, or an error occurs. 

### nonblocking I/O

* It equals to `poll(轮询)`. The application is continually polling the kernel to see if some operation is ready. 
* When an I/O operation that I request cannot be completed, do not put the process to sleep, but return an error instead.

### signal driven I/O (SIGIO)

* Using signals to tell the kernel to notify us with the SIGIO signal when the descriptor is ready.
* progress
  * install a signal handler using the `sigaction` system call. The return from this system call is immediate and our process continues; it is not blocked.
  * the specified signal is generated for our process when deciptor is ready
  * handle the signal
* it is just the same as `handler in android`

***

## select

### what is it

* a standard system call in Unix-like operating systems.

### what it does

* This function allows the process to instruct the kernel to wait for any one of multiple events to occur and to wake up the process only when one or more of these events occurs or when a specified amount of time has passed. 

### definition

* `int select(int maxfdp, fd_set readset, fd_set writeset, fd_set exceptset, const struct timeval timeout)`
* args
  * maxfdp - the highest-numbered file descriptor in any of the three sets, plus 1.   
* returns
  * success - the number of file descriptors contained in the three returned descriptor sets.
  * 
    if the timeout expired before any file descriptors became ready. - zero
  * error - -1
* if first three arguments are all null, then it is a higher precision timer than the normal Unix `sleep` function.

### condition of read descriptor ready

* The number of bytes of data in the socket receive/send buffer is greater than

  or equal to the current size of the low-water mark for the socket receive buffer.

* The read half of the connection is closed.

### example

```c++
#include <iostream>
#include <sys/select.h>
#include <unistd.h>

int main() {
  fd_set readfds;
  FD_ZERO(&readfds);
  FD_SET(STDIN_FILENO, &readfds); // set the standard input file descriptor

  struct timeval timeout;
  timeout.tv_sec = 5; // set the timeout to 5 seconds
  timeout.tv_usec = 0;

  int num_ready = select(STDIN_FILENO + 1, &readfds, NULL, NULL, &timeout);
  if (num_ready == -1) {
    std::cerr << "Error occurred during select system call\n";
    return 1;
  } else if (num_ready == 0) {
    std::cout << "Timeout occurred\n";
  } else {
    if (FD_ISSET(STDIN_FILENO, &readfds)) {
      std::cout << "Data is available on standard input\n";
      char input[1024];
      std::cin.getline(input, sizeof(input));
      std::cout << "You entered: " << input << "\n";
    }
  }
  return 0;
}
```

***

## poll

### what is ?

* it is a syterm call.
* it is part of the POSIX standard. it is available on POSIX-compliant operating systems, such as Linux and Unix.

### definition

* `int poll(struct pollfd *fds, nfds_t nfds, int timeout);`
* parameter
  * nfds - the number of items in the fds array in nfds.
* return: count of ready descriptors, 0 on timeout, –1 on error

### pollfd

```c
struct pollfd {
	int     fd; 
	short   events; // interested events, such as reading, writting
	short   revents; // events that really currently happens
};
```

### example

```c++
#include <iostream>
#include <sys/poll.h>
#include <unistd.h>

int main() {
    int fd = STDIN_FILENO; // File descriptor to monitor (standard input)
    struct pollfd fds[1];
    fds[0].fd = fd;
    fds[0].events = POLLIN; // Monitor input events

    while (true) {
        int ret = poll(fds, 1, -1); // Wait indefinitely for an event
        if (ret < 0) {
            std::cerr << "poll error\n";
            break;
        }
        if (fds[0].revents & POLLIN) {
            std::cout << "input available\n";
            char buf[256];
            int n = read(fd, buf, sizeof(buf));
            if (n < 0) {
                std::cerr << "read error\n";
                break;
            }
            if (n == 0) {
                std::cout << "end of input\n";
                break;
            }
            buf[n] = '\0';
            std::cout << "read " << n << " bytes: " << buf << '\n';
        }
    }
    return 0;
}
```

***

## epoll

### what is?

* it is is a Linux kernel system call.
* it is a Linux-specific interface.

### if not Linux?

*  `kqueue` on FreeBSD and macOS
* `IOCP` on Windows
*  `/dev/poll` on Solaris

### definition

* `int epoll_create1(int flags);`
  * return - its file descriptor
* `int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event);`
  * param op - can be ADD, MODIFY or DELETE.
* `int epoll_wait(int epfd, struct epoll_event *events, int maxevents, int timeout);`

### key struct

* epoll_event

  ```c++
  typedef union epoll_data {
    void        *ptr;
    int          fd;
    uint32_t     u32;
    uint64_t     u64;
  }
  ```

* epoll_data

  ```c++
  struct epoll_event {
    uint32_t     events; 
    epoll_data_t data;
  };
  ```

  * events - is a bit mask composed by ORing together zero or diffrent event types(read, write and so on)
  * data - the data that the kernel should save and then return (via epoll_wait(2)) when this file descriptor becomes ready.

### example

```c++
#include <iostream>
#include <sys/epoll.h>
#include <unistd.h>
#include <fcntl.h>
#include <cstring>

using namespace std;

const int MAX_EVENTS = 10;
const int TIMEOUT = 5000;

int main() {
    // Create a file descriptor for reading from stdin
    int stdin_fd = fileno(stdin);

    // Set stdin to non-blocking mode
    int flags = fcntl(stdin_fd, F_GETFL, 0);
    fcntl(stdin_fd, F_SETFL, flags | O_NONBLOCK);

    // Create an epoll instance
    int epoll_fd = epoll_create1(0);
    if (epoll_fd == -1) {
        cerr << "Error creating epoll instance: " << strerror(errno) << endl;
        return 1;
    }

    // Add stdin to the epoll instance
    struct epoll_event event;
    event.data.fd = stdin_fd;
    event.events = EPOLLIN | EPOLLET; // Monitor for read events and use edge-triggered mode
    if (epoll_ctl(epoll_fd, EPOLL_CTL_ADD, stdin_fd, &event) == -1) {
        cerr << "Error adding file descriptor to epoll instance: " << strerror(errno) << endl;
        close(epoll_fd);
        return 1;
    }

    // Wait for events
    struct epoll_event events[MAX_EVENTS];
    int num_events = epoll_wait(epoll_fd, events, MAX_EVENTS, TIMEOUT);

    // Handle events
    for (int i = 0; i < num_events; ++i) {
        if (events[i].data.fd == stdin_fd) {
            // Read input from stdin
            char buffer[1024];
            int num_bytes = read(stdin_fd, buffer, sizeof(buffer));
            if (num_bytes == -1 && errno != EAGAIN) {
                cerr << "Error reading from stdin: " << strerror(errno) << endl;
                break;
            } else if (num_bytes > 0) {
                // Echo input back to stdout
                write(STDOUT_FILENO, buffer, num_bytes);
            }
        }
    }

    // Clean up
    close(epoll_fd);
    return 0;
}
```

### refer

* more details please refer [this page](https://www.ulduzsoft.com/2014/01/select-poll-epoll-practical-difference-for-system-architects/)

### poll and epoll

* `epoll` is generally considered to be more efficient and scalable than `poll`
* `poll` is its simplicity, as it requires fewer system calls and data structures than `epoll`.
* `poll` can become less efficient than `epoll` when monitoring large numbers of file descriptors, since it requires iterating over the entire list of file descriptors each time it is called.
* It differs both from *poll* and *select* in such a way that it keeps the information about the currently monitored descriptors and associated events inside the kernel, and exports the API to add/remove/modify those.
* `poll()` uses a linear list to manage the file descriptors, while `epoll()` uses a scalable data structure based on a red-black tree. 
* `poll()` blocks the calling thread until an event occurs on one of the monitored file descriptors, while `epoll()` uses an event-driven model and can be used in either blocking or non-blocking mode.

***

## QUIC

### reference

* [QUIC - wiki](https://en.wikipedia.org/wiki/QUIC)

### what is 

* It is is a general-purpose transport layer network protocol.

### network stack

* traditional tcp
  * http - user space
  * tls - user space
  * tcp - kernel space
* QUIC
  * http - user space
  * quic - user space
    * tls - user space, and it is included in quic
  * udp - kernel space

### disadv of tcp

* head of line
  * TCP will see any error on a connection as a blocking operation, stopping further transfers until the error is resolved or the connection is considered failed.
  * If a single connection is being used to send multiple streams of data, as is the case in the HTTP/2 protocol, all of these streams are blocked although only one of them might have a problem. 
* many handshakes
  * tcp deliberately contains little understanding of the data it transmits.
  * if use tls, first we should make tcp handshakes, then make tls handshakes. So the latency may be too long.

### characteristics

* goal
  * QUIC aims to be nearly equivalent to a TCP connection but with much-reduced latency.
* simple setup
  * reduce handshake times
* use udp instead of tcp
  * it seperately implements loss recovery. and it is more efficient than tcp
  * it seperately implements congess control.
* improve performance during network-switching events.
* user space
  * less systerm call, less context switch

***