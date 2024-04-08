package com.example.mynoteapp.objects;

import java.io.Serializable;

public class Note implements Serializable{

    int _id;
    String _title;
    String _content;

    public Note() {

    }

    public Note(int _id, String _title, String _content) {
        this._id = _id;
        this._title = _title;
        this._content = _content;
    }

    public Note(String _title, String _content) {
        this._title = _title;
        this._content = _content;
    }

    public String toString() {
        return _title+":"+_content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }
}
