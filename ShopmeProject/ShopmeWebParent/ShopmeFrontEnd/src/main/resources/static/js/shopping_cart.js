$(document).ready(function() {
    $(".minus").on("click", function(evt) {
        evt.preventDefault();
        decreaseQuantity($(this))
    });

    $(".plus").on("click", function(evt) {
        evt.preventDefault();
        increaseQuantity($(this))
    });
    $(".linkRemove").on("click", function(evt) {
        evt.preventDefault();
        removeCart($(this))
    });
});
function decreaseQuantity(link){
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateCart(productId)
    } else {
        showModalDialog("Thông báo","Số lượng phải lớn hơn 0");
    }
}
function increaseQuantity(link){
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity <= 5) {
        quantityInput.val(newQuantity);
        updateCart(productId)
    } else {
        showModalDialog("Thông báo","Tối đa 5 sản phẩm, nếu muốn mua trên 5 sản phẩm vui lòng liên hệ bộ phận CSKH");
    }
}
function updateCart(productId) {
    quantity = $("#quantity" + productId).val();
    url = contextPath + "cart/update/" + productId + "/" + quantity;
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response) {
        console.log(response)
        $("#totalPayment").html('&nbsp;&nbsp;' + response);
    }).fail(function() {
        showModalDialog("Lỗi","Lỗi khi thêm mới sản phẩm.");
    });
}

function removeCart(link) {
    url = link.attr("href")
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response) {
        rowNumber = link.attr("rowNumber")
        $("#totalPayment").html('&nbsp;&nbsp;' + response);
        removeProductHTML(rowNumber);
        $("#row" + rowNumber).remove()
        updateCountNumbers()
    }).fail(function() {
        showModalDialog("Lỗi","Không thể xóa sản phẩm.");
    });
}

function removeProductHTML(rowNumber) {
    $("#row" + rowNumber).remove();
    $("#blankLine" + rowNumber).remove();
}

function updateCountNumbers() {
    $(".divCount").each(function(index, element) {
        element.innerHTML = "" + (index + 1);
    });
}