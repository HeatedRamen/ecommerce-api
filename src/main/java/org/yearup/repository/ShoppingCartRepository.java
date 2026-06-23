package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yearup.models.CartItem;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<CartItem, Integer>
{
    List<CartItem> findByUserId(int userId);

    CartItem findByUserIdAndProduct_ProductId(int userId, int productId);

    void deleteByUserId(int userId);

    @Query("""
            SELECT c
            FROM CartItem c
            JOIN FETCH c.product
            WHERE c.userId = :userId
            """)
    List<CartItem> findByUserIdWithProducts(int userId);
}
