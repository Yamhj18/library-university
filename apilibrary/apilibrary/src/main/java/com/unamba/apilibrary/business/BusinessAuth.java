package com.unamba.apilibrary.business;

import java.util.Date;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.request.RequestAuthLogin;
import com.unamba.apilibrary.dto.request.RequestAuthRegister;
import com.unamba.apilibrary.dto.response.ResponseAuthLogin;
import com.unamba.apilibrary.dto.response.ResponseAuthRegister;
import com.unamba.apilibrary.entity.EntityUser;
import com.unamba.apilibrary.helper.JwtHelper;
import com.unamba.apilibrary.repository.RepositoryUser;
import com.unamba.apilibrary.staticdata.EnumUserRole;
import com.unamba.apilibrary.staticdata.EnumUserStatus;

@Service
public class BusinessAuth {

    private final RepositoryUser repositoryUser;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public BusinessAuth(
            RepositoryUser repositoryUser,
            PasswordEncoder passwordEncoder,
            JwtHelper jwtHelper) {
        this.repositoryUser = repositoryUser;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    public ResponseAuthLogin login(RequestAuthLogin request) {
        ResponseAuthLogin response = new ResponseAuthLogin();

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Email recibido: " + request.getEmail());

        EntityUser user = repositoryUser.findByEmail(request.getEmail()).orElse(null);

        System.out.println("Usuario encontrado: " + (user != null ? user.getEmail() : "NULL"));

        if (user == null) {
            response.listMessage.add("Correo o contraseña incorrectos.");
            return response;
        }

        System.out.println("Password BD (inicio): " + user.getPassword().substring(0, 10));
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println("Password coincide: " + matches);

        if (!matches) {
            response.listMessage.add("Correo o contraseña incorrectos.");
            return response;
        }

        if (!user.getStatus().equals(EnumUserStatus.ACTIVE.toString())) {
            response.listMessage.add("Su cuenta está inactiva.");
            return response;
        }

        String token = jwtHelper.generateToken(
                user.getEmail(),
                user.getRole(),
                user.getIdUser(),
                user.getFullName());

        response.setToken(token);
        response.setRole(user.getRole());
        response.setFullName(user.getFullName());
        response.success();
        return response;
    }

    public ResponseAuthRegister register(RequestAuthRegister request) {
        ResponseAuthRegister response = new ResponseAuthRegister();

        if (repositoryUser.existsByEmail(request.getEmail())) {
            response.listMessage.add("El correo ya está registrado.");
            return response;
        }

        EntityUser user = new EntityUser();
        user.setIdUser(UUID.randomUUID().toString());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(EnumUserRole.STUDENT.toString());
        user.setStatus(EnumUserStatus.ACTIVE.toString());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        repositoryUser.save(user);

        response.success();
        response.listMessage.add("Cuenta creada exitosamente.");
        return response;
    }
}