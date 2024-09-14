package personalproject.social.network.response;

import java.util.List;

public class ListFriendRequestResponse {
	private List<FriendRequestResponse> listFriendRe;
	private Long countRequestPending;

	public List<FriendRequestResponse> getListFriendRe() {
		return listFriendRe;
	}

	public void setListFriendRe(List<FriendRequestResponse> listFriendRe) {
		this.listFriendRe = listFriendRe;
	}

	public Long getCountRequestPending() {
		return countRequestPending;
	}

	public void setCountRequestPending(Long countRequestPending) {
		this.countRequestPending = countRequestPending;
	}

}
