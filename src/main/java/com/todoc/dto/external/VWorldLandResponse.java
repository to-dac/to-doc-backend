// VWorld 토지특성정보 API 응답 매핑
package com.todoc.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record VWorldLandResponse(
        @JsonProperty("landCharacteristicss") LandCharacteristics landCharacteristics
) {

    public record LandCharacteristics(
            String totalCount,
            List<Field> field
    ) {}

    public record Field(
            String pnu,
            @JsonProperty("ldCode") String ldCode,
            @JsonProperty("ldCodeNm") String ldCodeNm,
            @JsonProperty("regstrSeCode") String regstrSeCode,
            @JsonProperty("regstrSeCodeNm") String regstrSeCodeNm,
            @JsonProperty("mnnmSlno") String mnnmSlno,
            @JsonProperty("lndcgrCode") String lndcgrCode,
            @JsonProperty("lndcgrCodeNm") String lndcgrCodeNm,
            @JsonProperty("lndpclAr") String lndpclAr,
            @JsonProperty("oficlLndpcl") String oficlLndpcl,
            @JsonProperty("prposArea1") String prposArea1,
            @JsonProperty("prposArea1Nm") String prposArea1Nm,
            @JsonProperty("prposArea2") String prposArea2,
            @JsonProperty("prposArea2Nm") String prposArea2Nm,
            @JsonProperty("ladUseSittn") String ladUseSittn,
            @JsonProperty("ladUseSittnNm") String ladUseSittnNm,
            @JsonProperty("tpgrphHgCode") String tpgrphHgCode,
            @JsonProperty("tpgrphHgCodeNm") String tpgrphHgCodeNm,
            @JsonProperty("tpgrphFrmCode") String tpgrphFrmCode,
            @JsonProperty("tpgrphFrmCodeNm") String tpgrphFrmCodeNm,
            @JsonProperty("roadSideCode") String roadSideCode,
            @JsonProperty("roadSideCodeNm") String roadSideCodeNm,
            @JsonProperty("pblntfPclnd") String pblntfPclnd,
            @JsonProperty("lastUpdtDt") String lastUpdtDt
    ) {}
}
