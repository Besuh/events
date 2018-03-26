package com.besuh.events.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.besuh.events.models.Role;
import com.besuh.events.models.User;
import com.besuh.events.repositories.RoleRepository;
import com.besuh.events.repositories.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	public void saveWithUserRole(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(roleRepository.findByName("ROLE_USER"));
		userRepository.save(user);
	}
	public void updateWithUserRole(User user) {
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }
	public void saveUserWithAdminRole(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
		userRepository.save(user);
	}
	public void updateUserWithAdminRole(User user) {
        user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
        userRepository.save(user);
    }
	public void saveWithSuperRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_SUPER"));
        userRepository.save(user);
    }
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	public void updateUserDate(Long id, User user) {
		user.setUpdatedAt(new Date());
		userRepository.save(user);	
    }
	public List<User> all(){
		return (List<User>) userRepository.findAll();
	}
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}
	public User findUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}
	public void initRoles() {
		if (roleRepository.findAll().size() < 1) {
			ArrayList<Role> roles = new ArrayList<Role>();
			roles.add(new Role("ROLE_USER"));
			roles.add(new Role("ROLE_ADMIN"));
			roles.add(new Role("ROLE_SUPER"));
			roleRepository.saveAll(roles);
		}
	}
}
