// VWorld 토지이용계획 API 응답 매핑 DTO
package com.todoc.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record VWorldLandUseResponse(
        @JsonProperty("landUses") LandUses landUses
) {
    public record LandUses(
            String totalCount,
            List<Field> field
    ) {}

    public record Field(
            String pnu,
            @JsonProperty("prposAreaDstrcCode") String code,
            @JsonProperty("prposAreaDstrcCodeNm") String name,
            @JsonProperty("cnflcAtNm") String conflictType,
            @JsonProperty("registDt") String registDt,
            @JsonProperty("lastUpdtDt") String lastUpdtDt
    ) {}
}
