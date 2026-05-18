package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Danmaku;
import com.anime.entity.User;
import com.anime.repository.DanmakuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanmakuService {

    @Autowired
    private DanmakuRepository danmakuRepository;

    public Danmaku saveDanmaku(User user, Anime anime, String content, Double time, String color, String type) {
        Danmaku danmaku = new Danmaku();
        danmaku.setUser(user);
        danmaku.setAnime(anime);
        danmaku.setContent(content);
        danmaku.setTime(time);
        danmaku.setColor(color != null ? color : "#FFFFFF");
        danmaku.setType(type != null ? type : "scroll");
        return danmakuRepository.save(danmaku);
    }

    public List<Danmaku> getDanmakuByAnime(Anime anime) {
        return danmakuRepository.findByAnimeOrderByTimeAsc(anime);
    }

    public List<Danmaku> getDanmakuByAnimeId(Long animeId) {
        return danmakuRepository.findByAnimeIdOrderByTimeAsc(animeId);
    }

    public long getDanmakuCount(Anime anime) {
        return danmakuRepository.countByAnime(anime);
    }
}