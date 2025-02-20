package pl.mbassara.jnapi.features.downloader.napiprojekt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubtitlesDto(@JsonProperty("content") String base64Content) {
}
