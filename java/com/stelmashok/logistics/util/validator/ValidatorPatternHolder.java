package com.stelmashok.logistics.util.validator;

public class ValidatorPatternHolder {

    //1-30, letters, digits, whitespaces, :.'- symbols
    public static final String NAME_PATTERN = "^([\\w\\s:.'-]{1,30})$";
    //valid email such as name@gmail.com
    public static final String EMAIL_PATTERN = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$";
    //6-50, at least one digit one lower letter and one capital letter
    public static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+$).{6,50}$";
    //1-50, letters, digits, whitespaces, :.,-' symbols
    public static final String TITLE_PATTERN = "^([\\w\\s:.,'-]{1,50})$";
    //1-30, letters, digits, whitespaces, :.'- symbols
    public static final String CATEGORY_TITLE_PATTERN = "^([\\w\\s:.,-]{1,30})$";
    //1-30, letters, digits, whitespaces, '- symbols
    public static final String CITY_TITLE_PATTERN = "^([\\w\\s:.,'-]{1,30})$";
    //1-10, digits
    public static final String PRICE_PATTERN = "^\\d{1,10}$";
    //1-300, letters, digits, whitespaces, :!?.,_'- symbols
    public static final String DESCRIPTION_PATTERN = "^([\\w\\s:!?.,_'-]{1,300})$";
    //1-30, letters, digits, :.@'()+- symbols
    public static final String CONTACT_PATTERN = "^([\\w:.@'()+-]{1,30})$";
    //1-30, letters, digits, whitespaces, '- symbols
    public static final String CITY_PATTERN = "^([\\w\\s:.,'-]{1,30})$";
    //1-30, letters, digits, whitespaces, :.'- symbols
    public static final String CATEGORY_PATTERN = "^([\\w\\s:.'-]{1,30})$";
    //jpg, jpeg, png
    public static final String PICTURE_EXTENSION_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|png))$)";
    //1-500, letters, digits, whitespaces, :!?.,_'- symbols
    public static final String COMMENT_TEXT_PATTERN = "^([\\w\\s:!?.,_'-]{1,500})$";

    private ValidatorPatternHolder() {
    }
}
