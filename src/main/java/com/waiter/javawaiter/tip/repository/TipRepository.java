package com.waiter.javawaiter.tip.repository;

import com.waiter.javawaiter.tip.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {
}
