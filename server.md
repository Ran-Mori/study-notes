# server

## OSS

### reference

1. [object storage - wiki](https://en.wikipedia.org/wiki/Object_storage)
2. [object storage - google cloud](https://cloud.google.com/learn/what-is-object-storage#:~:text=Object%20storage%20is%20a%20data,for%20easy%20access%20and%20retrieval.)
3. [outscale](https://docs.outscale.com/en/userguide/About-OOS.html)

### introduction

* what is? - It is a data storage architecture for storing unstructured data, which sections data into units—objects—and stores them in a structurally flat data environment. Each object includes the data, metadata, and a unique identifier that applications can use for easy access and retrieval. 
* what's for? - storing amounts of unstructured data in which data is written once and read once (or many times)
  1. videos and photos on facebook
  2. songs on Spotify
  3. files in online collaboration services

### data component

1. data itself
2. a variable amount of metadata
3. a uuid

### buckets

* it is a logical resource for hosting objects.
* the name of a bucket must be unique for the whole Region

### how it work?

1. save a object together with its relevant metadata and a custom identifier into a flat data environment known as a storage pool. 
2. use the unique identifier and the metadata to find the object you need.

### features

* a unique identifier within a bucket, you can use it to locate it.
* explicitly separates file metadata from data to support additional capabilities.

### storage classes

* File storage
  * It stores and organizes data into folders. 
  * To locate a piece of data, you’ll need to know the correct path to find it.
  * Searching and retrieving data files can become time-consuming as the number of files grows.
* Block storage
  * It breaks files into separate blocks and storing them separately.
  * It will assign a unique identifier to each chunk of raw data, which can then be used to reassemble them into the complete file when you need to access it.
  * Block storage doesn’t require a single path to data, so you can store it wherever is.
  * It suits for applications that need minimal delay and consistent performance. 
  * it can be expensive, offers no metadata capabilities, and requires an operating system to access blocks. 
* Object storage
  * It works best for static storage, especially for unstructured data, where you write data once but may need to read it many times. 
  * it’s not a good solution for dynamic data that is changing constantly as you’ll need to rewrite the entire object to modify it

### advs

* Massive scalability
* Reduced complexity -  no folders or directories
* Searchability - Metadata makes it easy to search through and navigate without the need of a separate application.

### commercialized impl

* layer - indexing layer, real storage layer
* use mysql to save the index, and use the index data to really access the data.

## protobuf vs json

### json

```json
{
	"name": "John"
}
```

* 佔據18bytes

### protobuf

* `.proto`聲明

  ```protobuf
  syntax = "proto3";
  
  message Person {
   string name = 1;
  }
  ```

* binary representation

  * **`0a 04 4a 6f 68 6e`** (represented in hexadecimal) 只占據6bytes

* encoded in base 64

  * **`CgRKb2hu`** 佔據8bytes

### why base 64

* 為什麼二進制佔據空間小，但還要進行一遍base64編碼？
* reasons
  1. Many of the systems we use every day are designed to handle **text**, not raw binary data. These "text-based" systems can get confused when they encounter raw binary.
  2. The Null Byte (`0x00`): In many programming languages (like C), this byte signifies the end of a string. If a text parser encounters a null byte in the middle of your data, it might think the message has ended prematurely, and truncate your data.
  3. Control Characters: Other bytes are interpreted as control signals, like "new line" (0x0A), "carriage return" (0x0D), or "end of transmission" (0x04). A system processing your data as text could misinterpret these bytes and corrupt the message's formatting or terminate the connection.
  4. Encoding Issues: Text-based systems expect data to follow a specific character encoding (like UTF-8 or ASCII). Raw binary data doesn't conform to these encodings, which can lead to errors.
* so text-based protocol is safer

