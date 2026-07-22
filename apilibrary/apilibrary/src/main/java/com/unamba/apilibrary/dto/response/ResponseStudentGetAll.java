package com.unamba.apilibrary.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unamba.apilibrary.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStudentGetAll extends ResponseGeneric {
    List<StudentDto> listStudent = new ArrayList<>();
}
