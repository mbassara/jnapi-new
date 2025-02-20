package pl.mbassara.jnapi.features.printer;

import org.springframework.stereotype.Component;
import pl.mbassara.jnapi.model.Format;

@Component
public class SubtitlesPrinterFactory {

    public SubtitlesPrinter create(Format format) {
        return switch (format) {
            case MicroDVD -> new MicroDVDPrinter();
            case MPL2 -> new MPL2Printer();
            case SubRip -> new SubRipPrinter();
        };
    }
}
