package com.group.hrapp.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Leave implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate start;
    private LocalDate end;
    private String type; // Sick, Vacation, Unpaid
    private boolean approved;

    public Leave(LocalDate start, LocalDate end, String type, boolean approved) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.approved = approved;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "start=" + start +
                ", end=" + end +
                ", type='" + type + '\'' +
                ", approved=" + approved +
                '}';
    }
}
