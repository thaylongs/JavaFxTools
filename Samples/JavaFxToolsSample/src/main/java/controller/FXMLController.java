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
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.javafxtools.FxmlLoader;
import javafx.javafxtools.controller.GenericController;
import javafx.javafxtools.controller.annotations.FxmlController;
import javafx.javafxtools.tableview.annotations.TableViewBase;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import model.Person;


/**
 *
 * @author Thaylon Guede Santos
 * @email thaylon_guedes@hotmail.com
 */
@FxmlController(fxmlPath = "/fxml/Scene.fxml", title = "Main", iconPath = "/img/java.png")
public class FXMLController extends GenericController {

    Integer windowCount = 0;
    @FXML
    @TableViewBase(classe = Person.class)
    TableView<Person> personTable;

    @FXML
    ListView<String> argsList;

    @FXML
    public void openNewWindow() throws IOException {
        FxmlLoader.load(FXMLController.class, getStage(), windowCount);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Person person = new Person(10, "Person 1", "9999999999", new Date());
        personTable.getItems().add(person);
    }

    @Override
    public void setData(Object... args) {
        if (args != null && args.length > 0) {
            if (args[0] instanceof Integer) {
                windowCount = (Integer) args[0];
                windowCount++;
                argsList.getItems().add(windowCount + "");
            } else {
                for (Object s : args) {
                    argsList.getItems().add((String) s);
                }
            }
        }
    }

}
