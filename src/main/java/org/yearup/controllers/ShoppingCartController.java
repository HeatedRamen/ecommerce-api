package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.*;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService){
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart getCart(Principal principal)
    {

        int userId = getUserId(principal);

        // Use the shoppingCartService to get all items in the cart and return the cart
        return shoppingCartService.getByUserId(userId);
    }

    @PostMapping("products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> addItemToCart(@PathVariable int productId, Principal principal){

        int userId = getUserId(principal);

        ShoppingCart added = shoppingCartService.addProductById(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @PutMapping("products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart updateProduct(@PathVariable int productId, @RequestBody ShoppingCartItem updatedShoppingCartItem, Principal principal){

        int userId = getUserId(principal);

        return shoppingCartService.update(userId, productId, updatedShoppingCartItem);
    }

    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart(Principal principal){

        int userId = getUserId(principal);

        ShoppingCart cart = shoppingCartService.clear(userId);
        return ResponseEntity.ok(cart);
    }

    // Helper to get User ID
    private int getUserId(Principal principal){

        String userName = principal.getName();

        User user = userService.getByUserName(userName);
        return user.getId();
    }
}
