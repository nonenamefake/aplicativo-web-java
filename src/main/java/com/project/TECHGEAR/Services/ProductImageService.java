package com.project.TECHGEAR.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TECHGEAR.Model.ProductImage;
import com.project.TECHGEAR.Repository.ProductImagerepositorio;

@Service
public class ProductImageService {
    @Autowired
    private ProductImagerepositorio productImagerepositorio;

    public List<ProductImage> get(){
        return productImagerepositorio.findAll();
    }
    
    public ProductImage get(int id){
        return productImagerepositorio.findById(id).orElseThrow(() -> new RuntimeException("ProductImage not found with id: " + id));
    }
    
    public List<ProductImage> getByProductId(int productId){
        return productImagerepositorio.findByProductId(productId);
    }
    
    public void save(ProductImage productImage){
        productImagerepositorio.save(productImage);
    }
    
    public void update(ProductImage productImage){
        if (productImage.getId() == null || !productImagerepositorio.existsById(productImage.getId())) {
            throw new RuntimeException("ProductImage not found with id: " + productImage.getId());
        }
        productImagerepositorio.save(productImage);
    }
    
    public void delete(int id){
        if (!productImagerepositorio.existsById(id)) {
            throw new RuntimeException("ProductImage not found with id: " + id);
        }
        productImagerepositorio.deleteById(id);
    }
}

