package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(int userid) {
        return noteMapper.getNotes(userid);
    }

    public int saveOrUpdateNote(int userid, String noteId, String noteTitle, String noteDescription) {
        System.out.println("START NoteService saveOrUpdateNote()");
        if(noteId == null || noteId.isBlank()) {
            return noteMapper.insert(new Note(noteTitle, noteDescription, userid));
        } else {
            return noteMapper.update(noteTitle, noteDescription, Integer.valueOf(noteId), userid);
        }
    }

    public int deleteNote(int noteid, int userid) {
        return noteMapper.delete(noteid, userid);
    }
}
