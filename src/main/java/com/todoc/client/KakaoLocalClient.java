// 카카오 로컬 주소 검색 API HTTP 클라이언트
package com.todoc.client;

import com.todoc.config.ExternalApiConfig;
import com.todoc.dto.external.KakaoAddressResponse;
import com.todoc.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final RestClient restClient;
    private final ExternalApiConfig config;

    public KakaoAddressResponse.Address searchAddress(String address) {
        KakaoAddressResponse response = restClient.get()
                .uri(config.getKakao().getAddressSearchUrl() + "?query={query}", address)
                .header("Authorization", "KakaoAK " + config.getKakao().getApiKey())
                .retrieve()
                .body(KakaoAddressResponse.class);

        if (response == null || response.documents() == null || response.documents().isEmpty()) {
            throw new NotFoundException("주소를 찾을 수 없습니다: " + address);
        }

        KakaoAddressResponse.Address addr = response.documents().get(0).address();
        if (addr == null) {
            throw new NotFoundException("지번 주소 정보를 찾을 수 없습니다: " + address);
        }
        return addr;
    }
}
