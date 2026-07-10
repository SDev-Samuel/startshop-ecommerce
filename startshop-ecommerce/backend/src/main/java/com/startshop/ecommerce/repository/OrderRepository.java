package com.startshop.ecommerce.repository;

import com.startshop.ecommerce.entity.Order;
import com.startshop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
