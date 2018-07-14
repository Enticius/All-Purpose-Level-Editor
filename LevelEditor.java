import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.lang.Character;

public class LevelEditor extends JPanel{
  //Stores all assets in folder.
  private ArrayList<ArrayList<File>> assetList = new ArrayList<ArrayList<File>>();
  //Stores all directories
  private ArrayList<String> directoryNameList = new ArrayList<String>();
  //Stores assets that are used in the legend.
  private ArrayList<ArrayList<File>> usedAssetList = new ArrayList<ArrayList<File>>();
  //Stores character keys found in legend.
  private ArrayList<ArrayList<Character>> charKeys = new ArrayList<ArrayList<Character>>();
  //Stores all assets converted to objects, use this when adding images.
  private ArrayList<ArrayList<ImageObject>> imageObjects = new ArrayList<ArrayList<ImageObject>>();
  //Stores used directories(sub-folders in assets folder).
  private ArrayList<String> usedDirectoryNameList = new ArrayList<String>();
  //Stores objects placed on the grid.
  private ArrayList<PlacedObject> placedObjects = new ArrayList<PlacedObject>();
  private File key;
  private Selector s;
  //Offsets are measured in tiles.
  private int xOffset = 0;
  private int yOffset = 0;
  private static int tileSize = 16;
  boolean needToSave = false;
  boolean needToLoad = false;
  
  public LevelEditor(){
    File folder = new File(System.getProperty("user.dir") + "/Assets");
    getFiles(folder);
    
    setUpWithKey();
    
    for(int i = 0; i < usedAssetList.size(); i++){
      System.out.println(usedDirectoryNameList.get(i));
      for(int j = 0; j < usedAssetList.get(i).size(); j++){
        System.out.println(usedAssetList.get(i).get(j).getName());
        System.out.println(charKeys.get(i).get(j));
      }
    }
    
    convertAssetsToObjects();
    
    s = new Selector(imageObjects, usedDirectoryNameList, this);
    
    addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
      }
      @Override
      public void mouseEntered(MouseEvent e) {
      }
      @Override
      public void mouseExited(MouseEvent e) {
        
      }
      @Override
      public void mousePressed(MouseEvent e) {
        s.mousePressed(e);
      }
      @Override
      public void mouseReleased(MouseEvent e) {
        s.mouseReleased(e);
      }
    });
    
    addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e){
        s.keyPressed(e);
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
          needToSave = true;
        } else if(e.getKeyCode() == KeyEvent.VK_L){
          needToLoad = true;
        }
      }
      @Override
      public void keyReleased(KeyEvent e){
        s.keyReleased(e);
      }
      @Override
      public void keyTyped(KeyEvent e){
        
      }
    });
    
    setFocusable(true);
  }
  
  public void convertAssetsToObjects(){
    for(int i = 0; i < usedAssetList.size(); i++){
      imageObjects.add(new ArrayList<ImageObject>());
      for(int j = 0; j < usedAssetList.get(i).size(); j++){
        imageObjects.get(i).add(new ImageObject(usedAssetList.get(i).get(j), charKeys.get(i).get(j).charValue()));
      }
    }
  }
  
  public void setUpWithKey(){
    key = new File(System.getProperty("user.dir") + "/Keys" + "/" + getInput("Key Name") + ".txt");
    ArrayList<String> usedAssets = new ArrayList<String>();
    ArrayList<Character> charKeysTemp = new ArrayList<Character>();
    
    try{
      FileReader fr = new FileReader(key);
      BufferedReader br = new BufferedReader(fr);
      
      String line;
      
      while((line = br.readLine()) != null){
        String[] temp = line.split("-");
        usedAssets.add(temp[0]);
        charKeysTemp.add(new Character(temp[1].charAt(0)));
      }
      
      br.close();
    } catch (IOException e){
      
    }
    
    //Tracks if upper directory was added.
    boolean added = false;
    
    //Check all assets to see if they match anything in the key.
    for(int i = 0; i < assetList.size(); i++){
      added = false;
      for(int j = 0; j < assetList.get(i).size(); j++){
        //AssetList contains <Name>.png, so chop off the .png when comparing to names given in Key.
        if(usedAssets.contains(assetList.get(i).get(j).getName().substring(0, assetList.get(i).get(j).getName().length() - 4))){
          //Upper Directory only needs to be added once.
          if(!added){
            usedDirectoryNameList.add(directoryNameList.get(i));
            usedAssetList.add(new ArrayList<File>());
            charKeys.add(new ArrayList<Character>());
            added = true;
          }
          usedAssetList.get(i).add(assetList.get(i).get(j));
          //Retrieve the index of the item found, which matches with the character key it's given.
          charKeys.get(i).add(charKeysTemp.get(usedAssets.indexOf(assetList.get(i).get(j).getName().substring(0, assetList.get(i).get(j).getName().length() - 4))));
        }
      }
    }
  }
  
  
  //Finds all images in "Assets" folder and sorts them by folder name, pseudo-recursive.
  public void getFiles(File folder){
    File[] fileList = folder.listFiles();
    
    ArrayList<File> imageList = new ArrayList<File>();
    
    for(int i = 0; i < fileList.length; i++){
      if(fileList[i].isFile()){
        imageList.add(fileList[i]);
      } else if(fileList[i].isDirectory()) {
        getFiles(fileList[i]);
      }
    }
    
    directoryNameList.add(folder.getName());
    assetList.add(imageList);
  }
  
  public synchronized String getInput(String target){
    EditorInput e = new EditorInput(target);
    
    String text = null;
    
    while(e.getText() == null){
      try{
        Thread.sleep(10);
      } catch(InterruptedException i){
        
      }
    }
    
    text = e.getText();
    
    return text;
  }
  
  public void paint(Graphics g){
    super.paint(g);
    
    Graphics2D g2d = (Graphics2D)g;
    
    s.paint(g2d);
    
    for(int i = 0; i < placedObjects.size(); i++){
      placedObjects.get(i).paint(g2d, xOffset, yOffset);
    }
  }
  
  public void update(){
    s.update();
    
    if(needToSave){
      saveLevelAsArray();
      needToSave = false;
    }
    
    if(needToLoad){
      loadLevelArray();
      needToLoad = false;
    }
  }
  
  public void placeObject(ImageObject asset, int x, int y){
    placedObjects.add(new PlacedObject(asset, x, y));
  }
  
  public void removeObject(int x, int y){
    int index = -1;
    
    int minX = 0;
    int maxX = 0;
    int minY = 0;
    int maxY = 0;
    
    for(int i = 0; i < placedObjects.size(); i++){
      minX = placedObjects.get(i).getX();
      maxX = placedObjects.get(i).getX() + placedObjects.get(i).getWidth();
      minY = placedObjects.get(i).getY();
      maxY = placedObjects.get(i).getY() + placedObjects.get(i).getHeight();
      
      if(x >= minX && x <= maxX && y >= minY && y <= maxY){
        index = i;
      }
    }
    
    if(index != -1){
      placedObjects.remove(index);
    }
  }
  
  public void saveLevelAsArray(){
    int minX = 0;
    int minY = 0;
    int maxX = 0;
    int maxY = 0;
    if(placedObjects.size() > 0){
      minX = placedObjects.get(0).getX();
      minY = placedObjects.get(0).getY();
      maxX = placedObjects.get(0).getX();
      maxY = placedObjects.get(0).getY();
    }
    int xShift = 0;
    int yShift = 0;
    
    for(int i = 0; i < placedObjects.size(); i++){
      if(placedObjects.get(i).getX() < minX){
        minX = placedObjects.get(i).getX();
      }
      
      if(placedObjects.get(i).getX() > maxX){
        maxX = placedObjects.get(i).getX() + (placedObjects.get(i).getWidth() - 1);
      }
      
      if(placedObjects.get(i).getY() < minY){
        minY = placedObjects.get(i).getY();
      }
      
      if(placedObjects.get(i).getY() > maxY){
        maxY = placedObjects.get(i).getY() + (placedObjects.get(i).getHeight());
      }
    }
    
    while(minX < 0){
      minX++;
      maxX++;
      xShift++;
    }
    
    while(minY < 0){
      minY++;
      maxY++;
      yShift++;
    }
    
    while(minX > 0){
      minX--;
      maxX--;
      xShift--;
    }
    
    while(minY > 0){
      minY--;
      maxY--;
      yShift--;
    }
    
    System.out.println(maxX + " " + maxY);
    
    char[][] levelArray = new char[maxY + 1][maxX + 1];
    
    for(int i = 0; i < levelArray.length; i++){
      for(int j = 0; j < levelArray[i].length; j++){
        //0 is temp default char, get user to specify in legend later.
        levelArray[i][j] = '0';
      }
    }
    
    for(int i = 0; i < placedObjects.size(); i++){
      levelArray[placedObjects.get(i).getY() + yShift][placedObjects.get(i).getX() + xShift] = placedObjects.get(i).getCharKey();
      
      for(int j = 0; j < placedObjects.get(i).getHeight(); j++){
        for(int k = 0; k < placedObjects.get(i).getWidth(); k++){
          if(j != 0 || k != 0){
            levelArray[placedObjects.get(i).getY() + yShift + j][placedObjects.get(i).getX() + xShift + k] = 'X';
          }
        }
      }
    }
    
    try{
      String levelName = getInput("Level Name");
      File f = new File(System.getProperty("user.dir") + "/SavedLevelArrays/" + levelName + ".txt");
      FileWriter fw = new FileWriter(f);
      PrintWriter pw = new PrintWriter(fw);
      for(int i = 0; i < levelArray.length; i++){
        for(int j = 0; j < levelArray[i].length; j++){
          if(j != levelArray[i].length - 1){
            pw.print(levelArray[i][j] + " ");
          } else if(i != levelArray.length - 1){
            pw.println(levelArray[i][j]);
          } else {
            pw.print(levelArray[i][j]);
          }
        }
      }
      
      pw.close();
    } catch (IOException e){
      System.out.println("Failed to save file.");
    }
  }
  
  public void loadLevelArray(){
    try{
      String levelName = getInput("Level Name");
      FileReader fr = new FileReader(System.getProperty("user.dir") + "/SavedLevelArrays/" + levelName + ".txt");
      BufferedReader br = new BufferedReader(fr);
      
      String line;
      int i = 0;
      while((line = br.readLine()) != null){
        String[] charArray = line.split(" ");
        for(int j = 0; j < charArray.length; j++){
          boolean found = false;
          
          if(charArray[j].charAt(0) != 'X' && charArray[j].charAt(0) != '0'){
            for(int m = 0; m < imageObjects.size(); m++){
              for(int n = 0; n < imageObjects.get(m).size(); n++){
                if(charArray[j].charAt(0) == imageObjects.get(m).get(n).getCharKey()){
                  placedObjects.add(new PlacedObject(imageObjects.get(m).get(n), j, i));
                  found = true;
                  break;
                }
              }
              
              if(found){
                break;
              }
            }
          }
        }
        
        i++;
      }
    } catch(IOException e){
      System.out.println("Failed to Load Level");
    }
  }
  
  public static int getTileSize(){
    return tileSize;
  }
  
  public int getXOffset(){
    return xOffset;
  }
  
  public int getYOffset(){
    return yOffset;
  }
  
  public void incrementXOffset(int increment){
    xOffset += increment;
  }
  
  public void incrementYOffset(int increment){
    yOffset += increment;
  }
  
  public static void main(String[] args) throws InterruptedException{
    JFrame frame = new JFrame("Editor Window");
    
    LevelEditor editor = new LevelEditor();
    frame.add(editor);
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
    
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    while(true){
      editor.update();
      editor.repaint();
      Thread.sleep(10);
    }
  }
}
