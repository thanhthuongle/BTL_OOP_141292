package application_btl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import test.questionview;

class IndentationTreeItem extends TreeItem<String> {
    private int indentation;

    public IndentationTreeItem(String value, int indentation) {
        super(value);
        this.indentation = indentation;
    }

    public int getIndentation() {
        return indentation;
    }
}

public class Controller2 implements Initializable {

	@FXML
    private TreeView<String> treeView;
	
	@FXML
	private Label labelcategory;
	
	@FXML 
	TableView<questionview> table;
	
	@FXML
	TableColumn<questionview, StringProperty> questionColumn;
	
	@FXML
	TableColumn<questionview, StringProperty> actionsColumn;
	
	@FXML
	private TextField addCategory;
	
	@FXML
	private TextField viewTooltip;
	
	@FXML
	private Tooltip noticeCategory;
	
	@FXML
	private Tooltip noticeCategory1;
	
	@FXML
	private TextField viewTooltip1;
	
	@FXML
	private Tooltip noticeImportFile;
	
	@FXML
	private Tooltip noticeImportFile1;
	
	@FXML
	private ComboBox<String> category;
	
	@FXML
	private ScrollPane SP;
	
	@FXML
    private Label namefile;
	
	@FXML
    private AnchorPane  importFile;
	
	private String linkfile;
	
	public static String categoryTo3_3, questionNameto3_3, questionTextto3_3, choiceto3_3="", answerto3_3, questionNeedChange;
	
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
            	category.getItems().add(line);
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
    			ObservableList<questionview> dataList = FXCollections.observableArrayList();
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
    	            		questionview questionView = new questionview(question);
	        	    	    dataList.add(questionView);
    	            		i = 0;
    	            		} 
    	            }
    			} catch (IOException e) {
    	            e.printStackTrace();
    	        }
    			
    			questionColumn.setCellValueFactory(new PropertyValueFactory<questionview, StringProperty>("question"));
    			actionsColumn.setCellValueFactory(new PropertyValueFactory<questionview, StringProperty>("actions"));
    			table.setItems(dataList);
    	        table.getSelectionModel().setCellSelectionEnabled(true);// cho phép chọn từng ô hehe
    	        
    	        table.setOnMouseClicked(event -> {
    	            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && table.getSelectionModel().getSelectedCells().get(0).getColumn() == 1) {
    	                try {
    	                	TablePosition<?, ?> pos = table.getSelectionModel().getSelectedCells().get(0);
    	                	TableColumn<?, ?> selectedColumn = pos.getTableColumn();
    	                	ObservableList<TableColumn<questionview, ?>> tableColumns = table.getColumns();

    	                	int columnIndex = tableColumns.indexOf(selectedColumn);
    	                	TableColumn<?, ?> leftColumn = tableColumns.get(columnIndex - 1);
    	                    Object leftItemObject = leftColumn.getCellData(pos.getRow());
    	                	
    	                	String itemString = leftItemObject.toString();
    	                	String[] parts = labelcategory.getText().trim().split(" \\(");
    	                	String firstPart = parts[0]; // "abcd"
    	                	questionNeedChange = AikenQuestion.getQuestionContent(itemString , firstPart.trim());
    	                	categoryTo3_3 = firstPart.trim();
    	                	String[] lines = questionNeedChange.split("\n");
    	                	int numberOfLines = lines.length;
    	                	int i = 1;
    	                    for (String line : lines) {
    	                    	// chuyển dữ liệu
    	                    	if(i == 1) {
    	                    		String[] part = line.split("\\ ", 2);
    	                    		questionNameto3_3 = part[0].trim();
    	                    		questionTextto3_3 = part[1].trim();
    	                    	} else if(i < numberOfLines)
    	                    		choiceto3_3 += line + "\n";
    	                    	else if(i == numberOfLines){
    	                    		answerto3_3 = line;
    	                    	}
    	                        i+= 1; 
    	                    }
    	                	//System.out.println(itemString);
//    	                	System.out.println(check);
    	                	
    	                	
    	                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	                    FXMLLoader loader = new FXMLLoader();
    	                    loader.setLocation(getClass().getResource("Sample3_3.fxml"));
    	                    Parent CreatQuestionParent = loader.load();
    	                    Scene scene = new Scene(CreatQuestionParent);
    	                    scene.getStylesheets().add(getClass().getResource("application3_2.css").toExternalForm());
    	                    stage.setScene(scene);
    	                } catch (IOException e) {
    	                    e.printStackTrace();
    	                }
    	            }
    	        });
    		}
    	});
        
        
        // phần kéo thả import file
        Node node = importFile;

        node.setOnDragOver(event -> {
            if (event.getGestureSource() != node &&
                event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
            	namefile.setText(db.getFiles().get(0).getName());
            	linkfile = db.getFiles().get(0).getAbsolutePath();
                // Xử lý file ở đây. Ví dụ: db.getFiles().get(0) sẽ trả về file đầu tiên.
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void expandAllItems(TreeItem<String> item) {
        if (item != null) {
            item.setExpanded(true);
            for (TreeItem<String> child : item.getChildren()) {
                expandAllItems(child);
            }
        }
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

    private boolean hasChildren(TreeItem<String> item) {
        return item.getChildren().size() > 0;
    }
    
    
    public void treeView_popup(MouseEvent e){
		if(treeView.isVisible() == false)
			treeView.setVisible(true);
		else
			treeView.setVisible(false);
	}
    
    
    public void addNewCategory(ActionEvent event) throws IOException {
    	String filePath = "src/data/Categories_question.txt";
        String lineParent = category.getValue().trim();
        String newLine = addCategory.getText().trim();
        
    	noticeCategory.setShowDelay(Duration.seconds(0.5));
        
    	// Kiểm tra TextField có trống hay không
        if (addCategory.getText().trim().isEmpty()) {
        	noticeCategory1.setText("Tên của loại câu hỏi không thể trống");
        	noticeCategory1.show(viewTooltip, 1064, 280);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(e1 -> noticeCategory1.hide());
        	pause.play();
        	return;
        }

        // Kiểm tra nội dung TextField có trùng với nội dung của bất kỳ dòng nào trong file hay không
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().equals(addCategory.getText().trim())) {
                	noticeCategory1.setText("Loại câu hỏi đã tồn tại");
                	noticeCategory1.show(viewTooltip, 1064, 280);
                	PauseTransition pause = new PauseTransition(Duration.seconds(5));
                	pause.setOnFinished(e -> noticeCategory1.hide());
                	pause.play();
                    return;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        if(lineParent == null) {
        	try (FileWriter writer = new FileWriter(filePath, true)) {
        	    writer.write("\n  "+ newLine);
        	} catch (IOException e) {
        	    e.printStackTrace();
        	}
        } else {
            File inputFile = new File(filePath);
            File tempFile = File.createTempFile("temp", null, inputFile.getParentFile());
        	try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            	String currentLine;
            	 while ((currentLine = reader.readLine()) != null) {
                     writer.write(currentLine + System.getProperty("line.separator"));
                     if (currentLine.trim().equals(lineParent)) {
                     	for(int i = 0; i <= getIndentationLevel(currentLine); i++)
                     		newLine = "  " + newLine;
                         writer.write(newLine + System.getProperty("line.separator"));
                     }
            	 }
            	 reader.close();
            	 writer.close();
            	 // Xóa file txt ban đầu
            	 boolean isDeleted = inputFile.delete();
            	 File newFile = new File(inputFile.getAbsolutePath());
            	 boolean isRenamed = tempFile.renameTo(newFile);
               } catch (IOException e2) {
                   e2.printStackTrace();
               }
        }
        noticeCategory.setText("Thêm loại câu hỏi mới thành công");
        noticeCategory.show(viewTooltip, 1064, 280);
    	PauseTransition pause = new PauseTransition(Duration.seconds(5));
    	pause.setOnFinished(e -> noticeCategory.hide());
    	pause.play();
      }
    
    public void ChooseFile (ActionEvent e){
        Stage stage = (Stage) SP.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null){
        	namefile.setText(file.getName());
        	linkfile = file.getAbsolutePath();
        }
    }
    
    public void importFile (ActionEvent event){
    	if(namefile.getText().isEmpty()) {
    		noticeImportFile1.setText("Bạn chưa chọn file để import");
    		noticeImportFile1.show(viewTooltip1, 1064, 280);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(e -> noticeImportFile1.hide());
        	pause.play();
    	} else {
    		if(namefile.getText().endsWith(".txt")) {
        		if(AikenFormatChecker.CheckAikenFormat(linkfile) == true) {
        			try {
        		        Path sourcePath = Paths.get(linkfile);
        		        Path targetPath = Paths.get("src/data", sourcePath.getFileName().toString());

        		        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        		        noticeImportFile.setText("OK import file thành công");
                		noticeImportFile.show(viewTooltip1, 1064, 280);
                    	PauseTransition pause = new PauseTransition(Duration.seconds(5));
                    	pause.setOnFinished(e -> noticeImportFile.hide());
                    	pause.play();
        		    } catch (IOException e) {
        		        e.printStackTrace();
        		    }
        		} else {
        			noticeImportFile1.setText("Import file thất bại do sai định dạng Aiken");
            		noticeImportFile1.show(viewTooltip1, 1064, 280);
                	PauseTransition pause = new PauseTransition(Duration.seconds(5));
                	pause.setOnFinished(e -> noticeImportFile1.hide());
                	pause.play();
        		}
        	} else {
        		noticeImportFile1.setText("Import file thất bại do file không phải là file văn bản");
        		noticeImportFile1.show(viewTooltip1, 960, 280);
            	PauseTransition pause = new PauseTransition(Duration.seconds(5));
            	pause.setOnFinished(e -> noticeImportFile1.hide());
            	pause.play();
        	}
    	}
    	
    }
    
    public void changeSceneCreatQuestion(MouseEvent e) throws IOException {
      Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("Sample3_2.fxml"));
      Parent CreatQuestionParent = loader.load();
      Scene scene = new Scene(CreatQuestionParent);
      scene.getStylesheets().add(getClass().getResource("application3_2.css").toExternalForm());
      stage.setScene(scene);
    }
    
    public void changeSceneHome(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample1.fxml"));
        Parent changeSceneHome = loader.load();
        Scene scene = new Scene(changeSceneHome);
        scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
        stage.setScene(scene);
      }
}