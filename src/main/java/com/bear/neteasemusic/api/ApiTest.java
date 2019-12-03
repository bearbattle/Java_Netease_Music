package com.bear.neteasemusic.api;

import com.bear.neteasemusic.api.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiTest {

    private NeteaseAPI api;
    private long userId;
    private long playlistId;
    private long trackId;

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
        var respj = api.postRequest(req);
        System.out.println(respj.toJSONString());
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
        Assert.assertTrue(resp.isOk());
        trackId = resp.getTracks().get(2).id;
        for (var track : resp.getTracks()) {
            System.out.println(String.format("%d: %s by %s in %s", track.id, track.name, track.artistName, track.albumName));
        }
    }

    @Test(dependsOnMethods = "testGetPlaylistDetails")
    public void testGetTrackUrl() throws IOException {
        GetTrackUrlRequest req = new GetTrackUrlRequest(trackId, GetTrackUrlRequest.Rate.mp3_320);
        var resp = GetTrackUrlResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        System.out.println(resp.getUrl());
    }

    @Test(dependsOnMethods = "testGetPlaylistDetails")
    public void testGetLyrics() throws IOException {
        System.out.print(trackId);
        GetLyricsRequest req = new GetLyricsRequest(trackId);
        var resp = GetLyricsResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        if (resp.isPureMusic()) {
            System.out.println("是纯音乐");
        } else if (resp.isLyricMissing()) {
            System.out.println("暂无歌词");
        } else {
            System.out.println(resp.getLyric());
        }
    }

    @Parameters({"keyword"})
    @Test
    public void testSearch(String keyword) throws IOException {
        SearchRequest req = new SearchRequest(keyword);
        var resp = SearchResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        for (var track : resp.getTracks()) {
            System.out.println(String.format("%s by %s in %s", track.name, track.artistName, track.albumName));
        }
    }

    @Parameters({"keyword"})
    @Test
    public void testSearchSuggest(String keyword) throws IOException {
        String str = keyword.substring(0, 1);
        var req = new SearchSuggestRequest(str);
        var resp = SearchSuggestResponse.parse(api.postRequest(req));
        Assert.assertTrue(resp.isOk());
        System.out.println("“" + str + "” 的搜索建议：" + String.join(", ", resp.getSuggests()));
    }
}