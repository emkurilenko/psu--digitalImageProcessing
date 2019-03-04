package kurilenko.view;


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kurilenko.proccess.ImageToBinary;
import kurilenko.proccess.ImageToGray;
import kurilenko.proccess.Noise;
import kurilenko.proccess.Utills;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class Controller {

    private Stage stage;

    @FXML
    private ImageView oldImageView;
    @FXML
    private ImageView newImageView;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Slider brightnessSlider;

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
        brightnessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BufferedImage bufferedImage = Utills.changeBrightnessS(newBufferedImage, (Double) newValue);
            newImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        });
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
            brightnessSlider.setValue(0);
        }else{

        }
    }

    @FXML
    void operationRedo(ActionEvent event) {
        if(!redoImageStack.isEmpty()) {
            BufferedImage pop = redoImageStack.pop();
            undoImageStack.push(pop);
            newImageView.setImage(SwingFXUtils.toFXImage(undoImageStack.peek(), null));
            newBufferedImage = undoImageStack.peek();
        }
    }

    @FXML
    void operationUndo(ActionEvent event) {
        if(!undoImageStack.isEmpty()) {
            BufferedImage pop = undoImageStack.pop();
            redoImageStack.push(pop);
            newImageView.setImage(SwingFXUtils.toFXImage(redoImageStack.peek(), null));
            newBufferedImage = redoImageStack.peek();
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
            newBufferedImage = convertToBinaryImage;
        }
    }

    @FXML
    void convertToGrayImage(ActionEvent event) {
        if(newBufferedImage != null) {
            BufferedImage convertToGrayImage = ImageToGray.convertToGrayImage(newBufferedImage);
            undoImageStack.push(SwingFXUtils.fromFXImage(newImageView.getImage(), null));
            Image image = SwingFXUtils.toFXImage(convertToGrayImage, null);
            newImageView.setImage(image);
            newBufferedImage = convertToGrayImage;
        }
    }

    @FXML
    void saltAndPepperNoise(ActionEvent event) {
        if(newBufferedImage != null) {
            BufferedImage pepperNoise = Noise.saltPepperNoise(newBufferedImage, getPercent()/100);
            undoImageStack.push(SwingFXUtils.fromFXImage(newImageView.getImage(), null));
            Image image = SwingFXUtils.toFXImage(pepperNoise, null);
            newImageView.setImage(image);
            newBufferedImage = pepperNoise;
        }
    }

    @FXML
    void gaussianNoise(ActionEvent event) {
        if(newBufferedImage != null) {
            BufferedImage pepperNoise = Noise.gaussianNoise(newBufferedImage, 0.5,0.4);
            undoImageStack.push(SwingFXUtils.fromFXImage(newImageView.getImage(), null));
            Image image = SwingFXUtils.toFXImage(pepperNoise, null);
            newImageView.setImage(image);
            newBufferedImage = pepperNoise;
        }
    }

    public double getPercent(){
        TextInputDialog dialog = new TextInputDialog("50");
        dialog.setTitle("Get percent");
        dialog.setContentText("Please enter percent noise(0-100):");
        Optional<String> result = dialog.showAndWait();
        Double aDouble = Double.valueOf(result.orElse("0"));
        return aDouble < 100 && aDouble > 0 ? aDouble : 0;
    }


    @FXML
    void showHistogram(ActionEvent event) {
        int[] imageHistogram = ImageToBinary.imageHistogram(ImageToGray.convertToGrayImage(newBufferedImage));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("");
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis,yAxis);

        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();

        for (int i = 0; i < imageHistogram.length; i++) {
            dataSeries1.getData().add(new XYChart.Data<>(String.valueOf(i), imageHistogram[i]));
        }

        barChart.getData().add(dataSeries1);

        VBox vbox = new VBox(barChart);

        Scene secondScene = new Scene(vbox, 400, 400);

        Stage newWindow = new Stage();
        newWindow.setTitle("Histogram");
        newWindow.setScene(secondScene);

        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);

        newWindow.show();
    }


    @FXML
    void improveContrast(ActionEvent event) {
        BufferedImage grayImage = ImageToGray.convertToGrayImage(newBufferedImage);
        BufferedImage improveImage = Utills.improveImage(newBufferedImage);

        undoImageStack.push(SwingFXUtils.fromFXImage(newImageView.getImage(), null));
        Image image = SwingFXUtils.toFXImage(improveImage, null);
        newImageView.setImage(image);
        newBufferedImage = improveImage;
    }
}
