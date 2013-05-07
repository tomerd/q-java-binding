package com.mishlabs.q.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestsRunner
{
    public static void main(String[] args)
    {
        Result result = JUnitCore.runClasses(RedisTests.class, TransientTests.class);
        for (Failure failure : result.getFailures())
        {
            System.out.println(failure.toString());
        }
    }
}