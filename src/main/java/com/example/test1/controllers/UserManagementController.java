package com.example.test1.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test1.dto.RoleDTO;
import com.example.test1.dto.UserDTO;
import com.example.test1.dto.UserRoleDTO;
import com.example.test1.exceptions.BadRequestException;
import com.example.test1.models.Role;
import com.example.test1.models.User;
import com.example.test1.services.UserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user_management")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/get_users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userManagementService.getAllUsers());
    }

    @PostMapping("/create_user")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDto){
        return new ResponseEntity<>(userManagementService.saveUser(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/create_role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody RoleDTO roleDto){
        return new ResponseEntity<>(userManagementService.saveRole(new Role(roleDto.getRoleName())), HttpStatus.CREATED);
    }

@PutMapping("/add_role_to_user")
    public ResponseEntity<User> addRole(@Valid @RequestBody UserRoleDTO userRoleDTO){
        return new ResponseEntity<>(userManagementService.addRoleToUser(userRoleDTO.getUserName(), userRoleDTO.getRoleName()), HttpStatus.OK);
    }

    @GetMapping("/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Optional<User> optionalUser = userManagementService.getUser(username);
                if(!optionalUser.isPresent()) throw new BadRequestException("No such user exist");
                User user = optionalUser.get();
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName ).collect(Collectors.toList()))
                        .sign(algorithm);
//        response.setHeader("access_token", access_token);
//        response.setHeader("access_token", access_token);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                exception.printStackTrace();
                System.out.println("Arrived here");
                System.out.println("Error logging in: " + exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
//                    response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else{
           throw new BadRequestException("Refresh token is required");
        }
    }

}
