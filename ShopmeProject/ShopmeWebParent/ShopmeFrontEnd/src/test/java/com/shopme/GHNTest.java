package com.shopme;

import com.shopme.checkout.GHNClient;
import com.shopme.checkout.GHNService;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class GHNTest {

    @Autowired
    private GHNService ghnService;

    @Test
    public void testGetService() {
        String from_id = "1488";
        String to_id = "1442";
        List<Map<String, Object>> services = ghnService.getAvailableServices(from_id, to_id);
        System.out.println(services);
    }

    @Test
    public void testGetFee() {
        // Dữ liệu test
        String serviceId = "53321";
        String fromDistrictId = "1488";
        String toDistrictId = "1963";
        String toWardCode = "100817";
        int height = 10;
        int length = 20;
        int weight = 500;
        int width = 15;
        int cod_value = 1000000;

        Map<String, Object> fee = ghnService.getShippingFee(
                serviceId,
                toDistrictId,
                toWardCode,
                height,
                length,
                weight,
                width
        );
        System.out.println(fee.get("total"));
    }

}
