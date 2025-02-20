package pl.mbassara.jnapi.features.downloader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubtitlesDownloaderTest {

    private static final SubtitlesLanguage LANG = SubtitlesLanguage.PL;
    private static final String TITLE = "TITLE";
    public static final RawSubtitles SUBTITLES = new RawSubtitles(TITLE, new byte[0]);

    @Mock
    private Path path;
    @Mock
    private SubtitlesProvider provider1;
    @Mock
    private SubtitlesProvider provider2;

    private SubtitlesDownloader downloader;

    @BeforeEach
    void setup() {
        downloader = new SubtitlesDownloader(List.of(provider1, provider2));
    }

    @Test
    void shouldReturnResultFromFirstProvider() {
        // given
        when(provider1.downloadSubtitles(path, LANG)).thenReturn(Optional.of(SUBTITLES));

        // when
        var subtitles = downloader.downloadSubtitles(path, LANG);

        // then
        assertThat(subtitles).isEqualTo(SUBTITLES);
    }

    @Test
    void shouldReturnResultFromSecondProvider() {
        // given
        when(provider2.downloadSubtitles(path, LANG)).thenReturn(Optional.of(SUBTITLES));

        // when
        var subtitles = downloader.downloadSubtitles(path, LANG);

        // then
        assertThat(subtitles).isEqualTo(SUBTITLES);
    }

    @Test
    void shouldThrowExceptionWhenBothProvidersFailed() {
        // given
        var providerName1 = "providerName1";
        var providerName2 = "providerName2";
        when(provider1.getName()).thenReturn(providerName1);
        when(provider2.getName()).thenReturn(providerName2);

        // when
        var exception = catchException(() -> downloader.downloadSubtitles(path, LANG));

        // then
        assertThat(exception)
                .hasMessageContaining("Cannot find any subtitles")
                .hasMessageContaining(providerName1)
                .hasMessageContaining(providerName2);
    }
}