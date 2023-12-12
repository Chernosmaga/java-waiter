package com.waiter.javawaiter.comment.mapper;

import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.comment.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentMapper {

    public CommentShortDto toCommentShortDto(Comment comment) {
        log.info("Из Comment в CommentsShortDto: {}", comment);
        if (comment == null) {
            return null;
        }
        return new CommentShortDto(
                comment.getCommentId(),
                comment.getComment());
    }
}
