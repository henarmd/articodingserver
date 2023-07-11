package com.articoding.model.in;

import com.articoding.model.IUser;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface IClassRoom {

    Long getId();
    String getName();
    String getDescription();
    List<IUser> getTeachers();
    @Value("#{target.students.size()}")
    String getStudents();
}