package pl.mbassara.jnapi.application;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import pl.mbassara.jnapi.config.TestConfiguration;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(
        properties = "wiremock.server.port=8081"
)
@WireMockTest(httpPort = 8081)
@ActiveProfiles("test")
@Import(TestConfiguration.class)
public abstract class BaseSpringBootTest {

    @Autowired
    protected ResourceLoader resourceLoader;

    @TempDir
    private Path tempDir;

    @SneakyThrows
    protected Path tempFile(String name, String content) {
        var path = tempDir.resolve(name);
        Files.writeString(path, content);
        return path;
    }

    @SneakyThrows
    protected String readClasspathResource(String path) {
        var resource = resourceLoader.getResource("classpath:" + path);
        return resource.getContentAsString(UTF_8);
    }
}
