$(document).ready(function (){
    $("#logoutLink").on("click",function (e){
        e.preventDefault();
        document.logoutForm.submit();
    })
})
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}