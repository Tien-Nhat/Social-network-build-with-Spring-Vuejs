package personalproject.social.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import personalproject.social.network.request.CommentsRequest;
import personalproject.social.network.response.DataResponse;
import personalproject.social.network.services.CommentsService;
import personalproject.social.network.services.PostLikesService;
import personalproject.social.network.services.PostService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {
	@Autowired
	PostService postService;

	@Autowired
	PostLikesService potsLikesService;

	@Autowired
	CommentsService commentsService;

	@PostMapping("/add")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse addPost(MultipartHttpServletRequest data) {
		return postService.addPost(data);
	}

	@GetMapping("/posts")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getPosts(@RequestParam(required = false) Long userId) {
		return postService.getPosts(userId);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse deletePost(@RequestParam Long postId) {
		return postService.deletePost(postId);
	}

	@PutMapping("/restore")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse restorePost(@RequestParam Long postId) {
		return postService.restorePost(postId);
	}

	@PutMapping("/edit")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse editPost(MultipartHttpServletRequest data) {
		return postService.editPost(data);
	}

	@GetMapping("/search")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse search(@RequestParam String txtSearch) {
		return postService.searchPostUser(txtSearch);
	}

	@PostMapping("/add-like")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse addLike(@RequestParam Long postId) {
		return potsLikesService.addLike(postId);
	}

	@GetMapping("/get-likes")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getLikes(@RequestParam Long postId) {
		return potsLikesService.getListLikes(postId);
	}

	@PostMapping("/add-comment")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse addComment(@RequestBody CommentsRequest commentsRequest) {
		return commentsService.addComment(commentsRequest);
	}

	@GetMapping("/get-post")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public DataResponse getPost(@RequestParam Long postId) {
		return postService.getPost(postId);
	}

}
