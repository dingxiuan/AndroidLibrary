package com.dxa.android.ormlite;

/**
 * 字段名和值
 */
public class FieldValue {

    private String field;
    private Object value;

    public FieldValue() {
    }

    public FieldValue(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "field: " + field + ", value: " + value;
    }
}
