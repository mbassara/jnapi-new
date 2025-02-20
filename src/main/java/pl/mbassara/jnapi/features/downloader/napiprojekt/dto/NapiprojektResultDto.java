package pl.mbassara.jnapi.features.downloader.napiprojekt.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "result")
public record NapiprojektResultDto(SubtitlesDto subtitles,
                                   MovieDto movie) {
}
