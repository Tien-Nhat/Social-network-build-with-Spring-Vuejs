package personalproject.social.network.response;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

	private List<PostResponse> listPost=new ArrayList<>();
	private List<UserLiteResponse> listUser=new ArrayList<>();

	public List<PostResponse> getListPost() {
		return listPost;
	}

	public void setListPost(List<PostResponse> listPost) {
		this.listPost = listPost;
	}

	public List<UserLiteResponse> getListUser() {
		return listUser;
	}

	public void setListUser(List<UserLiteResponse> listUser) {
		this.listUser = listUser;
	}

}
