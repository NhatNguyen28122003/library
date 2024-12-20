package com.nguyenvannhat.library.responses;

import lombok.Getter;

@Getter
public enum SuccessCode {
    USER_CREATED("success.user.created"),
    USER_UPDATED("success.user.updated"),
    USER_DELETED("success.user.deleted"),

    BOOK_CREATED("success.book.created"),
    BOOK_UPDATED("success.book.updated"),
    BOOK_DELETED("success.book.deleted"),

    CATEGORY_CREATED("success.category.created"),
    CATEGORY_UPDATED("success.category.updated"),
    CATEGORY_DELETED("success.category.deleted"),

    POST_CREATED("success.post.created"),
    POST_UPDATED("success.post.updated"),
    POST_DELETED("success.post.deleted"),

    COMMENT_CREATED("success.comment.created"),
    COMMENT_UPDATED("success.comment.updated"),
    COMMENT_DELETED("success.comment.deleted");

    private final String code;

    SuccessCode(String code) {
        this.code = code;
    }

}
