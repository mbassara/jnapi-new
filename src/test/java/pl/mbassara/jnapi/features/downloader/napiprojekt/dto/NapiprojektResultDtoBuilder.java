package pl.mbassara.jnapi.features.downloader.napiprojekt.dto;

import java.util.Base64;

public class NapiprojektResultDtoBuilder {
    private String base64Content;
    private StatusDto status;
    private String title;

    public static NapiprojektResultDtoBuilder withTitle(String title) {
        var builder = new NapiprojektResultDtoBuilder();
        builder.title = title;
        return builder;
    }

    public NapiprojektResultDtoBuilder withContent(String content) {
        this.base64Content = content;
        return this;
    }

    public NapiprojektResultDtoBuilder withBase64Content(String content) {
        this.base64Content = Base64.getEncoder().encodeToString(content.getBytes());
        return this;
    }

    public NapiprojektResultDtoBuilder withStatus(StatusDto status) {
        this.status = status;
        return this;
    }

    public NapiprojektResultDto build() {
        return new NapiprojektResultDto(
                new SubtitlesDto(base64Content),
                new MovieDto(status, title)
        );
    }
}