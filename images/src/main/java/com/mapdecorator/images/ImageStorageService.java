package com.mapdecorator.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.DatatypeConverter;

public interface ImageStorageService {
  String FILE_TYPE = "jpg";

  int THUMBNAIL_MAX_WIDTH = 160;
  int THUMBNAIL_MAX_HEIGHT = 120;

  String saveMedium(String identifier, byte[] imageData) throws IOException;

  String createAndSaveThumbnail(String identifier, byte[] imageData) throws IOException;

  BufferedImage getImage(String relativePathWithFileName) throws IOException;

  void writeImageToOutputStream(String relativePathWithFileName, OutputStream outputStream) throws IOException;

  static String pathWithFilename(String directory, String identifier){
    return directory + "/" + identifier + "." + FILE_TYPE;
  }

}
