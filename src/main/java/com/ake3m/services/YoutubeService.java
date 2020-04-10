package com.ake3m.services;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.Extension;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class YoutubeService {
    private final YoutubeDownloader downloader = new YoutubeDownloader();

    public CompletableFuture<Void> download(String videoId, File outputDir) {
        var completableFuture = new CompletableFuture<Void>();
        try {
            var video = downloader.getVideo(videoId);
            Stream<AudioVideoFormat> stream = video.videoWithAudioFormats().stream();
            var format = stream
                    .filter(audioVideoFormat -> audioVideoFormat.extension() == Extension.MP4)
                    .findFirst()
                    .orElse(video.videoWithAudioFormats().get(0));
            video.downloadAsync(format, outputDir, new Listener(video, completableFuture));
        } catch (Exception e) {
            e.printStackTrace();
            completableFuture.completeExceptionally(e);
        }

        return completableFuture;
    }

    private static class Listener implements OnYoutubeDownloadListener {
        CompletableFuture<Void> completableFuture;
        YoutubeVideo video;

        public Listener(YoutubeVideo video, CompletableFuture<Void> completableFuture) {
            this.video = video;
            this.completableFuture = completableFuture;
        }

        @Override
        public void onDownloading(int progress) {
            System.out.printf("Downloaded %d%% of %s\n", progress, video.details().title());
        }

        @Override
        public void onFinished(File file) {
            System.out.println(String.format("%s has downloaded to %s", video.details().title(), file.toPath()));
            completableFuture.complete(null);
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("Error: " + throwable.getLocalizedMessage());
            completableFuture.completeExceptionally(throwable);
        }
    }
}
