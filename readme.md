***************************************************************************************

This project is work in progress. if you are interested in contributing or otherwise have input
please touch base via github

***************************************************************************************

### about

q is a queueing toolkit. the idea is to provide a universal application programming interface that can be used throughout the entire
application development lifecycle without the need to commit to a specific queueing technology or to set up complex queueing environments 
where such are not required. you can think of it as an ORM for queueing. 

q runs on multiple back-ends and has bindings to many programing languages. and so, while during development you will most likely run it in-memory and let it clear when the process dies, you may choose a redis back-end on your test environment and running dedicated servers backed by rabbitMQ, amazon SQS or some other enterprise queueing system on production. 

see more about the core library at https://github.com/tomerd/q

### q bindings for java

##### Important


* qlib does not build with the JNI headers by default. to enable Java support you must build it with the "--with-java" flag

		aclocal && autoreconf -i && automake
		./configure --with-java
		make && make install

* make sure to set java.library.path to where qlib is installed (normally /usr/local/lib). otherwise Java will not be able to load the q library.

##### Example

	Q q = new Q();
    String v = q.getVersion();
    System.out.println("using q version " +  v);

    q.connect("{ \"driver\": \"redis\", \"host\": \"127.0.0.1\" }");

    q.worker("channel1", new Worker()
    {
        public void perform(String data)
        {
            System.out.println("java worker1 received '" + data + "'");
        }
    });

    q.post("channel1", "java 1");
    q.postAt("channel1", "java 2", System.currentTimeMillis() + 5 * 1000);
    q.post("channel1", "java 3");
    q.postAt("channel1", "java 4", System.currentTimeMillis() + 3 * 1000);
    q.post("channel1", "java 5");
    q.postAt("channel1", "java 6", System.currentTimeMillis() + 3 * 1000);

    Thread.sleep(7*1000);

    q.disconnect();
    System.out.println("done");
