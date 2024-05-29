package com.shopme.checkout;

import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CheckoutService {
    @Autowired
    private GHNService ghnService;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems,String to_district) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCost(cartItems,to_district);
        float paymentTotal = productTotal + shippingCostTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        checkoutInfo.setDeliverDays(3);
        // Tam thoi de 3 ngay van chuyen do chua call api
        checkoutInfo.setCodSupported(paymentTotal<5000000);

        return checkoutInfo;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0F;

        for (CartItem cartItem : cartItems){
            total += cartItem.getTotalPayment();
        }
        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0F;

        for (CartItem cartItem : cartItems){
            cost += cartItem.getProduct().getCost() * cartItem.getQuantity();
        }
        return cost;
    }

    private float calculateShippingCost(List<CartItem> cartItems, String to_district) {
        float cost = 0.0F;
        int maxLength = 0;
        int maxWidth = 0;
        int maxHeight = 0;
        int totalWeight = 0;

        String service_Id = null;
        List<Map<String, Object>> services = ghnService.getAvailableServices("1488",to_district);
        for(Map<String,Object> service : services){
            if(service.get("service_type_id").equals(2)){
                service_Id = String.valueOf(service.get("service_id"));
            }
        }

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            maxLength = (int) Math.max(maxLength, product.getLength());
            maxWidth = (int) Math.max(maxWidth, product.getWidth());
            maxHeight = (int) Math.max(maxHeight, product.getHeight());
            totalWeight += (int) product.getWeight() * quantity;
        }
        float shippingFee = (float) (int) ghnService.getShippingFee(service_Id,to_district,null,maxHeight,maxLength,totalWeight,maxWidth).get("total");

        for (CartItem item : cartItems){
            item.setShippingCost(shippingFee/cartItems.size());
        }

        return shippingFee;
    }
}
