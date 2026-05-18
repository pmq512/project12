package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.VideoSource;
import com.anime.repository.VideoSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoSourceService {

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    public List<VideoSource> getVideoSourcesByAnime(Anime anime) {
        return videoSourceRepository.findByAnimeOrderByIdAsc(anime);
    }

    public List<VideoSource> getVideoSourcesByAnimeId(Long animeId) {
        return videoSourceRepository.findByAnimeIdOrderByIdAsc(animeId);
    }

    public VideoSource saveVideoSource(VideoSource videoSource) {
        return videoSourceRepository.save(videoSource);
    }

    public void deleteVideoSource(Long id) {
        videoSourceRepository.deleteById(id);
    }

    public void deleteByAnime(Anime anime) {
        videoSourceRepository.deleteByAnime(anime);
    }
}