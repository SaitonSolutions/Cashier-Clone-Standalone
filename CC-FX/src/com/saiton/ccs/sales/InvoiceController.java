/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saiton.ccs.sales;

import com.saiton.ccs.base.UserPermission;
import com.saiton.ccs.base.UserSession;
import com.saiton.ccs.basedao.CustomerRegistrationDAO;
import com.saiton.ccs.msgbox.MessageBox;
import com.saiton.ccs.msgbox.SimpleMessageBoxFactory;
import com.saiton.ccs.popup.CustomerPopup;
import com.saiton.ccs.popup.InvoiceDetailsPopup;
import com.saiton.ccs.popup.ItemInvoicePopup;
import com.saiton.ccs.salesdao.InvoiceDAO;
import com.saiton.ccs.uihandle.ComponentControl;
import com.saiton.ccs.uihandle.ReportGenerator;
import com.saiton.ccs.uihandle.StagePassable;
import com.saiton.ccs.uihandle.UiMode;
import com.saiton.ccs.util.EnglishNumberToWords;
import com.saiton.ccs.validations.CustomDatePickerValidationImpl;
import com.saiton.ccs.validations.CustomTableViewValidationImpl;
import com.saiton.ccs.validations.CustomTextFieldValidationImpl;
import com.saiton.ccs.validations.ErrorMessages;
import com.saiton.ccs.validations.FormatAndValidate;
import com.saiton.ccs.validations.Validatable;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

/**
 *
 * @author nadeesha
 */
public class InvoiceController implements Initializable, Validatable,
        StagePassable {

    private String customerId = null;
    private String invoiceNo = null;

    private final ValidationSupport validationSupport = new ValidationSupport();
    private final ValidationSupport validationSupportDiscount
            = new ValidationSupport();
    private final ValidationSupport validationSupportTableData
            = new ValidationSupport();
    private final ValidationSupport validationSupportDate
            = new ValidationSupport();
    private final FormatAndValidate fav = new FormatAndValidate();
    private final ValidationSupport validationSupportTable
            = new ValidationSupport();

    private final ValidationSupport validationSupportItem
            = new ValidationSupport();

    ComponentControl component = new ComponentControl();
    private String userId = null;
    private String userName;
    private String userCategory;
    private boolean insert = false;
    private boolean update = false;
    private boolean delete = false;
    private boolean view = false;

    private boolean reprintAccess = false;

    private MessageBox mb;
    private Stage stage;
    boolean print = false;
    private double discountValue = 0.00;

    DecimalFormat decimal = new DecimalFormat("#.##");

    //Customer Popup -------------------------------
    private PopOver customerPop;
    private TableView customerTable = new TableView();

    private ObservableList<CustomerPopup> data = FXCollections.
            observableArrayList();
    private final CustomerRegistrationDAO customerRegistrationDAO
            = new CustomerRegistrationDAO();
    private CustomerPopup customerPopup = new CustomerPopup();

    //Item Info Pop up
    private PopOver itemPop;
    private TableView itemTable = new TableView();
    private ObservableList<ItemInvoicePopup> itemPopData = FXCollections.
            observableArrayList();
    private ItemInvoicePopup itemPopup = new ItemInvoicePopup();

    //Invoice Popup
    private PopOver invoicePop;
    private TableView invoiceTable = new TableView();
    private ObservableList<InvoiceDetailsPopup> invoiceData = FXCollections.
            observableArrayList();
    public InvoiceDAO invoiceDAO = new InvoiceDAO();
    private InvoiceDetailsPopup invoicePopup = new InvoiceDetailsPopup();
    ObservableList<String> paymentTypes = FXCollections.observableArrayList(
            "Cash", "Credit", "Visa", "Master Card", "American Express");

    ObservableList<String> WarrentyPeriods = FXCollections.observableArrayList(
            "Month(s)", "Year(s)");

    final ObservableList<Item> itemData = FXCollections.observableArrayList();
    private Item item = new Item();

    boolean isTableContentSaved = false;

    LocalDate date = null;

    private EnglishNumberToWords englishNumberToWords
            = new EnglishNumberToWords();
//    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    double discountAmount = 0.0;
    //<editor-fold defaultstate="collapsed" desc="InitComponents">
    @FXML
    private TextField txtInvoiceNo;
    @FXML
    private Button btnInvoiceNoSearch;
    @FXML
    private TextField txtDate;
    private TextField txtPONumber;
    @FXML
    private TextField txtCustomerName;
    private TextField txtAddress;
//    private ComboBox<String> cmbPaymentType;
    @FXML
    private TextField txtItemCode;
    private TextField txtDescription;
    @FXML
    private TextField txtUnitPrice;
    @FXML
    private TableView<Item> tableInvoice;
    @FXML
    private TableColumn<Item, String> tcItemCode;
    @FXML
    private TableColumn<Item, String> tcDescription;
    @FXML
    private TableColumn<Item, String> tcBatchNo;
    @FXML
    private TableColumn<Item, String> tcQuantity;
    @FXML
    private TableColumn<Item, String> tcUnitPrice;
    @FXML
    private TableColumn<Item, String> tcValue;
    @FXML
    private TableColumn<Item, String> tcDiscountPercentage;
    @FXML
    private TableColumn<Item, String> tcDiscountAmount;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtTotalAmount;
    @FXML
    private TextArea txtAreaAmountInWrds;
    @FXML
    private Button btnNameSearch;
    private DatePicker dtpPODate;
    @FXML
    private TextField txtSalesExecutive;
    private TextField txtNBT;
    private TextField txtVAT;
    private TextField txtValueNBT;
    private TextField txtValueVAT;
    @FXML
    private TextField txtNetTotal;
    @FXML
    private Button btnItemCodeSearch;
    @FXML
    private TextField txtDiscount;
//    private ComboBox<String> cmbWarrentyPeriods;
//    private TextField txtWarrentyPeriod;
    @FXML
    private TextField txtItemDiscount;
    @FXML
    private Label lblBachNo;
    private TextField txtBatchNo;
    @FXML
    private Label lblInvoiceNo;
    private CheckBox chkPODetails;
    @FXML
    private CheckBox chkUnitPrice;
    private RadioButton radioTax;
    private RadioButton radioNonTax;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnClose;
    @FXML
    private ComboBox<String> cmbCustomerType;
    @FXML
    private TextField txtVehicleNo;
    @FXML
    private TextField txtItemSearch;
    @FXML
    private Button btnRefresh1;
    @FXML
    private ComboBox<?> cmbBatchNo;
    @FXML
    private ComboBox<?> cmbUnit;
    @FXML
    private ComboBox<?> cmbUnitQty;
    @FXML
    private ComboBox<String> cmbPaymentType;
    @FXML
    private TextField txtPartNo;

//</editor-fold>
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnCancel.setVisible(false);
        txtDiscount.setText("0.00");

        mb = SimpleMessageBoxFactory.createMessageBox();
        //<editor-fold defaultstate="collapsed" desc="Table Columns">
        tcItemCode.setCellValueFactory(new PropertyValueFactory<Item, String>(
                "colItemCode"));
        tcDescription.setCellValueFactory(
                new PropertyValueFactory<Item, String>(
                        "colDescription"));
        tcBatchNo.setCellValueFactory(new PropertyValueFactory<Item, String>(
                "colBatchNo"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory<Item, String>(
                "colQuantity"));
        tcUnitPrice.setCellValueFactory(new PropertyValueFactory<Item, String>(
                "colUnitPrice"));
        tcValue.setCellValueFactory(new PropertyValueFactory<Item, String>(
                "colValue"));
        tcDiscountPercentage.setCellValueFactory(
                new PropertyValueFactory<Item, String>(
                        "colDiscountPercentage"));
        tcDiscountAmount.setCellValueFactory(
                new PropertyValueFactory<Item, String>(
                        "colDiscountAmount"));

        tableInvoice.setItems(itemData);

//</editor-fold>
        
        //Combo Box Items
        cmbPaymentType.setItems(paymentTypes);
        cmbPaymentType.getSelectionModel().selectFirst();
        
//        cmbWarrentyPeriods.setItems(WarrentyPeriods);
//        cmbWarrentyPeriods.getSelectionModel().selectFirst();

//        dateFormatter("yyyy-MM-dd", dtpPODate);
//        dtpPODate.setValue(LocalDate.now());
//        date = dtpPODate.getValue();
        
        txtDate.setText(LocalDate.now().toString());
        btnCancel.setVisible(false);
        generateId();
        loadMainCategoryToCombobox();
        disablePODetails();
        txtUnitPrice.setDisable(true);

    }

    @Override
    public boolean isValid() {
        return true;

    }

    public void disablePODetails() {
//        chkPODetails.setSelected(false);
//        txtPONumber.setDisable(true);
//        dtpPODate.setDisable(true);
//        txtPONumber.setText(null);
//        dtpPODate.setValue(LocalDate.now());
    }

    public void enablePODetails() {

//        chkPODetails.setSelected(true);
//        txtPONumber.setDisable(false);
//        dtpPODate.setDisable(false);

    }

    @Override
    public void clearInput() {

        txtInvoiceNo.setText(invoiceDAO.generateId());
        txtDiscount.clear();
        txtValueNBT.clear();
        txtItemDiscount.clear();
        txtQuantity.clear();
        txtTotalAmount.clear();
        txtAreaAmountInWrds.clear();
        txtValueVAT.clear();
        txtAddress.clear();
        txtItemDiscount.clear();
        txtPONumber.clear();
        txtBatchNo.clear();
        txtItemCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
//        txtWarrentyPeriod.clear();
        txtCustomerName.clear();
        txtNetTotal.clear();
        itemData.clear();
        generateId();
        resetExtraButtons();
        disablePODetails();
        btnSave.setVisible(true);
        btnCancel.setVisible(false);
        reprintAccess = false;
        btnSave.setText("Save");
    }

    @Override
    public void clearValidations() {

    }

    public void resetExtraButtons() {
        chkPODetails.setSelected(false);
        chkUnitPrice.setSelected(false);
//        txtUnitPrice.setDisable(true);
        radioTax.setSelected(true);
        chkPODetails.setDisable(false);
//        chkUnitPrice.setDisable(false);
    }

    @Override
    public void setStage(Stage stage, Object[] obj) {
        this.stage = stage;
        
        disablePODetails();
        setUserAccessLevel();
        validatorInitialization();
        chkUnitPrice.setDisable(!delete);

        //Customer popup------------------------
        customerTable = customerPopup.tableViewLoader(data);

        customerTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    CustomerPopup p = (CustomerPopup) customerTable.
                            getSelectionModel().getSelectedItem();

                    if (p.getColCustomerId() != null) {

                        txtCustomerName.setText(p.getColName());
                        customerId = p.getColCustomerId();

                        loadCustomerDetails(customerId);

                    }
                } catch (NullPointerException n) {

                }

                customerPop.hide();
                validatorInitialization();
            }

        });

        customerTable.setOnMousePressed(e -> {

            if (e.getButton() == MouseButton.SECONDARY) {

                customerPop.hide();
                validatorInitialization();

            }

        });

        //Item Popup
        itemTable = itemPopup.tableViewLoader(itemPopData);

        itemTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {

                    ItemInvoicePopup ISP = null;
                    ISP = (ItemInvoicePopup) itemTable.getSelectionModel().
                            getSelectedItem();
                    if (ISP.getColItemID() != null) {
                        txtItemCode.setText(ISP.getColItemID());
                        txtDescription.setText(ISP.getColItemName());
                        txtBatchNo.setText(ISP.getColBatchNo());
                        txtUnitPrice.setText(ISP.getColUnit());

                        loadItemDetails(ISP.getColItemID());
                    }
                } catch (NullPointerException ex) {

                }
                itemPop.hide();

                setZeroDiscount();
                txtQuantity.requestFocus();
                validatorInitialization();
            }
        });

        itemTable.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                itemPop.hide();
                validatorInitialization();
            }
        });
        //Invoice PopUp
        invoiceTable = invoicePopup.tableViewLoader(invoiceData);

        invoiceTable.setOnMouseClicked(e -> {

            if (e.getClickCount() == 2) {
                try {
                    btnCancel.setVisible(true);
                    btnSave.setVisible(false);
                    InvoiceDetailsPopup p = null;

                    p = (InvoiceDetailsPopup) invoiceTable.getSelectionModel().
                            getSelectedItem();

                    if (p.getColInvoiceNo() != null) {
                        txtInvoiceNo.setText(p.getColInvoiceNo());
                        chkPODetails.setDisable(true);
                        chkUnitPrice.setDisable(true);
                        txtDate.setText(p.getColDate());
                        if (p.getColPoNo() != null && p.getColPoDate() != null) {
                            enablePODetails();
                            txtPONumber.setText(p.getColPoNo());
                            dtpPODate.
                                    setValue(LocalDate.parse(p.getColPoDate()));
                        } else {
                            disablePODetails();
                        }

                        loadInvoiceDetails(p.getColInvoiceNo());
                        loadItemsToTable(txtInvoiceNo.getText());
                        loadInvoiceCustomerDetails(txtInvoiceNo.getText());
                        invoicePop.hide();

                        if (insert && update) {
                            btnSave.setVisible(true);
                            btnSave.setDisable(false);
                            btnSave.setText("print");

                            reprintAccess = true;
                        }
                        validatorInitialization();
                    }
                } catch (Exception e2) {

                }
            }

        });

        invoiceTable.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                invoicePop.hide();
                validatorInitialization();
            }
        });

        customerPop = new PopOver(customerTable);
        itemPop = new PopOver(itemTable);
        invoicePop = new PopOver(invoiceTable);
        stage.setOnCloseRequest(e -> {

            if (invoicePop.isShowing() | itemPop.isShowing() | customerPop.
                    isShowing()) {
                e.consume();
                invoicePop.hide();
                itemPop.hide();
                customerPop.hide();
            }

        });

        validatorInitialization();

    }

    //<editor-fold defaultstate="collapsed" desc="ActionEvent">
    @FXML
    private void btnRefreshOnAction(ActionEvent event) {
        clearInput();
    }

    private void radioTaxOnAction(ActionEvent event) {
        generateId();

    }

    @FXML
    private void chkUnitPriceOnAction(ActionEvent event) {
        if (delete) {
            txtUnitPrice.setDisable(!chkUnitPrice.isSelected());
        }

    }

    private void radioNonTaxOnAction(ActionEvent event) {

        generateId();
    }

    @FXML
    private void btnSaveOnActionEvent(javafx.event.ActionEvent event) {

        boolean isInvoiceInserted = false;
        validatorInitialization();
        boolean validationSupportResult = false;
        boolean validationSupportTableResult = false;
        boolean validationSupportDiscountResult = false;

        ValidationResult v = validationSupport.getValidationResult();
        ValidationResult v1 = validationSupportTable.getValidationResult();
        ValidationResult v2 = validationSupportDiscount.getValidationResult();

        if (v != null) {
            validationSupportResult = validationSupport.isInvalid();

            validationSupportDiscountResult = validationSupportDiscount.
                    isInvalid();

            if (validationSupportResult == true) {

                mb.ShowMessage(stage, ErrorMessages.MandatoryError, "Error",
                        MessageBox.MessageIcon.MSG_ICON_FAIL,
                        MessageBox.MessageType.MSG_OK);

            } else if (validationSupportResult == false) {

                if (reprintAccess) {

                    //=====================Print out===============================
                    HashMap param = new HashMap();
                    param.put("inv_no", txtInvoiceNo.getText().trim());

                    File fil = new File(".//Reports//TaxInvoice.jasper");
                    String img = fil.getAbsolutePath();
                    ReportGenerator r = new ReportGenerator(img, param);
                    r.setVisible(true);
                    //=============================================================
                    clearInput();

                } else {

                    int isTaxInvoice = 0;
                    if (radioTax.isSelected()) {
                        isTaxInvoice = 1;
                    } else {
                        isTaxInvoice = 0;

                    }

                    if (chkPODetails.isSelected()) {
                        isInvoiceInserted = invoiceDAO.insertInvoice(
                                txtInvoiceNo.getText(),
                                isTaxInvoice + "",
                                txtDate.getText(),
                                txtPONumber.getText(),
                                dtpPODate.getValue().toString(),
                                customerId,
                                txtSalesExecutive.getText(),
                                "Payment Type",
                                "Warrenty",
                                "Wattenty",
                                Double.parseDouble(txtTotalAmount.getText()),
                                Double.parseDouble(txtValueNBT.getText()),
                                Double.parseDouble(txtValueVAT.getText()),
                                Double.parseDouble(txtNetTotal.getText()),
                                txtAreaAmountInWrds.getText(),
                                userId,
                                Double.parseDouble(txtDiscount.getText()),
                                Double.parseDouble(txtNBT.getText()),
                                Double.parseDouble(txtVAT.getText())
                        );
                    } else {
                        isInvoiceInserted = invoiceDAO.insertInvoice(
                                txtInvoiceNo.getText(),
                                isTaxInvoice + "",
                                txtDate.getText(),
                                null,
                                null,
                                customerId,
                                txtSalesExecutive.getText(),
                                "Payment Type",
                                "warrenty",
                                "warrenty",
                                Double.parseDouble(txtTotalAmount.getText()),
                                Double.parseDouble(txtValueNBT.getText()),
                                Double.parseDouble(txtValueVAT.getText()),
                                Double.parseDouble(txtNetTotal.getText()),
                                txtAreaAmountInWrds.getText(),
                                userId,
                                Double.parseDouble(txtDiscount.getText()),
                                Double.parseDouble(txtNBT.getText()),
                                Double.parseDouble(txtVAT.getText()));
                    }
                    saveTableContent();
                    updateItemTable();

                    if (isInvoiceInserted == true && isTableContentSaved == true) {

                        mb.ShowMessage(stage, ErrorMessages.SuccesfullyCreated,
                                "Information",
                                MessageBox.MessageIcon.MSG_ICON_SUCCESS,
                                MessageBox.MessageType.MSG_OK);

                        //=====================Print out===============================
                        HashMap param = new HashMap();
                        param.put("inv_no", txtInvoiceNo.getText().trim());

                        File fil = new File(".//Reports//TaxInvoice.jasper");
                        String img = fil.getAbsolutePath();
                        ReportGenerator r = new ReportGenerator(img, param);
                        r.setVisible(true);
                        //=============================================================
                        clearInput();

                    } else {
                        mb.ShowMessage(stage,
                                ErrorMessages.NotSuccesfullyCreated,
                                "Error", MessageBox.MessageIcon.MSG_ICON_FAIL,
                                MessageBox.MessageType.MSG_OK);
                    }

                }

            }
        }

    }

    @FXML
    private void btnCloseOnAction(javafx.event.ActionEvent event) {

        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void chkPODetailsOnAction(ActionEvent event) {
        if (chkPODetails.isSelected() == false) {
            txtPONumber.setDisable(true);
            dtpPODate.setDisable(true);
        } else if (chkPODetails.isSelected() == true) {
            txtPONumber.setDisable(false);
            dtpPODate.setDisable(false);
        }
    }

    @FXML
    private void btnNameSearchOnAction(javafx.event.ActionEvent event) {

        customerTableDataLoader(txtCustomerName.getText().trim());
        customerTable.setItems(data);
        if (!data.isEmpty()) {
            customerPop.show(btnNameSearch);
        }
    }

    @FXML
    private void btnItemSearchOnAction(javafx.event.ActionEvent event) {
        itemTableDataLoader(txtDescription.getText().trim());
        itemTable.setItems(itemPopData);
        if (!itemPop.isShowing()) {
            itemPop.show(btnItemCodeSearch);
        }

    }

    @FXML
    private void btnInvoiceSearchOnAction(javafx.event.ActionEvent event) {

        invoiceTableDataLoader(txtInvoiceNo.getText().trim());
        invoiceTable.setItems(invoiceData);
        if (!invoicePop.isShowing()) {
            invoicePop.show(btnInvoiceNoSearch);
        }
    }

    private void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    private void btnCancelOnAction(ActionEvent event) {
        boolean isUpdated = false;

        validatorInitialization();
        boolean validationSupportResult = false;

        ValidationResult v = validationSupport.getValidationResult();

        if (v != null) {
            validationSupportResult = validationSupport.isInvalid();

            if (validationSupportResult == true) {
                mb.ShowMessage(stage, ErrorMessages.MandatoryError, "Error",
                        MessageBox.MessageIcon.MSG_ICON_FAIL,
                        MessageBox.MessageType.MSG_OK);
            } else if (validationSupportResult == false) {
                MessageBox.MessageOutput option = mb.ShowMessage(stage,
                        ErrorMessages.Cancel, "Information",
                        MessageBox.MessageIcon.MSG_ICON_NONE,
                        MessageBox.MessageType.MSG_YESNO);
                if (option.equals(MessageBox.MessageOutput.MSG_YES)) {
                    isUpdated = invoiceDAO.cancelInvoice(txtInvoiceNo.getText(),
                            1);
                    updateItem_InvoiceCanceled();

                    mb.ShowMessage(stage, ErrorMessages.SuccesfullyUpdated,
                            "Success", MessageBox.MessageIcon.MSG_ICON_SUCCESS,
                            MessageBox.MessageType.MSG_OK);

                    clearInput();
                }
            } else {
                mb.ShowMessage(stage, ErrorMessages.NotSuccesfullyUpdated,
                        "Error", MessageBox.MessageIcon.MSG_ICON_FAIL,
                        MessageBox.MessageType.MSG_OK);
            }
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="KeyEvents">
    @FXML
    private void txtDiscountOnKeyReleased(KeyEvent event) {
    }

    @FXML
    private void txtItemDiscountOnKeyReleased(KeyEvent event) {
        validatorInitialization();

        if (!txtDiscount.getText().isEmpty() && txtDiscount.getText().length()
                >= 1) {

            boolean validationSupportResult = false;
            boolean validationSupportTableResult = false;
            boolean validationSupportDiscountResult = false;

            ValidationResult v = validationSupport.getValidationResult();
            ValidationResult v1 = validationSupportTable.getValidationResult();
            ValidationResult v2 = validationSupportDiscount.
                    getValidationResult();

            if (v != null && v1 != null && v2 != null) {
                validationSupportResult = validationSupport.isInvalid();
                validationSupportTableResult = validationSupportTable.
                        isInvalid();
                validationSupportDiscountResult = validationSupportDiscount.
                        isInvalid();

                if (validationSupportResult == true
                        || validationSupportTableResult
                        == true || validationSupportDiscountResult
                        == true) {

                } else if (validationSupportResult == false
                        && validationSupportTableResult == false
                        && validationSupportDiscountResult == false) {

                    try {
                        calculateNetTotal();
                    } catch (Exception e) {

                        txtDiscount.setText("0.00");
                        calculateNetTotal();

                    }

                }
            }
        }
        if (txtDiscount.getText().isEmpty()) {
            txtDiscount.setText("0.00");
            calculateNetTotal();
        }

        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
            validatorInitialization();
        }
    }

    @FXML
    private void txtInvoiceNoOnKeyReleased(KeyEvent event) {
        if (txtInvoiceNo.getText().length() >= 3) {
            invoiceTableDataLoader(txtInvoiceNo.getText().trim());
            invoiceTable.setItems(invoiceData);
            if (!invoicePop.isShowing()) {
                invoicePop.show(btnInvoiceNoSearch);

            }
            validatorInitialization();
        }
    }

    private void txtPONumberOnKeyReleased(KeyEvent event
    ) {
        validatorInitialization();
    }

    @FXML
    private void txtCustomerNameOnKeyReleased(KeyEvent event) {

        if (txtCustomerName.getText().length() >= 3) {
            customerTableDataLoader(txtCustomerName.getText().trim());
            customerTable.setItems(data);
            if (!data.isEmpty()) {
                customerPop.show(btnNameSearch);
            }
            validatorInitialization();
        }

    }

    private void txtAddressOnKeyRelesed(KeyEvent event
    ) {
        validatorInitialization();
    }

    private void txtCreditAllowedOnKeyReleased(KeyEvent event) {
        validatorInitialization();
    }

    @FXML
    private void txtSalesExecutiveOnKeyReleased(KeyEvent event) {
        validatorInitialization();
    }

    @FXML
    private void txtWarrentyCertificateNoOnKeyReleased(KeyEvent event) {
        validatorInitialization();
    }

    @FXML
    private void txtItemCodeOnKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
            validatorInitialization();
        }
    }

    private void txtDescriptionOnKeyReleased(KeyEvent event) {
        validatorInitialization();
    }

    @FXML
    private void txtUnitPriceOnKeyReleased(KeyEvent event) {
        validatorInitialization();
    }

    private void txtBatchNoOnKeyReleased(KeyEvent event) {
        validatorInitialization();
    }

    @FXML
    private void tblItemsOnMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Item itemTblData = (Item) tableInvoice.getSelectionModel().
                    getSelectedItem();

            String itemCode = itemTblData.colItemCode.get();

            if (itemCode != null) {
                txtDescription.setText(itemTblData.colDescription.get());
                txtItemCode.setText(itemCode);
                txtBatchNo.setText(itemTblData.colBatchNo.get());
                txtUnitPrice.setText(itemTblData.colUnitPrice.get());
                txtItemDiscount.setText(itemTblData.colDiscountPercentage.get());
                txtQuantity.setText(itemTblData.colQuantity.get());
            }
            validatorInitialization();
            calculateNetTotal();
        }
    }

    @FXML
    private void txtQuantityOnKeyReleased(KeyEvent event) {

        boolean validationSupportResult = true;

        validatorInitialization();
        if (event.getCode() == KeyCode.ENTER) {

            ValidationResult v = validationSupportTableData.
                    getValidationResult();
            if (v != null) {

                validationSupportResult = validationSupportTableData.
                        isInvalid();
                if (validationSupportResult == true) {
                    mb.ShowMessage(stage, ErrorMessages.MandatoryError, "Error",
                            MessageBox.MessageIcon.MSG_ICON_FAIL,
                            MessageBox.MessageType.MSG_OK);

                } else if (validationSupportResult == false) {

                    if (tableInvoice.getItems().size() != 0) {
                        int n = tableInvoice.getItems().size();
                        for (int s = 0; s < n; s++) {
                            item
                                    = (Item) tableInvoice.
                                    getItems().get(s);
                            if (txtItemCode.getText().equals(item.
                                    getColItemCode())
                                    && tableInvoice.getItems().size() > 0) {
                                itemData.remove(s);
                                n--;
                            }
                        }
                    }

                    discountAmount = ((Double.
                            parseDouble(txtUnitPrice.getText()) * Double.
                            parseDouble(txtQuantity.getText()))
                            * Double.parseDouble(txtItemDiscount.getText()))
                            / 100;
                    double itemTotal = (Double.parseDouble(txtUnitPrice.
                            getText()) * Double.parseDouble(txtQuantity.
                                    getText())) - discountAmount;

                    item = new Item();
                    item.colItemCode.setValue(txtItemCode.
                            getText());
                    item.colUnitPrice.setValue(txtUnitPrice.
                            getText());
                    item.colBatchNo.setValue(txtBatchNo.
                            getText());
                    item.colQuantity.setValue(txtQuantity.
                            getText());
                    item.colDescription.setValue(txtDescription.
                            getText());

                    item.colDiscountPercentage.setValue(txtItemDiscount.
                            getText());
                    item.colDiscountAmount.setValue(discountAmount + "");
                    item.colValue.setValue(itemTotal + "");

                    itemData.add(item);

                    //Calculation part---------------------
                    calculateNetTotal();
                    clearItem();

                }
            }
        }

        validatorInitialization();

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Methods">
    
    private void loadMainCategoryToCombobox() {

        cmbCustomerType.setItems(null);
        ArrayList<String> customerTypeList = null;
        customerTypeList = invoiceDAO.loadCustomerType();
        if (customerTypeList != null) {
            try {
                ObservableList<String> List = FXCollections.observableArrayList(
                        customerTypeList);
                cmbCustomerType.setItems(List);
                cmbCustomerType.setValue(List.get(0));
            } catch (Exception e) {

            }

        }
             

    }
    //working
     private void loadVehicleToCombobox() {

        cmbCustomerType.setItems(null);
        ArrayList<String> customerTypeList = null;
        customerTypeList = invoiceDAO.loadCustomerType();
        if (customerTypeList != null) {
            try {
                ObservableList<String> List = FXCollections.observableArrayList(
                        customerTypeList);
                cmbCustomerType.setItems(List);
                cmbCustomerType.setValue(List.get(0));
            } catch (Exception e) {

            }

        }
             

    }
    
    private void calculateNetTotal() {

        String amount = calculateTableTotal() + "";

        String discountAmt = calculateTotalDiscountAmt() + "";

        double value = Double.parseDouble(amount);
        double nbtVal = Double.parseDouble(txtNBT.getText());
        double vatVal = Double.parseDouble(txtVAT.getText());

        txtDiscount.setText(discountAmt);//Set Total Discount

        double nbt = (value * nbtVal) / 100;

        double vat = ((value + nbt) * vatVal) / 100;
        double netTotal = value + (nbt + vat);

        txtValueVAT.setText(decimal.format(vat));
        txtValueNBT.setText(decimal.format(nbt));

        txtTotalAmount.setText(amount);

        txtNetTotal.setText(decimal.format(netTotal));

//        convertToWords(netTotal);
        convertToWords(decimal.format(netTotal));

    }

    private void setZeroDiscount() {
        if (!txtDiscount.getText().isEmpty() && txtDiscount.getText().length()
                >= 1) {

            txtItemDiscount.setText("0.00");

        } else if (txtItemDiscount.getText()
                .isEmpty() || txtItemDiscount.
                getText().length() == 0) {
            txtItemDiscount.setText("0.00");

        }

    }

    private void clearItem() {

        txtItemCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQuantity.clear();
        txtBatchNo.clear();
        txtItemDiscount.clear();
        generateId();

    }

    private void loadCustomerDetails(String customerId) {

        ArrayList<String> customerDataList = null;

        customerDataList = invoiceDAO.loadingCustomerInfo(customerId);

        if (customerDataList != null) {

            if (!customerDataList.isEmpty()) {

                txtAddress.setText(customerDataList.get(0).toString());

            }
        }
    }

    private void loadItemDetails(String ItemID) {

        ArrayList<String> itemDataList = null;
        itemDataList = invoiceDAO.loadingItemInfo(ItemID);

        if (itemDataList != null) {
            if (!itemDataList.isEmpty()) {
                txtDescription.setText(itemDataList.get(0).toString());
                txtUnitPrice.setText(itemDataList.get(1).toString());
                txtBatchNo.setText(itemDataList.get(2).toString());
            }
        }
    }

    private void loadInvoiceDetails(String InvoiceID) {
        ArrayList<String> invoiceDataList = null;

        invoiceDataList = invoiceDAO.loadInvoiceInfo(InvoiceID);

        if (invoiceDataList != null) {
            if (!invoiceDataList.isEmpty()) {

                txtDate.setText(invoiceDataList.get(1).toString());
//                txtPONumber.setText(invoiceDataList.get(2).toString());
//                dtpPODate.setValue(LocalDate.parse(invoiceDataList.get(3)));
                txtTotalAmount.setText(invoiceDataList.get(4));
                txtValueNBT.setText(invoiceDataList.get(5));
                txtValueVAT.setText(invoiceDataList.get(6));
                txtNetTotal.setText(invoiceDataList.get(7));
                txtDiscount.setText(invoiceDataList.get(8));
                txtAreaAmountInWrds.setText(invoiceDataList.get(9));

                int isTaxInvoiced = Integer.parseInt(invoiceDataList.get(0));
                if (isTaxInvoiced == 1) {
                    radioTax.setSelected(true);
                    radioNonTax.setSelected(false);
                } else {
                    radioNonTax.setSelected(true);
                    radioTax.setSelected(false);
                }

            }
        }
    }

    private void loadItemsToTable(String keyword) {
        itemTableCleaner();

        ArrayList<ArrayList<String>> itemDetails
                = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> list
                = invoiceDAO.searchInvoiceItems(keyword);

        if (list != null) {

            for (int i = 0; i < list.size(); i++) {
                itemDetails.add(list.get(i));

            }

        }
        if (itemDetails != null && itemDetails.size() > 0) {

            for (int i = 0; i < itemDetails.size(); i++) {
                Item item = new Item();
                item.colItemCode.setValue(itemDetails.get(i).get(0));
                item.colDescription.setValue(itemDetails.get(i).get(1));
                item.colBatchNo.setValue(itemDetails.get(i).get(2));
                item.colQuantity.setValue(itemDetails.get(i).get(3));
                item.colDiscountPercentage.setValue((itemDetails.get(i).get(4)));
                item.colDiscountAmount.setValue(itemDetails.get(i).get(5));
                item.colUnitPrice.setValue(itemDetails.get(i).get(6));
                item.colValue.setValue(itemDetails.get(i).get(7));

                itemData.add(item);
            }
        } else {
            mb.ShowMessage(stage, ErrorMessages.InvalidEvent,
                    "Error", MessageBox.MessageIcon.MSG_ICON_FAIL,
                    MessageBox.MessageType.MSG_OK);

        }

    }

    private void loadInvoiceCustomerDetails(String KeyWord) {
        ArrayList<String> cusDataList = null;

        cusDataList = invoiceDAO.loadInvoiceCusInfo(KeyWord);

        if (cusDataList != null) {
            if (!cusDataList.isEmpty()) {

                txtCustomerName.setText(cusDataList.get(0).toString());
                txtAddress.setText(cusDataList.get(1).toString());
                txtSalesExecutive.setText(cusDataList.get(2).toString());
//                txtWarrentyPeriod.setText(cusDataList.get(3).toString());
//                cmbWarrentyPeriods.setValue(cusDataList.get(4).toString());
//                cmbPaymentType.setValue(cusDataList.get(5).toString());

            }
        }
    }

    public void updateItemTable() {

        Item item;
        boolean value = false;
        ArrayList<ArrayList<String>> MainList
                = new ArrayList<ArrayList<String>>();

        if (tableInvoice.getItems().size() != 0) {

            for (int j = 0; j < tableInvoice.getItems().size(); j++) {
                ArrayList<String> list = new ArrayList<>();
                item = (Item) tableInvoice.getItems().get(j);

                list.add(item.getColItemCode());
                list.add(item.getColBatchNo());
                list.add(item.getColQuantity());
                MainList.add(list);

            }

        } else {
            mb.ShowMessage(stage,
                    ErrorMessages.Error,
                    "Error", MessageBox.MessageIcon.MSG_ICON_FAIL,
                    MessageBox.MessageType.MSG_OK);
        }

        for (int i = 0; i < MainList.size(); i++) {

            if (update = true) {
                invoiceDAO.updateItemTableQty(
                        MainList.get(i).get(0).toString(),
                        MainList.get(i).get(1).toString(),
                        Double.parseDouble(MainList.get(i).get(2)));

            }

        }

    }

    public void updateItem_InvoiceCanceled() {

        Item item;
        boolean value = false;
        ArrayList<ArrayList<String>> MainList
                = new ArrayList<ArrayList<String>>();

        if (tableInvoice.getItems().size() != 0) {

            for (int j = 0; j < tableInvoice.getItems().size(); j++) {
                ArrayList<String> list = new ArrayList<>();
                item = (Item) tableInvoice.getItems().get(j);

                list.add(item.getColItemCode());
                list.add(item.getColBatchNo());
                list.add(item.getColQuantity());
                MainList.add(list);

            }

        } else {
            mb.ShowMessage(stage,
                    ErrorMessages.Error,
                    "Error", MessageBox.MessageIcon.MSG_ICON_FAIL,
                    MessageBox.MessageType.MSG_OK);
        }

        for (int i = 0; i < MainList.size(); i++) {

            if (update = true) {
                invoiceDAO.updateItemTableQty(
                        MainList.get(i).get(0).toString(),
                        MainList.get(i).get(1).toString(),
                        Double.parseDouble(MainList.get(i).get(2)));

            }

        }

    }

    public void itemTableCleaner() {
        itemData.clear();
    }

    private double calculateTableTotal() {
        double total = 0.0;

        for (Item row : tableInvoice.getItems()) {

            total += Double.parseDouble(row.getColValue());

        }

        return total;
    }

    private double calculateTotalDiscountAmt() {
        double totDiscount = 0.0;

        for (Item col : tableInvoice.getItems()) {
            totDiscount += Double.parseDouble(col.getColDiscountAmount());
        }
        return totDiscount;
    }

    private void validatorInitialization() {

        System.gc();

        validationSupport.registerValidator(txtInvoiceNo,
                new CustomTextFieldValidationImpl(txtInvoiceNo,
                        !fav.validName(txtInvoiceNo.getText()),
                        ErrorMessages.InvalidId));

        validationSupport.registerValidator(txtCustomerName,
                new CustomTextFieldValidationImpl(txtCustomerName,
                        !fav.validName(txtCustomerName.getText()),
                        ErrorMessages.InvalidCustomerName));

//        validationSupport.registerValidator(txtWarrentyPeriod,
//                new CustomTextFieldValidationImpl(txtWarrentyPeriod,
//                        !fav.
//                        validNumberWithoutSpace(txtWarrentyPeriod.getText()),
//                        ErrorMessages.InvalidWarrentyPeriod));

        validationSupportDiscount.registerValidator(txtItemDiscount,
                new CustomTextFieldValidationImpl(txtItemDiscount,
                        !fav.validDiscountPercentage(txtItemDiscount.getText()),
                        ErrorMessages.InvalidDiscount));

        validationSupportTableData.registerValidator(txtItemCode,
                new CustomTextFieldValidationImpl(txtItemCode,
                        !fav.validName(txtItemCode.getText()),
                        ErrorMessages.InvalidId));

        validationSupportTableData.registerValidator(txtItemDiscount,
                new CustomTextFieldValidationImpl(txtItemDiscount,
                        !fav.validDiscountPercentage(txtItemDiscount.getText()),
                        ErrorMessages.InvalidDiscount));

        validationSupportTableData.registerValidator(txtQuantity,
                new CustomTextFieldValidationImpl(txtQuantity,
                        !fav.validPositiveDoubleAmount(txtQuantity.getText()),
                        ErrorMessages.InvalidQty));

//        validationSupportTableData.registerValidator(txtBatchNo,
//                new CustomTextFieldValidationImpl(txtBatchNo,
//                        !fav.validName(txtBatchNo.getText()),
//                        ErrorMessages.InvalidBatchNo));

        //Table
        validationSupportTable.registerValidator(tableInvoice,
                new CustomTableViewValidationImpl(tableInvoice,
                        !fav.validTableView(tableInvoice),
                        ErrorMessages.EmptyListView));

//        validationSupportDate.registerValidator(dtpPODate,
//                new CustomDatePickerValidationImpl(dtpPODate,
//                        dtpPODate.getValue().isAfter(LocalDate.now()),
//                        ErrorMessages.DateGraterThanToday));

    }

    private void saveTableContent() {

        Item itemTable;

//// Loading to db
////=============================================================================================================== 
        if (tableInvoice.getItems().size() != 0) {
            for (int i = 0; i < tableInvoice.getItems().size(); i++) {
                itemTable = (Item) tableInvoice.getItems().get(i);

                isTableContentSaved = invoiceDAO.insertInvoiceItems(
                        txtInvoiceNo.getText(),
                        itemTable.getColItemCode(),
                        itemTable.getColDescription(),
                        Double.parseDouble(itemTable.getColQuantity()),
                        Double.parseDouble(itemTable.getColUnitPrice()),
                        Double.parseDouble(itemTable.getColValue()),
                        Double.parseDouble(itemTable.getColDiscountAmount()),
                        Double.parseDouble(itemTable.getColDiscountPercentage()),
                        itemTable.getColBatchNo()
                );

            }
        }

    }

    private void generateId() {

      

            String id = invoiceDAO.generateId();

            if (id != null) {
                txtInvoiceNo.setText(id);
            }
        

    }

    private void dateFormatter(String pattern, DatePicker dtp) {

        dtp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
                    pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

    }

    private void customerTableDataLoader(String keyword) {

        data.clear();
        ArrayList<ArrayList<String>> custInfo
                = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> list = customerRegistrationDAO.
                searchItemDetails(keyword);

        if (list != null) {

            for (int i = 0; i < list.size(); i++) {

                custInfo.add(list.get(i));

            }

            if (custInfo != null && custInfo.size() > 0) {
                for (int i = 0; i < custInfo.size(); i++) {

                    customerPopup = new CustomerPopup();

                    customerPopup.colCustomerId.setValue(custInfo.get(i).get(0));
                    customerPopup.colTitle.setValue(custInfo.get(i).get(1));
                    customerPopup.colName.setValue(custInfo.get(i).get(2));
                    customerPopup.colAddress.setValue(custInfo.get(i).get(3));

                    data.add(customerPopup);
                }
            }

        }

    }

    private void itemTableDataLoader(String Keyword) {

        itemPopData.clear();
        ArrayList<ArrayList<String>> itemInfo
                = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> list = invoiceDAO.
                searchItemDetails(Keyword);

        if (list != null) {

            for (int i = 0; i < list.size(); i++) {
                itemInfo.add(list.get(i));

            }

            if (itemInfo != null && itemInfo.size() > 0) {
                for (int i = 0; i < itemInfo.size(); i++) {

                    itemPopup = new ItemInvoicePopup();

                    itemPopup.colItemID.setValue(itemInfo.get(i).get(0));
                    itemPopup.colBatchNo.setValue(itemInfo.get(i).get(1));
                    itemPopup.colItemName.setValue(itemInfo.get(i).get(2));
                    itemPopup.colUnit.setValue(itemInfo.get(i).get(3));

                    itemPopData.add(itemPopup);
                }
            }
        }
    }

    private void invoiceTableDataLoader(String Keyword) {
        invoiceData.clear();

        ArrayList<ArrayList<String>> invoiceInfo
                = new ArrayList<ArrayList<String>>();

        ArrayList<ArrayList<String>> list = invoiceDAO.
                searchInvoiceDetails(Keyword);

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                invoiceInfo.add(list.get(i));
            }

            if (invoiceInfo != null && invoiceInfo.size() > 0) {
                for (int i = 0; i < invoiceInfo.size(); i++) {
                    invoicePopup = new InvoiceDetailsPopup();

                    invoicePopup.colInvoiceNo.
                            setValue(invoiceInfo.get(i).get(0));
                    invoicePopup.colIsTaxInvoiced.setValue(invoiceInfo.get(i).
                            get(1));

                    invoicePopup.colDate.setValue(invoiceInfo.get(i).get(2));
                    invoicePopup.colPoNo.setValue(invoiceInfo.get(i).get(3));
                    invoicePopup.colPoDate.setValue(invoiceInfo.get(i).get(4));

                    invoiceData.add(invoicePopup);

                }
            }
        }
    }

    private void convertToWords(String value) {

        double amount = 0;
        double cents = 0;
        String[] valueTxt = null;
        String amountTxt = null;
        String inWords = null;
        String centsTxt = null;

        if (value.contains(".")) {

            valueTxt = value.split("\\.");

            centsTxt = valueTxt[1];
            amountTxt = valueTxt[0];
        }

        if (amountTxt != null) {
            amount = Double.parseDouble(amountTxt);
        }

        if (centsTxt != null) {
            cents = Double.parseDouble(centsTxt);

        }

        if (cents != 0) {

            inWords = EnglishNumberToWords.convert((long) amount) + " and "
                    + EnglishNumberToWords.convert((long) cents)
                    + " cents only.";
        } else {

            inWords = englishNumberToWords.convert(
                    (long) Double.parseDouble(value));

        }

        txtAreaAmountInWrds.setText(inWords);

    }

    private void setUiMode(UiMode uiMode) {
/*
        switch (uiMode) {

            case SAVE:
                disableUi(false);
                component.deactivateSearch(lblInvoiceNo, txtInvoiceNo,
                        btnInvoiceNoSearch, 140.0, USE_PREF_SIZE);
                txtUnitPrice.setDisable(true);
                chkUnitPrice.setDisable(true);
                break;

            case DELETE:
                disableUi(false);

                btnCancel.setVisible(false);

                txtPONumber.setDisable(true);
                dtpPODate.setDisable(true);
                txtUnitPrice.setDisable(true);

                break;

            case READ_ONLY:
                disableUi(false);

                btnSave.setDisable(true);
                btnSave.setVisible(false);

                btnCancel.setVisible(false);
                chkUnitPrice.setDisable(true);
                txtPONumber.setDisable(true);
                dtpPODate.setDisable(true);
                txtUnitPrice.setDisable(true);

                break;

            case ALL_BUT_DELETE:
                disableUi(false);

                btnCancel.setVisible(false);

                txtPONumber.setDisable(true);
                dtpPODate.setDisable(true);
                txtUnitPrice.setDisable(true);

                break;

            case FULL_ACCESS:
                disableUi(false);
                btnCancel.setVisible(false);

                txtPONumber.setDisable(true);
                dtpPODate.setDisable(true);
                txtUnitPrice.setDisable(true);
                break;

            case NO_ACCESS:
                disableUi(true);

                break;

        }
*/
    }

    private void setUserAccessLevel() {

        userId = UserSession.getInstance().getUserInfo().getEid();
        userName = UserSession.getInstance().getUserInfo().getName();
        userCategory = UserSession.getInstance().getUserInfo().getCategory();
        txtSalesExecutive.setText(userName);
        String title = stage.getTitle();
/*
        if (!title.isEmpty()) {

            UserPermission userPermission = UserSession.getInstance().
                    findPermission(title);

            if (userPermission.canInsert() == true) {
                insert = true;

            }

            if (userPermission.canDelete() == true) {
                delete = true;

            }

            if (userPermission.canUpdate() == true) {
                update = true;

            }

            if (userPermission.canView() == true) {
                view = true;

            }

            if (insert == true && delete == true && update == true && view
                    == true) {

                setUiMode(UiMode.FULL_ACCESS);

            } else if (insert == false && delete == true && update == true
                    && view
                    == true) {

                setUiMode(UiMode.FULL_ACCESS);

            } else if (insert == true && delete == false && update == true
                    && view
                    == true) {

                setUiMode(UiMode.ALL_BUT_DELETE);

            } else if (insert == true && delete == true && update == false
                    && view
                    == true) {

                setUiMode(UiMode.FULL_ACCESS);

            } else if (insert == true && delete == true && update == true
                    && view
                    == false) {

                setUiMode(UiMode.SAVE);

            } else if (insert == false && delete == false && update == true
                    && view
                    == true) {

                setUiMode(UiMode.FULL_ACCESS);

            } else if (insert == false && delete == true && update == false
                    && view
                    == true) {

                setUiMode(UiMode.DELETE);

            } else if (insert == false && delete == true && update == true
                    && view
                    == false) {

                setUiMode(UiMode.NO_ACCESS);

            } else if (insert == true && delete == false && update == false
                    && view
                    == true) {

                setUiMode(UiMode.ALL_BUT_DELETE);

            } else if (insert == true && delete == false && update == true
                    && view
                    == false) {

                setUiMode(UiMode.SAVE);

            } else if (insert == true && delete == true && update == false
                    && view
                    == false) {

                setUiMode(UiMode.SAVE);

            } else if (insert == false && delete == false && update == false
                    && view
                    == true) {

                setUiMode(UiMode.READ_ONLY);

            } else if (insert == false && delete == false && update == true
                    && view
                    == false) {

                setUiMode(UiMode.NO_ACCESS);

            } else if (insert == false && delete == true && update == false
                    && view
                    == false) {

                setUiMode(UiMode.NO_ACCESS);

            } else if (insert == true && delete == false && update == false
                    && view
                    == false) {

                setUiMode(UiMode.SAVE);

            } else if (insert == false && delete == false && update == false
                    && view
                    == false) {

                setUiMode(UiMode.NO_ACCESS);

            }
        } else {

            setUiMode(UiMode.NO_ACCESS);

        }
*/
    }

    private void disableUi(boolean state) {

//        chkPODetails.setDisable(state);
//        chkPODetails.setVisible(!state);
//
//        chkUnitPrice.setDisable(state);
//        chkUnitPrice.setVisible(!state);
//
//        btnCancel.setDisable(state);
//        btnCancel.setVisible(!state);
//        btnSave.setDisable(state);
//        btnSave.setVisible(!state);
//        btnCancel.setDisable(state);
//        btnCancel.setVisible(!state);
//        btnNameSearch.setDisable(state);
//        btnNameSearch.setVisible(!state);
//        btnInvoiceNoSearch.setDisable(state);
//        btnInvoiceNoSearch.setVisible(!state);
//        btnItemCodeSearch.setDisable(state);
//        btnItemCodeSearch.setVisible(!state);
//
//        txtInvoiceNo.setDisable(state);
//        txtInvoiceNo.setVisible(!state);
//        txtDate.setDisable(state);
//        txtDate.setVisible(!state);
//        txtPONumber.setVisible(!state);
//        txtPONumber.setDisable(state);
//        dtpPODate.setDisable(state);
//        dtpPODate.setVisible(!state);
//        txtDescription.setVisible(!state);
//        txtDescription.setDisable(state);
//        txtItemCode.setDisable(state);
//        txtItemCode.setVisible(!state);
//        txtBatchNo.setVisible(!state);
//        txtBatchNo.setDisable(state);
//        txtItemDiscount.setDisable(state);
//        txtItemDiscount.setVisible(!state);
//        txtUnitPrice.setVisible(!state);
//        txtUnitPrice.setDisable(state);
//        txtQuantity.setVisible(!state);
//        txtQuantity.setDisable(state);
//        tableInvoice.setVisible(!state);
//        tableInvoice.setDisable(state);
//        txtTotalAmount.setVisible(!state);
//        txtTotalAmount.setDisable(state);
//        txtValueNBT.setVisible(!state);
//        txtValueNBT.setDisable(state);
//        txtValueVAT.setVisible(!state);
//        txtValueVAT.setDisable(state);
//        txtDiscount.setVisible(!state);
//        txtDiscount.setDisable(state);
//        txtNetTotal.setVisible(!state);
//        txtNetTotal.setDisable(state);
//
//        txtAreaAmountInWrds.setVisible(!state);
//        txtAreaAmountInWrds.setDisable(state);

    }

    @FXML
    private void txtItemSearchOnKeyReleased(KeyEvent event) {
    }

    @FXML
    private void txtPartNoOnKeyReleased(KeyEvent event) {
    }

//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="Class">
    public class Item {

        public SimpleStringProperty colItemCode = new SimpleStringProperty(
                "tcItemCode");
        public SimpleStringProperty colDescription = new SimpleStringProperty(
                "tcDescription");
        public SimpleStringProperty colQuantity = new SimpleStringProperty(
                "tcQuantity");
        public SimpleStringProperty colUnitPrice = new SimpleStringProperty(
                "tcUnitPrice");
        public SimpleStringProperty colValue = new SimpleStringProperty(
                "tcValue");
        public SimpleStringProperty colDiscountPercentage
                = new SimpleStringProperty(
                        "tcDiscountPercentage");
        public SimpleStringProperty colDiscountAmount
                = new SimpleStringProperty(
                        "tcDiscountAmount");
        public SimpleStringProperty colBatchNo = new SimpleStringProperty(
                "tcBatchNo");

        public String getColItemCode() {
            return colItemCode.get();
        }

        public String getColDescription() {
            return colDescription.get();
        }

        public String getColDiscountPercentage() {
            return colDiscountPercentage.get();
        }

        public String getColBatchNo() {
            return colBatchNo.get();
        }

        public String getColDiscountAmount() {
            return colDiscountAmount.get();
        }

        public String getColQuantity() {
            return colQuantity.get();
        }

        public String getColUnitPrice() {
            return colUnitPrice.get();
        }

        public String getColValue() {
            return colValue.get();
        }

    }

//</editor-fold>
}
