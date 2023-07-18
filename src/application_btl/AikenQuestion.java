package application_btl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AikenQuestion {
	 // Khai báo các trường dữ liệu
    private String questionText; // Nội dung câu hỏi
    private Map<String, String> choices; // Các lựa chọn, với key là ký tự đại diện và value là nội dung lựa chọn
    private Map<String, Double> correctAnswers; // Các câu trả lời đúng, với key là ký tự đại diện và value là điểm số

    // Constructor
    public AikenQuestion(String questionText, Map<String, String> choices, Map<String, Double> correctAnswers) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswers = correctAnswers;
    }

    // Phương thức tĩnh để chuyển đổi từ định dạng Aiken sang AikenQuestion
    public static AikenQuestion fromAikenFormat(String aikenText) {
        // Chia văn bản Aiken thành các dòng
        String[] lines = aikenText.split("\n");
        // Lấy nội dung câu hỏi từ dòng đầu tiên
        String questionText = lines[0];
        // Tạo ra Map cho các lựa chọn
        Map<String, String> choices = new HashMap<>();
        // Phân tích cú pháp các dòng tiếp theo để lấy các lựa chọn
        for (int i = 1; i < lines.length - 1; i++) {
            choices.put(lines[i].substring(0, 1), lines[i].substring(2));
        }
        // Tạo ra Map cho các câu trả lời đúng
        Map<String, Double> correctAnswers = new HashMap<>();
        // Phân tích cú pháp dòng cuối cùng để lấy các câu trả lời đúng
        String[] correctAnswerLines = lines[lines.length - 1].substring(7).split(",");
        for (String correctAnswerLine : correctAnswerLines) {
            String[] parts = correctAnswerLine.split(":");
            correctAnswers.put(parts[0], Double.parseDouble(parts[1]));
        }
        // Tạo ra và trả về một AikenQuestion từ dữ liệu đã lấy
        return new AikenQuestion(questionText, choices, correctAnswers);
    }

    // Phương thức để chuyển đổi từ AikenQuestion sang định dạng Aiken
    public String toAikenFormat() {
        // Tạo ra một StringBuilder để xây dựng văn bản Aiken
        StringBuilder sb = new StringBuilder(questionText + "\n");
        // Thêm các lựa chọn vào văn bản
        for (Map.Entry<String, String> choice : choices.entrySet()) {
            sb.append(choice.getKey() + ". " + choice.getValue() + "\n");
        }
        // Thêm các câu trả lời đúng và mark vào văn bản
        sb.append("ANSWER: ");
        for (Map.Entry<String, Double> correctAnswer : correctAnswers.entrySet()) {
            //sb.append(correctAnswer.getKey() + ":" + correctAnswer.getValue() + ",");
        	String formattedValue;
            if (correctAnswer.getKey().equals("Mark")) {
                formattedValue = String.format("%.0f", correctAnswer.getValue());
            } else {
                formattedValue = correctAnswer.getValue().toString();
            }
            sb.append(correctAnswer.getKey() + ":" + formattedValue + ",");
        }
        // Xóa dấu phẩy cuối cùng
        sb.deleteCharAt(sb.length() - 1);    
        // Thêm 2 dòng trắng vào cuối
        sb.append("\n");
        // Trả về văn bản Aiken
        return sb.toString();
    }
    
    public static String getQuestionContent(String displayedQuestion, String nameCategory) throws IOException {
        Path path = Paths.get("src/data/" + nameCategory + ".txt");
        try (Stream<String> lines = Files.lines(path)) {
        	String[] questions = lines.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
            for (String question : questions) {
                if (question.replace("\n", "").equals(displayedQuestion)) {
                    return question;
                }
            }
            return null;
        }
    }
}
