package com.example.marketingapplication.model;

import com.google.firebase.database.Exclude;

public class ProductModel {

    @Exclude
    public boolean isFavourite = false;
    //same name with in firebase database
    private String discountAvailable, discountNote, discountPrice, originalPrice, productCategory,
            productDescription, productId, productImage, productName, productStock, productSize,
            timestamp, uid;

    public ProductModel(){

    }

    public ProductModel(String id, String name, String price, String discount, String dNote, String url, String category){

    }

    public ProductModel(String discountAvailable, String discountNote, String discountPrice, String originalPrice, String productCategory,
                        String productDescription, String productId, String productImage, String productName, String productStock, String productSize,
                        String timestamp, String uid) {


        System.out.println(discountAvailable + discountNote + discountPrice+ originalPrice + productCategory +
                productDescription + productId + productImage + productName + productStock +
                timestamp + uid);
        this.discountAvailable = discountAvailable;
        this.discountNote = discountNote;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productStock = productStock;
        this.productSize = productSize;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getDiscountNote() {
        return discountNote;
    }

    public void setDiscountNote(String discountNote) {
        this.discountNote = discountNote;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
