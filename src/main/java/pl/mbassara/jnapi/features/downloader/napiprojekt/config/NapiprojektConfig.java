package pl.mbassara.jnapi.features.downloader.napiprojekt.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
class NapiprojektConfig {

    @Bean
    Base64.Decoder base64Decoder() {
        return Base64.getDecoder();
    }

    @Bean
    XmlMapper xmlMapper() {
        var xmlMapper = new XmlMapper();
        xmlMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return xmlMapper;
    }
}
