package com.nguyenvannhat.library.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //Lỗi các đối tượng đã tồn tại
    USER_EXIST("error.user.exist"),
    BOOK_EXIST("error.book.exist"),
    CATEGORY_EXIST("error.category.exist"),

    //Lỗi các đối tượng không được tìm thấy
    USER_NOT_FOUND("error.user.not_found"),
    BOOK_NOT_FOUND("error.book.notfound"),
    CATEGORY_NOT_FOUND("error.category.not_found"),
    POST_NOT_FOUND("error.post.not_found"),
    COMMENT_NOT_FOUND("error.comment.not_found"),
    ROLE_NOT_FOUND("error.role.not_found"),
    TOKEN_NOT_FOUND("error.token.not_found"),

    //Lỗi đăng nhập
    WRONG_USER_NAME_PASSWORD("error.wrong_user_name_password"),

    //Lỗi tạo token
    TOKEN_NOT_CREATE("error.token.not_create"),

    //Lỗi server
    INTERNAL_SERVER("error.internal.server");
    private String code;

    ErrorCode(String code) {
        this.code = code;
    }

}
