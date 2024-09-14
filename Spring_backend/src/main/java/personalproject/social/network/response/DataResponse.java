package personalproject.social.network.response;

public class DataResponse {

	private String status;
	private String message;
	private Object result=null;

	public DataResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataResponse(String status, String message, Object result) {
		super();
		this.status = status;
		this.message = message;
		this.result=result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
