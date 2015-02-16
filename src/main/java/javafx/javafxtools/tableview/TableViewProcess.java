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
package javafx.javafxtools.tableview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.javafxtools.tableview.annotations.FieldColumn;
import javafx.javafxtools.tableview.annotations.MethodColumn;
import javafx.javafxtools.tableview.annotations.NotIncludeField;
import javafx.javafxtools.tableview.annotations.TableViewBase;
import javafx.javafxtools.util.reflection.ReflectUtil;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * @author Thaylon Guede Santos
 * @email thaylon_guedes@hotmail.com
 */
public class TableViewProcess {

    private TableViewProcess() {
    }

    public static void process(Object obj) {
        List<Field> campos = ReflectUtil.getFieldContainsAnnotations(obj.getClass(), TableViewBase.class);
        campos.stream().forEach((Field tabela) -> {
            TableViewBase anotacao = tabela.getAnnotation(TableViewBase.class);
            TableView tabelaInstance = (TableView) ReflectUtil.getValue(obj, tabela);
            if (tabela.getType().equals(TableView.class)) {
                Map<Integer, TableColumn> colunasComIndex = new HashMap<>();
                List<Field> fieldInEntity = ReflectUtil.getFieldNotContainsAnnotations(anotacao.classe(), NotIncludeField.class);
                processField(tabelaInstance, fieldInEntity, colunasComIndex);
                List<Method> methodsInEntity = ReflectUtil.getMethodContainsAnnotations(anotacao.classe(), MethodColumn.class);
                processMethod(tabelaInstance, methodsInEntity, colunasComIndex);
                insertColumn(tabelaInstance, colunasComIndex);
            }
        });
    }

    private static void processField(TableView tabela, List<Field> fieldInEntity, Map<Integer, TableColumn> colunasComIndex) {
        if (tabela == null || fieldInEntity == null || fieldInEntity.isEmpty()) {
            return;
        }

        fieldInEntity.stream().forEach((Field atributoDaClasse) -> {
            TableColumn coluna = new TableColumn();

            if (atributoDaClasse.isAnnotationPresent(FieldColumn.class)) {
                FieldColumn colunaDetalhes = atributoDaClasse.getAnnotation(FieldColumn.class);
                coluna.setPrefWidth(colunaDetalhes.width());
                if (colunaDetalhes.labelValue() != null && !colunaDetalhes.labelValue().isEmpty()) {
                    coluna.setText(colunaDetalhes.labelValue());
                } else {
                    coluna.setText(atributoDaClasse.getName());
                }
                if (colunaDetalhes.index() != -1) {
                    if (!containsIndex(colunasComIndex, colunaDetalhes.index())) {
                        colunasComIndex.put(colunaDetalhes.index(), coluna);
                    } else {
                        System.err.println("Coluna com index duplicado, não será inserida a coluna " + colunaDetalhes.labelValue() + " na tabela");
                    }
                } else {
                    insertColumn(tabela, coluna);
                }
            } else {
                coluna.setText(atributoDaClasse.getName());
                coluna.setPrefWidth(150);
                insertColumn(tabela, coluna);
            }

            if (atributoDaClasse.getType().equals(Date.class)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                coluna.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Object, String> p) {
                        return new ReadOnlyObjectWrapper(sdf.format(ReflectUtil.getValue(p.getValue(), atributoDaClasse)));
                    }
                });
            } else if (atributoDaClasse.getType().equals(List.class)) {
                coluna.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Object, String> p) {
                        return new ReadOnlyObjectWrapper((((List) ReflectUtil.getValue(p.getValue(), atributoDaClasse))).size());
                    }
                });
            } else {                
                 coluna.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Object, String> p) {
                        return new ReadOnlyObjectWrapper(((ReflectUtil.getValue(p.getValue(), atributoDaClasse))).toString());
                    }
                });                
            }
        });
    }

    private static void processMethod(TableView tabela, List<Method> methodsInEntity, Map<Integer, TableColumn> colunasComIndex) {
        if (tabela == null || methodsInEntity == null || methodsInEntity.isEmpty()) {
            return;
        }
        methodsInEntity.stream().forEach((Method metodoDaClasse) -> {
            MethodColumn metodoData = metodoDaClasse.getAnnotation(MethodColumn.class);
            TableColumn coluna = new TableColumn();
            coluna.setPrefWidth(metodoData.width());
            coluna.setText(metodoData.labelValue());
            coluna.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Object, String> p) {
                    return new ReadOnlyObjectWrapper(ReflectUtil.getValueToString(p.getValue(), metodoDaClasse));
                }
            });
            if (metodoData.index() != -1) {
                if (!containsIndex(colunasComIndex, metodoData.index())) {
                    colunasComIndex.put(metodoData.index(), coluna);
                } else {
                    System.err.println("Coluna com index duplicado, não será inserida a coluna " + metodoData.labelValue() + " na tabela");
                }
            } else {
                insertColumn(tabela, coluna);
            }
        });
    }

    private static boolean containsIndex(Map<Integer, TableColumn> colunas, Integer key) {
        return colunas.containsKey(key);
    }

    public static void insertColumn(TableView tabela, TableColumn coluna) {
        if (tabela != null) {
            tabela.getColumns().add(coluna);
        }
    }

    public static void insertColumn(TableView tabela, Map<Integer, TableColumn> coluna) {
        if (tabela != null && coluna != null && coluna.size() > 0) {
            coluna.forEach((key, value) -> {
                if ((key - 1) < tabela.getColumns().size()) {
                    tabela.getColumns().add((key - 1), value);
                }
            });
        }
    }
}
