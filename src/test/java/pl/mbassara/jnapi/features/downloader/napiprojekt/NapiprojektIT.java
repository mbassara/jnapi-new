package pl.mbassara.jnapi.features.downloader.napiprojekt;

import feign.FeignException.InternalServerError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.mbassara.jnapi.application.BaseSpringBootTest;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class NapiprojektIT extends BaseSpringBootTest {

    private static final Path PATH = Path.of("PATH");
    private static final String SUBTITLES_ID = "SUBTITLES_ID";
    private static final SubtitlesLanguage LANGUAGE = SubtitlesLanguage.PL;


    @MockitoBean
    private SubtitlesIdProvider idProvider;

    @Autowired
    private NapiprojektApiMock apiMock;

    @Autowired
    private Napiprojekt napiprojekt;

    @Test
    void shouldReturnEmpty_onNotFound() {
        // given
        when(idProvider.getSubtitlesId(PATH)).thenReturn(SUBTITLES_ID);

        apiMock.stubError(SUBTITLES_ID, LANGUAGE, NOT_FOUND);

        // when
        var result = napiprojekt.downloadSubtitles(PATH, LANGUAGE);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldRethrowException_onInternalServerError() {
        // given
        when(idProvider.getSubtitlesId(PATH)).thenReturn(SUBTITLES_ID);

        apiMock.stubError(SUBTITLES_ID, LANGUAGE, INTERNAL_SERVER_ERROR);

        // when
        var exception = catchException(() -> napiprojekt.downloadSubtitles(PATH, LANGUAGE));

        // then
        assertThat(exception).isInstanceOf(InternalServerError.class);
    }

}