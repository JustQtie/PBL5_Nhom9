package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoLieuThongKeResponse {
    @JsonProperty("total_order_success")
    private int tongGiaoDichThanhCong;
    @JsonProperty("total_order_canceled")
    private int tongGiaoDichBiHuy;
    @JsonProperty("total_order")
    private int tongTatCaGiaoDich;
    @JsonProperty("total_order_success_user")
    private int tongGiaoDichThanhCongOfUser;
    @JsonProperty("total_order_canceled_user")
    private int tongGiaoDichBiHuyOfUser;
    @JsonProperty("total_order_user")
    private int tongTatCaGiaoDichOfUser;
    @JsonProperty("total_money_user")
    private float tongTienChiTieuOfUser;
    @JsonProperty("EC")
    private String EC;
}
