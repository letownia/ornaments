package com.mapdecorator.images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import javax.imageio.ImageIO;

import com.mapdecorator.images.util.ImageScalingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class FileSystemImageStorageService implements ImageStorageService {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final String saveDirectory;
  private final String mediumPhotoDirectory;
  private final String thumbnailDirectory;

  public FileSystemImageStorageService(@Value("${mapdecorator.images.save-directory}") String saveDirectory) {
    Objects.requireNonNull(saveDirectory);

    logger.info("Starting FileSystemImageStorageService w/ directory :" + saveDirectory);
    this.saveDirectory = saveDirectory;
    mediumPhotoDirectory = saveDirectory + "/" + "medium";
    thumbnailDirectory =  saveDirectory + "/" + "thumbnail";
  }

  @Override
  public String saveMedium(String imageIdentifier, byte[] imageData) throws IOException{
    logger.info("saveMedium " + imageIdentifier);
    Objects.requireNonNull(imageIdentifier);
    Objects.requireNonNull(imageData);
    String pathWithFilename = ImageStorageService.pathWithFilename(mediumPhotoDirectory, imageIdentifier);
    savePhotoToDisk(pathWithFilename, imageData);
    return pathWithFilename;
  }

  @Override
  public String createAndSaveThumbnail(String imageIdentifier, byte[] imageData) throws IOException {
    logger.info("createAndSaveThumbnail " + imageIdentifier);
    Objects.requireNonNull(imageIdentifier);
    Objects.requireNonNull(imageData);
    String pathWithFileName = ImageStorageService.pathWithFilename (thumbnailDirectory, imageIdentifier);

    byte[] rescaledImageBytes = ImageScalingUtils.rescaleImage(imageData, THUMBNAIL_MAX_WIDTH, THUMBNAIL_MAX_HEIGHT, FILE_TYPE);

    savePhotoToDisk(pathWithFileName, rescaledImageBytes);
    return pathWithFileName;
  }

  @Override
  public BufferedImage getImage(String relativePathWithFileName) throws IOException {
    String fullPath = saveDirectory + "/" + relativePathWithFileName;
    logger.info("Getting image w/ path " +  fullPath);
    return ImageIO.read(new File(fullPath));
  }

  @Override
  public void writeImageToOutputStream(String relativePathWithFileName, OutputStream outputStream) throws IOException {
    BufferedImage image = getImage(relativePathWithFileName);
    logger.info("Hosting image w/ path :" +  relativePathWithFileName);
    ImageIO.write(image, FILE_TYPE, outputStream);
  }


  private void savePhotoToDisk(String pathWithFilename, byte[] imageData) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(pathWithFilename)) {
      fos.write(imageData);
    }
  }


}
