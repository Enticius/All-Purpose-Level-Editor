import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageObject{
  private BufferedImage image;
  private char charKey;
  private int width;
  private int height;
  private int tileSize;
  
  public ImageObject(File imageFile, char charKey){
    try{
      image = ImageIO.read(imageFile);
    } catch(IOException e){
      System.out.println("Could not find Image " + imageFile.getName());
    }
    this.charKey = charKey;
    tileSize = LevelEditor.getTileSize();
    width = Math.max((int)(image.getWidth() / tileSize), 1);
    height = Math.max((int)(image.getHeight() / tileSize), 1);
  }
  
  public BufferedImage getImage(){
    return image;
  }
  
  public int getWidth(){
    return width;
  }
  
  public int getHeight(){
    return height;
  }
  
  public char getCharKey(){
    return charKey;
  }
}