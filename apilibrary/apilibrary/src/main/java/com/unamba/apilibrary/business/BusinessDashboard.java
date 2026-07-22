package com.unamba.apilibrary.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.response.ResponseDashboard;
import com.unamba.apilibrary.dto.response.ResponseDashboardCharts;

@Service
public class BusinessDashboard {

    private final JdbcTemplate jdbcTemplate;

    public BusinessDashboard(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseDashboard getStats() {
        ResponseDashboard response = new ResponseDashboard();

        String sql = "SELECT * FROM v_dashboard_stats LIMIT 1";

        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql);
            Map<String, Object> stats = new HashMap<>();

            stats.put("booksAvailable", result.get("books_available"));
            stats.put("booksUnavailable", result.get("books_unavailable"));
            stats.put("booksTotal", result.get("books_total"));
            stats.put("totalCopies", result.get("total_copies"));
            stats.put("totalCopiesAvailable", result.get("total_copies_available"));
            stats.put("studentsRegistered", result.get("students_registered"));
            stats.put("loansActive", result.get("loans_active"));
            stats.put("loansOverdue", result.get("loans_overdue"));
            stats.put("loansReturned", result.get("loans_returned"));
            stats.put("loansTotal", result.get("loans_total"));
            stats.put("categoriesTotal", result.get("categories_total"));

            response.setStats(stats);
            response.success();
        } catch (Exception e) {
            response.error();
            response.listMessage.add("Error al obtener estadísticas del dashboard.");
        }

        return response;
    }

    public ResponseDashboardCharts getCharts() {
        ResponseDashboardCharts response = new ResponseDashboardCharts();

        try {
            // Loans by school
            String sqlBySchool = """
                SELECT ps.name AS school_name, COUNT(l.id_loan) AS total_loans
                FROM tloan l
                INNER JOIN tuser u ON l.id_user = u.id_user
                LEFT JOIN tprofessional_school ps ON u.id_school = ps.id_school
                GROUP BY ps.name
                ORDER BY total_loans DESC
            """;
            List<Map<String, Object>> loansBySchool = jdbcTemplate.queryForList(sqlBySchool);
            response.setLoansBySchool(loansBySchool);

            // Loans by month (last 12 months)
            String sqlByMonth = """
                SELECT DATE_FORMAT(l.loan_date, '%Y-%m') AS month_label,
                       COUNT(l.id_loan) AS total_loans
                FROM tloan l
                WHERE l.loan_date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
                GROUP BY month_label
                ORDER BY month_label ASC
            """;
            List<Map<String, Object>> loansByMonth = jdbcTemplate.queryForList(sqlByMonth);
            response.setLoansByMonth(loansByMonth);

            // Most loaned books (top 10)
            String sqlMostLoaned = """
                SELECT b.title AS book_title, b.code AS book_code,
                       COUNT(l.id_loan) AS total_loans
                FROM tloan l
                INNER JOIN tbook b ON l.id_book = b.id_book
                GROUP BY b.title, b.code
                ORDER BY total_loans DESC
                LIMIT 10
            """;
            List<Map<String, Object>> mostLoaned = jdbcTemplate.queryForList(sqlMostLoaned);
            response.setMostLoanedBooks(mostLoaned);

            // Books by category
            String sqlByCategory = """
                SELECT c.name AS category_name, COUNT(b.id_book) AS total_books
                FROM tbook b
                INNER JOIN tcategory c ON b.id_category = c.id_category
                WHERE b.status = 'Available'
                GROUP BY c.name
                ORDER BY total_books DESC
            """;
            List<Map<String, Object>> byCategory = jdbcTemplate.queryForList(sqlByCategory);
            response.setBooksByCategory(byCategory);

            response.success();
        } catch (Exception e) {
            response.error();
            response.listMessage.add("Error al obtener datos de los gráficos.");
        }

        return response;
    }
}
