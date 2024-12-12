package com.nguyenvannhat.library.configurations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("roles.properties")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleProperties {
    @Value("${api.v1.library.book.insert}")
    private String bookCreate;
    @Value("${api.v1.library.book.update}")
    private String bookUpdate;
    @Value("${api.v1.library.book.read}")
    private String bookRead;
    @Value("${api.v1.library.book.delete}")
    private String bookDelete;

    @Value("${api.v1.library.user.create}")
    private String userCreate;
    @Value("${api.v1.library.user.read}")
    private String userRead;
    @Value("${api.v1.library.user.update}")
    private String userUpdate;
    @Value("${api.v1.library.user.delete}")
    private String userDelete;

    @Value("${api.v1.library.category.create}")
    private String categoryCreate;
    @Value("${api.v1.library.category.read}")
    private String categoryRead;
    @Value("${api.v1.library.category.update}")
    private String categoryUpdate;
    @Value("${api.v1.library.category.delete}")
    private String categoryDelete;

    @Value("${api.v1.library.post.create}")
    private String postCreate;
    @Value("${api.v1.library.post.read}")
    private String postRead;
    @Value("${api.v1.library.post.update}")
    private String postUpdate;
    @Value("${api.v1.library.post.delete}")
    private String postDelete;

    @Value("${api.v1.library.comment.create}")
    private String commentCreate;
    @Value("${api.v1.library.comment.read}")
    private String commentRead;
    @Value("${api.v1.library.comment.update}")
    private String commentUpdate;
    @Value("${api.v1.library.comment.delete}")
    private String commentDelete;
}
