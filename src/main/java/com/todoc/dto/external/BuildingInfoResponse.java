// 건축물대장 총괄표제부 API 응답 — item이 단건/복수에 따라 구조 달라져 JsonNode로 수신
package com.todoc.dto.external;

public record BuildingInfoResponse(
        String platPlc,
        String bldNm,
        String mainPurpsCdNm,
        String etcPurps,
        Double platArea,
        Double archArea,
        Double totArea,
        Double bcRat,
        Double vlRat,
        Double heit,
        Integer grndFlrCnt,
        Integer ugrndFlrCnt,
        String useAprDay,
        String strctCdNm
) {}
