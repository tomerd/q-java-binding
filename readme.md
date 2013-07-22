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

q bindings for java uses JNI to bind to q's native API. q is represented by the Q class which exposes a simple API:

* *getVersion():* returns the version of q

* *connect(config):* connects to the library and initialized a connection to the backend specified by the config param. see further documentation on backends at the core library.

* *disconnect:* disconnect from the library. no further calls can be made after this.

* *post(queue, data):* posts a job to a named queue (aka channel).

* *post(queue, uid, data):* posts a uniquely identified job to a named queue (aka channel). useful if you are planning to update the job.

* *postAt(queue, data, run_at):* posts a job to a named queue (aka channel) in a future date.

* *postAt(queue, uid, data, run_at):* posts a uniquely identified job to a named queue (aka channel) in a future date. useful if you are planning to rescheduling the job.
	

* *reschedule(uid, run_at):* reschedule the job identified by uid to a new target date.

* *cancel(uid):* cancel a scheduled jobs.

* *worker(queue, Worker):* register a worker for a named queue (aka channel). a worker is implemented by implementing the Worker interface which includes a single "perform" method. the worker will start receiving jobs immediately.

* *observer(queue, Observer):* register an observer for a named queue (aka channel). an observer is implemented by implementing the Observer interface which includes a single "perform" method. the observer will start receiving jobs immediately. the difference between an observer and a worker is that the observer is passive in nature and as such is notified only after a
worker has completed the job successfully.

* *drop:* careful, drops all queues! useful in development scenarios when you need to flush the entire queue system.

##### important

* qlib does not build with the JNI headers by default. to enable Java support you must build it with the "--with-java" flag

		aclocal && autoreconf -i && automake
		./configure --with-java
		make && make install

* make sure to set java.library.path to where qlib is installed (normally /usr/local/lib). otherwise the JVM will not be able to find the q library.

##### usage example

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
