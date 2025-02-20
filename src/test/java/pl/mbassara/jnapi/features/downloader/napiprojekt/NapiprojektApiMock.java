package pl.mbassara.jnapi.features.downloader.napiprojekt;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.NapiprojektResultDtoBuilder;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
@RequiredArgsConstructor
public class NapiprojektApiMock {

    private final XmlMapper xmlMapper;

    @SneakyThrows
    public void stubSubtitles(String subtitlesId, SubtitlesLanguage language, NapiprojektResultDtoBuilder responseDto) {
        var responseBody = xmlMapper.writeValueAsString(responseDto.build());

        stubFor(post(urlEqualTo("/api/api-napiprojekt3.php"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded; charset=UTF-8"))
                .withRequestBody(containingDefaultParamsWith(subtitlesId, language))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(responseBody)
                        .withHeader("Content-Type", "text/xml")));
    }

    public void stubError(String subtitlesId, SubtitlesLanguage language, HttpStatus httpStatus) {
        stubFor(post(urlEqualTo("/api/api-napiprojekt3.php"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded; charset=UTF-8"))
                .withRequestBody(containingDefaultParamsWith(subtitlesId, language))
                .willReturn(aResponse()
                        .withStatus(httpStatus.value())));
    }

    private StringValuePattern containingDefaultParamsWith(String subtitlesId, SubtitlesLanguage language) {
        return and(
                containing("downloaded_subtitles_lang=" + language),
                containing("downloaded_subtitles_id=" + subtitlesId),
                containing("downloaded_subtitles_txt=1"),
                containing("client=AutoMove"),
                containing("mode=3"),
                containing("downloaded_cover_id=" + subtitlesId)
        );
    }
}
