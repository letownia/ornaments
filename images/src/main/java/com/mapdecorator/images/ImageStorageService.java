package com.mapdecorator.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.DatatypeConverter;

public interface ImageStorageService {

  static byte[] imagaDataURLtoBinary(String imageDataURL){
      return DatatypeConverter.parseBase64Binary(imageDataURL.substring(imageDataURL.indexOf(',') + 1));
  }

  String saveMedium(String imageFileName, byte[] imageData);

  String createAndSaveThumbnail(String imageFileName, byte[] imageData) throws IOException;

  BufferedImage getImage(String relativePathWithFileName) throws IOException;

  void writeImageToOutputStream(String relativePathWithFileName, OutputStream outputStream) throws IOException;
}
