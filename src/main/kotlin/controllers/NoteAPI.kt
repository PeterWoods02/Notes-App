package controllers

import models.Note
import persistence.Serializer
import java.util.Arrays.sort
import java.util.Date

class NoteAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType

    private var notes = ArrayList<Note>()



//adding to notes
    fun add(note: Note): Boolean {
        return notes.add(note)
    }

//list every note
    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
        else notes.joinToString (separator = "\n") { note ->
            notes.indexOf(note).toString() + ": " + note.toString() }
//list only notes that are not archived
    fun listActiveNotes(): String =
        if  (numberOfActiveNotes() == 0)  "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived})
//list archived notes
    fun listArchivedNotes(): String =
        if  (numberOfArchivedNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived})


//list by newest to the oldest notes
    fun listByDate(): String = notes.sortBy { it.dateCreated}.toString()


    //returns number of notes
    fun numberOfNotes(): Int {
        return notes.size
    }

    //finds notes based on number in array
    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }


    //searching by title
    fun searchByTitle (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })



    //utility method to determine if an index is valid in a list
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }



//returns number of notes that are archived
    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }

    //returns number of notes that are not archived
    fun numberOfActiveNotes(): Int = notes.count() { note: Note -> !note.isNoteArchived }

//List by highest to the lowest priority
    fun listByLeast() = notes.sortBy { it.notePriority}.toString()
//list by lowest to the highest priority
    fun listByMost() = notes.sortByDescending { it.notePriority}.toString()

    //returns number of notes based on priority inputed
    fun numberOfNotesByPriority(priority: Int): Int = notes.count() {note: Note -> note.notePriority == priority}



//deletes note based on index
    fun deleteNote(indexToDelete: Int): Note? {
    return if(isValidListIndex(indexToDelete, notes)) {
        notes.removeAt(indexToDelete)
    }
    else null
}

//updates note
    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }
        //if the note was not found, return false, update was not successful
        return false
    }

//returns if user input is a valid index
    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }
//archive a note
    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }

        return false
    }

//load persistence
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }
//save for persistence
    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

//removes duplication within NoteAPI
    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

}



