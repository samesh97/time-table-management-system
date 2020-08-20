package views.controllers;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import database.QueriesOfWorkingDays;
import enums.ProgramType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class WorkingDaysMainController implements Initializable
{
	
	@FXML
	private ComboBox<Object> combo_working_days_type,combo_working_days;

	
	@FXML
	private ComboBox<Integer> combo_number_of_working_days;
	@FXML
	private Button addTimeSlotButton;
	@FXML
	private Button numberOfWorkingDaysAddBtn,workingTimeDurationAddBtn;
	@FXML
	private TextField hoursTextFiled,minutesTextFiled;
	
	private int programType = ProgramType.WEEK_DAY;


	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		
		initializeWorkingDaysTypeCombo();
		initializeWorkingDaysCombo();
		setupNumberOfWorkingDaysRow();
		
		
		
		
		//listeners
		combo_working_days_type.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
	         
			if(newValue.equals("Weekend"))
			{
				programType = ProgramType.WEEK_END;
			}
			else if(newValue.equals("Weekday"))
			{
				programType = ProgramType.WEEK_DAY;
			}
			
			setupNumberOfWorkingDaysRow();
	    }
	    ); 
	
		
	}
	private void initializeWorkingDaysTypeCombo()
	{
		ObservableList<Object> data = FXCollections.observableArrayList();
		data.add("Weekday");
		data.add("Weekend");
		combo_working_days_type.setItems(null);
		combo_working_days_type.setItems(data);
		
		combo_working_days_type.getSelectionModel().selectFirst();
		
	
	}
	private void initializeNumberOfWorkingDaysCombo(int value)
	{
		ObservableList<Integer> data = FXCollections.observableArrayList();
		data.add(1);
		data.add(2);
		data.add(3);
		data.add(4);
		data.add(5);
		data.add(6);
		data.add(7);
		combo_number_of_working_days.setItems(null);
		combo_number_of_working_days.setItems(data);
		
		if(value != -99)
		{
			combo_number_of_working_days.getSelectionModel().clearAndSelect(value - 1);
		}
		
	}

	private void initializeWorkingDaysCombo()
	{
		ObservableList<Object> data = FXCollections.observableArrayList();
	
		
		data.add("Monday");
		data.add("Tuesday");
		data.add("Wednesday");
		data.add("Thursday");
		data.add("Friday");
		data.add("Saturday");
		data.add("Sunday");
		combo_working_days.setItems(null);
		combo_working_days.setItems(data);
		
	}
	public void onAddTimeSlotButtonClicked(ActionEvent event)
	{
		Scene scene = addTimeSlotButton.getScene();
		AnchorPane pane = (AnchorPane) scene.lookup("#controllerPane");
		changeCenterContent(pane,"../WorkingDaysTimeSlots.fxml");
	}
	public void changeCenterContent(AnchorPane controllerPane,String fxmlFileName)
	{
		
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
			Node _node = loader.load();
			AnchorPane.setTopAnchor(_node, 0.0);
			AnchorPane.setRightAnchor(_node, 0.0);
			AnchorPane.setLeftAnchor(_node, 0.0);
			AnchorPane.setBottomAnchor(_node, 0.0);
			// container child clear
			controllerPane.getChildren().clear();

			// new container add
			controllerPane.getChildren().add(_node);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void setupNumberOfWorkingDaysRow()
	{
		ResultSet set = QueriesOfWorkingDays.sync(programType);
		if(set != null)
		{
			try
			{
				set.next();
				int workingDays = set.getInt(2);
				initializeNumberOfWorkingDaysCombo(workingDays);
				numberOfWorkingDaysAddBtn.setText("Update");
			} 
			catch (SQLException e)
			{
				numberOfWorkingDaysAddBtn.setText("Add");
				initializeNumberOfWorkingDaysCombo(-99);
			}
			
			setupWorkingTimeDuration(set);
		}
	}
	public void setupWorkingTimeDuration(ResultSet set)
	{
		try
		{
			int hours = set.getInt(3);
			int minutes = set.getInt(4);
			
			if(hours != -99 && minutes != -99)
			{
				hoursTextFiled.setText("" + hours);
				minutesTextFiled.setText("" + minutes);
				workingTimeDurationAddBtn.setText("Update");
			}
			else
			{
				hoursTextFiled.setText("");
				minutesTextFiled.setText("");
			}
		} 
		catch (SQLException e)
		{
			workingTimeDurationAddBtn.setText("Add");
			hoursTextFiled.setText("");
			minutesTextFiled.setText("");
		}
	}
	public void addNumberOfWorkingDays(ActionEvent event)
	{
		
		try
		{
			int item = combo_number_of_working_days.getSelectionModel().getSelectedItem();
			boolean res = QueriesOfWorkingDays.addNumberOfWorkingDays(programType,item);
		
			if(res)
				showAlert("Successfull");
			else
				showAlert("Unsuccessfull");
			
			setupNumberOfWorkingDaysRow();
		}
		catch(Exception e)
		{
			showAlert("Please choose a number first");
			
		}
		
	}
	public void addWorkingTimeDuration(ActionEvent event)
	{
		try
		{
			String hours = hoursTextFiled.getText().toString();
			String minutes = minutesTextFiled.getText().toString();
			
			int hoursInt = Integer.parseInt(hours);
			int minutesInt = Integer.parseInt(minutes);
			
			boolean res = QueriesOfWorkingDays.addWorkingTimeDuration(programType,hoursInt, minutesInt);
			if(res)
			{
				showAlert("Successfull");
			}
			else
			{
				showAlert("Unsuccessfull");
			}
			
			setupNumberOfWorkingDaysRow();
		}
		catch(Exception e)
		{
			showAlert("Please enter only Integers");
		}
	
	}
	public void showAlert(String message)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.show();
	}
	
}
