package application_btl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import test.questionview;

public class Controller6_3 implements Initializable{
	@FXML
    private TreeView<String> treeView;
	
	@FXML
    private Label labelcategory;
	
	@FXML 
	TableView<QuestionQuizView> table;
	
	@FXML
    private TableColumn<QuestionQuizView, Void> checkboxColumn;

    @FXML
    private TableColumn<QuestionQuizView, StringProperty> questionColumn;
    
    @FXML
    private TableColumn<QuestionQuizView, Void> imageColumn;
    
    @FXML
    private CheckBox selectAllCheckBox;

	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		IndentationTreeItem rootItem = new IndentationTreeItem("Course: IT", 0);
        treeView.setRoot(rootItem);
        
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/Categories_question.txt"))) {
            String line;
            // phần category của add new category
//            while ((line = reader.readLine()) != null) {
//    	    	category.getItems().add(line);
//    	    }
            //
            IndentationTreeItem currentItem = rootItem;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    int indentationLevel = getIndentationLevel(line);
                    String itemName = getItemName(line);

                    IndentationTreeItem newItem = new IndentationTreeItem(itemName, indentationLevel);

                    if (indentationLevel > currentItem.getIndentation()) {
                        currentItem.getChildren().add(newItem);
                    } else {
                        IndentationTreeItem parentItem = findParentItem(currentItem, indentationLevel);
                        parentItem.getChildren().add(newItem);
                    }

                    currentItem = newItem;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        treeView.setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("Course: IT")) {
                        setFont(Font.font("System", FontWeight.BOLD, 12));
                    } else {
                    	setFont(Font.font("System", FontWeight.NORMAL, 12));
                    }
                }
            }
        });
        
        expandAllItems(treeView.getRoot());
        
        addCountQuestion(rootItem.getChildren());
        
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    	    if (newValue != null && newValue.isLeaf()) {
    	        String selectedLeaf = newValue.getValue();
    	        labelcategory.setText("   "+ selectedLeaf);
    	        treeView.setVisible(false);
    	        String titlequestion = newValue.getValue();
    	        int startIndex = titlequestion.indexOf("(");
    			int endIndex = titlequestion.indexOf(")");
    			
    			if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
    			    // Chuỗi có phần số trong ngoặc, loại bỏ nó
    				titlequestion = titlequestion.substring(0, startIndex).trim();
    			}
    	        File file = new File("src/data/" + titlequestion + ".txt");
    			ObservableList<QuestionQuizView> dataList = FXCollections.observableArrayList();
    			try(BufferedReader reader = new BufferedReader(new FileReader(file))){
    				String line;
    				String question = "";
    				int i = 1;
    		    	while ((line = reader.readLine()) != null) {
    		    		line = line.trim(); // Loại bỏ khoảng trắng ở đầu và cuối dòng
    	            	if(i == 0 && line.isEmpty()) {
    	            		i+=1;
    	            		question = "";
    	            		continue;
    	            	}
    	            	if(i == 1) {
    	            		if (!line.isEmpty()) {  // Kiểm tra dòng rỗng không, kiem tra tieu de
    	            			// Đọc dữ liệu từ file và tạo đối tượng QuestionView
    	        	    	    //questionview questionView = new questionview(line);
    	        	    	    //dataList.add(questionView);
    	            			
    	            			question += line.replace("\n", "");
    	        	    	    i+=1;
    	            		}
    	            	} else if(line.matches("^[A-Z]\\.\\s.*")) {
    	            		question += line.replace("\n", "");
    	            		i+=1;  // Dòng chứa lựa chọn đáp án
    	            	} else if(line.startsWith("ANSWER:") ) {
    	            		question += line.replace("\n", "");
    	            		QuestionQuizView questionView = new QuestionQuizView(question);
	        	    	    dataList.add(questionView);
    	            		i = 0;
    	            		} 
    	            }
    			} catch (IOException e) {
    	            e.printStackTrace();
    	        }
    			
    			questionColumn.setCellValueFactory(new PropertyValueFactory<QuestionQuizView, StringProperty>("question"));
    			checkboxColumn.setCellFactory(param -> new TableCell<QuestionQuizView, Void>() {
    			    @Override
    			    protected void updateItem(Void item, boolean empty) {
    			        super.updateItem(item, empty);
    			        if (empty) {
    			            setGraphic(null);
    			        } else {
    			            setGraphic(getTableView().getItems().get(getIndex()).getCheckBox());
    			            setAlignment(Pos.CENTER);
    			        }
    			    }
    			});
    			imageColumn.setCellFactory(param -> new TableCell<QuestionQuizView, Void>() {
    			    @Override
    			    protected void updateItem(Void item, boolean empty) {
    			        super.updateItem(item, empty);
    			        if (empty) {
    			            setGraphic(null);
    			        } else {
    			            setGraphic(getTableView().getItems().get(getIndex()).getZoom());
    			            setAlignment(Pos.CENTER);
    			        }
    			    }
    			});
    			table.setItems(dataList);
    	        table.getSelectionModel().setCellSelectionEnabled(true);// cho phép chọn từng ô hehe
    	        
    	        selectAllCheckBox.setOnAction(e -> {
    	            if(selectAllCheckBox.isSelected()){
    	                for (QuestionQuizView obj : table.getItems()){
    	                    obj.getCheckBox().setSelected(true);
    	                }
    	            } else {
    	                for (QuestionQuizView obj : table.getItems()){
    	                    obj.getCheckBox().setSelected(false);
    	                }
    	            }
    	        });
    	        
    	        for (QuestionQuizView obj : table.getItems()){
    	            obj.getCheckBox().setOnAction(e -> {
    	                if(obj.getCheckBox().isSelected()){
    	                    boolean allSelected = true;
    	                    for (QuestionQuizView o : table.getItems()){
    	                        if(!o.getCheckBox().isSelected()){
    	                            allSelected = false;
    	                            break;
    	                        }
    	                    }
    	                    selectAllCheckBox.setSelected(allSelected);
    	                } else {
    	                    selectAllCheckBox.setSelected(false);
    	                }
    	            });
    	        }
    	        
    	    }
        });
        
        table.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && table.getSelectionModel().getSelectedCells().get(0).getColumn() == 2) {
            	QuestionQuizView selectedRow = table.getSelectionModel().getSelectedItem();
                String leftMostCellContent = selectedRow.getQuestion();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
            	alert.setTitle("Chi tiết câu hỏi");
            	alert.setHeaderText(null);
            	
                String[] part = labelcategory.getText().trim().split("\\(");
            	Path path = Paths.get("src/data/"+part[0].trim()+".txt");
            	try (Stream<String> lines = Files.lines(path)) {
                	String[] questions = lines.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
                	for (int i = 0; i < questions.length; i++) {
                        if (questions[i].replace("\n", "").trim().equals(leftMostCellContent)) {
                        	leftMostCellContent = questions[i];
                            break;
                        }
                    }
                	alert.setContentText(leftMostCellContent);
                }catch (IOException e1) {
            	    e1.printStackTrace();
            	}
            	
            	alert.showAndWait();
            }
        });
     
	}
	
	private boolean hasChildren(TreeItem<String> item) {
        return item.getChildren().size() > 0;
    }
	
	private int getIndentationLevel(String line) {
        int level = 0;
        int index = 0;
        while (line.charAt(index) == ' ') {
            level++;
            index++;
        }
        return level / 2;
    }

    private String getItemName(String line) {
        return line.trim();
    }
    
    private IndentationTreeItem findParentItem(IndentationTreeItem currentItem, int indentationLevel) {
        IndentationTreeItem parentItem = currentItem;
        while (parentItem.getIndentation() >= indentationLevel && parentItem.getParent() != null) {
            parentItem = (IndentationTreeItem) parentItem.getParent();
        }
        return parentItem;
    }
    
    private void expandAllItems(TreeItem<String> item) {
        if (item != null) {
            item.setExpanded(true);
            for (TreeItem<String> child : item.getChildren()) {
                expandAllItems(child);
            }
        }
    }
    
    private void addCountQuestion(List<TreeItem<String>> items) {
        for (TreeItem<String> item : items) {
            if (item.isLeaf() && !hasChildren(item)) {
            	String filePath = "src/data/" + item.getValue() + ".txt";
            	try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            		boolean isAiken = AikenFormatChecker.CheckAikenFormat(filePath);
            		int countQuestion = AikenFormatChecker.getCountQuestion();
            		if(countQuestion > 0) {
            			String countQuestionString = String.valueOf(countQuestion);
                		String oldValue = item.getValue();
                    	String newValue = oldValue + " (" + countQuestionString +")";
                    	item.setValue(newValue);
            		}
            	} catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            	addCountQuestion(item.getChildren());
            }
        }
    }
    
	
	public void treeView_popup(MouseEvent e){
		if(treeView.isVisible() == false)
			treeView.setVisible(true);
		else
			treeView.setVisible(false);
	}
	
	public void changeSceneBack(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample6_2.fxml"));
        Parent changeSceneBack = loader.load();
        Scene scene = new Scene(changeSceneBack);
        scene.getStylesheets().add(getClass().getResource("application6_2.css").toExternalForm());
        stage.setScene(scene);
    }
	
	public void changeSceneAddQuestionQuiz(MouseEvent e) throws IOException {
		String[] part = labelcategory.getText().trim().split("\\(");
    	Path path = Paths.get("src/data/"+part[0].trim()+".txt");
    	Path PrepareForQuiz = Paths.get("src/data/PrepareForQuiz.txt");
		if(selectAllCheckBox.isSelected()) {
        	try {
        		String content = new String(Files.readAllBytes(path));
        		if(Files.size(PrepareForQuiz) != 0) {
        			content = "\n" + content;
        		}
        		Files.write(PrepareForQuiz, content.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e1) {
        	    e1.printStackTrace();
        	}
		} else {
			for (QuestionQuizView row : table.getItems()) {
			    CheckBox checkbox = row.getCheckBox();
			    if (checkbox.isSelected()) {
			    	String rightMostCellContent = row.getQuestion();
			    	try (Stream<String> lines = Files.lines(path)) {
	                	String[] questions = lines.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
	                	for (int i = 0; i < questions.length; i++) {
	                        if (questions[i].replace("\n", "").trim().equals(rightMostCellContent)) {
	                        	questions[i] = questions[i] + "\n";
	                        	if(Files.size(PrepareForQuiz) != 0) {
	                        		questions[i] = "\n" + questions[i];
	                    		}
	                    		Files.write(PrepareForQuiz, questions[i].getBytes(), StandardOpenOption.APPEND);
	                            break;
	                        }
	                    }
	                }catch (IOException e1) {
	            	    e1.printStackTrace();
	            	}
			    }
			}
		}
		
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample6_2.fxml"));
        Parent changeSceneAddQuestionQuiz = loader.load();
        Scene scene = new Scene(changeSceneAddQuestionQuiz);
        scene.getStylesheets().add(getClass().getResource("application6_2.css").toExternalForm());
        stage.setScene(scene);
    }
}
