// VWorld 토지특성정보·토지이용계획·WFS API HTTP 클라이언트
package com.todoc.client;

import com.todoc.config.ExternalApiConfig;
import com.todoc.dto.external.VWorldLandResponse;
import com.todoc.dto.external.VWorldLandUseResponse;
import com.todoc.dto.external.VWorldWfsResponse;
import com.todoc.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VWorldClient {

    private final RestClient restClient;
    private final ExternalApiConfig config;

    public VWorldLandResponse.Field getLandCharacteristics(String pnu) {
        VWorldLandResponse response = restClient.get()
                .uri(config.getVworld().getLandUrl()
                        + "?pnu={pnu}&key={key}&format=json&numOfRows=1&pageNo=1", pnu,
                        config.getVworld().getApiKey())
                .retrieve()
                .body(VWorldLandResponse.class);

        if (response == null
                || response.landCharacteristics() == null
                || response.landCharacteristics().field() == null
                || response.landCharacteristics().field().isEmpty()) {
            throw new NotFoundException("토지 정보를 찾을 수 없습니다: pnu=" + pnu);
        }
        return response.landCharacteristics().field().get(0);
    }

    public List<VWorldLandUseResponse.Field> getLandUses(String pnu) {
        VWorldLandUseResponse response = restClient.get()
                .uri(config.getVworld().getLandUseUrl()
                        + "?pnu={pnu}&key={key}&format=json&numOfRows=100&pageNo=1", pnu,
                        config.getVworld().getApiKey())
                .retrieve()
                .body(VWorldLandUseResponse.class);

        if (response == null || response.landUses() == null || response.landUses().field() == null) {
            return Collections.emptyList();
        }
        return response.landUses().field();
    }

    public VWorldWfsResponse.Properties getPnuByCoordinate(double lat, double lng) {
        double[] bbox = toBbox3857(lat, lng, 50);
        VWorldWfsResponse response = restClient.get()
                .uri(config.getVworld().getWfsUrl()
                        + "?SERVICE=WFS&VERSION=2.0.0&REQUEST=GetFeature"
                        + "&TYPENAME=lp_pa_cbnd_bubun"
                        + "&BBOX={minX},{minY},{maxX},{maxY}"
                        + "&key={key}&output=application/json&COUNT=1",
                        bbox[0], bbox[1], bbox[2], bbox[3],
                        config.getVworld().getApiKey())
                .retrieve()
                .body(VWorldWfsResponse.class);

        if (response == null || response.features() == null || response.features().isEmpty()) {
            throw new NotFoundException(String.format("해당 좌표의 토지 정보를 찾을 수 없습니다: lat=%.6f, lng=%.6f", lat, lng));
        }
        return response.features().get(0).properties();
    }

    // WGS84(EPSG:4326) → Web Mercator(EPSG:3857) 변환 후 delta(m) 반경 BBOX 반환
    private double[] toBbox3857(double lat, double lng, double delta) {
        double x = lng * 20037508.34 / 180.0;
        double y = Math.log(Math.tan(Math.PI / 4 + Math.toRadians(lat) / 2)) * 6378137.0;
        return new double[]{x - delta, y - delta, x + delta, y + delta};
    }
}
