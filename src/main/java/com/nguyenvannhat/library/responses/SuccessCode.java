package com.nguyenvannhat.library.responses;

import lombok.Getter;

@Getter
public enum SuccessCode {
    USER_CREATED("success.user.created"),
    USER_UPDATED("success.user.updated"),
    USER_DELETED("success.user.deleted"),
    USER_INFORMATION("success.user.get_information"),

    BOOK_CREATED("success.book.created"),
    BOOK_UPDATED("success.book.updated"),
    BOOK_DELETED("success.book.deleted"),
    BOOK_INFORMATION("success.book.get_information"),

    CATEGORY_CREATED("success.category.created"),
    CATEGORY_UPDATED("success.category.updated"),
    CATEGORY_DELETED("success.category.deleted"),
    CATEGORY_INFORMATION("success.category.get_information"),

    POST_CREATED("success.post.created"),
    POST_UPDATED("success.post.updated"),
    POST_DELETED("success.post.deleted"),
    POST_INFORMATION("success.post.information"),
    POST_TOP("success.post.top"),
    POST_LIKED("success.post.liked"),
    POST_UNLIKED("success.post.unliked"),

    COMMENT_CREATED("success.comment.created"),
    COMMENT_UPDATED("success.comment.updated"),
    COMMENT_DELETED("success.comment.deleted"),
    COMMENT_INFORMATION("success.comment.get_information"),
    COMMENT_RETRIEVED("success.comment.retrieved");

    private final String code;

    SuccessCode(String code) {
        this.code = code;
    }
}
