package com.group.hrapp.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Attendance implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private boolean present;
    private boolean onLeave;

    public Attendance(LocalDate date, boolean present, boolean onLeave) {
        this.date = date;
        this.present = present;
        this.onLeave = onLeave;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isOnLeave() {
        return onLeave;
    }

    public void setOnLeave(boolean onLeave) {
        this.onLeave = onLeave;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "date=" + date +
                ", present=" + present +
                ", onLeave=" + onLeave +
                '}';
    }
}
