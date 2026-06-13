// 토지 정보 조회 비즈니스 로직 — VWorld WFS 좌표 → PNU → 토지특성·규제·건물 조회
package com.todoc.service;

import com.todoc.client.BuildingRegisterClient;
import com.todoc.client.VWorldClient;
import com.todoc.dto.external.BuildingInfoResponse;
import com.todoc.dto.external.VWorldLandResponse;
import com.todoc.dto.external.VWorldLandUseResponse;
import com.todoc.dto.external.VWorldWfsResponse;
import com.todoc.dto.response.LandInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LandService {

    private final VWorldClient vWorldClient;
    private final BuildingRegisterClient buildingRegisterClient;

    public LandInfoResponse getLandInfo(double lat, double lng) {
        VWorldWfsResponse.Properties wfs = vWorldClient.getPnuByCoordinate(lat, lng);
        String pnu = wfs.pnu();
        String address = wfs.addr();
        VWorldLandResponse.Field field = vWorldClient.getLandCharacteristics(pnu);
        BuildingInfoResponse building = buildingRegisterClient.getBuilding(pnu);
        List<VWorldLandUseResponse.Field> landUses = vWorldClient.getLandUses(pnu);
        return LandInfoResponse.of(pnu, address, field, building, landUses);
    }
}
