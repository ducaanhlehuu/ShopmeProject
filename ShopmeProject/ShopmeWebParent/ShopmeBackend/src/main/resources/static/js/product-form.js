
var extraImagesCount = 0;
dropdownBrands = $("#brand")
dropDownCategories = $("#category")

$(document).ready(function (){
    dropdownBrands.change(function (){
        dropDownCategories.empty()
        getCategories()
    });
    getCategoriesForUpdateForm();
    $("input[name='extraImage']").each(function (index){
        extraImagesCount++;
        $(this).change(function (){
            showExtraImageThumbnail(this,index);
        })

    })
    $("a[name='linkRemoveDetail']").each(function (index){
        $(this).click(function (){
            removeDetailByIndex(index);
        })

    })
    $("a[name='linkRemoveExtraImage']").each(function(index) {
        $(this).click(function() {
            removeExtraImage(index);
        });
    });
})
function getCategoriesForUpdateForm(){
    let cate_id = $("#category_id");
    let is_update = false;
    if(cate_id.length){
        is_update = true;
    }
    if(!is_update){
        getCategories();
    }
}

function getCategories() {
    var brandId = dropdownBrands.val();
    var url = brandModuleUrl + "/" + brandId + "/categories";

    $.get(url, function(responseJson) {
        $.each(responseJson, function(index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropDownCategories);
        });
    });
}

function showExtraImageThumbnail(fileInput,index){
    let file = fileInput.files[0];
    fileName = file.name;
    imageNameHiddenField = $("#imageName" + index);
    if(imageNameHiddenField.length){
        imageNameHiddenField.val(fileName)
    }

    let reader = new FileReader();
    reader.onload = function (e) {
        console.log(file);
        $("#extraThumbnail"+index).attr("src",e.target.result);
    }
    reader.readAsDataURL(file);

    if (index >= extraImagesCount - 1) {
        addExtraImageSection(index + 1);
    }
}
function addExtraImageSection(index){
    const html = `
        <div class="col border m-3 p-2">
            <div th:id="'extraImageHeader' + ${index}"><label class="col-sm-10">Ảnh bổ sung: #${index + 1} </label></div>
            <div class="w-25 mb-2">
                <img id="extraThumbnail${index}" alt="Extra image preview ${index}" class="img-fluid" src=${defaultImageThumbnail} />
            </div>
            <div>
                <input type="file" name="extraImage" accept="image/png, image/jpeg" 
                onchange="showExtraImageThumbnail(this,${index})" style={{maxWidth: "300px", maxHeight: "200px"}} />
            </div>
        </div>`
    ;
    let htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark float-right"
			href="javascript:removeExtraImage(${index - 1})" 
			title="Remove this image"></a>
	`;

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    $("#productImages").append(html);
}

function addNextDetailSection(){
    let allDivDetails = $("[id^='divDetail']")
    let count = allDivDetails.length;

    let html =`
        <div class="form-inline" id="divDetail${count}">
                <label class="m-3">Tên</label>
                <input type="text" class="form-control w-25" name="detailNames" maxlength="255">
                <label class="m-3">Giá trị</label>
                <input type="text" class="form-control w-25" name="detailValues" maxlength="255">
            </div>`
    $("#productDetails").append(html);
    let previousDivDetailSection = allDivDetails.last();
    let previousDivDetailSectionId = previousDivDetailSection.attr("id");

    let htmlLinkRemove = `
    <a class="btn fas fa-times-circle fa-2x icon-dark"
            href = "javascript:deleteDetailSection('${previousDivDetailSectionId}')" 
            title ="Remove this detail"/>`

    previousDivDetailSection.append(htmlLinkRemove)
}

function deleteDetailSection(id){
    $("#" + id).remove();
}
function removeDetailByIndex(index){
    $("#divDetail" + index).remove();
}
function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}