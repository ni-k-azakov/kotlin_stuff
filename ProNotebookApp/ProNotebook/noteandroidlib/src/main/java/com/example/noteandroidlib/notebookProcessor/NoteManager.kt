package com.example.noteandroidlib.notebookProcessor

import util.SectionNameTaken
import util.UnknownSectionName

object NoteManager {
    private val noteStorage = NoteStorage()
    private val sections: MutableCollection<String> = mutableSetOf()
    private val sectionColors: MutableMap<String, Int> = mutableMapOf()

    fun addNote(note: Note) {
        noteStorage.addNote(note)
    }
    fun getNotes(): List<Note> {
        return noteStorage.notes.values.sortedBy { it.priority }
    }
    fun deleteNote(note: Note) {
        noteStorage.delete(note.id)
    }
    fun addSection(sectionName: String, color: Int) {
        if (sections.contains(sectionName)) throw SectionNameTaken()
        sections.add(sectionName)
        sectionColors[sectionName] = color
    }
    fun deleteSection(sectionName: String) {
        if (!sections.contains(sectionName)) throw UnknownSectionName(sectionName)
        sections.remove(sectionName)
        sectionColors.remove(sectionName)
        for (note in noteStorage.notes.values) {
            if (note.section == sectionName) {
                note.section = ""
            }
        }
    }
}