package com.mishlabs.q;

import java.util.Date;

public class Q
{
    private native String native_version();

    private native long native_connect(String config);

    private native void native_disconnect(long q);

    private native String native_post(long q, String queue, String data, long at);

    private native void native_worker(long q, String queue, NativeWorker worker);

    private native void native_observer(long q, String queue, NativeObserver observer);

    static
    {
        // FIXME!
        //System.loadLibrary("libq");
        System.load("/usr/local/lib/libq.dylib");
    }

    private long qp = 0;

    public Q()
    {
    }

    public String getVersion()
    {
        return this.native_version();
    }

    public void connect(String config) throws Exception
    {
        if (0 != qp) throw new IllegalStateException("Q already connected");
        qp = this.native_connect(config);
    }

    public void disconnect() throws Exception
    {
        if (0 == qp) return;
        this.native_disconnect(qp);
        qp = 0;
    }

    public String post(String queue, String data) throws Exception
    {
        return this.postAt(queue, data, null);
    }

    public String postAt(String queue, String data, Date at) throws Exception
    {
        return this.postAt(queue, data, null != at ? at.getTime() : 0);
    }

    public String postAt(String queue, String data, long at) throws Exception
    {
        if (0 == qp) throw new IllegalStateException("Q disconnected");
        if (null == queue) throw new IllegalArgumentException();
        if (null == data) throw new IllegalArgumentException();
        return this.native_post(qp, queue, data, at/1000);
    }

    public void worker(String queue, final Worker worker) throws Exception
    {
        if (0 == qp) throw new IllegalStateException("Q disconnected");
        if (null == queue) throw new IllegalArgumentException();
        if (null == worker) throw new IllegalArgumentException();
        this.native_worker(qp, queue, new NativeWorker()
        {
            public void perform(String data)
            {
                worker.perform(data);
            }
        });
    }

    public void observer(String queue, final Observer observer) throws Exception
    {
        if (0 == qp) throw new IllegalStateException("Q disconnected");
        if (null == queue) throw new IllegalArgumentException();
        if (null == observer) throw new IllegalArgumentException();
        this.native_observer(qp, queue, new NativeObserver()
        {
            public void perform(String data)
            {
                observer.perform(data);
            }
        });
    }

}
