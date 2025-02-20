package pl.mbassara.jnapi.features.downloader.napiprojekt;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;

@Component
class SubtitlesIdProvider {

    private static final int _10_MB = 10 * 1024 * 1024;

    // Napiprojekt uses HEX formatted MD5 of the first 10MB of a movie file as a subtitles identifier
    String getSubtitlesId(Path movieFile) {
        var theFirst10MB = readFirst10MB(movieFile);
        var md5 = computeMd5(theFirst10MB);
        return formatHex(md5);
    }

    @SneakyThrows
    private byte[] readFirst10MB(Path movieFile) {
        var buffer = ByteBuffer.allocate(_10_MB);

        try (var channel = FileChannel.open(movieFile)) {
            var bytesRead = channel.read(buffer);
            return Arrays.copyOf(buffer.array(), bytesRead);
        }
    }

    @SneakyThrows
    private byte[] computeMd5(byte[] bytes) {
        var digest = MessageDigest.getInstance("MD5");
        digest.update(bytes);
        return digest.digest();
    }

    private String formatHex(byte[] bytes) {
        return HexFormat.of()
                .formatHex(bytes)
                .toLowerCase();
    }
}
