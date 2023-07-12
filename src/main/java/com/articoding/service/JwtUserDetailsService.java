package com.articoding.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.articoding.model.PotencialUser;
import com.articoding.model.Role;
import com.articoding.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.articoding.repository.UserRepository;
import com.articoding.model.User;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
				true, getAuthorities(user.getRoles()));
	}

	private List<? extends GrantedAuthority> getAuthorities(
			List<Role> roles) {

		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role rol : roles) {
			authorities.add(new SimpleGrantedAuthority(rol.getName()));
		}
		return authorities;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

	public User save(PotencialUser user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEnabled(true);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<SimpleGrantedAuthority> privileges = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
		List<String> actualUserRoles = privileges.stream().map(simpleGrantedAuthority -> simpleGrantedAuthority.getAuthority()).collect(Collectors.toList());
		List<Role> roleList = new ArrayList<>();
		for (String role: user.getRoles()) {
			Role newRole = null;
			switch (role) {
				case "ROLE_ADMIN":{
					if (actualUserRoles.contains("ROLE_ADMIN")) {
						newRole = roleRepository.findByName("ROLE_ADMIN");
					} else {
					throw new RuntimeException("No eres tan importante maquina");
					}
				} break;
				case "ROLE_TEACHER":{
					if (actualUserRoles.contains("ROLE_ADMIN")) {
						newRole = roleRepository.findByName("ROLE_TEACHER");
					} else {
						throw new RuntimeException("No eres tan importante maquina");
					}
				}break;
				case "ROLE_USER":{
					newRole = roleRepository.findByName("ROLE_USER");
				}break;
				default:
					throw new RuntimeException("pero que role es ese, maquina...");//TODO
			}
			roleList.add(newRole);
		}
		newUser.setRoles(roleList);
		return userRepository.save(newUser);
	}
}