package com.waiter.javawaiter.comment.mapper;

import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentShortDto toCommentShortDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentShortDto(
                comment.getCommentId(),
                comment.getComment());
    }
}
