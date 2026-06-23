package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        List<CartItem> userCartItem =  shoppingCartRepository.findByUserIdWithProducts(userId);

        ShoppingCart shoppingCart = new ShoppingCart();

        for (CartItem cartItem : userCartItem){
            ShoppingCartItem item = new ShoppingCartItem();

            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());

            shoppingCart.add(item);
        }

        return shoppingCart;
    }

    @Transactional
    public ShoppingCart addProductById(int userId, int productId){

        Product product = productService.getById(productId);

        CartItem existingItem = shoppingCartRepository.findByUserIdAndProduct_ProductId(userId, productId);

        // Looks for item
        if (existingItem != null) {

            existingItem.setQuantity(
                    existingItem.getQuantity() + 1);

            shoppingCartRepository.save(existingItem);
        }
        else {
            CartItem cartItem = new CartItem();

            cartItem.setUserId(userId);
            cartItem.setProduct(product);

            shoppingCartRepository.save(cartItem);
        }
        return getByUserId(userId);
    }

    public ShoppingCart update(int userId, int productId, ShoppingCartItem updatedShoppingCartItem){

        CartItem existingCartItem = shoppingCartRepository.findByUserIdAndProduct_ProductId(userId, productId);
        existingCartItem.setQuantity(updatedShoppingCartItem.getQuantity());
        shoppingCartRepository.save(existingCartItem);

        return getByUserId(userId);
    }

    @Transactional
    public ShoppingCart clear(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }

}
