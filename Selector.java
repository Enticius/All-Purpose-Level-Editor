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
  private int currentDirectory = 0;
  private int currentObject = 0;
  private int tabHeight = 0;
  
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
    
    //UI is made into a separate method for readability.
    drawUI(g2d);
  }
  
  public void drawUI(Graphics2D g2d){
    g2d.setFont(new Font("TimesRoman", Font.BOLD, 56));
    
    //Draw tabs based on directories.
    tabHeight = g2d.getFontMetrics().getHeight() + 4;
    for(int i = 0; i < usedDirectoryNameList.size(); i++){
      int x = i * (le.getWidth() / usedDirectoryNameList.size());
      if(i == currentDirectory){
        g2d.setColor(Color.LIGHT_GRAY);
      } else {
        g2d.setColor(Color.GRAY);
      }
      g2d.fillRect((x), 0, le.getWidth() / usedDirectoryNameList.size(), tabHeight);
      g2d.setColor(Color.DARK_GRAY);
      g2d.drawRect((x), 0, le.getWidth() / usedDirectoryNameList.size(), tabHeight);
      
      g2d.setColor(Color.WHITE);
      if(g2d.getFontMetrics().stringWidth(usedDirectoryNameList.get(i)) < ((le.getWidth() / usedDirectoryNameList.size())) - 4){
        g2d.drawString(usedDirectoryNameList.get(i), x + 2, g2d.getFontMetrics().getHeight() - 2);
      } else {
        g2d.drawString(usedDirectoryNameList.get(i).substring(0, usedDirectoryNameList.get(i).length() - 3) + "...", x + 2, tabHeight - 2);
      }
    }
    
    //Draw Images available in directory.
    int x = 2;
    for(int i = 0; i < imageObjects.get(currentDirectory).size(); i++){
      g2d.drawImage(imageObjects.get(currentDirectory).get(i).getImage(), x, tabHeight + 5, Color.WHITE, null);
      x += (imageObjects.get(currentDirectory).get(i).getImage().getWidth() + 4);
    }
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
    //If the player clicks in an area where the tabs will be.
    if((((int)(MouseInfo.getPointerInfo().getLocation().getY())) - ((int)(le.getLocationOnScreen().getY()))) < tabHeight){
      switchTab();
    } else {
      mouseHeld = true;
    }
  }
  
  public void mouseReleased(MouseEvent e){
    mouseHeld = false;
  }
  
  public void switchTab(){
    //Take mouse location, divide by tab width, floor.
    int newDirectory = (int)(((int)(MouseInfo.getPointerInfo().getLocation().getX()) - ((int)(le.getLocationOnScreen().getX()))) / (le.getWidth() / usedDirectoryNameList.size()));
    currentDirectory = newDirectory;
  }
}