package anningtex.view;

import anningtex.config.Constants;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * desc:已废弃
 */
public class TestMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        Scene scene = new Scene(vBox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        String fileName = Constants.TEST_PIC;
        List<String> decode = decode(new File(fileName));
        Image image = new Image(new File(fileName).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        vBox.getChildren().add(imageView);
        decode.forEach(str -> {
            vBox.getChildren().add(new TextField(str));
        });
    }

    public static List<String> decode(File file) throws Exception {
        List<String> itemLists = new ArrayList<>();
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            System.out.println("图片不存在!");
            return itemLists;
        }
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        try {
            Result result = new MultiFormatReader().decode(bitmap, hints);
            itemLists.add(result.getText());
        } catch (Exception e) {
            QRCodeMultiReader qrCodeMultiReader = new QRCodeMultiReader();
            Result[] results = qrCodeMultiReader.decodeMultiple(bitmap, hints);
            Arrays.stream(Optional.of(results).orElse(new Result[]{})).forEach(result -> {
                itemLists.add(result.getText());
            });
        }
        return itemLists;
    }

    public static void main(String[] args) {
        launch(args);
    }
}