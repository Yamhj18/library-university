package com.unamba.apilibrary.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unamba.apilibrary.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSchoolGetAll extends ResponseGeneric {
    List<Map<String, Object>> listSchool = new ArrayList<>();
}
