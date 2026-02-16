/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mvcjava.sagt.javafx.controller;

import com.mvcjava.sagt.javafx.dao.model.Category;
import com.mvcjava.sagt.javafx.service.interfaces.ProductService;
import com.mvcjava.sagt.javafx.service.interfaces.SupplierService;
import com.mvcjava.sagt.javafx.dao.model.Supplier;
import com.mvcjava.sagt.javafx.exception.DataAccessException;
import com.mvcjava.sagt.javafx.service.impl.ProductServiceImpl;
import com.mvcjava.sagt.javafx.service.impl.SupplierServiceImpl;
import com.mvcjava.sagt.javafx.viewmodel.ProductViewModel;
import com.mvcjava.sagt.javafx.dto.ProductWithRelations;
import com.mvcjava.sagt.javafx.service.impl.CategoryServiceImpl;
import com.mvcjava.sagt.javafx.service.interfaces.CategoryService;
import com.mvcjava.sagt.javafx.util.BasicStringValidator;
import com.mvcjava.sagt.javafx.util.EditableCellFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 *
 * @author lucas
 */
public class ProductController {
    private ProductService productService;
    private SupplierService supplierService;
    private CategoryService categoryService;
    
    @FXML 
    private TableView<ProductViewModel> productsTable;
    
    private TableColumn<ProductViewModel, UUID> idColumn;
    private TableColumn<ProductViewModel, String> nameColumn;
    private TableColumn<ProductViewModel, String> brandColumn;
    private TableColumn<ProductViewModel, String> modelColumn;
    private TableColumn<ProductViewModel, Number> purchasePriceColumn;
    private TableColumn<ProductViewModel, Number> salePriceColumn;
    private TableColumn<ProductViewModel, Number> stockColumn;
    private TableColumn<ProductViewModel, Number> minStockColumn;
    private TableColumn<ProductViewModel, Supplier> supplierColumn;
    private TableColumn<ProductViewModel, String> loadedByNameColumn;
    private TableColumn<ProductViewModel, Timestamp> entryDateColumn;
    private TableColumn<ProductViewModel, Timestamp> updateDateColumn;
    private TableColumn<ProductViewModel, String> categoriesColumn;
    
    private ObservableList<ProductViewModel> productViewModels;
    private ObservableList<Supplier> avaibleSuppliers;
    private ObservableList<Category> avaibleCategories;
    
    private Map<UUID, Map<String, Object>> productsToUpdate;
    
    private BasicStringValidator stringValidator;
   
    public ProductController() {}
    
    @FXML
    public void initialize() {
        initializeDependencies();
        productViewModels = FXCollections.observableArrayList();
        avaibleSuppliers = FXCollections.observableArrayList();
        avaibleCategories = FXCollections.observableArrayList();
        
        setupTableColumns();
        productsTable.setItems(productViewModels);
        
        loadCategories();
        loadSuppliers();
        loadProducts();
        
        productsToUpdate = new HashMap<UUID, Map<String, Object>>();
    }
    
    private void initializeDependencies() {
        this.productService = new ProductServiceImpl();
        this.supplierService = new SupplierServiceImpl();
        this.categoryService = new CategoryServiceImpl();
        
        this.stringValidator = new BasicStringValidator();
    }
    
    private void setupTableColumns() {
        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        idColumn.setEditable(false);
        
        nameColumn = new TableColumn<>("Nombre");
        nameColumn.setUserData("nombre");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setCellFactory(EditableCellFactory.forString(
                3,
                100,
                (vm, value) -> vm.nameProperty().set(value))
        );
        nameColumn.setEditable(true);
        nameColumn.setOnEditCommit(col -> handleStringEdit(col));
        
        brandColumn = new TableColumn<>("Marca");
        brandColumn.setUserData("marca");
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        brandColumn.setCellFactory(EditableCellFactory.forString(
                1,
                100,
                (vm, value) -> vm.brandProperty().set(value))
        );
        brandColumn.setEditable(true);
        brandColumn.setOnEditCommit(col -> handleStringEdit(col));
        
        modelColumn = new TableColumn<>("Modelo");
        modelColumn.setUserData("modelo");
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        modelColumn.setCellFactory(EditableCellFactory.forString(
                1,
                100,
                (vm, value) -> vm.modelProperty().set(value))
        );
        modelColumn.setEditable(true);
        modelColumn.setOnEditCommit(col -> handleStringEdit(col));
        
        purchasePriceColumn = new TableColumn<>("Precio de compra");
        purchasePriceColumn.setUserData("precio_compra");
        purchasePriceColumn.setCellValueFactory(cellData -> cellData.getValue().purchasePriceProperty());
        purchasePriceColumn.setCellFactory(EditableCellFactory.forNumber(
                price -> (price >= 0), "El precio no puede ser negativo", 
                (vm, value) -> vm.purchasePriceProperty().set(value.floatValue()),
                true)
        );
        purchasePriceColumn.setEditable(true);
        
        salePriceColumn = new TableColumn<>("Precio de venta");
        salePriceColumn.setUserData("precio_venta");
        salePriceColumn.setCellValueFactory(cellData -> cellData.getValue().salePriceProperty());
        salePriceColumn.setCellFactory(EditableCellFactory.forNumber(
                price -> price >= 0,
                "El precio no puede ser negativo",
                (vm, value) -> vm.salePriceProperty().set(value.floatValue()),
                true)
        );
        salePriceColumn.setEditable(true);
        
        stockColumn = new TableColumn<>("Stock");
        stockColumn.setUserData("stock");
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        stockColumn.setCellFactory(EditableCellFactory.forNumber(
                stock -> stock >= 0,
                "El stock no puede ser negativo", 
                (vm, value) -> vm.stockProperty().set(value.intValue()),
                false)
        );
        stockColumn.setEditable(true);
        
        minStockColumn = new TableColumn<>("Stock minimo");
        minStockColumn.setUserData("stock_minimo");
        minStockColumn.setCellValueFactory(cellData -> cellData.getValue().minStockProperty());
        minStockColumn.setCellFactory(EditableCellFactory.forNumber(
                stock -> stock >= 0,
                "El stock minimo no puede ser negativo", 
                (vm, value) -> vm.minStockProperty().set(value.intValue()),
                false)
        );
        minStockColumn.setEditable(true);
        
        supplierColumn = new TableColumn<>("Proveedor");
        supplierColumn.setUserData("id_proveedor");
        supplierColumn.setCellValueFactory(cellData -> 
            cellData.getValue().supplierProperty()
        );
        
        //configurar comboBox
        supplierColumn.setCellFactory(col -> {
            ComboBoxTableCell<ProductViewModel, Supplier> cell = 
                    new ComboBoxTableCell<ProductViewModel, Supplier>() {
                    
                        @Override
                        public void updateItem(Supplier item, boolean empty) {
                            super.updateItem(item, empty);
                            
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getName());
                            }
                        }
                    };
            cell.getItems().setAll(avaibleSuppliers);
            
            return cell;
        });
        supplierColumn.setEditable(true);
        
        loadedByNameColumn = new TableColumn<>("Cargado por");
        loadedByNameColumn.setCellValueFactory(cellData -> 
                cellData.getValue().loadedByNameProperty()
        );
        loadedByNameColumn.setEditable(false);
        
        entryDateColumn = new TableColumn<>("Fecha de carga");
        entryDateColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getEntryDate())
        );
        entryDateColumn.setEditable(false);
        
        updateDateColumn = new TableColumn<>("Fecha de actualizacion");
        updateDateColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getUpdateDate())
        );
        updateDateColumn.setEditable(false);
        
        categoriesColumn = new TableColumn<>("Categorias");
        categoriesColumn.setCellValueFactory(cellData -> cellData.getValue().categoriesDisplayProperty());
        categoriesColumn.setCellFactory(col -> {
            return new TableCell<ProductViewModel, String>() {
                private final Label label = new Label();
                private final HBox container = new HBox(10, label);
                {
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setPadding(new Insets(2,5,2,5));
                    
                    container.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !isEmpty()) { // Doble click
                            ProductViewModel vm = getTableRow().getItem();
                            if (vm != null) {
                                openCategoriesDialog(vm);
                            }
                        }
                    });
                }
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        label.setText(item);
                        setGraphic(container);
                    }
                }
            };
        });
        categoriesColumn.setEditable(true);
        //categoriesColumn.setOnEditStart(e -> openCategoriesDialog(e.getRowValue()));
        
        productsTable.getColumns().addAll(
                idColumn,
                nameColumn,
                brandColumn,
                modelColumn,
                categoriesColumn,
                purchasePriceColumn,
                salePriceColumn,
                stockColumn,
                minStockColumn,
                supplierColumn,
                loadedByNameColumn,
                entryDateColumn,
                updateDateColumn
        );
        
        productsTable.setEditable(true);
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    @FXML
    protected void refresh(ActionEvent e) {
        productsTable.refresh();
    }
    
    private void loadCategories() {
        try {
            List<Category> categories = categoryService.getAll();
            avaibleCategories.setAll(categories);
        } catch (DataAccessException ex) {
            showError(ex.getMessage());
        }
    }
    
    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.getAll();
            avaibleSuppliers.setAll(suppliers);
        } catch (DataAccessException ex) {
            showError(ex.getMessage());
        }
    }
    
    private void loadProducts() {
       try {
           List<ProductWithRelations> products = productService.getAllWithRelations();
           
           List<ProductViewModel> viewModels = new ArrayList<>();
           for (ProductWithRelations dto : products) {
               UUID supplierId = dto.getProduct().getIdSupplier();
               Supplier supplier = findSupplierById(supplierId);
               
               ProductViewModel productVm = new ProductViewModel(dto, supplier);
               viewModels.add(productVm);
           }
           
           productViewModels.setAll(viewModels);
           
       } catch (DataAccessException ex) {
           showError("Error al cargar productos: " + ex.getMessage());
       }
    }
    
    private Supplier findSupplierById(UUID id) {
        if (id == null) {
            return null;
        }
        
        return avaibleSuppliers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    private void openCategoriesDialog(ProductViewModel viewModel) {
        Dialog<List<Category>> dialog = new Dialog<>();
        dialog.setTitle("Gestionar categorias");
        dialog.setHeaderText("Producto: " + viewModel.nameProperty().getValue());
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);
        
        Label instruction = new Label("Seleccione las categorias que desea agregar a este producto:");
        
        VBox checkContainer = new VBox(8);
        checkContainer.setPadding(new Insets(10));
        
        //Mapa para trackear los checkboxes
        Map<Category, CheckBox> checkBoxMap = new LinkedHashMap<>();
        
        //Obtener categorias actuales
        List<Category> currentCategories = new ArrayList<>(viewModel.getCategories());
        
        for (Category category : avaibleCategories) {
            CheckBox check = new CheckBox(category.getName());
            //marcar si ya tiene esta categoria
            boolean isSelected = currentCategories.stream()
                    .anyMatch(c -> c.getId().equals(category.getId()));
            check.setSelected(isSelected);
            
            checkBoxMap.put(category, check);
            checkContainer.getChildren().add(check);
        }
        
        //ScrollPane para muchas categorias
        ScrollPane scrollPane = new ScrollPane(checkContainer);
        scrollPane.setFitToWidth(true);
        
        //Agregar todo al contenedor
        content.getChildren().addAll(instruction, scrollPane);
        
        dialog.getDialogPane().setContent(content);
        
        ButtonType saveButton = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);
        
        //Convertir el resultado
        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                List<Category> selected = new ArrayList<>();
                for (Map.Entry<Category, CheckBox> entry : checkBoxMap.entrySet()) {
                    if (entry.getValue().isSelected()) {
                        selected.add(entry.getKey());
                    }
                }
                return selected;
            }
            return null;
        });
        
        //Mostrar y procesar resultado
        Optional<List<Category>> result = dialog.showAndWait();
        result.ifPresent(selectedCategories -> {
            //Manejar las categorias actualizadas
            viewModel.setCategories(selectedCategories);
        });
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private TextFieldTableCell<ProductViewModel, String> createTextFieldForString (TableColumn<ProductViewModel, String> col) {
        return new TextFieldTableCell<ProductViewModel, String>(new DefaultStringConverter()) {
            //Se valida antes de que se haga el commit para no setear el texto de la celda con texto que no corresponde
            @Override
            public void commitEdit(String t) {
                try {
                    stringValidator.validate(t, 3, 100, col.getText());
                    super.commitEdit(t);

                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                    cancelEdit();
                }
            }
        };
    }
    
    private TextFieldTableCell<ProductViewModel, Number> createTextFieldForFloat (TableColumn<ProductViewModel, Number> col, StringConverter<Number> converter) {
        return new TextFieldTableCell<ProductViewModel, Number>(converter) {
            //Validacion antes de actualizar la property
            @Override
            public void commitEdit(Number t) {
                try {
                    if (t == null) {
                        cancelEdit();
                    } else if (t.floatValue() < 0) {
                        throw new IllegalArgumentException("El campo '" + col.getText() + "' no puede ser negativo.");
                    }
                    //Posteriormente manejar validaciones de negocio
                    super.commitEdit(t);
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                    cancelEdit();
                }
            }
        };
    }
    
    private void handleStringEdit(TableColumn.CellEditEvent<ProductViewModel, String> e) {
        System.out.println("Entra en handleStringEdit");
        System.out.println("Viejo: " + e.getOldValue() + " Nuevo: " + e.getNewValue());
        if (!e.getOldValue().equalsIgnoreCase(e.getNewValue())) {
            ProductViewModel vm = e.getRowValue();
            UUID productId = vm.getId();
            String value = e.getNewValue();
            String fieldName = e.getTableColumn().getUserData().toString();
        
            //ComputeIfAbsent siemrpe devuelve ese mapa asociado a esa id, sea viejo o nuevo
            productsToUpdate.computeIfAbsent(productId, v -> new HashMap<>()).put(fieldName, value);
            productsToUpdate.forEach((key ,v) -> {
                System.out.println("Actualizaciones para el producto: " + key.toString());
                v.forEach((k, nv) -> System.out.println("Key: " + k + " Value: " + nv));
            });
            
            System.out.println("Valor de la property: " + vm.nameProperty());
        }
    }
    
    private StringConverter<Number> createFloatConverter() {
        return new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                if (number == null) {
                    return "";
                }
                
                return String.format("$%.2f", number.floatValue());
            }
            
            @Override
            public Number fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return 0;
                }
                string = string.replace("$", "").trim();
                try {
                    return Float.valueOf(string);
                } catch (NumberFormatException ex) {
                    showError("Formato de numero inválido.\n\n*Sólo use el punto (.) para decimales.*\n*No use el punto (.) o la coma (,) para separar miles.*"); 
                    return null;
                }
            }
        };
    }
    
    private StringConverter<Number> createIntegerConverter() {
        return new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                if (number == null) {
                    return "";
                }
                return String.valueOf(number.intValue());
            }

            @Override
            public Number fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return 0;
                }
                try {
                    return Integer.valueOf(string.trim());
                } catch (NumberFormatException ex) {
                    showError("Formato de numero inválido.\n\n*Sólo se admiten números enteros*");
                    return null;
                }
            }
        };
    }
}
