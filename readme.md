***************************************************************************************

This project is work in progress. if you are interested in contributing or otherwise have input
please touch base via github

***************************************************************************************

### about

q is a queueing framework. the idea behind it is to provide a universal application interface that can be used across all
development phases and scaling requirements. q runs on multiple back-ends and has binding to many programing languages. and so
while during development you probably want to run it using an in-memory back-end that clears with the process, you may choose 
to use redis on your test environment and amazon SQS on production.

https://github.com/tomerd

### q bindings for java

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

    Thread.sleep(5*1000);

    q.disconnect();
    System.out.println("done");
