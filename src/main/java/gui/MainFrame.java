/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.HeadlessException;
import javax.mail.Transport;
import javax.swing.JFrame;
import logicaMail.Conexion;

/**
 *
 * @author usuario
 */
public class MainFrame extends JFrame{
    
    private Transport transport;

    public MainFrame(Conexion c){      
        setTitle("Mail");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.transport = transport;
        
    }
    
    
    
    
}
