package com.library.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String phone;
    private String password;
    private String name;
}
