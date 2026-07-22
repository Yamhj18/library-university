package com.unamba.apilibrary.dto.response;

import java.util.Map;

import com.unamba.apilibrary.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDashboard extends ResponseGeneric {
    private Map<String, Object> stats;
}
