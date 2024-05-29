package com.shopme.admin.entity.product;

import com.shopme.admin.entity.Brand;
import com.shopme.admin.entity.Category;
import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,length = 256,nullable = false)
    private String name;
    @Column(unique = true,length = 256,nullable = false)
    private String alias;
    @Column(length = 512,nullable = false, name = "short_description")
    private String shortDescription;
    @Column(length = 4096,nullable = false, name = "full_description")
    private String fullDescription;
    @Column(name="created_time")
    private Date createdTime;
    @Column(name="updated_time")
    private Date updateTime;

    private boolean enabled;
    @Column(name="in_stock")
    private boolean inStock;
    private float cost;
    private float price;
    @Column(name="discount_percent")
    private float discountPercent;
    private float length;
    private float width;
    private float height;
    private float weight;

    @Column(name = "main_image",nullable = false)
    private String mainImage;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    public Product(Integer id) {
        this.id = id;
    }

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }
    public void addExtraImage(String image){
        this.images.add(new ProductImage(image,this));
    }
    public String getMainImagePath(){
        if(id==null || mainImage==null)
            return "/ShopmeAdmin/images/image-thumbnail.png";
        return "/product-images/" + this.id + "/" + this.mainImage;
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }
    public void addDetail(String name,String value){
        this.details.add(new ProductDetail(name,value,this));
    }

    public boolean containsImageName(String fileName) {
        Iterator<ProductImage> iterator = images.iterator();

        while (iterator.hasNext()){
            ProductImage image = iterator.next();
            if(image.getName().equals(fileName)){
                return true;
            }
        }
        return false;
    }

    public boolean containsDetail(String name) {
        Iterator<ProductDetail> iterator = details.iterator();

        while (iterator.hasNext()){
            ProductDetail detail = iterator.next();
            if(detail.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getShorterName(){
        if(name.length()>70){
            return name.substring(0,70).concat("..");
        }
        return name;
    }
    @Transient
    public static String formatPrice(float price) {
        // Định dạng số theo kiểu tiền tệ của Việt Nam
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        // Xác định mẫu định dạng
        DecimalFormat decimalFormat = (DecimalFormat) format;
        decimalFormat.applyPattern("###,### đ");

        return decimalFormat.format(price);
    }

    @Transient
    public String getDiscountPrice(){
        if(discountPercent>0){
            return  formatPrice(price * (100 - discountPercent)/100);
        }
        return formatPrice(this.price);
    }

    @Transient
    public float getDiscountPriceNumber(){
        if(discountPercent>0){
            return  price * (100 - discountPercent)/100;
        }
        return this.price;
    }
    @Transient
    public String getFormattedPrice(){
        return formatPrice(this.price);
    }
}


