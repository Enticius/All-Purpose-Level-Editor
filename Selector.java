import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Selector{
  private ArrayList<ArrayList<ImageObject>> imageObjects = new ArrayList<ArrayList<ImageObject>>();
  private ArrayList<String> usedDirectoryNameList = new ArrayList<String>();
  private LevelEditor le;
  private int tileSize = LevelEditor.getTileSize();
  private int mouseX = 0;
  private int mouseY = 0;
  private int x = 0;
  private int y = 0;
  private boolean mouseHeld = false;
  
  public Selector(ArrayList<ArrayList<ImageObject>> tempImageObjects, ArrayList<String> tempUsedDirectoryNameList, LevelEditor le){
    this.le = le;
    
    for(int i = 0; i < tempImageObjects.size(); i++){
      imageObjects.add(new ArrayList<ImageObject>());
      for(int j = 0; j < tempImageObjects.get(i).size(); j++){
        imageObjects.get(i).add(tempImageObjects.get(i).get(j));
      }
    }
    
    for(int i = 0; i < tempUsedDirectoryNameList.size(); i++){
      usedDirectoryNameList.add(tempUsedDirectoryNameList.get(i));
    }
  }
  
  public void paint(Graphics2D g2d){
    g2d.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
  }
  
  public void update(){
    mouseX = (int)(MouseInfo.getPointerInfo().getLocation().getX()) - (int)(le.getLocationOnScreen().getX());
    mouseY = (int)(MouseInfo.getPointerInfo().getLocation().getY()) - (int)(le.getLocationOnScreen().getY());
    
    x = (int)(mouseX / tileSize);
    y = (int)(mouseY / tileSize);
    
    if(mouseHeld){
      le.placeObject(imageObjects.get(0).get(0), x, y);
    }
  }
  
  public void mousePressed(MouseEvent e){
    mouseHeld = true;
  }
  
  public void mouseReleased(MouseEvent e){
    mouseHeld = false;
  }
}