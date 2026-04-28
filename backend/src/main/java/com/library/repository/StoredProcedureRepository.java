package com.library.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Map;

@Repository
public class StoredProcedureRepository {

    private final JdbcTemplate jdbcTemplate;

    public StoredProcedureRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int registerUser(String phone, String password, String name) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_register_user")
                .declareParameters(
                        new SqlParameter("p_phone", Types.VARCHAR),
                        new SqlParameter("p_password", Types.VARCHAR),
                        new SqlParameter("p_name", Types.VARCHAR),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Map<String, Object> out = jdbcCall.execute(Map.of("p_phone", phone, "p_password", password, "p_name", name));
        return (Integer) out.get("p_result");
    }

    public int borrowBook(Long userId, Long inventoryId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_borrow_book")
                .declareParameters(
                        new SqlParameter("p_user_id", Types.INTEGER),
                        new SqlParameter("p_inventory_id", Types.INTEGER),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Map<String, Object> out = jdbcCall.execute(Map.of("p_user_id", userId, "p_inventory_id", inventoryId));
        return (Integer) out.get("p_result");
    }

    public int returnBook(Long recordId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_return_book")
                .declareParameters(
                        new SqlParameter("p_record_id", Types.INTEGER),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Map<String, Object> out = jdbcCall.execute(Map.of("p_record_id", recordId));
        return (Integer) out.get("p_result");
    }
}
