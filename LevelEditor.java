import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class LevelEditor extends JPanel{
  ArrayList<ArrayList<File>> assetList = new ArrayList<ArrayList<File>>();
  ArrayList<String> directoryNameList = new ArrayList<String>();
  ArrayList<String> usedDirectoryNameList = new ArrayList<String>();
  ArrayList<ArrayList<File>> usedAssetList = new ArrayList<ArrayList<File>>();
  ArrayList<Character> charKeys = new ArrayList<Character>();
  File key;
  
  public LevelEditor(){
    File folder = new File(System.getProperty("user.dir") + "/Assets");
    getFiles(folder);
    
    setUpWithKey();
    
    System.out.println(usedAssetList.size());
    
    for(int i = 0; i < usedAssetList.size(); i++){
      System.out.println(directoryNameList.get(i));
      for(int j = 0; j < usedAssetList.get(i).size(); j++){
        System.out.println(usedAssetList.get(i).get(j).getName());
        System.out.println(charKeys.get(i));
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
        charKeysTemp.add(temp[1].charAt(0));
      }
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
            added = true;
          }
          usedAssetList.get(i).add(assetList.get(i).get(j));
          //Retrieve the index of the item found, which matches with the character key it's given.
          charKeys.add(charKeysTemp.get(usedAssets.indexOf(assetList.get(i).get(j).getName().substring(0, assetList.get(i).get(j).getName().length() - 4))));
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
    Graphics2D g2d = (Graphics2D)g;
  }
  
  public void update(){
    
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
