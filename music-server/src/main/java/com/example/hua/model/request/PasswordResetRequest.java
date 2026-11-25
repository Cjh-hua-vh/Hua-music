package com.example.hua.model.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String code;
    private String password;
    private String confirmPassword;
}
