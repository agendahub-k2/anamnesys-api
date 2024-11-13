package com.anamnesys.domain;

import com.anamnesys.repository.model.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserAuthenticated extends UserModel {
    private String token;
}
