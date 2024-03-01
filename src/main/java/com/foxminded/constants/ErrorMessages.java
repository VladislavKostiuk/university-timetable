package com.foxminded.constants;

public final class ErrorMessages {
    private ErrorMessages() {}

    public static final String ENTITY_WAS_NOT_FOUND_BY_ID = "%s with id %d wasn't found";
    public static final String ENTITY_WAS_NOT_FOUND_BY_NAME = "%s with name %s wasn't found";
    public static final String ENTITY_CAN_NOT_BE_ADDED = "%s can't be added because it already exists";
    public static final String ENTITY_CAN_NOT_BE_UPDATED = "%s can't be updated because it doesn't exists";
    public static final String STUDENT_AND_TEACHER_WITH_SAME_NAME_FOUND = "Student and teacher with the same name were found";
}
