package com.articoding.error;

import com.articoding.model.Role;

public class NotAuthorization extends RestError  {

    public NotAuthorization(Role role, String action) {
        super (String.format("Un usuario %s no puede %s", role.getName(), action));
    }

    public NotAuthorization(String action) {
        super(String.format("El usuario logado no puede %s", action));
    }

}
