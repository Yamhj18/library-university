package com.unamba.apilibrary.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {
    private String idUser;
    private String fullName;
    private String dni;
    private String email;
    private String studentCode;
    private String phoneNumber;
    private String idSchool;
    private String profileImage;
    private String status;
    private String schoolName;
    private Long activeLoans;
    private Long inactiveLoans;
}
