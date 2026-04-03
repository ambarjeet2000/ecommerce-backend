package com.ac.ecom.service.impl;

import com.ac.ecom.dto.ProductDTO;
import com.ac.ecom.model.Product;
import com.ac.ecom.repository.ProductRepository;
import com.ac.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductDTO> getAllProducts() {

        List<Product>  products = productRepo.findAll();

        return products.stream()
                .map(prod -> modelMapper.map(prod,ProductDTO.class))
                .toList();
    }

    @Override
    public ProductDTO getProductById(int id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found "+ id));

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public Product addOrUpdateProduct(Product product, MultipartFile imageFile) throws IOException {

        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {

        productRepo.deleteById(id);
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {

        return productRepo.searchProducts(keyword);
    }


}
