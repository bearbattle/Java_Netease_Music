package com.bear.neteasemusic.api;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiTest {

    private NeteaseAPI api;
    private int userId;
    private int playlistId;

    @BeforeTest
    public void initialize() {
        api = new NeteaseAPI();
    }

    @Parameters({"username", "password"})
    @Test(groups = "login")
    public void testLogin(String username, String password) throws IOException {
        LoginRequest req = new LoginRequest(username, password);
        var resp = LoginResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        this.userId = resp.getUserId();
        System.out.println(resp.getNickName() + " " + resp.getSignature());
    }

    @Test(dependsOnGroups = "login")
    public void testGetUserPlaylists() throws IOException {
        GetUserPlaylistsRequest req = new GetUserPlaylistsRequest(userId);
        var resp = GetUserPlaylistsResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        playlistId = resp.getLists().get(0).id;
        for (var list : resp.getLists()) {
            System.out.println(String.format("%s (有 %d 首歌)", list.name, list.trackCount));
        }
    }

    @Test(dependsOnMethods = "testGetUserPlaylists")
    public void testGetPlaylistDetails() throws IOException {
        GetPlaylistDetailRequest req = new GetPlaylistDetailRequest(playlistId);
        var resp = GetPlaylistDetailResponse.parse(api.postRequest(req));
        for (var track : resp.getTracks()) {
            System.out.println(String.format("%s by %s in %s", track.name, track.artistName, track.albumName));
        }
    }
}