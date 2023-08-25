package com.articoding.model.in;

import java.util.List;

public interface IUserDetail {

    public Integer getId();

    public String getUsername();

    public boolean isEnabled();

    public IRole getRole();

    public List<ILevel> getCreatedLevels();

    public List<IClassRoom> getClassRooms();

    public List<IClassRoom> getOwnerClassRooms();

}
