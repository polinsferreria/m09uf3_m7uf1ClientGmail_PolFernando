/*
 * Esta clase proporciona un método para seleccionar archivos adjuntos utilizando un JFileChooser.
 */
package logicaMail;

import javax.swing.*;
import java.io.File;

public class AttachmentChooser {
    // Método estático para elegir archivos adjuntos
    public static File[] chooseAttachments() {
        // Crear un JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        // Permitir la selección de múltiples archivos
        fileChooser.setMultiSelectionEnabled(true);
        // Mostrar el cuadro de diálogo para abrir archivos
        int option = fileChooser.showOpenDialog(null);
        // Verificar si se seleccionaron archivos
        if (option == JFileChooser.APPROVE_OPTION) {
            // Obtener los archivos seleccionados
            return fileChooser.getSelectedFiles();
        }
        // Devolver un arreglo vacío si no se seleccionaron archivos
        return new File[] {};
    }
}
