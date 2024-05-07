$("#fileImage").change(function (){
    let fileSize = this.files[0].size;
    alert("File Size: "+ fileSize);
    if(fileSize > 1048576){
        this.setCustomValidity("You must choose an image size <1 MB");
        this.reportValidity();
    }
    else{
        this.setCustomValidity("");
        showImagePortrait(this);
    }
});
function showImagePortrait(fileInput){
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = function (e) {
        console.log(file);
        $("#thumbnail").attr("src",e.target.result);
    }
    reader.readAsDataURL(file);
}
