package com.ake3m.tasks;

import com.ake3m.services.YoutubeService;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class SaveYoutubeVideoTask implements ApplicationTask<Void> {

    private final YoutubeService youtubeService = new YoutubeService();
    private String videoId;
    private File outputDir;

    public SaveYoutubeVideoTask() { }

    public SaveYoutubeVideoTask withVideoFileConfig(String videoId, File outputDir) {
        this.videoId = videoId;
        this.outputDir = outputDir;
        return this;
    }

    public CompletableFuture<Void> execute() {
        return youtubeService.download(videoId, outputDir);
    }


}
