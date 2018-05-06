import java.awt.*;
import java.awt.image.BufferedImage;

public class PlacedObject{
  private ImageObject i;
  private int x;
  private int y;
  private static Color transparentColour = new Color(0.0f, 0.0f, 0.0f, 0.0f);
  private int tileSize = LevelEditor.getTileSize();
  
  public PlacedObject(ImageObject i, int x, int y){
    this.i = i;
    this.x = x;
    this.y = y;
  }
  
  public void paint(Graphics2D g2d, int xOffset, int yOffset){
    g2d.drawImage(i.getImage(), (x - xOffset) * tileSize, (y - yOffset) * tileSize, transparentColour, null);
  }
}