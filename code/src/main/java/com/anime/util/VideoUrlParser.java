package com.anime.util;

public class VideoUrlParser {
    
    public static String parseVideoUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            return "";
        }
        
        String url = originalUrl.trim();
        
        // 处理 B站链接
        if (url.contains("bilibili.com") || url.contains("b23.tv")) {
            return parseBilibiliUrl(url);
        }
        
        // 处理 YouTube 链接
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            return parseYoutubeUrl(url);
        }
        
        // 处理 优酷链接
        if (url.contains("youku.com")) {
            return parseYoukuUrl(url);
        }
        
        // 处理 腾讯视频链接
        if (url.contains("v.qq.com")) {
            return parseTencentUrl(url);
        }
        
        // 处理 爱奇艺链接
        if (url.contains("iqiyi.com")) {
            return parseIqiyiUrl(url);
        }
        
        // 如果是已经嵌入的链接，直接返回
        if (url.contains("embed")) {
            return url;
        }
        
        // 默认返回原链接（作为跳转链接）
        return url;
    }
    
    private static String parseBilibiliUrl(String url) {
        // B站视频链接格式: 
        // https://www.bilibili.com/video/BV1xx411c7mZ
        // https://b23.tv/xxxxxx
        
        String videoId = null;
        
        // 提取视频ID
        if (url.contains("/video/")) {
            int start = url.indexOf("/video/") + 7;
            int end = url.indexOf("?", start);
            if (end == -1) end = url.length();
            videoId = url.substring(start, end);
        } else if (url.contains("b23.tv/")) {
            // b23.tv 短链接直接返回原链接，让浏览器处理跳转
            return url;
        }
        
        if (videoId != null) {
            return "https://player.bilibili.com/player.html?aid=0&cid=0&bvid=" + videoId + "&page=1";
        }
        
        return url;
    }
    
    private static String parseYoutubeUrl(String url) {
        // YouTube 链接格式:
        // https://www.youtube.com/watch?v=VIDEO_ID
        // https://youtu.be/VIDEO_ID
        
        String videoId = null;
        
        if (url.contains("youtube.com/watch")) {
            int start = url.indexOf("v=") + 2;
            int end = url.indexOf("&", start);
            if (end == -1) end = url.length();
            videoId = url.substring(start, end);
        } else if (url.contains("youtu.be/")) {
            int start = url.indexOf("youtu.be/") + 9;
            int end = url.indexOf("?", start);
            if (end == -1) end = url.length();
            videoId = url.substring(start, end);
        }
        
        if (videoId != null) {
            return "https://www.youtube.com/embed/" + videoId;
        }
        
        return url;
    }
    
    private static String parseYoukuUrl(String url) {
        // 优酷链接格式:
        // https://v.youku.com/v_show/id_XNTg4NjYxMjYwNA==.html
        
        int start = url.indexOf("/id_") + 4;
        int end = url.indexOf(".html", start);
        if (start > 3 && end > start) {
            String videoId = url.substring(start, end);
            return "https://player.youku.com/embed/" + videoId;
        }
        
        return url;
    }
    
    private static String parseTencentUrl(String url) {
        // 腾讯视频链接格式:
        // https://v.qq.com/x/page/xxxx.html
        
        int start = url.indexOf("/page/") + 6;
        int end = url.indexOf(".html", start);
        if (start > 5 && end > start) {
            String videoId = url.substring(start, end);
            return "https://v.qq.com/txp/iframe/player.html?vid=" + videoId;
        }
        
        return url;
    }
    
    private static String parseIqiyiUrl(String url) {
        // 爱奇艺链接格式:
        // https://www.iqiyi.com/v_19rr7xh3v8.html
        
        int start = url.indexOf("/v_") + 3;
        int end = url.indexOf(".html", start);
        if (start > 2 && end > start) {
            String videoId = url.substring(start, end);
            return "https://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid=" + videoId + "&accessToken=undefined";
        }
        
        return url;
    }
    
    public static boolean isEmbedUrl(String url) {
        return url.contains("embed") || url.contains("player.bilibili.com") || 
               url.contains("player.youku.com") || url.contains("iframe") || url.contains("txp/iframe");
    }

    public static boolean isDirectVideoUrl(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        return lower.endsWith(".mp4") || lower.endsWith(".webm") || 
               lower.endsWith(".ogg") || lower.endsWith(".mov") ||
               lower.contains(".m3u8");
    }

    public static String detectFormat(String url) {
        if (url == null) return "unknown";
        String lower = url.toLowerCase();
        if (lower.endsWith(".mp4")) return "MP4";
        if (lower.endsWith(".webm")) return "WebM";
        if (lower.endsWith(".ogg")) return "OGG";
        if (lower.endsWith(".mov")) return "MOV";
        if (lower.contains(".m3u8")) return "HLS";
        if (lower.contains("bilibili")) return "B站";
        if (lower.contains("youtube") || lower.contains("youtu.be")) return "YouTube";
        if (lower.contains("youku")) return "优酷";
        if (lower.contains("v.qq.com")) return "腾讯视频";
        if (lower.contains("iqiyi")) return "爱奇艺";
        return "其他";
    }
}