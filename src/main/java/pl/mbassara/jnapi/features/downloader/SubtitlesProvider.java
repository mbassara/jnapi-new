package pl.mbassara.jnapi.features.downloader;

import pl.mbassara.jnapi.model.SubtitlesLanguage;

import java.nio.file.Path;
import java.util.Optional;

public interface SubtitlesProvider {

	String getName();

	Optional<RawSubtitles> downloadSubtitles(Path movieFile, SubtitlesLanguage language);
}
