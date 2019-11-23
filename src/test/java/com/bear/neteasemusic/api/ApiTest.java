package com.bear.neteasemusic.api;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiTest {

    private NeteaseAPI api;

    @BeforeTest
    public void initialize() {
        api = new NeteaseAPI();
    }

    @Parameters({"username", "password"})
    @Test
    public void testLogin(String username, String password) throws IOException {
        LoginRequest req = new LoginRequest(username, password);
        var resp = LoginResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        System.out.println(resp.getNickName() + " " + resp.getSignature());
    }
}