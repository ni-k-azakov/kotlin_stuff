package com.example.noteandroidlib.notebookProcessor

import util.Property
import util.SectionNameTaken
import util.SectionSizeError
import util.UnknownSectionName

object NoteManager {
    private val sections: MutableList<Section> = mutableListOf()
    val sectionsAmount: Int
        get() {
           return sections.size
        }

    fun addNote(sectionIndex: Int, message: String): Int {
        if (sectionIndex <= sections.size - 1) {
            return sections[sectionIndex].addNote(message) //returns new Note id
        } else {
            throw SectionSizeError()
        }
    }

    fun getNotesAmount(sectionIndex: Int): Int {
        return sections[sectionIndex].getNotesAmount()
    }

    fun getNotes(sectionIndex: Int): List<Note> {
        if (sectionIndex <= sections.size - 1) {
            return sections[sectionIndex].getNotes()
        } else {
            throw SectionSizeError()
        }
    }

    fun getSectionName(index: Int): String {
        if (index <= sections.size - 1) {
            return sections[index].name
        } else {
            throw SectionSizeError()
        }
    }

    fun getSectionColor(index: Int): Int {
        if (index <= sections.size - 1) {
            return sections[index].color
        } else {
            throw SectionSizeError()
        }
    }

    fun deleteNote(sectionIndex: Int, noteId: Int) {
        if (sectionIndex <= sections.size - 1) {
            sections[sectionIndex].delete(noteId)
        } else {
            throw SectionSizeError()
        }
    }

    fun addSection(sectionName: String, color: Int) {
        sections.add(Section(sectionName, color))
    }

    fun deleteSection(id: Int) {
        if (id <= sections.size - 1) {
            sections.removeAt(id)
        } else {
            throw SectionSizeError()
        }
    }

    fun addProperty(sectionIndex: Int, noteId: Int, newProperty: Property, propertyValue: String) {
        sections[sectionIndex].addProperty(noteId, newProperty, propertyValue)
    }

    private class Section(val name: String, val color: Int) {
        private val noteStorage = NoteStorage()

        fun delete(noteId: Int) {
            noteStorage.delete(noteId)
        }

        fun addNote(message: String): Int {
            return noteStorage.addNote(message)
        }

        fun getNotesAmount(): Int {
            return noteStorage.notes.size
        }

        fun getNotes(): List<Note> {
            return noteStorage.notes.sortedBy { it.priority }
        }

        fun addProperty(noteId: Int, newProperty: Property, propertyValue: String) {
            noteStorage.addProperty(noteId, newProperty, propertyValue)
        }
    }
}