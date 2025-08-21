📌 Overview

This project demonstrates the implementation of a web server in Java using TCP sockets.
It explores single-threaded, multi-threaded, and thread-pool-based server models to compare performance, scalability, and resource usage.

🚀 Features

Built from scratch using Java Sockets API

Single-threaded server – baseline model, processes one request at a time

Multi-threaded server – spawns a new thread for each client request

Thread-pool server – leverages ExecutorService to optimize resource management

Handles 100+ concurrent client connections

Demonstrates concepts of multithreading, context switching, and concurrency control

🛠️ Tech Stack

Language: Java

Core Concepts: TCP/IP Sockets, Multithreading, ExecutorService

Tools: JDK, IntelliJ/VS Code

📂 Project Structure
/src
 ├── singlethreaded
 │    ├── Client.java
 │    └── Server.java
 ├── multithreaded
 │    ├── Client.java
 │    └── Server.java
 ├── threadpool
 │    └── Server.java

⚡ How to Run

Clone the repository:

git clone https://github.com/<your-username>/multi-threaded-webserver-java.git
cd multi-threaded-webserver-java


Compile the Java files:

javac src/<path-to-file>/*.java


Run the server first:

java Server


Run one or multiple clients:

java Client

📊 Comparison Results

Single-threaded: ❌ Blocks on each client, cannot scale

Multi-threaded: ⚡ Handles multiple clients, but creates too many threads under high load

Thread-pool: ✅ Balanced, reuses threads efficiently and improves throughput

📖 Learning Outcomes

Hands-on understanding of network programming in Java

Learned trade-offs between different server models

Gained experience in concurrency, synchronization, and resource management
