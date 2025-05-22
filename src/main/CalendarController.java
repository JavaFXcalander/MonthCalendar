package main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalendarController {

    @FXML
    private GridPane calendarGrid;
    
    @FXML
    private Label monthYearLabel;
    
    @FXML
    private Button previousButton;
    
    @FXML
    private Button nextButton;
    
    private LocalDate currentDate;
    
    // 新增的變數用於事件管理
    @FXML
    private ListView<Event> eventListView;
    
    // 用於存儲事件的Map，按日期組織
    private Map<LocalDate, List<Event>> eventMap = new HashMap<>();
    private ObservableList<Event> todayEvents = FXCollections.observableArrayList(); // 用於顯示今日事件
    
    public void initialize() {
        // 初始化為當前日期
        currentDate = LocalDate.now();
        
        // 設置按鈕動態效果
        setupButtonEffects();
        
        // 設置月份切換按鈕
        if (previousButton != null) {
            previousButton.setOnAction(e -> {
                currentDate = currentDate.minusMonths(1);
                updateCalendar();
            });
        }
        
        if (nextButton != null) {
            nextButton.setOnAction(e -> {
                currentDate = currentDate.plusMonths(1);
                updateCalendar();
            });
        }
        
        // 初始顯示日曆
        updateCalendar();
    }
    
    private void setupButtonEffects() {
        // 添加滑鼠懸停和點擊效果
        if (previousButton != null) {
            String baseStyle = previousButton.getStyle();
            String hoverStyle = baseStyle + "-fx-background-color: #4a7da8;";
            String pressedStyle = baseStyle + "-fx-background-color: #3a6d98; -fx-translate-y: 1px;";
            
            previousButton.setOnMouseEntered(e -> previousButton.setStyle(hoverStyle));
            previousButton.setOnMouseExited(e -> previousButton.setStyle(baseStyle));
            previousButton.setOnMousePressed(e -> previousButton.setStyle(pressedStyle));
            previousButton.setOnMouseReleased(e -> {
                if (previousButton.isHover()) {
                    previousButton.setStyle(hoverStyle);
                } else {
                    previousButton.setStyle(baseStyle);
                }
            });
        }
        
        if (nextButton != null) {
            String baseStyle = nextButton.getStyle();
            String hoverStyle = baseStyle + "-fx-background-color: #4a7da8;";
            String pressedStyle = baseStyle + "-fx-background-color: #3a6d98; -fx-translate-y: 1px;";
            
            nextButton.setOnMouseEntered(e -> nextButton.setStyle(hoverStyle));
            nextButton.setOnMouseExited(e -> nextButton.setStyle(baseStyle));
            nextButton.setOnMousePressed(e -> nextButton.setStyle(pressedStyle));
            nextButton.setOnMouseReleased(e -> {
                if (nextButton.isHover()) {
                    nextButton.setStyle(hoverStyle);
                } else {
                    nextButton.setStyle(baseStyle);
                }
            });
        }
    }
    
    private void updateCalendar() {
        // 清除先前的日曆內容
        calendarGrid.getChildren().clear();
        
        // 設置月份年份標題 - 使用英文格式
        if (monthYearLabel != null) {
            String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
            int year = currentDate.getYear();
            monthYearLabel.setText(month + " " + year);
        }
        
        // 添加星期標題 (0行) - 使用英文格式
        for (int i = 0; i < 7; i++) {
            DayOfWeek day = DayOfWeek.of((i + 1) % 7 + 1); // 從星期日開始
            Label dayLabel = new Label(day.getDisplayName(TextStyle.SHORT, Locale.US));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.getStyleClass().add("day-header");
            calendarGrid.add(dayLabel, i, 0);
        }
        
        // 獲取當月的信息
        YearMonth yearMonth = YearMonth.from(currentDate);
        int daysInMonth = yearMonth.lengthOfMonth();
        
        // 獲取當月第一天是星期幾 (0=星期日, 1=星期一, ..., 6=星期六)
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue() % 7; // 調整為星期日為0
        
        // 填充日曆
        int day = 1;
        int row = 1; // 從第1行開始（第0行是星期標題）
        
        // 填充第一週前的空白
        for (int col = 0; col < dayOfWeekValue; col++) {
            VBox cell = createCell("");
            calendarGrid.add(cell, col, row);
        }
        
        // 填充日期
        for (int col = dayOfWeekValue; col < 7; col++) {
            if (day <= daysInMonth) {
                VBox cell = createDateCell(day, currentDate);
                calendarGrid.add(cell, col, row);
                day++;
            }
        }
        
        // 填充剩下的週
        while (day <= daysInMonth) {
            row++;
            for (int col = 0; col < 7 && day <= daysInMonth; col++) {
                VBox cell = createDateCell(day, currentDate);
                calendarGrid.add(cell, col, row);
                day++;
            }
        }
    }
    
    private VBox createCell(String text) {
        VBox cell = new VBox();
        cell.setPrefSize(100, 80);
        cell.getStyleClass().add("calendar-cell");
        cell.setAlignment(Pos.TOP_LEFT);
        
        if (!text.isEmpty()) {
            Label label = new Label(text);
            cell.getChildren().add(label);
        }
        
        return cell;
    }
    
    private VBox createDateCell(int day, LocalDate baseDate) {
        String text = String.valueOf(day);
        VBox cell = createCell(text);
        
        // 調整日期位置
        cell.getChildren().clear(); // 清除預設標籤
        
        Label dateLabel = new Label(text);
        dateLabel.getStyleClass().add("date-label");
        
        VBox dateContainer = new VBox(dateLabel);
        dateContainer.setAlignment(Pos.TOP_RIGHT);
        dateContainer.setPrefWidth(Double.MAX_VALUE);
        
        cell.getChildren().add(dateContainer);
        
        // 高亮顯示當天
        LocalDate cellDate = baseDate.withDayOfMonth(day);
        LocalDate today = LocalDate.now();
        
        if (cellDate.equals(today)) {
            cell.getStyleClass().add("today-cell");
            dateLabel.getStyleClass().add("today-label");
        }
        
        // 添加滑鼠懸停效果
        String baseStyle = cell.getStyle();
        String hoverStyle = baseStyle.isEmpty() ? 
            "-fx-border-color: lightgray; -fx-background-color: rgba(200, 220, 240, 0.3);" : 
            baseStyle + "-fx-background-color: rgba(200, 220, 240, 0.5);";
        
        cell.setOnMouseEntered(e -> cell.setStyle(hoverStyle));
        cell.setOnMouseExited(e -> cell.setStyle(baseStyle));
        
        // 添加點擊事件（可擴展為添加事件等功能）
        cell.setOnMouseClicked(e -> handleDateClick(cellDate));
        
        // 顯示該日期的事件
        displayEventsForDate(cell, cellDate);
        
        return cell;
    }
    
    private void handleDateClick(LocalDate date) {
        // 處理日期點擊事件，彈出窗口來新增/編輯事件
        showEventDialog(date);
    }
    
    private void showEventDialog(LocalDate selectedDate) {
        // 創建事件添加對話框
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("添加事件");
        dialog.setHeaderText("為 " + selectedDate + " 添加事件");
        
        // 設置按鈕
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // 創建表單
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField eventNameField = new TextField();
        eventNameField.setPromptText("事件名稱");
        
        TextField timeField = new TextField();
        timeField.setPromptText("時間 (HH:MM)");
        
        ColorPicker colorPicker = new ColorPicker(Color.CORNFLOWERBLUE);
        
        grid.add(new Label("事件名稱:"), 0, 0);
        grid.add(eventNameField, 1, 0);
        grid.add(new Label("時間:"), 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(new Label("顏色:"), 0, 2);
        grid.add(colorPicker, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        // 處理結果
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String eventName = eventNameField.getText().trim();
                String timeString = timeField.getText().trim();
                Color selectedColor = colorPicker.getValue();
                
                if (!eventName.isEmpty()) {
                    LocalTime time = LocalTime.of(12, 0); // 預設中午12點
                    
                    try {
                        // 嘗試解析時間
                        String[] parts = timeString.split(":");
                        if (parts.length == 2) {
                            int hour = Integer.parseInt(parts[0]);
                            int minute = Integer.parseInt(parts[1]);
                            if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
                                time = LocalTime.of(hour, minute);
                            }
                        }
                    } catch (Exception e) {
                        // 使用預設時間
                    }
                    
                    // 創建並添加事件
                    Event newEvent = new Event(selectedDate, time, eventName, selectedColor);
                    addEvent(newEvent);
                    
                    // 更新顯示
                    updateCalendar();
                }
            }
        });
    }
    
    // 事件管理方法
    private void addEvent(Event event) {
        LocalDate date = event.getDate();
        if (!eventMap.containsKey(date)) {
            eventMap.put(date, new ArrayList<>());
        }
        eventMap.get(date).add(event);
        
        // 如果是今天的事件，更新今日事件列表
        if (date.equals(LocalDate.now())) {
            todayEvents.add(event);
        }
    }
    
    private void removeEvent(Event event) {
        LocalDate date = event.getDate();
        if (eventMap.containsKey(date)) {
            eventMap.get(date).remove(event);
            
            // 如果是今天的事件，從今日事件列表移除
            if (date.equals(LocalDate.now())) {
                todayEvents.remove(event);
            }
        }
    }
    
    // 顯示日期的所有事件
    private void displayEventsForDate(VBox cell, LocalDate cellDate) {
        // 只顯示該日期存在的事件
        if (eventMap.containsKey(cellDate)) {
            List<Event> dateEvents = eventMap.get(cellDate);
            for (Event event : dateEvents) {
                HBox eventLabel = createEventLabel(event);
                cell.getChildren().add(eventLabel);
            }
        }
    }
    
    // 處理Diary按鈕點擊 - 新添加的方法
    @FXML
    private void handleDiaryButton() {
        System.out.println("跳轉到日記頁面");
        // 這裡可以實現跳轉到日記頁面的邏輯
    }
    
    // 處理Project按鈕點擊 - 新添加的方法
    @FXML
    private void handleProjectButton() {
        System.out.println("跳轉到專案管理頁面");
        // 這裡可以實現跳轉到專案管理頁面的邏輯
    }
    
    // 顯示事件詳情
    private void showEventDetails(Event event) {
        // 創建一個新窗口顯示事件詳情
        Stage detailStage = new Stage();
        detailStage.setTitle("事件詳情");
        detailStage.initModality(Modality.APPLICATION_MODAL);
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        
        Label dateLabel = new Label("日期: " + event.getDate().toString());
        Label timeLabel = new Label("時間: " + event.getTime().toString());
        Label descLabel = new Label("描述: " + event.getDescription());
        
        Button editButton = new Button("編輯");
        Button deleteButton = new Button("刪除");
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(editButton, deleteButton);
        
        layout.getChildren().addAll(dateLabel, timeLabel, descLabel, buttonBox);
        
        // 編輯事件
        editButton.setOnAction(e -> {
            editEvent(event);
            detailStage.close();
        });
        
        // 刪除事件
        deleteButton.setOnAction(e -> {
            removeEvent(event);
            updateCalendar();
            detailStage.close();
        });
        
        detailStage.setScene(new Scene(layout, 300, 200));
        detailStage.show();
    }
    
    // 編輯事件
    private void editEvent(Event event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("編輯事件");
        dialog.setHeaderText("編輯 " + event.getDate() + " 的事件");
        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField eventNameField = new TextField(event.getDescription());
        
        String timeString = String.format("%02d:%02d", 
                                        event.getTime().getHour(), 
                                        event.getTime().getMinute());
        TextField timeField = new TextField(timeString);
        
        ColorPicker colorPicker = new ColorPicker(event.getColor());
        
        grid.add(new Label("事件名稱:"), 0, 0);
        grid.add(eventNameField, 1, 0);
        grid.add(new Label("時間:"), 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(new Label("顏色:"), 0, 2);
        grid.add(colorPicker, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String eventName = eventNameField.getText().trim();
                String newTimeString = timeField.getText().trim();
                Color selectedColor = colorPicker.getValue();
                
                if (!eventName.isEmpty()) {
                    LocalTime time = event.getTime(); // 預設保持原來的時間
                    
                    try {
                        // 嘗試解析新時間
                        String[] parts = newTimeString.split(":");
                        if (parts.length == 2) {
                            int hour = Integer.parseInt(parts[0]);
                            int minute = Integer.parseInt(parts[1]);
                            if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
                                time = LocalTime.of(hour, minute);
                            }
                        }
                    } catch (Exception e) {
                        // 使用原來的時間
                    }
                    
                    // 更新事件
                    event.setDescription(eventName);
                    event.setTime(time);
                    event.setColor(selectedColor);
                    
                    // 更新顯示
                    updateCalendar();
                }
            }
        });
    }
    
    private HBox createEventLabel(Event event) {
        HBox eventBox = new HBox(5);
        eventBox.setMaxWidth(Double.MAX_VALUE);
        
        Label timeLabel = new Label(event.getTime().toString().substring(0, 5));
        timeLabel.setStyle("-fx-font-size: 8pt;");
        
        Label descLabel = new Label(event.getDescription());
        descLabel.setStyle("-fx-font-size: 8pt;");
        
        // 設置背景顏色
        Color eventColor = event.getColor();
        String colorStyle = String.format(
            "-fx-background-color: rgba(%d, %d, %d, 0.7);",
            (int)(eventColor.getRed() * 255),
            (int)(eventColor.getGreen() * 255),
            (int)(eventColor.getBlue() * 255)
        );
        
        eventBox.setStyle(colorStyle);
        eventBox.getChildren().addAll(timeLabel, descLabel);
        
        // 點擊事件標籤時顯示詳情
        eventBox.setOnMouseClicked(e -> {
            e.consume(); // 防止觸發單元格的點擊事件
            showEventDetails(event);
        });
        
        return eventBox;
    }
}