package pl.mbassara.jnapi.features.downloader.napiprojekt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.NapiprojektResultDtoBuilder;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.mbassara.jnapi.features.downloader.napiprojekt.dto.StatusDto.failed;
import static pl.mbassara.jnapi.features.downloader.napiprojekt.dto.StatusDto.success;

@ExtendWith(MockitoExtension.class)
class NapiprojektTest {

    private static final Path PATH = Path.of("PATH");
    private static final SubtitlesLanguage SUBTITLES_LANGUAGE = SubtitlesLanguage.PL;
    private static final String SUBTITLES_ID = "SUBTITLES_ID";
    private static final String BASE_64_CONTENT = "BASE_64_CONTENT";
    private static final String TITLE = "TITLE";
    private static final byte[] RAW_SUBTITLES = "RAW_SUBTITLES".getBytes();

    @Mock
    private SubtitlesIdProvider idProvider;
    @Mock
    private Base64.Decoder decoder;
    @Mock
    private NapiprojektClient napiprojektClient;

    @InjectMocks
    private Napiprojekt napiprojekt;

    @Test
    void shouldDownloadSubstitutes() {
        // given
        when(idProvider.getSubtitlesId(PATH)).thenReturn(SUBTITLES_ID);
        when(decoder.decode(BASE_64_CONTENT)).thenReturn(RAW_SUBTITLES);

        var rawResult = NapiprojektResultDtoBuilder
                .withTitle(TITLE)
                .withStatus(success)
                .withContent(BASE_64_CONTENT)
                .build();

        when(napiprojektClient.downloadSubtitles(SUBTITLES_ID, SUBTITLES_LANGUAGE))
                .thenReturn(Optional.of(rawResult));

        // when
        var result = napiprojekt.downloadSubtitles(PATH, SUBTITLES_LANGUAGE).orElseThrow();

        // then
        assertThat(result.title()).isEqualTo(TITLE);
        assertThat(result.subtitles()).isEqualTo(RAW_SUBTITLES);
    }

    @Test
    void shouldReturnEmpty_onFailure() {
        // given
        when(idProvider.getSubtitlesId(PATH)).thenReturn(SUBTITLES_ID);

        var rawResult = NapiprojektResultDtoBuilder
                .withTitle(TITLE)
                .withStatus(failed)
                .withContent(BASE_64_CONTENT)
                .build();
        
        when(napiprojektClient.downloadSubtitles(SUBTITLES_ID, SUBTITLES_LANGUAGE))
                .thenReturn(Optional.of(rawResult));

        // when
        var result = napiprojekt.downloadSubtitles(PATH, SUBTITLES_LANGUAGE);

        // then
        assertThat(result).isEmpty();
    }
}