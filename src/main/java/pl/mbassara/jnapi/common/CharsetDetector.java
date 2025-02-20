package pl.mbassara.jnapi.common;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.ByteOrderMark.*;

@Component
@RequiredArgsConstructor
public class CharsetDetector {

    @SneakyThrows
    public String readWithDetectedCharset(byte[] rawText) {
        var inputStream = bomSkippingInputStream(rawText);

        var detector = new com.ibm.icu.text.CharsetDetector();
        detector.setText(inputStream);
        return detector.detect().getString();
    }

    // A BOM (Byte Order Mark) is a special marker (e.g. 0xEF 0xBB 0xBF) sometimes placed
    // at the beginning of a text file to indicate its character encoding and byte order.
    // Common in subtitle files created on Windows
    private InputStream bomSkippingInputStream(byte[] rawText) throws IOException {
        return BOMInputStream.builder()
                .setByteArray(rawText)
                .setByteOrderMarks(UTF_8, UTF_16LE, UTF_16BE, UTF_32LE, UTF_32BE)
                .get();
    }
}
