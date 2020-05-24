package ru.shumilova.weatherapp.data.models;

import java.io.Serializable;

import ru.shumilova.weatherapp.R;

public enum ErrorType implements Serializable {
    CITY_NOT_FOUND(R.string.error_msg_city_not_found), CONNECTION_ERROR(R.string.error_msg_no_connection);

    private int errorMsg;

    public int getErrorMsg() {
        return errorMsg;
    }

    ErrorType(int errorMsg) {
        this.errorMsg = errorMsg;
    }
}
