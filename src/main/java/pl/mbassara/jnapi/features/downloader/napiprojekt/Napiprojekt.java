package pl.mbassara.jnapi.features.downloader.napiprojekt;

import feign.FeignException;
import feign.FeignException.NotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.features.downloader.RawSubtitles;
import pl.mbassara.jnapi.features.downloader.SubtitlesProvider;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.MovieDto;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.NapiprojektResultDto;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

import static pl.mbassara.jnapi.features.downloader.napiprojekt.dto.StatusDto.failed;

@Slf4j
@Component
@RequiredArgsConstructor
class Napiprojekt implements SubtitlesProvider {

    private final SubtitlesIdProvider subtitlesIdProvider;
    private final NapiprojektClient napiprojektClient;
    private final Base64.Decoder base64Decoder;

    @Override
    public String getName() {
        return "Napiprojekt";
    }

    @Override
    public Optional<RawSubtitles> downloadSubtitles(Path movieFile, SubtitlesLanguage language) {
        var subtitlesId = subtitlesIdProvider.getSubtitlesId(movieFile);

        try {
            return napiprojektClient.downloadSubtitles(subtitlesId, language)
                    .flatMap(this::processResult);
        } catch (FeignException e) {
            return handleError(e);
        }
    }

    private Optional<RawSubtitles> processResult(NapiprojektResultDto result) {
        var status = Optional.of(result)
                .map(NapiprojektResultDto::movie)
                .map(MovieDto::status)
                .orElse(failed);

        return switch (status) {
            case success -> Optional.of(convertResult(result));
            case failed -> Optional.empty();
        };
    }

    private RawSubtitles convertResult(NapiprojektResultDto result) {
        return new RawSubtitles(
                result.movie().title(),
                base64Decoder.decode(result.subtitles().base64Content())
        );
    }

    private Optional<RawSubtitles> handleError(FeignException e) {
        log.debug(e.getMessage(), e);

        if (e instanceof NotFound) {
            return Optional.empty();
        }
        throw e;
    }
}
