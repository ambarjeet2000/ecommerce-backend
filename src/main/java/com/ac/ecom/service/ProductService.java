package com.ac.ecom.service;

import com.ac.ecom.dto.ProductDTO;
import com.ac.ecom.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(int id);

    Product addOrUpdateProduct(Product product, MultipartFile imageFile) throws IOException;

    void deleteProduct(Integer id);

    List<ProductDTO> searchProducts(String keyword);
}
