package main;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.scene.control.Label;

/**
 * 用於日期導航和頁面跳轉的類
 */
public class DateNavigator {
    private LocalDate currentDate;
    private EventManager eventManager;
    
    public DateNavigator(EventManager eventManager) {
        this.currentDate = LocalDate.now();
        this.eventManager = eventManager;
    }
    
    /**
     * 獲取當前月份的年月文字
     */
    public String getMonthYearText() {
        String month = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
        int year = currentDate.getYear();
        return month + " " + year;
    }
    
    /**
     * 更新月年標籤
     */
    public void updateMonthYearLabel(Label monthYearLabel) {
        if (monthYearLabel != null) {
            monthYearLabel.setText(getMonthYearText());
        }
    }
    
    /**
     * 前往上一個月
     */
    public void goToPreviousMonth() {
        currentDate = currentDate.minusMonths(1);
    }
    
    /**
     * 前往下一個月
     */
    public void goToNextMonth() {
        currentDate = currentDate.plusMonths(1);
    }
    
    /**
     * 獲取當月的第一天是星期幾（0=星期日, 1=星期一, ..., 6=星期六）
     */
    public int getFirstDayOfMonthWeekday() {
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        return firstDayOfMonth.getDayOfWeek().getValue() % 7;
    }
    
    /**
     * 獲取當月的天數
     */
    public int getDaysInMonth() {
        YearMonth yearMonth = YearMonth.from(currentDate);
        return yearMonth.lengthOfMonth();
    }
    
    /**
     * 獲取當前顯示的日期（月份基準日期）
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }
    
    /**
     * 跳轉到日記頁面
     */
    public void navigateToDiary(LocalDate date) {
        System.out.println("跳轉到 " + date + " 的日記頁面");
        // 實際的日記頁面跳轉邏輯應該在這裡實現
    }
    
    /**
     * 跳轉到專案管理頁面
     */
    public void navigateToProject() {
        System.out.println("跳轉到專案管理頁面");
        // 實際的專案頁面跳轉邏輯應該在這裡實現
    }
}