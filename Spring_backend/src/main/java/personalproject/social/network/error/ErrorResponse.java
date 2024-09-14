package personalproject.social.network.error;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private HttpStatus status;
	private String message;
	private Object result;

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(HttpStatus status) {
		this.status = status;
	}

	public ErrorResponse(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
