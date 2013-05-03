package com.mishlabs;

import com.mishlabs.q.Observer;
import com.mishlabs.q.Q;
import com.mishlabs.q.Worker;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Q q = new Q();
            String v = q.getVersion();
            System.out.println("using q version " +  v);

            q.connect(null);

            q.worker("channel1", new Worker()
            {
                public void perform(String data)
                {
                    System.out.println("java worker1 received '" + data + "'");
                }
            });

            q.worker("channel1", new Worker()
            {
                public void perform(String data)
                {
                    System.out.println("java worker2 received '" + data + "'");
                    throw new RuntimeException("x");
                }
            });

            q.observer("channel1", new Observer()
            {
                public void perform(String data)
                {
                    System.out.println("java observer1 received '" + data + "'");
                }
            });

            q.post("channel1", "java 11");
            q.postAt("channel1", "java 12", System.currentTimeMillis() + 5 * 1000);
            q.post("channel1", "java 13");
            q.postAt("channel1", "java 14", System.currentTimeMillis() + 3 * 1000);
            q.post("channel1", "java 15");
            q.postAt("channel1", "java 16", System.currentTimeMillis() + 3 * 1000);

            Thread.sleep(30000);

            q.disconnect();
            System.out.println("done");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
