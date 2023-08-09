package com.articoding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique=true)
	private String username;
	@Column
	@JsonIgnore
	private String password;

	@Column
	private boolean enabled = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Valoration> valorationList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> commentList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Level> createdLevels;

	@ManyToOne()
	private Role role;

	@ManyToMany(mappedBy = "students")
	private List<ClassRoom> classRooms;

	@ManyToMany(mappedBy = "teachers")
	private List<ClassRoom> ownerClassRooms;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Valoration> getValorationList() {
		return valorationList;
	}

	public void setValorationList(List<Valoration> valorationList) {
		this.valorationList = valorationList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public List<Level> getCreatedLevels() {
		return createdLevels;
	}

	public void setCreatedLevels(List<Level> createdLevels) {
		this.createdLevels = createdLevels;
	}

	public List<ClassRoom> getClasses() {
		return classRooms;
	}

	public void setClasses(List<ClassRoom> classRooms) {
		this.classRooms = classRooms;
	}

	public List<ClassRoom> getOwnerClasses() {
		return ownerClassRooms;
	}

	public void setOwnerClasses(List<ClassRoom> ownerClassRooms) {
		this.ownerClassRooms = ownerClassRooms;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<ClassRoom> getClassRooms() {
		return classRooms;
	}

	public void setClassRooms(List<ClassRoom> classRooms) {
		this.classRooms = classRooms;
	}

	public List<ClassRoom> getOwnerClassRooms() {
		return ownerClassRooms;
	}

	public void setOwnerClassRooms(List<ClassRoom> ownerClassRooms) {
		this.ownerClassRooms = ownerClassRooms;
	}
}