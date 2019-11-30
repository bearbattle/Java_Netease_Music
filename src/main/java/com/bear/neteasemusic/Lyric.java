package com.bear.neteasemusic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Lyric {
    public Lyric(String lrcString) {
        LrcList = new ArrayList<LrcData>();
        String[] line = lrcString.split("\n");
        for (int i = 0; i < line.length; i++) {
            LrcAnalyzeLine(line[i]);
        }
        LrcList.sort(Comparator.comparing((l) -> l.time));
    }

    private final Pattern pattern = Pattern.compile("\\[(\\d*):(\\d*).(\\d*)\\] *(.*)");

    public static class LrcData {
        public long time; // time of long format ms
        public String content; // one line lrc
    }

    private List<LrcData> LrcList;

    private void LrcAnalyzeLine(String content) {
        var matcher = pattern.matcher(content);
        if (matcher.matches()) {
            LrcData newData = new LrcData();
            newData.time = Integer.parseInt(matcher.group(1)) * 60 * 1000 + Integer.parseInt(matcher.group(2)) * 1000 + Integer.parseInt(matcher.group(3)) * 10;
            newData.content = matcher.group(4);
            LrcList.add(newData);
        }
    }

    public String GetCurrentLyric(long duration) {
        var element = LrcList.stream().filter(l -> l.time > duration).findFirst();
        if (element.isPresent())
            return element.get().content;
        return "";
    }
}
