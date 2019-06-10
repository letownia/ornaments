package com.mapdecorator.images;

import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

public interface ImageStorageService {

  static byte[] imagaDataURLtoBinary(String imageDataURL){
      return DatatypeConverter.parseBase64Binary(imageDataURL.substring(imageDataURL.indexOf(",") + 1));
  }

  String saveMedium(String imageFileName, byte[] imageData);

  String createAndSaveThumbnail(String imageFileName, byte[] imageData) throws IOException;
}
