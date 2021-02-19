package com.mapdecorator.images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileSystemImageStorageService implements ImageStorageService {

  private static final Logger logger = LoggerFactory.getLogger(FileSystemImageStorageService.class);

  public static final int THUMBNAIL_MAX_WIDTH = 160;
  public static final int THUMBNAIL_MAX_HEIGHT = 120;

  private static final String THUMBNAIL_FOLDER = "thumbnails";
  private static final String MEDIUM_FOLDER = "medium";

  private final String saveDirectory;
  private final String mediumPhotoDirectory;
  private final String thumbnailDirectory;


  static final String FILE_TYPE = "jpg";

  public FileSystemImageStorageService(@Value("${mapdecorator.images.save-directory}") String saveDirectory) {
    Objects.requireNonNull(saveDirectory);
    logger.info("Starting FileSystemImageStorageService w/ directory :" + saveDirectory);
    this.saveDirectory = saveDirectory;
    mediumPhotoDirectory = saveDirectory + "/" + MEDIUM_FOLDER;
    thumbnailDirectory =  saveDirectory + "/" + THUMBNAIL_FOLDER;
  }

  @Override
  public String saveMedium(String imageIdentifier, byte[] imageData) {
    Objects.requireNonNull(imageIdentifier);
    Objects.requireNonNull(imageData);
    String pathWithFilename = pathWithFilename(mediumPhotoDirectory, imageIdentifier);
    savePhotoToDisk(pathWithFilename, imageData);
    return MEDIUM_FOLDER + "/" + imageIdentifier;
  }

  @Override
  public String createAndSaveThumbnail(String imageIdentifier, byte[] imageData) throws IOException {
    Objects.requireNonNull(imageIdentifier);
    Objects.requireNonNull(imageData);

    BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageData));

    BufferedImage rescaledImage = rescaleImage(inputImage, THUMBNAIL_MAX_WIDTH, THUMBNAIL_MAX_HEIGHT);

    /*Writing new image to byteStream */
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(rescaledImage, FILE_TYPE, baos);
    baos.flush();
    byte[] imageInByte = baos.toByteArray();
    baos.close();

    String pathWithFileName = pathWithFilename (thumbnailDirectory, imageIdentifier);
    savePhotoToDisk(pathWithFileName, imageInByte);
    return THUMBNAIL_FOLDER + "/" + imageIdentifier;
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
    logger.info("Uploading image w/ path :" +  relativePathWithFileName);
    ImageIO.write(image, FILE_TYPE, outputStream);
  }


  private void savePhotoToDisk(String pathWithFilename, byte[] imageData) {
    try (FileOutputStream fos = new FileOutputStream(pathWithFilename)) {
      fos.write(imageData);
    } catch (Exception e) {
      logger.error("Exception during saveFile :" + e.getMessage());
    }
  }

  static String pathWithFilename(String directory, String identifier){
    return directory + "/" + identifier + "." + FILE_TYPE;
  }

  static BufferedImage rescaleImage(BufferedImage inputImage, int maxWidth, int maxHeight) {

    int currWidth = inputImage.getWidth();
    int currHeight = inputImage.getHeight();

    if (currWidth > maxWidth) {
      currHeight *= (0.0 + maxWidth) / currWidth;
      currWidth = maxWidth;
    }
    if (currHeight > maxHeight) {
      currWidth *= (0.0 + maxHeight) / currHeight;
      currHeight = maxWidth;
    }

    Image temp = inputImage.getScaledInstance(currWidth, currHeight, Image.SCALE_SMOOTH);
    int type = inputImage.getTransparency() == Transparency.OPAQUE ?
        BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

    BufferedImage newImage = new BufferedImage(currWidth, currHeight, type);
    Graphics2D g2d = newImage.createGraphics();
    g2d.drawImage(temp, 0, 0, null);
    g2d.dispose();
    return newImage;
  }
}
