package ru.shumilova.weatherapp.data.models;

import ru.shumilova.weatherapp.R;

public enum ErrorType {
    CITY_NOT_FOUND(R.string.error_msg_city_not_found),
    CONNECTION_ERROR(R.string.error_msg_no_connection),
    UNKNOWN_ERROR (R.string.error_msg_unknown);

    private int errorMsg;

    public int getErrorMsg() {
        return errorMsg;
    }

    ErrorType(int errorMsg) {
        this.errorMsg = errorMsg;
    }
}
