package com.unamba.apilibrary.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unamba.apilibrary.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStudentProfile extends ResponseGeneric {
    private StudentDto student;
    private List<LoanDto> activeLoans = new ArrayList<>();
    private List<LoanDto> loanHistory = new ArrayList<>();
    private Map<String, Object> statistics;
}
