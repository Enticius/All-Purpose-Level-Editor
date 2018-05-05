import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class EditorInput{
  private InputField i;
  protected JFrame frame;
  private String text = null;
  
  public EditorInput(String target){
    SwingUtilities.invokeLater(new Runnable() {
      public void run(){
        InputField input = new InputField(target);
        String text = null;
        frame = new JFrame(target);
        
        frame.add(input);
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      }
    });
  }
  
  public String getText(){
    return text;
  }
  
  protected void setText(String text){
    this.text = text;
  }
  
  class InputField extends JPanel implements ActionListener{
    private JTextField inField;
    private JTextArea displayArea;
    String target;
    String text = null;
    int startReplace = 0;
    int count = 0;
    
    public InputField(String target){
      super(new GridBagLayout());
      this.target = target;
      
      createField();
    }
    
    public void actionPerformed(ActionEvent e){
      if(inField.getText().length() <= 0){
        displayArea.selectAll();
        displayArea.replaceRange("Please enter a " + target + " with at least 1 character. \n", startReplace, displayArea.getSelectionEnd());
      } else{
        //Set variable to text in JTextField
        text = inField.getText();
        //Show new text in JTextArea.
        displayArea.append(text + "\n");
        JFrame f = new JFrame();
        int i = JOptionPane.showConfirmDialog(f, "Is " + text + " the correct " + target + "?", "Confirm Input", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(i == JOptionPane.YES_OPTION){
          setText(text);
          frame.dispose();
        } else if (i == JOptionPane.NO_OPTION){
          displayArea.append("Enter " + target + ": \n");
          startReplace += 9 + target.length();
        }
      }
    }
  
    public void createField(){
      //Create and initialize JTextField, # param is chars it will show.
      inField = new JTextField(20);
      
      //Add the actionlistener to the field so they can take inputs (this class is an ActionListener).
      inField.addActionListener(this);      
      
      //Initialize JTextArea and print initial text
      displayArea = new JTextArea("Enter " + target + ": \n", 20, 10);
      startReplace = 9 + target.length();
      
      GridBagConstraints c = new GridBagConstraints();
      c.weightx = 1.0;
      c.anchor = GridBagConstraints.NORTHWEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridheight = 2;
      add(inField, c);
      
      c.gridy = 2;
      add(displayArea, c);
    }
  }
}