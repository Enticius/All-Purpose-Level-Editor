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
  private int lastX = 0;
  private int lastY = 0;
  private boolean mouseHeld = false;
  private boolean scrollUp = false;
  private boolean scrollDown = false;
  private boolean scrollLeft = false;
  private boolean scrollRight = false;
  private int currentDirectory = 0;
  private int currentObject = 0;
  private int tabHeight = 0;
  private int moveTimer = 0;
  
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
    g2d.drawRect((x - le.getXOffset()) * tileSize, (y - le.getYOffset()) * tileSize, tileSize * imageObjects.get(currentDirectory).get(currentObject).getWidth(), tileSize * imageObjects.get(currentDirectory).get(currentObject).getHeight());
    
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
      
      //If the object being drawn is the selected object, draw a red box around it.
      if(i == currentObject){
        g2d.setColor(Color.RED);
        g2d.drawRect(x, tabHeight + 5, imageObjects.get(currentDirectory).get(i).getImage().getWidth(), imageObjects.get(currentDirectory).get(i).getImage().getHeight());
      }
      
      x += (imageObjects.get(currentDirectory).get(i).getImage().getWidth() + 4);
    }
  }
  
  public void update(){
    if(moveTimer > 2){
      if(scrollLeft){
        le.incrementXOffset(-1);
      } else if(scrollRight){
        le.incrementXOffset(1);
      }
      
      if(scrollUp){
        le.incrementYOffset(-1);
      } else if(scrollDown){
        le.incrementYOffset(1);
      }
      moveTimer = 0;
    } else if (scrollLeft || scrollRight || scrollUp || scrollDown){
      moveTimer++;
    }
    
    mouseX = (int)(MouseInfo.getPointerInfo().getLocation().getX()) - (int)(le.getLocationOnScreen().getX());
    mouseY = (int)(MouseInfo.getPointerInfo().getLocation().getY()) - (int)(le.getLocationOnScreen().getY());
    
    x = (int)(mouseX / tileSize) + le.getXOffset();
    y = (int)(mouseY / tileSize) + le.getYOffset();
    
    if(mouseHeld && (lastX != x || lastY != y)){
      System.out.println(x + "," + y);
      le.placeObject(imageObjects.get(currentDirectory).get(currentObject), x, y);
      lastX = x;
      lastY = y;
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
    currentObject = 0;
  }
  
  public void keyPressed(KeyEvent e){
    if(e.getKeyCode() == KeyEvent.VK_LEFT){
      if(currentObject > 0){
        currentObject--;
      }
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
      if(currentObject < (imageObjects.get(currentDirectory).size() - 1)){
        currentObject++;
      }
    } else if(e.getKeyCode() == KeyEvent.VK_W){
      scrollUp = true;
      scrollDown = false;
    } else if(e.getKeyCode() == KeyEvent.VK_A){
      scrollLeft = true;
      scrollRight = false;
    } else if(e.getKeyCode() == KeyEvent.VK_S){
      scrollDown = true;
      scrollUp = false;
    } else if(e.getKeyCode() == KeyEvent.VK_D){
      scrollRight = true;
      scrollLeft = false;
    }
  }
  
  public void keyReleased(KeyEvent e){
    if(e.getKeyCode() == KeyEvent.VK_W){
      scrollUp = false;
    } else if(e.getKeyCode() == KeyEvent.VK_A){
      scrollLeft = false;
    } else if(e.getKeyCode() == KeyEvent.VK_S){
      scrollDown = false;
    } else if(e.getKeyCode() == KeyEvent.VK_D){
      scrollRight = false;
    }
  }
}