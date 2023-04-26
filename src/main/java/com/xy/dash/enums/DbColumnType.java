package com.xy.dash.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @Author: xiangwei
 * @Date: 2022/11/28 19:19
 * @Description
 **/
@Getter
@ToString
public enum DbColumnType {

    INTEGER("Integer", (String) null),
    LONG("Long", (String) null),
    FLOAT("Float", (String) null),
    DOUBLE("Double", (String) null),
    BOOLEAN("Boolean", (String) null),
    STRING("String", (String) null),
    BLOB("Blob", "java.sql.Blob"),
    CLOB("Clob", "java.sql.Clob"),
    LOCAL_DATE("LocalDate", "java.time.LocalDate"),
    LOCAL_TIME("LocalTime", "java.time.LocalTime"),
    YEAR("Year", "java.time.Year"),
    LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime"),
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),
    BYTE_ARRAY("byte[]", (String) null),
    BYTE("Byte", (String) null),
    SHORT("Short", (String) null),
    CHARACTER("Character", (String) null),
    YEAR_MONTH("YearMonth", "java.time.YearMonth"),
    OBJECT("Object", (String) null),
    BIG_INTEGER("BigInteger", "java.math.BigInteger");


    private final String type;
    private final String pkg;

    DbColumnType(final String type, final String pkg) {
        this.type = type;
        this.pkg = pkg;
    }

}
