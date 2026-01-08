package com.project.TECHGEAR.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TECHGEAR.Model.ProductImage;
import java.util.List;

@Repository
public interface ProductImagerepositorio extends JpaRepository<ProductImage, Integer> {
    
    List<ProductImage> findByProductId(Integer productId);
    
}

