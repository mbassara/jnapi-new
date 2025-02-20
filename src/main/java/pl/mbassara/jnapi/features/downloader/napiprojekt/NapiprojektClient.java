package pl.mbassara.jnapi.features.downloader.napiprojekt;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import pl.mbassara.jnapi.features.downloader.napiprojekt.dto.NapiprojektResultDto;
import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.util.Optional;

@FeignClient(name = "napiprojekt", url = "${napiprojekt.service.url:http://www.napiprojekt.pl}")
interface NapiprojektClient {

    @PostMapping(value = "/api/api-napiprojekt3.php", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    NapiprojektResultDto downloadSubtitlesRaw(@RequestPart("downloaded_subtitles_lang") String language,
                                              @RequestPart("downloaded_subtitles_id") String subtitlesId,
                                              @RequestPart("downloaded_subtitles_txt") String downloadSubtitlesText,
                                              @RequestPart("client") String client,
                                              @RequestPart("mode") String mode,
                                              @RequestPart("downloaded_cover_id") String movieInformationId);

    default Optional<NapiprojektResultDto> downloadSubtitles(String subtitlesId, SubtitlesLanguage language) {
        return Optional.ofNullable(
                downloadSubtitlesRaw(language.name(), subtitlesId, "1", "AutoMove", "3", subtitlesId)
        );
    }
}
