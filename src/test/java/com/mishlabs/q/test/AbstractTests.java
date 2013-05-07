package com.mishlabs.q.test;

import com.mishlabs.q.Q;
import junit.framework.Assert;
import org.junit.Test;

public abstract class AbstractTests
{
    String connection_string;

    @Test
    public void test1()
    {
        try
        {
            Q q = new Q();

            System.out.println("testing with " +  this.getClass().getSimpleName());
            System.out.println("using q version " +  q.getVersion());

            q.connect(connection_string);
            q.flush();

            TestWorker receiver = new TestWorker()
            {
                public void perform(String data)
                {
                    received++;
                    System.out.println("java worker received '" + data + "'");
                }
            };
            q.worker("channel1", receiver);

            int total = 1000;
            for (int index=0; index < total; index++)
            {
                q.post("channel1", "java " + index);
            }

            while (receiver.received < total)
            {
                Thread.sleep(1*1000);
                System.out.println("recieved " + receiver.received + "/" + total);
            }

            Assert.assertEquals("expected " + total, total, receiver.received);

            q.disconnect();
            System.out.println("done");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void test2()
    {
        try
        {
            Q q = new Q();

            System.out.println("testing with " +  this.getClass().getSimpleName());
            System.out.println("using q version " +  q.getVersion());

            q.connect(connection_string);
            q.flush();

            TestWorker receiver = new TestWorker()
            {
                public void perform(String data)
                {
                    received++;
                    System.out.println("java worker received '" + data + "'");
                }
            };
            q.worker("channel1", receiver);


            q.post("channel1", "java 1");
            q.postAt("channel1", "java 2", System.currentTimeMillis() + 5 * 1000);
            q.post("channel1", "java 3");
            q.postAt("channel1", "java 4", System.currentTimeMillis() + 3 * 1000);
            q.post("channel1", "java 5");
            q.postAt("channel1", "java 6", System.currentTimeMillis() + 3 * 1000);

            Thread.sleep(2*1000);
            Assert.assertEquals("expected 3", 3, receiver.received);

            Thread.sleep(2*1000);
            Assert.assertEquals("expected 5", 5, receiver.received);

            Thread.sleep(2*1000);
            Assert.assertEquals("expected 6", 6, receiver.received);

            q.disconnect();
            System.out.println("done");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void test3()
    {
        try
        {
            Q q = new Q();

            System.out.println("testing with " +  this.getClass().getSimpleName());
            System.out.println("using q version " +  q.getVersion());

            q.connect(connection_string);
            q.flush();

            TestWorker receiver = new TestWorker()
            {
                public void perform(String data)
                {
                    received++;
                    System.out.println("java worker received '" + data + "'");
                }
            };
            q.worker("channel1", receiver);

            long now = System.currentTimeMillis();
            q.postAt("channel1", "test1", "java 1", now+2*1000);
            q.postAt("channel1", "test2", "java 2", now+4*1000);

            Thread.sleep(3*1000);
            Assert.assertEquals("expected 1", 1, receiver.received);

            now = System.currentTimeMillis();
            q.update("test2",now+4*1000);

            Thread.sleep(2*1000);
            Assert.assertEquals("expected 1", 1, receiver.received);

            Thread.sleep(3*1000);
            Assert.assertEquals("expected 2", 2, receiver.received);

            q.disconnect();
            System.out.println("done");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void test4()
    {
        try
        {
            Q q = new Q();

            System.out.println("testing with " +  this.getClass().getSimpleName());
            System.out.println("using q version " +  q.getVersion());

            q.connect(connection_string);
            q.flush();

            TestWorker receiver = new TestWorker()
            {
                public void perform(String data)
                {
                    received++;
                    System.out.println("java worker received '" + data + "'");
                }
            };
            q.worker("channel1", receiver);

            long now = System.currentTimeMillis();
            q.postAt("channel1", "test1", "java 1", now+4*1000);

            Thread.sleep(2*1000);
            Assert.assertEquals("expected 0", 0, receiver.received);

            q.remove("test1");

            Thread.sleep(3*1000);
            Assert.assertEquals("expected 0", 0, receiver.received);

            q.disconnect();
            System.out.println("done");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
