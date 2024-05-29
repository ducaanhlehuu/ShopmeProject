function checkPasswordMatch(confirmPassword){
    if(confirmPassword.value!=$("#password").val()){
        confirmPassword.setCustomValidity("Mật khẩu không khớp")
        console.log(confirmPassword.value)
    }
    else{
        confirmPassword.setCustomValidity('')
    }
}
function checkEmailUnique(form){
    let url = "[[@{/customers/check_email_unique}]]"
    let customerEmail = $("#email").val();
    let csrf = $("input[name='_csrf']").val();
    let params = {email:customerEmail,_csrf: csrf};

    $.post(url,params, function (response){
        if(response=="OK"){
            form.submit();
        }
        else if(response=="Duplicated"){
            showModalDialog("Thông báo ","Email đã tồn tại: "+customerEmail);
        }
        else {
            showModalDialog("Error","Unknown response from server");
        }
    }).fail(function (){
        showModalDialog("Error","Could not connect to the server");
    });
    return false;
}
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}