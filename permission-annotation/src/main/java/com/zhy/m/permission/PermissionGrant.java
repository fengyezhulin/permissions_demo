package com.zhy.m.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PermissionGrant
{
    int value();
}
