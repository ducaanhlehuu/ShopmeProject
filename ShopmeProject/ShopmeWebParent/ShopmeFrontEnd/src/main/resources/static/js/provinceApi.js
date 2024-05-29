
const token = '62bcbe1d-1867-11ef-a9c4-9e9a72686e07';
function getProvinces(){
    fetch('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', {
        headers: {
            'Content-Type': 'application/json',
            'Token': token
        }
    }).then(response => response.json())
        .then(data => {
            const provinces = data.data.map(province => {
                return {
                    id: province.ProvinceID,
                    name: province.ProvinceName
                };
            });
            const customerProvinceId = $("#provinceId").val();
            console.log(customerProvinceId)
            provinces.forEach(province => {
                const $option = $("<option></option>")
                    .attr("value", province.id)
                    .text(province.name);
                if (province.id == customerProvinceId) {
                    $option.attr("selected", "selected");
                    $("#provinceName").val(province.name);
                }
                $("#listProvinces").append($option);
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateDistricts(option) {
    const provinceId = option.value;
    const provinceName = option.text;
    $("#provinceId").val(provinceId);
    $("#provinceName").val(provinceName);

    $("#listDistricts").empty().append('<option value="0" disabled selected>Chọn quận huyện</option>');
    $("#listWards").empty().append('<option value="0" disabled>Chọn xã, phường, thị trấn</option>');

    fetch(`https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${provinceId}`, {
        headers: {
            'Content-Type': 'application/json',
            'Token': token
        },
    })
        .then(response => response.json())
        .then(data => {
            const districts = data.data.map(district => {
                return {
                    id: district.DistrictID,
                    name: district.DistrictName
                };
            });
            const customerDistrictId = $("#districtId").val();
            districts.forEach(district => {
                const $option = $("<option></option>")
                    .attr("value", district.id)
                    .text(district.name);
                if (district.id == customerDistrictId) {
                    $option.attr("selected", "selected");
                    $("#districtName").val(district.name);
                }
                $("#listDistricts").append($option);
            });
        })
        .catch(error => {
            console.error('Error fetching districts:', error);
        });
}

function updateWards(option) {
    const districtId = option.value;
    const districtName = option.text;
    $("#districtId").val(districtId);
    $("#districtName").val(districtName);

    $("#listWards").empty().append('<option value="0" disabled selected>Chọn xã, phường, thị trấn</option>');

    fetch(`https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${districtId}`, {
        headers: {
            'Content-Type': 'application/json',
            'Token': token
        },
    })
        .then(response => response.json())
        .then(data => {
            const wards = data.data.map(ward => {
                return {
                    id: ward.WardCode,
                    name: ward.WardName
                };
            });
            const customerWardId = $("#wardId").val()
            wards.forEach(ward => {
                const $option = $("<option></option>")
                    .attr("value", ward.id)
                    .text(ward.name);
                if (ward.id == customerWardId) {
                    $option.attr("selected", "selected");
                    $("#wardName").val(ward.name);
                }
                $("#listWards").append($option);
            });
        })
        .catch(error => {
            console.error('Error fetching wards:', error);
        });
}

function updateWardFields(option) {
    const wardId = option.value;
    const wardName = option.text;
    $("#wardId").val(wardId);
    $("#wardName").val(wardName);
}