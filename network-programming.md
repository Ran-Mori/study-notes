# Network-Programming

### Introduction

* Routers are the building blocks of WANs. The largest WAN today is the Internet. Many companies build their own WANs and these private WANs may or may not be connected to the Internet.

### A Simple Daytime Client

* `sa_family`
  * `AF_INET` is an address family that is used to designate the type of addresses that your socket can communicate with (in this case, Internet Protocol v4 addresses). 
  * `AF_UNIX`、`AF_IMPLINK`、`AF_PUP`……
* differences of the term socket
  * the API that we are using is called the sockets API
  * a function named socket that is part of the sockets API
  * a TCP socket, which is synonymous with a TCP endpoint.
* `inet_pton` function
  * to convert an ASCII dotted-decimal string into the correct format
* read
  * `while ( (n = read(sockfd, recvline, MAXLINE)) > 0) { //... }`
  * Normally, a single segment containing all 26 bytes of data is returned, but with larger data sizes, we cannot assume that the server's reply will be returned by a single read.
  * 
* record boundaries
  * TCP  is a *byte-stream* protocol with no record boundaries.
  * In this example, the end of the record is being denoted by the server closing the connection.This technique is also used by version 1.0 of HTTP.
  * Other techniques are available. For example, the SMTP marks the end of a record with the two-byte sequence of an ASCII carriage return followed by an ASCII linefeed.
  * The important concept here is that TCP itself provides no record markers: If an application wants to delineate the ends of records, it must do so itself and there are a few common ways to accomplish this.
* terminate program
  * Unix always closes all open descriptors when a process terminates, so our TCP socket is now closed.

### Protocol-Dependen

* Our program in Figure 1.5 is *protocol-dependent* on IPv4. We allocate and initialize a sockaddr_in structure, we set the family of this structure to AF_INET, and we specify the first argument to socket as AF_INET.
* To modify the program to work under IPv6, we must change the code.

### A Simple Daytime Server

* Bind server's well-known port to socket
  * `Bind(listenfd, (SA *) &servaddr, sizeof(servaddr));`
* Convert socket to listening socket
  * By calling listen, the socket is converted into a listening socket

### OSI Model

* The sockets programming interfaces described in this book are interfaces from the upper three layers (the application) into the transport layer. Or the upper one layer in five layers network models.
* TCP/IP are implemented in kernel. And the data will be copied between kernel and user process.
* layer
  * application
  * tcp/udp
  * ipv4/ipv6
* raw socket
  * an application to bypass tcp and use IPv4 or IPv6 directly
* focus of this book
  * how to write applications using sockets that use either TCP or UDP
* reason for the interface of socket
  * application knows little about the communication details. The lower four layers know little about the application, but handle all the communication details:ending data, waiting for acknowledgments, sequencing data that arrives out of order, calculating and verifying checksums, and so on.
  * application what is called a *user process* while the lower four layers are normally provided as part of the operating system (OS) kernel.

### I/O multiplexing

* what is it
  * the capability to tell the kernel that we want to be notified if one or more I/O conditions are ready

### I/O Models

* five I/O models
  * blocking I/O
  * nonblocking I/O
  * I/O multiplexing (select and poll)
  * signal driven I/O (SIGIO)
  * asynchronous I/O (the POSIX aio_functions)
* nonblocking I/O
  * It equals to `poll(轮询)`
  * When an I/O operation that I request cannot be completed, do not put the process to sleep, but return an error instead.
* multiplexing model
  * `select` is a system call
  * It will be blocked when make a call of `select`，the blocking will be end only if one of descriptors is ready.
* signal driven I/O (SIGIO)
  * Using signals to tell the kernel to notify us with the SIGIO signal when the descriptor is ready.
  * progress
    * install a signal handler using the `sigaction` system call. The return from this system call is immediate and our process continues; it is not blocked.
    * the specified signal is generated for our process when deciptor is ready
    * handle the signal
  * it is just the same as `handler in android`

### select

* what is it

  * a system call

* what it does

  * This function allows the process to instruct the kernel to wait for any one of multiple events to occur and to wake up the process only when one or more of these events occurs or when a specified amount of time has passed.

* definition

  * `int select(int maxfdp1, fd_set readset, fd_set writeset, fd_set exceptset, const struct timeval timeout)`
  * Returns: positive count of ready descriptors, 0 on timeout, –1 on error
  * if first three arguments are all null, then it is a higher precision timer than the normal Unix `sleep` function.

* Descriptor Ready

  * The number of bytes of data in the socket receive/send buffer is greater than

    or equal to the current size of the low-water mark for the socket receive buffer.

  * The read half of the connection is closed.

* `select example`

  ```c
  void
  str_cli(FILE *fp, int sockfd)
  {
  	int			maxfdp1;
  	fd_set		rset;
  	char		sendline[MAXLINE], recvline[MAXLINE];
  
  	FD_ZERO(&rset);
  	for ( ; ; ) {
  		FD_SET(fileno(fp), &rset);
  		FD_SET(sockfd, &rset);
  		maxfdp1 = max(fileno(fp), sockfd) + 1;
  		Select(maxfdp1, &rset, NULL, NULL, NULL);
  
  		if (FD_ISSET(sockfd, &rset)) {	/* socket is readable */
  			if (Readline(sockfd, recvline, MAXLINE) == 0)
  				err_quit("str_cli: server terminated prematurely");
  			Fputs(recvline, stdout);
  		}
  
  		if (FD_ISSET(fileno(fp), &rset)) {  /* input is readable */
  			if (Fgets(sendline, MAXLINE, fp) == NULL)
  				return;		/* all done */
  			Writen(sockfd, sendline, strlen(sendline));
  		}
  	}
  }

### poll

* definition
  * `int poll (struct pollfd *fdarray, unsigned long nfds, int timeout);`
  * return: count of ready descriptors, 0 on timeout, –1 on error

* `pollfd`

  ```c
  struct pollfd {
  	int     fd; 
  	short   events; // interested events, such as reading, writting
  	short   revents; // events that really currently happens
  };

* `poll` is more efficient than `select`

### epoll

* featrues
  * only Linux supports
  * added to linux kernel in 2002
  * It differs both from *poll* and *select* in such a way that it keeps the information about the currently monitored descriptors and associated events inside the kernel, and exports the API to add/remove/modify those.
* more details please refer [this page](https://www.ulduzsoft.com/2014/01/select-poll-epoll-practical-difference-for-system-architects/)