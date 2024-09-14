package personalproject.social.network.response;

public class ChatTextMediaResponse {
	private ChatMessageResponse chatTextResponse;
	private ChatMessageResponse chatMediaResponse;

	public ChatMessageResponse getChatTextResponse() {
		return chatTextResponse;
	}

	public void setChatTextResponse(ChatMessageResponse chatTextResponse) {
		this.chatTextResponse = chatTextResponse;
	}

	public ChatMessageResponse getChatMediaResponse() {
		return chatMediaResponse;
	}

	public void setChatMediaResponse(ChatMessageResponse chatMediaResponse) {
		this.chatMediaResponse = chatMediaResponse;
	}

}
