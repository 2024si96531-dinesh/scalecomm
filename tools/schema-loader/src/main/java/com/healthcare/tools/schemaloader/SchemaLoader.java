package com.healthcare.tools.schemaloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class SchemaLoader {

    private static final String HOST_ENV = "SCHEMA_LOADER_HOST";
    private static final String PORT_ENV = "SCHEMA_LOADER_PORT";
    private static final String USERNAME_ENV = "SCHEMA_LOADER_USERNAME";
    private static final String PASSWORD_ENV = "SCHEMA_LOADER_PASSWORD";
    private static final String SCHEMA_SCRIPT_ENV = "SCHEMA_LOADER_SCHEMA_SCRIPT";
    private static final String TABLE_SCRIPT_ENV = "SCHEMA_LOADER_TABLE_SCRIPT";

    private SchemaLoader() {
    }

    public static void main(String[] args) throws Exception {
        String host = resolveValue(args, 0, HOST_ENV, "host");
        String port = resolveValue(args, 1, PORT_ENV, "port");
        String username = resolveValue(args, 2, USERNAME_ENV, "username");
        String password = resolveValue(args, 3, PASSWORD_ENV, "password");
        Path schemaScript = Path.of(resolveValue(args, 4, SCHEMA_SCRIPT_ENV, "schema script path"));
        Path tableScript = Path.of(resolveValue(args, 5, TABLE_SCRIPT_ENV, "table script path"));

        executeScripts(host, port, username, password, List.of(schemaScript, tableScript));
    }

    private static String resolveValue(String[] args, int index, String environmentKey, String label) {
        if (args.length > index && !args[index].isBlank()) {
            return args[index];
        }

        String fromEnvironment = System.getenv(environmentKey);
        if (fromEnvironment != null && !fromEnvironment.isBlank()) {
            return fromEnvironment;
        }

        throw new IllegalArgumentException("Missing required value for " + label + ". Provide it as an argument or environment variable " + environmentKey + '.');
    }

    private static void executeScripts(String host, String port, String username, String password, List<Path> scripts)
            throws SQLException, IOException {
        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%s/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                host,
                port
        );

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            for (Path script : scripts) {
                for (String statementSql : splitStatements(Files.readString(script))) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(statementSql);
                    }
                }
            }
        }
    }

    private static List<String> splitStatements(String scriptContent) {
        String[] rawStatements = scriptContent.split(";");
        List<String> statements = new ArrayList<>();
        for (String raw : rawStatements) {
            String cleaned = raw.strip();
            if (!cleaned.isEmpty()) {
                statements.add(cleaned);
            }
        }
        return statements;
    }
}