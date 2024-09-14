package personalproject.social.network.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Utils {

	private static final String ROLE_ADMIN = "ROLE_ADMIN";;

	public String fileToBase64StringConversion(String filePath) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}

	public String getDateNow() {
		LocalDateTime instance = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.FORMAT_DATE);
		return formatter.format(instance);
	}

	public Boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_ADMIN));
	}

}
