// 카카오·VWorld 외부 API 키 및 URL 설정
package com.todoc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "external")
public class ExternalApiConfig {

    private Kakao kakao = new Kakao();
    private Vworld vworld = new Vworld();
    private DataGoKr dataGoKr = new DataGoKr();

    @Getter
    @Setter
    public static class Kakao {
        private String apiKey;
        private String addressSearchUrl;
    }

    @Getter
    @Setter
    public static class Vworld {
        private String apiKey;
        private String landUrl;
        private String landUseUrl;
        private String wfsUrl;
    }

    @Getter
    @Setter
    public static class DataGoKr {
        private String apiKey;
        private String buildingUrl;
    }
}
