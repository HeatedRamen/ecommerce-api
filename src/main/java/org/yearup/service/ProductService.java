package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public List<Product> search(Integer categoryId, Double minPrice, Double maxPrice, String subCategory)
    {
        List<Product> products = categoryId != null
                ? productRepository.findByCategoryId(categoryId)
                : productRepository.findAll();

        return products.stream()
                       .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                       .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                       .filter(p -> subCategory == null || subCategory.equalsIgnoreCase(p.getSubCategory()))
                       .filter(Product::isFeatured)
                       .toList();
    }

    public List<Product> listByCategoryId(int categoryId)
    {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getById(int productId)
    {
        return productRepository.findById(productId).orElse(null);
    }

    public Product create(Product product)
    {
        product.setProductId(0);
        return productRepository.save(product);
    }

    public Product update(int productId, Product updatedProduct)
    {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategoryId(updatedProduct.getCategoryId());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setSubCategory(updatedProduct.getSubCategory());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setFeatured(updatedProduct.isFeatured());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        return productRepository.save(existingProduct);
    }

    public void delete(int productId)
    {
        productRepository.deleteById(productId);
    }
}
