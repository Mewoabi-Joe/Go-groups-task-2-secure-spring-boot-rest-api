package com.example.test1.services;

import com.example.test1.dto.UserDTO;
import com.example.test1.exceptions.BadRequestException;
import com.example.test1.exceptions.NotFoundException;
import com.example.test1.models.Role;
import com.example.test1.models.User;
import com.example.test1.repositories.RoleRepository;
import com.example.test1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isPresent()) throw new BadRequestException("No user exist with that name");
        User theUser = optionalUser.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        theUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(theUser.getUsername(),theUser.getPassword(),authorities);
    }

    public User saveUser(UserDTO userDto){
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if(optionalUser.isPresent())  throw new BadRequestException("A user with that name already exist");
        User user = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save((user));
    }

    public User addRoleToUser(String username, String roleName){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isPresent()) throw new NotFoundException("No user with that name exist");

        Optional<Role> optionalRole = getTheRoleByName(roleName);
        if(!optionalRole.isPresent()) throw new NotFoundException("No role exist with that name");

        User theUser = optionalUser.get();
       Collection<Role> theUsersRoles = theUser.getRoles();

       theUsersRoles.add(optionalRole.get());
       theUser.setRoles(theUsersRoles);
       return  userRepository.save(theUser);
    }

    public Optional<User> getUser(String username){
        return userRepository.findByUsername(username);
    }
    public User getUserOrThrowError(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isPresent()) throw new NotFoundException("No user exit with that name");
        return optionalUser.get();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<Role> getTheRoleByName (String roleName){
        return roleRepository.findByName(roleName);
    }

    public Role saveRole(Role role){
        return roleRepository.save((role));
    }
}
