package views.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DatabaseHandler_Students;
import database.DatabaseHandler_Tags;
import enums.Student;
import enums.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class TagsViewController implements Initializable {
	
ObservableList<Tag> subjectList = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Tag,String> column_tags;
	@FXML
	private TableColumn<Tag,String> column_name;
	@FXML
	private TableColumn<Tag,String> column_yearSem;
	@FXML
	private TableColumn<Tag,String> column_discription;
	@FXML
	private Button delete_button;
	
	
	@FXML
	private TableView<Tag> table_ViewStudent;
	
	private String tag;
	
	public void mapFields()
	{
		column_tags.setCellValueFactory(new PropertyValueFactory<Tag,String>("tag"));
		column_name.setCellValueFactory(new PropertyValueFactory<Tag,String>("name"));
		column_yearSem.setCellValueFactory(new PropertyValueFactory<Tag,String>("yearSem"));
		column_discription.setCellValueFactory(new PropertyValueFactory<Tag,String>("discription"));
	

		
		
		
		
	}
	private void setTableView() 
	{
		
		ObservableList<Tag> list = FXCollections.observableArrayList();
		ResultSet set = DatabaseHandler_Tags.getAllTags();
		if(set != null)
		{
			try 
			{
				while(set.next())
				{
					Tag std = new Tag();
					std.setTag(set.getString(2));
					std.setName(set.getString(3));
					std.setYearSem(set.getString(4));
					std.setDiscription(set.getString(5));
				
	
	
					list.add(std);
				}
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		table_ViewStudent.setItems(null);
		table_ViewStudent.setItems(list);
		
	}
	public void onViewAllEnteredSubjects(ActionEvent event) {
		setTableView();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		// TODO Auto-generated method stub
		mapFields();
		setTableView();
		
	}
	
	//mouse click
	
	public void tableMouseClicked() {
		Tag tag1 = table_ViewStudent.getSelectionModel().getSelectedItem();
		
		this.tag = tag1.getTag();
		System.out.println(tag);
		System.out.println("asdf");
		
		
	}
	public void deleteRecord() {
		
		try {
		    boolean result= DatabaseHandler_Tags.deleteTags(tag);
			if(result== true) {
				showAlert("Successfully Deleted");
			}
			else {
				showAlert("Unsuccessful");
			}
			mapFields();
			setTableView();
	
	}catch(Exception e) {
    	showAlert("Error!");
    }
}
public void showAlert(String message)
{
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setContentText(message);
	alert.show();
}
}