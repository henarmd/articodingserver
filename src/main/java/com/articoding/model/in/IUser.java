package com.articoding.model.in;

import com.articoding.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

public interface IUser {

    public Integer getId();

    public String getUsername();

    public boolean isEnabled();
    @JsonIgnore
    public List<Role> getRoles();

    default String getRol() {
        return getRoles().stream().map(Role::getName).collect(Collectors.joining(", "));
    }
}
