package com.anime.service;

import com.anime.util.VideoUrlParser;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    
    public String processVideoUrl(String originalUrl) {
        return VideoUrlParser.parseVideoUrl(originalUrl);
    }
    
    public boolean isEmbedUrl(String url) {
        return VideoUrlParser.isEmbedUrl(url);
    }
}