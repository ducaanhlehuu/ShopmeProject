package com.shopme.admin.product;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.entity.Product;
import com.shopme.admin.entity.ProductImage;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProductSaveHelper {
    public static void setExistingImageName(Product product, String[] imageIds, String[] imageNames) {
        if(imageIds==null || imageIds.length==0) return;
        Set<ProductImage> images = new HashSet<>();

        for(int i =0 ;i < imageNames.length;i++){
            Integer id = Integer.parseInt(imageIds[i]);
            String name = imageNames[i];
            images.add(new ProductImage(id,name,product));
        }

        product.setImages(images);
    }

    public static void setProductDetails(Product product, String[] detailNames, String[] detailValues) {
        if(detailNames==null || detailNames.length==0)
            return;
        for(int i =0 ;i < detailNames.length;i++){
            String name = detailNames[i];
            String value = detailValues[i];
            if(!name.isEmpty() && !value.isEmpty()&&!product.containsDetail(name)){
                product.addDetail(name,value);
            }
        }
    }

    public static void saveUploadedImages(MultipartFile mainImageFile, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
        if(!mainImageFile.isEmpty()){
            String fileName = StringUtils.cleanPath(mainImageFile.getOriginalFilename());
            String uploadDir = "../product-images/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,mainImageFile);
        }
        if(extraImageMultiparts.length > 0) {
            String uploadDir = "../product-images/" + savedProduct.getId() + "/extra/";
            for(MultipartFile extraImageFile : extraImageMultiparts){
                if(!extraImageFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(extraImageFile.getOriginalFilename());
                    FileUploadUtil.saveFile(uploadDir,fileName,extraImageFile);
                }
            }
        }

    }

    public static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product){
        if(extraImageMultiparts.length > 0) {
            for(MultipartFile file : extraImageMultiparts){
                if(!file.isEmpty()){
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    if(!product.containsImageName(fileName)){
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

}
