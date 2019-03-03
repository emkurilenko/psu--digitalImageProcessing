package kurilenko.view;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kurilenko.transfer.ImageToBinary;
import kurilenko.transfer.ImageToGray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class Controller {

    private Stage stage;

    @FXML
    private ImageView oldImageView;
    @FXML
    private ImageView newImageView;
    @FXML
    private SplitPane splitPane;

    private BufferedImage oldBufferedImage;

    private BufferedImage newBufferedImage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    final FileChooser fileChooser = new FileChooser();

    private Stack<BufferedImage> undoImageStack = new Stack<>();

    private Stack<BufferedImage> redoImageStack = new Stack<>();

    @FXML
    public void initialize() {
        SplitPane.setResizableWithParent(splitPane, Boolean.TRUE);
    }

    @FXML
    void openPicture(ActionEvent event) throws IOException {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg")); // limit chooser options to image files
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String urlPath = file.toURI().toURL().toString();
            Image image = new Image(urlPath);
            oldImageView.setImage(image);
            newImageView.setImage(image);
            oldBufferedImage = ImageIO.read(file);
            newBufferedImage = ImageIO.read(file);
        }else{

        }
    }

    @FXML
    void operationRedo(ActionEvent event) {
        if(!redoImageStack.isEmpty()) {
            BufferedImage pop = redoImageStack.pop();
            undoImageStack.push(pop);
            newImageView.setImage(SwingFXUtils.toFXImage(undoImageStack.peek(), null));
        }
    }

    @FXML
    void operationUndo(ActionEvent event) {
        if(!undoImageStack.isEmpty()) {
            BufferedImage pop = undoImageStack.pop();
            redoImageStack.push(pop);
            newImageView.setImage(SwingFXUtils.toFXImage(redoImageStack.peek(), null));
        }
    }

    @FXML
    void savePicture(ActionEvent event) throws IOException{
        File f1 = fileChooser.showSaveDialog(stage);
        if (f1 != null) {
            String name = f1.getName();
            String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();

            ImageIO.write(newBufferedImage, extension, f1);
        }
    }

    @FXML
    void convertToBinaryImage(ActionEvent event) {
        if(newBufferedImage != null) {
            BufferedImage convertToBinaryImage = ImageToBinary.binarize(ImageToGray.convertToGrayImage(newBufferedImage));
            undoImageStack.push(SwingFXUtils.fromFXImage(newImageView.getImage(), null));
            Image image = SwingFXUtils.toFXImage(convertToBinaryImage, null);
            newImageView.setImage(image);
        }
    }

    @FXML
    void convertToGrayImage(ActionEvent event) {
        if(newBufferedImage != null) {
            BufferedImage convertToGrayImage = ImageToGray.convertToGrayImage(newBufferedImage);
            undoImageStack.push(SwingFXUtils.fromFXImage(newImageView.getImage(), null));
            Image image = SwingFXUtils.toFXImage(convertToGrayImage, null);
            newImageView.setImage(image);
        }
    }

}
