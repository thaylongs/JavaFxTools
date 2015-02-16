/*
 * The MIT License
 *
 * Copyright 2015 Thaylon Guedes Santos.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package javafx.javafxtools;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.javafxtools.controller.GenericController;
import javafx.javafxtools.controller.annotations.FxmlController;
import javafx.javafxtools.interfaces.FxmlPathModel;
import javafx.javafxtools.interfaces.ImagePathModel;
import javafx.javafxtools.tableview.TableViewProcess;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Thaylon Guede Santos
 * @email thaylon_guedes@hotmail.com
 */
public class FxmlLoader {

    private FxmlLoader() {
    }
    /**
     * 
     * @param target A classe do controlador que deseja carregar 
     * @param root Stage da tela que tem que ficar atras da tela atual
     * @param args Os argumentos para o controlador da nova tela
     * @throws IOException 
     */
    public static void load(Class target, Stage root, Object... args) throws IOException {
        if (target.isAnnotationPresent(FxmlController.class)) {
            FxmlController fxmlControllerInfo = (FxmlController) target.getAnnotation(FxmlController.class);
            loadAndImagem(fxmlControllerInfo.fxmlPath(), fxmlControllerInfo.title(), fxmlControllerInfo.iconPath(), root, args);
        }
    }

    public static void load(FxmlPathModel path, Stage stageRoot, Object... args) throws IOException {
        loadAndImagem(path.getPath(), path.getTitle(), "", stageRoot, args);
    }

    public static void load(FxmlPathModel path, ImagePathModel imagePath, Stage stageRoot, Object... args) throws IOException {
        loadAndImagem(path.getPath(), path.getTitle(), imagePath.getImagePath(), stageRoot, args);
    }

    private static void loadAndImagem(String fxml_path, String fxml_title, String fxml_image_icon, Stage stageRoot, Object... args) throws IOException {

        FXMLLoader loader = new FXMLLoader(FxmlLoader.class.getResource(fxml_path));
        Stage newStage = new Stage();
        Parent root = loader.load();
        GenericController controlador = loader.getController();
        controlador.setStage(newStage);
        if (objIsNotEmpy(args)) {
            controlador.setData(args);
        }
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle(fxml_title);
        if (fxml_image_icon!=null && !fxml_image_icon.isEmpty()) {        
          newStage.getIcons().add(new Image(fxml_image_icon.getClass().getResourceAsStream(fxml_image_icon)));
        }
        controlador.setScene(scene);
        
        newStage.setOnCloseRequest((WindowEvent windowEventt) -> {
            controlador.Close();
        });
        
        if (stageRoot != null) {
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(stageRoot);
        }
        TableViewProcess.process(controlador);
        newStage.show();
    }

    private static boolean objIsNotEmpy(Object... args) {
        return args != null && args.length > 0;
    }

}
