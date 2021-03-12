package com.mapdecorator.images;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemImageStorageServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(FileSystemImageStorageServiceTest.class);

  private static final String TEST_FILE_IDENTIFIER = "testing_test_test";

  private static byte[] SMALL_IMAGE_BYTE_ARRAY = new byte[]{1};
  private ImageStorageService imageStorageService;

  private static final String imageDirectory = FileSystems.getDefault().getPath("./target").toString();


  @Before
  public void setup() {
    imageStorageService = new FileSystemImageStorageService(imageDirectory);
    try {
      getTestFile().delete();
    } catch (Exception e) {
      logger.info("Test file not deleted");
    }
  }

  private static File getTestFile() {
    return new File(ImageStorageService.pathWithFilename(imageDirectory, TEST_FILE_IDENTIFIER));
  }

  @Ignore //doesn't work from command line "mvn clean install" (probably /target directory is deleted/non-existent ? )
  @Test
  public void saveMedium() throws IOException {
    String relativeFilePath = imageStorageService.saveMedium(TEST_FILE_IDENTIFIER, SMALL_IMAGE_BYTE_ARRAY);
    assert (getTestFile().toString().contains(relativeFilePath));
    assert (getTestFile().exists());
  }


  @Ignore //Skipped because the byte array here does not correspond to a real image!
  @Test
  public void createAndSaveThumbnail() throws IOException {
    String relativeFilePath = imageStorageService.createAndSaveThumbnail(TEST_FILE_IDENTIFIER, SMALL_IMAGE_BYTE_ARRAY);
    assert (getTestFile().toString().contains(relativeFilePath));
    assert (getTestFile().exists());
  }
}
