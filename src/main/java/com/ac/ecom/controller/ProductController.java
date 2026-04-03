package com.ac.ecom.controller;

import com.ac.ecom.dto.ProductDTO;
import com.ac.ecom.model.Product;
import com.ac.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {

        ProductDTO productDTO = productService.getProductById(id);
        if (productDTO.getId() > 0 )

        return new ResponseEntity(productDTO, HttpStatus.ACCEPTED);

        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("product/{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Integer id) {

        ProductDTO product = productService.getProductById(id);

        return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {

        Product savedProduct = null;
        try {
            savedProduct = productService.addOrUpdateProduct(product,imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct (@PathVariable Integer id,@RequestPart Product product,
                                                 @RequestPart MultipartFile imageFile) {

        Product updatedProduct = null;
        try {
            productService.addOrUpdateProduct(product, imageFile);
            return new ResponseEntity<>("Updated...",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {

        ProductDTO productDTO = productService.getProductById(id);
        if (productDTO != null) {
            productService.deleteProduct(id);

            return new ResponseEntity<>("Deleted...",HttpStatus.OK);
        }
        else  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(@RequestParam String keyword) {

        List<ProductDTO> productDTO = productService.searchProducts(keyword);
        System.out.println("Searching with "+ keyword);

        return new ResponseEntity<>(productDTO, HttpStatus.ACCEPTED);
    }


}
