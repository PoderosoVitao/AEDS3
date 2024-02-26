package src.code;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FolderNavigator extends JFrame {

    private JLabel selectedFolderLabel;

    public FolderNavigator() {
        setTitle("Folder Navigator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton selectButton = new JButton("Select Folder");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int result = fileChooser.showOpenDialog(FolderNavigator.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    selectedFolderLabel.setText("Selected folder: " + selectedFolder.getAbsolutePath());
                    // List files in the selected folder
                    File[] files = selectedFolder.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            System.out.println("File: " + file.getName());
                        }
                    }
                }
            }
        });

        selectedFolderLabel = new JLabel("Selected folder: ");
        panel.add(selectButton);
        panel.add(selectedFolderLabel);

        add(panel);
    }
}