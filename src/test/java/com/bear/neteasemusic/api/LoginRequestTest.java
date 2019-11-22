package com.bear.neteasemusic.api;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginRequestTest {

    private NeteaseAPI api;

    @BeforeTest
    public void initialize() {
        api = new NeteaseAPI();
    }

    @Parameters({ "username", "password" })
    @Test
    public void testLogin(String username, String password) throws IOException {
        LoginRequest req = new LoginRequest(username, password);
        var x = api.postRequest(req);
        Assert.assertTrue(x != null && !x.equals(""), "Invalid data returned.");
    }
}