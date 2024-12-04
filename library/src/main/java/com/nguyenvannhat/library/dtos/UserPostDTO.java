package com.nguyenvannhat.library.dtos;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.dtos.UserDTO;
import lombok.Data;

@Data
public class UserPostDTO {
    private PostDTO postDTO;
    private UserDTO userDTO;
}
