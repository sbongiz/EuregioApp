package com.almaviva.euregio.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a.sciarretta on 16/03/2018.
 */

public class Esercente implements Serializable {
    private String lesson;
    private String startLesson;
    private String endLesson;
    private String room;
    private String roomPreview;

    public Esercente(String lesson, String startLesson, String endLesson, String room) throws ParseException {



        this.lesson = lesson;
        this.startLesson = startLesson;
        this.endLesson = endLesson;
        this.room = room;

    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getStartLesson() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = format.parse(startLesson);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public void setStartLesson(String startLesson) {
        this.startLesson = startLesson;
    }

    public String getEndLesson() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = format.parse(endLesson);
        return new SimpleDateFormat("HH:mm").format(date);
    }


    public void setEndLesson(String endLesson) {
        this.endLesson = endLesson;
    }

    public String getRoom() {
        return room;
    }

    public String getRoomPreview(){
        return roomPreview;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    @Override
    public String toString() {
        return "Lesson{" +
                "lesson='" + lesson + '\'' +
                ", startLesson='" + startLesson + '\'' +
                ", finishLesson='" + endLesson + '\'' +
                ", room='" + room + '\'' +
                '}';
    }

}
