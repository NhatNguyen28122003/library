package com.nguyenvannhat.library.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class LoginResponse {
    String token;
    OffsetDateTime expires;
}
