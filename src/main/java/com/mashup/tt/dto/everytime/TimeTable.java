package com.mashup.tt.dto.everytime;

import java.util.ArrayList;

public class TimeTable {

    private int year;
    private int semester;
    private ArrayList<Subject> subjectList;

    public TimeTable(int year, int semester, ArrayList<Subject> subjectList) {
        this.year = year;
        this.semester = semester;
        this.subjectList = subjectList;
    }

    public int getYear() {
        return year;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Subject> getSubjectList() {
        return subjectList;
    }
}
