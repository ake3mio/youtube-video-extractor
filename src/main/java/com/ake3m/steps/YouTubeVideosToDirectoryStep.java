package com.ake3m.steps;

import com.ake3m.automation.pages.Page;
import com.ake3m.automation.pages.PageToken;
import com.ake3m.tasks.SaveYoutubeVideoTask;
import com.ake3m.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class YouTubeVideosToDirectoryStep implements ApplicationStep<Void> {

    @Inject
    private ThreadPoolExecutor threadPoolExecutor;

    @Inject
    @Named("output.dir")
    private String dir;

    private File outputDir;
    private List<PageToken> pageTokens = Collections.emptyList();

    public CompletableFuture<Void> run(Page page) {

        this.outputDir = createDirectory(dir);

        return CompletableFuture.runAsync(() -> {

            Function<PageToken, CompletableFuture<Void>> projectFuture =
                    pageToken -> CompletableFuture.allOf(this.apply(pageToken));

            CompletableFuture<Void>[] futures =
                    pageTokens.stream().map(projectFuture).toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).join();

        }, threadPoolExecutor);
    }

    public YouTubeVideosToDirectoryStep withPageTokens(List<PageToken> pageTokens) {
        this.pageTokens = pageTokens;
        return this;
    }


    private File createDirectory(String pathName) {

        deleteDirectoryStream(new File(pathName).toPath());

        File file = new File(pathName);

        boolean bool = file.mkdir();
        if (bool) {
            System.out.printf("Directory: %s created successfully\n", pathName);
        } else {
            System.out.printf("Sorry couldn’t create specified directory: %s\n", pathName);
        }
        return file;
    }

    private void deleteDirectoryStream(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignore) {
        }
    }

    private CompletableFuture<Void>[] apply(PageToken pageToken) {

        String pathName = String.format(
                "%s%s%s",
                this.outputDir.getPath(),
                File.separator,
                StringUtils.slugify(pageToken.name())
        );

        File output = createDirectory(pathName);

        Stream<String> stream = pageToken.videoIds().stream();

        Function<String, CompletableFuture<Void>> projectFuture =
                videoId -> new SaveYoutubeVideoTask()
                        .withVideoFileConfig(videoId, output)
                        .execute();

        return stream
                .map(projectFuture)
                .toArray(CompletableFuture[]::new);


    }
}
