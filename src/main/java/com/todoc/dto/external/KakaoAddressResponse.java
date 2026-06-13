// 카카오 로컬 주소 검색 API 응답 매핑
package com.todoc.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record KakaoAddressResponse(
        List<Document> documents
) {

    public record Document(
            Address address,
            @JsonProperty("road_address") RoadAddress roadAddress
    ) {}

    public record Address(
            @JsonProperty("address_name") String addressName,
            @JsonProperty("b_code") String bCode,
            @JsonProperty("mountain_yn") String mountainYn,
            @JsonProperty("main_address_no") String mainAddressNo,
            @JsonProperty("sub_address_no") String subAddressNo
    ) {}

    public record RoadAddress(
            @JsonProperty("address_name") String addressName
    ) {}
}
