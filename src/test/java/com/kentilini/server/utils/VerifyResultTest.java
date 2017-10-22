package com.kentilini.server.utils;

import com.kentilini.server.exception.NoResultException;
import com.kentilini.server.exception.NonUniqueResultException;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VerifyResultTest {
    @Test(groups = "mock", expectedExceptions = NonUniqueResultException.class)
    public void testIsOneResult_Multiple() throws Exception {
        List<String> list = Arrays.asList("a", "b");

        VerifyResult.isOneResult(list);
    }

    @Test(groups = "mock", expectedExceptions = NoResultException.class)
    public void testIsOneResult_Empty() throws Exception {
        VerifyResult.isOneResult(Collections.emptyList());
    }

    @Test(groups = "mock")
    public void testIsOneResult_Success() throws Exception {
        VerifyResult.isOneResult(Collections.singletonList(""));
    }
}