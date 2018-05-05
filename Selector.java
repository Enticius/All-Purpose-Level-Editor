import java.util.*;
import java.io.*;
import java.awt.*;

public class Selector{
  private ArrayList<ArrayList<File>> usedAssetList = new ArrayList<ArrayList<File>>();
  private ArrayList<ArrayList<Character>> charKeys = new ArrayList<ArrayList<Character>>();
  private ArrayList<String> usedDirectoryNameList = new ArrayList<String>();
  private LevelEditor le;
  private int tileSize = 16;
  private int mouseX = 0;
  private int mouseY = 0;
  private int x = 0;
  private int y = 0;
  
  public Selector(ArrayList<ArrayList<File>> tempUsedAssetList, ArrayList<ArrayList<Character>> tempCharKeys, ArrayList<String> tempUsedDirectoryNameList, LevelEditor le){
    this.le = le;
    
    for(int i = 0; i < tempUsedAssetList.size(); i++){
      usedAssetList.add(new ArrayList<File>());
      for(int j = 0; j < tempUsedAssetList.get(i).size(); j++){
        usedAssetList.get(i).add(tempUsedAssetList.get(i).get(j));
      }
    }
   
    for(int i = 0; i < tempCharKeys.size(); i++){
      charKeys.add(new ArrayList<Character>());
      for(int j = 0; j < tempCharKeys.get(i).size(); j++){
        charKeys.get(i).add(tempCharKeys.get(i).get(j));
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
    mouseX = ((int)(MouseInfo.getPointerInfo().getLocation().getX())) - ((int)(le.getLocationOnScreen().getX()));
    mouseY = (int)(MouseInfo.getPointerInfo().getLocation().getY()) - ((int)(le.getLocationOnScreen().getY()));
    
    x = (int)(mouseX / tileSize);
    y = (int)(mouseY / tileSize);
  }
}