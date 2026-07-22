package com.unamba.apilibrary.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unamba.apilibrary.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDashboardCharts extends ResponseGeneric {
    private List<Map<String, Object>> loansBySchool = new ArrayList<>();
    private List<Map<String, Object>> loansByMonth = new ArrayList<>();
    private List<Map<String, Object>> mostLoanedBooks = new ArrayList<>();
    private List<Map<String, Object>> booksByCategory = new ArrayList<>();
}
