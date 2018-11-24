package com.mashup.tt.dto.everytime;

public class Subject {

    private int id;
    private String name;
    private String professor;
    private Time time;

    public Subject(int id, String name, String professor, Time time) {
        this.id = id;
        this.name = name;
        this.professor = professor;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfessor() {
        return professor;
    }

    public Time getTime() {
        return time;
    }
}
