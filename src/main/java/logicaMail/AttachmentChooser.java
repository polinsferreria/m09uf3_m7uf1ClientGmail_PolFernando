/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicaMail;
import javax.swing.*;
import java.io.File;

public class AttachmentChooser {
  public static File[] chooseAttachments() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setMultiSelectionEnabled(true);
      int option = fileChooser.showOpenDialog(null);
      if (option == JFileChooser.APPROVE_OPTION) {
          return fileChooser.getSelectedFiles();
      }
      return new File[] {};
  }
}
