package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DatabaseInfoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-info")
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get database metadata
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();

                // Add database info
                response.put("databaseProductName", metaData.getDatabaseProductName());
                response.put("databaseProductVersion", metaData.getDatabaseProductVersion());
                response.put("driverName", metaData.getDriverName());
                response.put("driverVersion", metaData.getDriverVersion());
                response.put("url", metaData.getURL());
                response.put("username", metaData.getUserName());

                // Get table information
                List<Map<String, Object>> tables = new ArrayList<>();
                try (ResultSet rs = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})) {
                    while (rs.next()) {
                        Map<String, Object> table = new HashMap<>();
                        String tableName = rs.getString("TABLE_NAME");
                        table.put("name", tableName);

                        // Get column information for each table
                        List<Map<String, Object>> columns = new ArrayList<>();
                        try (ResultSet columnRs = metaData.getColumns(conn.getCatalog(), null, tableName, "%")) {
                            while (columnRs.next()) {
                                Map<String, Object> column = new HashMap<>();
                                column.put("name", columnRs.getString("COLUMN_NAME"));
                                column.put("type", columnRs.getString("TYPE_NAME"));
                                column.put("size", columnRs.getInt("COLUMN_SIZE"));
                                column.put("nullable", columnRs.getBoolean("IS_NULLABLE"));
                                columns.add(column);
                            }
                        }
                        table.put("columns", columns);

                        // Count rows in the table
                        try {
                            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
                            table.put("rowCount", count);
                        } catch (Exception e) {
                            table.put("rowCount", "Error counting rows: " + e.getMessage());
                        }

                        tables.add(table);
                    }
                }
                response.put("tables", tables);
            }
        } catch (Exception e) {
            response.put("error", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}
