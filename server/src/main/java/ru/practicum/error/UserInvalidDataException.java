package ru.practicum.error;

public class UserInvalidDataException extends RuntimeException {
    public UserInvalidDataException(String message) {
        super(message);
    }
}
