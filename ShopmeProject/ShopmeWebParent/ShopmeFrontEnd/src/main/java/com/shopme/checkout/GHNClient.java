package com.shopme.checkout;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ghnClient", url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order")
public interface GHNClient {

    @PostMapping("/available-services")
    Map<String, Object> getAvailableServices(
            @RequestHeader("Token") String token,
            @RequestBody Map<String, Object> request
    );

    @PostMapping("/fee")
    @Headers("Content-Type: application/json")
    Map<String, Object> getShippingFee(@RequestHeader("Token") String token,
                                       @RequestHeader("ShopId") String shopId,
                                       @RequestBody Map<String, Object> request);
}
