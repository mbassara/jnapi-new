package pl.mbassara.jnapi.features.downloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubtitlesDownloader {

    private final List<SubtitlesProvider> providers;

    public RawSubtitles downloadSubtitles(Path movieFile, SubtitlesLanguage language) {
        return providers.stream()
                .flatMap(provider -> tryWithProvider(provider, movieFile, language))
                .findFirst()
                .orElseThrow(this::cannotFindSubtitlesException);
    }

    private Stream<RawSubtitles> tryWithProvider(SubtitlesProvider provider, Path movieFile, SubtitlesLanguage language) {
        log.info("Trying to download subtitles. Provider: {}", provider.getName());
        return provider.downloadSubtitles(movieFile, language)
                .stream()
                .peek(subs -> log.info("Subtitles successfully downloaded. Provider: {}. Movie identified as: {}", provider.getName(), subs.title()));
    }

    private RuntimeException cannotFindSubtitlesException() {
        var providerNames = providers.stream()
                .map(SubtitlesProvider::getName)
                .toList();

        return new IllegalStateException("Cannot find any subtitles using the available providers:" + providerNames);
    }
}
