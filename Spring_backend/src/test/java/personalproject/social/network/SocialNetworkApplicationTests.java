package personalproject.social.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personalproject.social.network.util.Utils;

@SpringBootTest
class SocialNetworkApplicationTests {
	@Autowired
	Utils utils;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetDateNow() {
		LocalDateTime instance = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateTimeNow = formatter.format(instance);
		assertEquals(dateTimeNow, utils.getDateNow());
	}

	@Test
	void testConvertBase64() throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File("D:/media-SNFE/1/4/1710751714487.download.jfif"));
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		assertEquals(encodedString,
				utils.fileToBase64StringConversion("D:/media-SNFE/1/4/1710751714487.download.jfif"));
	}

}
