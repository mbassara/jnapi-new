package pl.mbassara.jnapi.features.printer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.mbassara.jnapi.model.Format;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static pl.mbassara.jnapi.model.Format.*;

class SubtitlesPrinterFactoryTest {

    private final SubtitlesPrinterFactory factory = new SubtitlesPrinterFactory();

    @ParameterizedTest
    @MethodSource("formatsAndExpectedPrinterTypes")
    void shouldReturnCorrectPrinterForFormat(Format format, Class<?> expectedPrinterType) {
        // when
        var printer = factory.create(format);

        // then
        assertThat(printer).isInstanceOf(expectedPrinterType);
    }

    private static Stream<Arguments> formatsAndExpectedPrinterTypes() {
        return Stream.of(
                arguments(MicroDVD, MicroDVDPrinter.class),
                arguments(MPL2, MPL2Printer.class),
                arguments(SubRip, SubRipPrinter.class)
        );
    }

}