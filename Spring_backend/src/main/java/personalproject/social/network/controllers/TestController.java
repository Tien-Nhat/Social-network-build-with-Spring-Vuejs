package personalproject.social.network.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public ResponseEntity<?> allAccess() {
	  Resource videoFile = new FileSystemResource("D:/media-SNFE/167e5929-7e9c-4c5d-a69e-6d828f54cd92.Sparkle - Your Name AMV.mp4");
		return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType("video/mp4"))
              .body(videoFile);
  }
  
  @GetMapping("/all/image")
  public ResponseEntity<?> allAccess2() throws IOException {
	  String videoFile = convertToBase64("D:/media-SNFE/167e5929-7e9c-4c5d-a69e-6d828f54cd92.Sparkle - Your Name AMV.mp4");
		
	  return ResponseEntity.ok()
              .body(videoFile);
  }

  public static String convertToBase64(String path) throws IOException  {
		byte[] byteData = Files.readAllBytes(Paths.get(path));
		String base64String = Base64.getEncoder().encodeToString(byteData);
		return base64String;
	}
  
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
