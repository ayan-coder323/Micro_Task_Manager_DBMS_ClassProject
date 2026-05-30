package mth.dto;

public class ApiResponseDTO<T> {

    private String status;
    private int code;
    private String message;
    private T data;

    public ApiResponseDTO() {
    }

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setStatus("success");
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponseDTO<T> error(String message, int code) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setStatus("error");
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    // ── Getters & Setters ──────────────────────────────────

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
