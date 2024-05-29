$(document).ready(function() {
    $(".minus").on("click", function(evt) {
        evt.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) - 1;

        if (newQuantity > 0) {
            quantityInput.val(newQuantity);
        } else {
            showModalDialog("Thông báo","Số lượng phải lớn hơn 0");
        }
    });

    $(".plus").on("click", function(evt) {
        evt.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) + 1;

        if (newQuantity <= 5) {
            quantityInput.val(newQuantity);
        } else {
            showModalDialog("Thông báo","Tối đa 5 sản phẩm, nếu muốn mua trên 5 sản phẩm vui lòng liên hệ bộ phận CSKH");
        }
    });
});