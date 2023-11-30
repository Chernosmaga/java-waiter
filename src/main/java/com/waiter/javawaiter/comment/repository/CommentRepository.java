package com.waiter.javawaiter.comment.repository;

import com.waiter.javawaiter.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteCommentsByDishId(Long dishId);
    Comment findByDishId(Long dishId);
    Long findDishIdByCommentId(Long commentId);
}
