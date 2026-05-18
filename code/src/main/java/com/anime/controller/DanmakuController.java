package com.anime.controller;

import com.anime.entity.Anime;
import com.anime.entity.Danmaku;
import com.anime.entity.User;
import com.anime.service.AnimeService;
import com.anime.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/danmaku")
public class DanmakuController {

    @Autowired
    private DanmakuService danmakuService;

    @Autowired
    private AnimeService animeService;

    @GetMapping("/{animeId}")
    public List<Map<String, Object>> getDanmaku(@PathVariable Long animeId) {
        List<Danmaku> danmakuList = danmakuService.getDanmakuByAnimeId(animeId);
        return danmakuList.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("content", d.getContent());
            map.put("time", d.getTime());
            map.put("color", d.getColor());
            map.put("type", d.getType());
            map.put("username", d.getUser() != null ? d.getUser().getUsername() : "匿名");
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/{animeId}")
    public Map<String, Object> sendDanmaku(@PathVariable Long animeId,
                                           @RequestParam String content,
                                           @RequestParam Double time,
                                           @RequestParam(required = false, defaultValue = "#FFFFFF") String color,
                                           @RequestParam(required = false, defaultValue = "scroll") String type,
                                           HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }

        Anime anime = animeService.getAnimeById(animeId);
        if (anime == null) {
            result.put("success", false);
            result.put("message", "动漫不存在");
            return result;
        }

        Danmaku danmaku = danmakuService.saveDanmaku(user, anime, content, time, color, type);
        result.put("success", true);
        result.put("id", danmaku.getId());
        result.put("content", danmaku.getContent());
        result.put("time", danmaku.getTime());
        result.put("color", danmaku.getColor());
        result.put("type", danmaku.getType());
        result.put("username", user.getUsername());
        return result;
    }
}