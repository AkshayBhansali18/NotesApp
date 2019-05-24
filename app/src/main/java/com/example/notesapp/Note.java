package com.example.notesapp;

public class Note {
    int priority;
    String documentId;
    public Note() {

    }
    String title,description;
    Note(String title,String description,int priority)
    {
        this.title=title;
        this.description=description;
        this.priority=priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
