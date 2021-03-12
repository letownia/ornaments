package com.mapdecorator.images.util;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageScalingUtils {


  public static byte[] imagaDataURLtoBinary(String imageDataURL){
      return DatatypeConverter.parseBase64Binary(imageDataURL.substring(imageDataURL.indexOf(',') + 1));
  }

  public static byte[] rescaleImage(byte[] imageBytes, int maxWidth, int maxHeight, String fileType) throws IOException {
    BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

    BufferedImage rescaledImage = ImageScalingUtils.rescaleImageInternal(inputImage, maxWidth, maxHeight);

    /*Writing new image to byteStream */
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(rescaledImage, fileType, baos);
    baos.flush();
    byte[] rescaledImageBytes = baos.toByteArray();
    baos.close();
    return rescaledImageBytes;
  }



  private static BufferedImage rescaleImageInternal(BufferedImage inputImage, int maxWidth, int maxHeight) {

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
