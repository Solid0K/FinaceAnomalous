package com.krishu.finaceanomoly;

public enum ExpenseCategory {
    TRAVEL,
    MEAL,
    SOFTWARE,
    OFFICE_SUPPLIES,
    ACCOMMODATION,
    OTHER;

    public static ExpenseCategory getCategory(String raw){
        if(raw==null){
            return OTHER;
        }
        try {
            return ExpenseCategory.valueOf(raw.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
