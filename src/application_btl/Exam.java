package application_btl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class Exam {
    private String name;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Duration limitTime;

    public Exam(String name, LocalDateTime openTime, LocalDateTime closeTime, Duration limitTime) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.limitTime = limitTime;
    }

    public String getName() {
        return name;
    }

    public Duration getLimitTime() {
		return limitTime;
	}

	public boolean isOpen() {
        LocalDateTime now = LocalDateTime.now();
        return (openTime == null || now.isAfter(openTime)) && (closeTime == null || now.isBefore(closeTime));
    }

    public static Exam fromString(String str) {
        String[] parts = str.split(",");
        String name = parts[0];
        LocalDateTime openTime = (parts.length > 1 && !parts[1].isBlank()) ? LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
        LocalDateTime closeTime = (parts.length > 2 && !parts[2].isBlank()) ? LocalDateTime.parse(parts[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
        Duration limitTime = (parts.length > 3 && !parts[3].isBlank()) ? Duration.ofMinutes(Long.parseLong(parts[3])) : null;
        return new Exam(name, openTime, closeTime, limitTime);
    }

    @Override
    public String toString() {
        return name + "," + 
               (openTime == null ? "" : openTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) + "," + 
               (closeTime == null ? "" : closeTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) + "," + 
               (limitTime == null ? "" : Long.toString(limitTime.toMinutes()));
    }
    
    public static String getLimitTimeByName(String examName) throws IOException {
        Path path = Paths.get("src/data/ExamList.txt");
        try (Stream<String> lines = Files.lines(path)) {
        	DecimalFormat df = new DecimalFormat("0.##");  // Định dạng số với tối đa 2 chữ số thập phân, không hiển thị nếu bằng 0

        	return lines.map(Exam::fromString)
        	            .filter(exam -> exam.getName().equals(examName))
        	            .findFirst()
        	            .map(exam -> exam.getLimitTime() != null ? df.format(exam.getLimitTime().toMinutes() / 60.0) : null)
        	            .orElse(null);
//            return lines.map(Exam::fromString)
//                        .filter(exam -> exam.getName().equals(examName))
//                        .findFirst()
//                        .map(exam -> exam.getLimitTime() != null ? String.format("%.2f", exam.getLimitTime().toMinutes() / 60.0) : null)
//                        .orElse(null);
        }
    }
}