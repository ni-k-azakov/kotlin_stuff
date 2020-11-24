package notebookProcessor

class NoteStorage {
    val notes: MutableMap<Long, Note> = mutableMapOf()

    private val _freeIds: MutableList<Long> = mutableListOf()
    private val leastId: Long
        get() {
            _freeIds.sort()
            return _freeIds[0]
        }

    fun addNote(newNote: Note) {
        newNote.id = leastId
        notes[leastId] = newNote
        if (_freeIds.size == 1) {
            val tempLeast = _freeIds[0]
            _freeIds[0] = tempLeast + 1
        } else {
            _freeIds.removeAt(0)
        }
    }
    fun delete(id: Long) {
        _freeIds.add(id)
        notes.remove(id)
    }
}
