package com.library.common;

public enum ProcedureResult {
    SUCCESS(1, "Success"),
    FAIL(-1, "Fail");

    private final int code;
    private final String description;

    ProcedureResult(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get ProcedureResult by code
     * @param code The integer code returned by the stored procedure
     * @return Corresponding ProcedureResult enum
     */
    public static ProcedureResult fromCode(int code) {
        for (ProcedureResult result : ProcedureResult.values()) {
            if (result.getCode() == code) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown procedure result code: " + code);
    }
}
