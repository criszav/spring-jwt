package com.czavala.springsecurityjwt.persistance.entities.util;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_ONE_PRODUCT,
            Permission.CREATE_ONE_PRODUCT,
            Permission.UPDATE_ONE_PRODUCT,
            Permission.DISABLE_ONE_PRODUCT,

            Permission.READ_ALL_CATEGORIES,
            Permission.READ_ONE_CATEGORY,
            Permission.CREATE_ONE_CATEGORY,
            Permission.UPDATE_ONE_CATEGORY,
            Permission.DISABLE_ONE_CATEGORY,

            Permission.READ_MY_PROFILE
    )),
    ASSISTANT_ADMIN(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_ONE_PRODUCT,
            Permission.UPDATE_ONE_PRODUCT,

            Permission.READ_ALL_CATEGORIES,
            Permission.READ_ONE_CATEGORY,
            Permission.UPDATE_ONE_CATEGORY,

            Permission.READ_MY_PROFILE
    )),
    CUSTOMER(Arrays.asList(
            Permission.READ_MY_PROFILE
    ));

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
