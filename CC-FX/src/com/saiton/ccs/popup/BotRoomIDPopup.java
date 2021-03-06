/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saiton.ccs.popup;

import com.saiton.ccs.base.CustomerRegistrationController;
import com.saiton.ccs.basedao.CustomerRegistrationDAO;
import com.saiton.ccs.uihandle.StagePassable;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Polypackaging-A1
 */
public class BotRoomIDPopup {

    public SimpleStringProperty colBotRoomID = new SimpleStringProperty("tcBotRoomID");
    public SimpleStringProperty colDate = new SimpleStringProperty("tcDate");
    public SimpleStringProperty colCustomerID = new SimpleStringProperty("tcCustomerID");
    public SimpleStringProperty colCustomerName = new SimpleStringProperty("tcCustomerName");

    public String getColBotRoomID() {
        return colBotRoomID.get();
    }
    
    public String getColDate() {
        return colDate.get();
    }
    
    public String getColCustomerID() {
        return colCustomerID.get();
    }
    
    public String getColCustomerName() {
        return colCustomerName.get();
    }

    public TableView tableViewLoader(ObservableList observableList) {
        TableView tableView = new TableView();

        TableColumn tcBotRoomID = new TableColumn("Bot Room ID");
        tcBotRoomID.setMinWidth(100);
        tcBotRoomID.setCellValueFactory(
                new PropertyValueFactory<>("colBotRoomID"));
        
        TableColumn tcDate = new TableColumn("Date");
        tcDate.setMinWidth(100);
        tcDate.setCellValueFactory(
                new PropertyValueFactory<>("colDate"));
        
        TableColumn tcCustomerID = new TableColumn("Customer ID");
        tcCustomerID.setMinWidth(100);
        tcCustomerID.setCellValueFactory(
                new PropertyValueFactory<>("colCustomerID"));
        
        TableColumn tcCustomerName = new TableColumn("Customer Name");
        tcCustomerName.setMinWidth(100);
        tcCustomerName.setCellValueFactory(
                new PropertyValueFactory<>("colCustomerName"));

        tableView.setItems(observableList);
        tableView.getColumns().addAll(tcBotRoomID, tcDate, tcCustomerID, tcCustomerName);

        return tableView;
    }

}
