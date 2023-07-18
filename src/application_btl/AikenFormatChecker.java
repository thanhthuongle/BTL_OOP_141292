package application_btl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AikenFormatChecker {
	private static int CountLine;
	private static int CountQuestion;
	
	public static int getCountQuestion() {
		return CountQuestion;
	}
    
	public static boolean CheckAikenFormat(String filePath) {
		CountLine = 0;
		CountQuestion = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int chosseCount = 0;
            int i = 1;
            // Đọc từng dòng trong file
            while ((line = reader.readLine()) != null) {
            	CountLine += 1;
            	line = line.trim(); // Loại bỏ khoảng trắng ở đầu và cuối dòng
            	if(i == 0 && line.isEmpty()) {
            		i+=1;
            		continue;
            	}
            	if(i == 1) {
            		if (line.isEmpty())  // Kiểm tra dòng rỗng không, kiem tra tieu de
            			return false;
            	} else if(line.matches("^[A-Z]\\.\\s.*")) {
            		chosseCount++;  // Dòng chứa lựa chọn đáp án, đếm số câu hỏi
            	} else if(line.startsWith("ANSWER:") ) {
            		if(chosseCount >= 2) {
            			i = -1;
            			chosseCount = 0;
            			CountQuestion += 1;
            		} else
            			return false;
            	} else
            		return false;
            	i+= 1;
            }
            	return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }  
	
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
        //String filePath = "C:\\Users\\KIM OANH\\Desktop\\check.txt"; // đường dẫn tới file cần ktra
        System.out.println("Vui lòng nhập địa chỉ file: ");
        String filePath = scanner.nextLine();;
        File file = new File(filePath);
        while(!(file.exists())) {  // kiem tra file co ton tai khong
        	System.out.println("Địa chỉ file không hợp lệ, vui lòng nhập lại địa chỉ file: ");
        	filePath = scanner.nextLine();
            file = new File(filePath);
        }
        scanner.close();
        boolean isAiken = CheckAikenFormat(filePath);

        if (isAiken) {
            System.out.println("Success " + CountQuestion);
        } else {
            System.out.println("“Error at " + CountLine);
        }
    }
}


