package com.shopme.admin.order;

import com.shopme.admin.entity.order.Order;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    private String defaultRedirectURL = "redirect:/orders/page/1";

    @GetMapping("/orders/page/{pageNum}")
    public String listPageOrder(@PathVariable("pageNum") Integer pageNum, Model model, @Param("keyword") String keyword) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        Page<Order> orderPage = orderService.listByPage(pageNum, keyword);
        List<Order> orderList = orderPage.getContent();

        long startCount = (pageNum - 1) * orderService.ORDERS_PER_PAGE + 1;
        long endCount = Math.min(startCount + orderService.ORDERS_PER_PAGE - 1, orderPage.getTotalElements());

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listOrders", orderList);
        model.addAttribute("page", orderPage);
        model.addAttribute("keyword", keyword);
        return "orders/order";
    }

    @GetMapping("/orders")
    public String listFirstPage() {
        return defaultRedirectURL;
    }


    @GetMapping("/orders/update/{id}")
    public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
                            HttpServletRequest request) {
        try {
            Order order = orderService.get(id);
            model.addAttribute("pageTitle", "Sửa đơn hàng (ID: " + id + ")");
            model.addAttribute("order", order);
            return "orders/order_form";

        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }

    }
    @GetMapping("/orders/detail/{id}")
    public String viewDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
                            HttpServletRequest request) {
        try {
            Order order = orderService.get(id);
            model.addAttribute("order", order);
            return "orders/order_detail";

        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }

    }
    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer orderId, Model model, RedirectAttributes ra,
                             HttpServletRequest request) {
        try {
            orderService.delete(orderId);
            ra.addFlashAttribute("message", "Đã xóa đơn hàng ID:" + orderId);

        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }
}
