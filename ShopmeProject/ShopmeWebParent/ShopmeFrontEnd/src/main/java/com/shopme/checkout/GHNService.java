package com.shopme.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GHNService {

    @Autowired
    private GHNClient ghnClient;

    private static final String API_TOKEN = "62bcbe1d-1867-11ef-a9c4-9e9a72686e07";
    private static final int SHOP_ID = 5083568;

    public List<Map<String, Object>> getAvailableServices(String fromDistrictId, String toDistrictId) {
        Map<String, Object> request = new HashMap<>();
        request.put("shop_id", SHOP_ID);
        int from_district = Integer.parseInt(fromDistrictId);
        int to_district = Integer.parseInt(toDistrictId);
        request.put("from_district", from_district);
        request.put("to_district", to_district);
        Map<String, Object> response = ghnClient.getAvailableServices(API_TOKEN, request);
        return (List<Map<String, Object>>) response.get("data");
    }

    public Map<String, Object> getShippingFee(String serviceId, String toDistrictId, String toWardCode, int height, int length, int weight, int width) {

        Map<String, Object> request = new HashMap<>();
        request.put("service_id", Integer.parseInt(serviceId));
        request.put("insurance_value", 0);
        request.put("coupon", null);
        request.put("from_district_id", Integer.parseInt("1488"));
        request.put("to_district_id", Integer.parseInt(toDistrictId));
        request.put("to_ward_code", toWardCode);
        request.put("height", height);
        request.put("length", length);
        request.put("weight", weight);
        request.put("width", width);
        System.out.println(request);
        return (Map<String, Object>) ghnClient.getShippingFee(API_TOKEN,String.valueOf(SHOP_ID),request).get("data");
    }
}
