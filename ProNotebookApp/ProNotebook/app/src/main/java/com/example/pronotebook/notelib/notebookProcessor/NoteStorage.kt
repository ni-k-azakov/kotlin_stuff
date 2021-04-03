package com.example.noteandroidlib.notebookProcessor

import util.Property

class NoteStorage {
    val notes: MutableList<Note> = mutableListOf()

    fun addNote(message: String): Int {
        notes.add(Note(message))
        return notes.size - 1
    }
    fun delete(id: Int) {
        notes.removeAt(id)
    }
    fun addProperty(noteId: Int, newProperty: Property, propertyValue: String) {
        notes[noteId].addProperty(newProperty, propertyValue)
    }
}
