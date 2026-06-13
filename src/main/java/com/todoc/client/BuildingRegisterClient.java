// 건축물대장 총괄표제부 API HTTP 클라이언트
package com.todoc.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoc.config.ExternalApiConfig;
import com.todoc.dto.external.BuildingInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuildingRegisterClient {

    private final RestClient restClient;
    private final ExternalApiConfig config;
    private final ObjectMapper objectMapper;

    public BuildingInfoResponse getBuilding(String pnu) {
        String sigunguCd = pnu.substring(0, 5);
        String bjdongCd = pnu.substring(5, 10);
        // PNU 토지구분: 1=일반토지→대지(0), 2=임야→산(1)
        String platGbCd = "2".equals(pnu.substring(10, 11)) ? "1" : "0";
        String bun = pnu.substring(11, 15);
        String ji = pnu.substring(15, 19);

        String raw = restClient.get()
                .uri(config.getDataGoKr().getBuildingUrl()
                                + "?serviceKey={key}&sigunguCd={sigunguCd}&bjdongCd={bjdongCd}"
                                + "&platGbCd={platGbCd}&bun={bun}&ji={ji}&numOfRows=1&pageNo=1&_type=json",
                        config.getDataGoKr().getApiKey(),
                        sigunguCd, bjdongCd, platGbCd, bun, ji)
                .accept(MediaType.ALL)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        JsonNode root;
        try {
            root = objectMapper.readTree(raw);
        } catch (Exception e) {
            log.warn("[BuildingRegisterClient] JSON parse failed: {}", e.getMessage());
            return null;
        }

        if (root == null) return null;

        String totalCount = root.path("response").path("body").path("totalCount").asText("0");
        if ("0".equals(totalCount)) return null;

        JsonNode itemNode = root.path("response").path("body").path("items").path("item");
        JsonNode item = itemNode.isArray() ? itemNode.get(0) : itemNode;
        if (item == null || item.isMissingNode()) return null;

        return new BuildingInfoResponse(
                textOrNull(item, "platPlc"),
                textOrNull(item, "bldNm"),
                textOrNull(item, "mainPurpsCdNm"),
                textOrNull(item, "etcPurps"),
                doubleOrNull(item, "platArea"),
                doubleOrNull(item, "archArea"),
                doubleOrNull(item, "totArea"),
                doubleOrNull(item, "bcRat"),
                doubleOrNull(item, "vlRat"),
                doubleOrNull(item, "heit"),
                intOrNull(item, "grndFlrCnt"),
                intOrNull(item, "ugrndFlrCnt"),
                textOrNull(item, "useAprDay"),
                textOrNull(item, "strctCdNm")
        );
    }

    private String textOrNull(JsonNode node, String field) {
        JsonNode n = node.path(field);
        if (n.isMissingNode() || n.isNull()) return null;
        String v = n.asText("").trim();
        return v.isEmpty() ? null : v;
    }

    private Double doubleOrNull(JsonNode node, String field) {
        JsonNode n = node.path(field);
        return (n.isMissingNode() || n.isNull()) ? null : n.asDouble();
    }

    private Integer intOrNull(JsonNode node, String field) {
        JsonNode n = node.path(field);
        return (n.isMissingNode() || n.isNull()) ? null : n.asInt();
    }
}
