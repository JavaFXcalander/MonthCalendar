package main;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.scene.paint.Color;

public class Event {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private Color color;
    
    public Event(LocalDate date, LocalTime time, String description, Color color) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.color = color;
    }
    
    // Getters
    public LocalDate getDate() {
        return date;
    }
    
    public LocalTime getTime() {
        return time;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Color getColor() {
        return color;
    }
    
    // Setters
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public String toString() {
        return time.toString() + " - " + description;
    }
}