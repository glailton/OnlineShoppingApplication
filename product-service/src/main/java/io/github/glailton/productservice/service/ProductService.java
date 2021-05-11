package io.github.glailton.productservice.service;

import io.github.glailton.productservice.model.Product;
import io.github.glailton.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor()
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void create(Product product) {
        productRepository.save(product);
    }
}
