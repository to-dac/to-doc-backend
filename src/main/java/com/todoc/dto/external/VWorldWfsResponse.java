// VWorld WFS 연속지적도 API 응답 매핑 DTO
package com.todoc.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record VWorldWfsResponse(
        List<Feature> features
) {
    public record Feature(
            @JsonProperty("properties") Properties properties
    ) {}

    public record Properties(
            String pnu,
            String addr
    ) {}
}
