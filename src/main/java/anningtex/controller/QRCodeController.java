package anningtex.controller;

import anningtex.config.Constants;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * desc:解析多个二维码扫描
 */
public class QRCodeController implements Initializable {
    @FXML
    Button btnLocal;
    @FXML
    ImageView imageView;
    @FXML
    VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLocal.setBackground(new Background(new BackgroundFill(Color.PURPLE, null, null)));
        btnLocal.setTextFill(Color.WHITE);
    }

    @FXML
    public void btnOpenLocal(ActionEvent actionEvent) {
        JFrame jFrame = new JFrame("本地文件");
        jFrame.setSize(800, 800);
        String localPath = Constants.TEST_PIC_MORE;
//        String localPath = showFileOpenDialog(jFrame);
        System.out.println(localPath);
        File file = new File(localPath);
        imageView.setImage(new Image(file.toURI().toString()));
        //解析识别二维码
        List<String> decode = new ArrayList<>();
        try {
            decode.addAll(decode(new File(localPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image(new File(localPath).toURI().toString());
        imageView.setImage(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        decode.forEach(str -> {
            vBox.getChildren().add(new TextField(str));
        });
    }

    /**
     * 打开文件
     */
    public static String showFileOpenDialog(Component parent) {
        String path = "";
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));
        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(true);
        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
//         fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        // 设置默认使用的文件过滤器
        fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            // msgTextArea.append("打开文件: " + file.getAbsolutePath() + "\n\n");
            path = file.getAbsolutePath();
        }
        return path;
    }

    public static List<String> decode(File file) throws Exception {
        List<String> itemLists = new ArrayList<>();
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            new Alert(Alert.AlertType.ERROR, "图片不存在!").showAndWait();
            return itemLists;
        }
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
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
}