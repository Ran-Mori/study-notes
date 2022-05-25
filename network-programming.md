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
* TCP/IP are implemented in kernel.
* layer
  * application
  * tcp/udp
  * ipv4/ipv6
* raw socket
  * an application to bypass tcp and use IPv4 or IPv6 directly
* ocus of this book
  * how to write applications using sockets that use either TCP or UDP
* reason for the interface of socket
  * application knows little about the communication details. The lower four layers know little about the application, but handle all the communication details:ending data, waiting for acknowledgments, sequencing data that arrives out of order, calculating and verifying checksums, and so on.
  * application what is called a *user process* while the lower four layers are normally provided as part of the operating system (OS) kernel.