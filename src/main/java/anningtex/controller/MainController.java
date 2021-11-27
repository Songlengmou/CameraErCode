package anningtex.controller;

import anningtex.config.Constants;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * desc:生成和解析单个二维码
 */
public class MainController {
    @FXML
    private ImageView ivPic;

    @FXML
    private Text tvResult;

    @FXML
    void generateOnClick(ActionEvent event) {
        int width = 300;
        int height = 300;
        String format = "png";
        String content = "SW8023";
        HashMap map = new HashMap();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 2);
        try {
            BitMatrix birMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, map);
            Path file = new File("D:/codePic.png").toPath();
            MatrixToImageWriter.writeToPath(birMatrix, format, file);
            System.out.println("创建二维码成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //显示到界面
        File file = new File("D:/codePic.png");
        ivPic.setImage(new Image(file.toURI().toString()));
    }

    @FXML
    public void analysisOnClick(ActionEvent actionEvent) {
        MultiFormatReader formatReader = new MultiFormatReader();
        File file = new File("D:/codePic.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        Result result;
        try {
            result = formatReader.decode(binaryBitmap, hints);
            System.err.println("解析结果：" + result.toString());
            System.out.println(result.getBarcodeFormat());
            System.out.println(result.getText());
            tvResult.setText("Result：" + result);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void analysisMoreOnClick(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(Constants.QR_CODE_VIEW_PATH));
            Scene scene = new Scene(root);
            stage.setTitle("解析多个二维码");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}