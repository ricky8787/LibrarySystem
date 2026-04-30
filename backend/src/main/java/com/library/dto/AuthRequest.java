package com.library.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class AuthRequest {
    @NotBlank(message = "電話號碼不可為空")
    @Pattern(regexp = "^[0-9]{10}$", message = "電話號碼必須為10位數字")
    private String phone;

    @NotBlank(message = "密碼不可為空")
    @Size(min = 3, max = 20, message = "密碼長度必須在3到20個字元之間")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9 \\u4e00-\\u9fa5]*$", message = "名字不能包含特殊字元")
    private String name;
}
