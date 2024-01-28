package com.example.springbatchoverview;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseRowMapper implements RowMapper<Map<Integer, Integer>> {

    @Override
    public Map<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {

        Map<Integer, Integer> map = new HashMap<>();

        map.put(rs.getInt("id"), rs.getInt("number"));

        return map;
    }
}
