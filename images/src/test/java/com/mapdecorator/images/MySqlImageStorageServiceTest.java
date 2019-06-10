package com.mapdecorator.images;

import static com.mapdecorator.images.MySqlImageStorageService.FILE_TYPE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlImageStorageServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(MySqlImageStorageServiceTest.class);

  private static final String TEST_FILE_NAME = "testing_test_test." + FILE_TYPE;

  private static byte[] SMALL_IMAGE_BYTE_ARRAY = new byte[]{1};
  private ImageStorageService imageStorageService;

  private static final Path imageDirectory = FileSystems.getDefault().getPath("./target");


  @Before
  public void setup() {
    imageStorageService = new MySqlImageStorageService(imageDirectory.toString());
   try {
      getTestFile().delete();
    } catch (Exception e) {
      logger.info("Test file not deleted");
    }
  }

  private static File getTestFile() {
    return new File(imageDirectory + "/" + TEST_FILE_NAME);
  }

  @Test
  public void saveMedium() {
    imageStorageService.saveMedium(TEST_FILE_NAME, SMALL_IMAGE_BYTE_ARRAY);
    assert(getTestFile().exists());
  }


  @Ignore
  @Test
  public void createAndSaveThumbnail() throws IOException {
    imageStorageService.createAndSaveThumbnail(TEST_FILE_NAME, SMALL_IMAGE_BYTE_ARRAY);
    assert(getTestFile().exists());
  }

//  String createAndSaveThumbnail(String imageFileName, byte[] imageData) throws IOException;
}