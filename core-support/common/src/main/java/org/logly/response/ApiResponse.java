package org.logly.response;

import lombok.Getter;

import org.logly.error.ErrorMessage;
import org.logly.error.ErrorType;

@Getter
public class ApiResponse<T> {

    private ResultType result;

    private T data;

    private ErrorMessage error;
    // For JSON Object
    public ApiResponse() {}

    public ApiResponse(ResultType result, T data, ErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(ErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error));
    }

    public static ApiResponse<?> error(ErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error, errorData));
    }
}
