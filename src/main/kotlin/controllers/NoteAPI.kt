package controllers

import models.Note
import persistence.Serializer

class NoteAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType

    private var notes = ArrayList<Note>()




    fun add(note: Note): Boolean {
        return notes.add(note)
    }


    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        }
        else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }


    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }
    //utility method to determine if an index is valid in a list
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }


    fun listActiveNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        }
        else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if(notes.get(i).isNoteArchived)
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }

    }

    fun listArchivedNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        }
        else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if(!notes.get(i).isNoteArchived)
                    listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

    fun numberOfArchivedNotes(): Int {

        var numOfNotes = 0
        for (note in notes) {
            if(note.isNoteArchived)
                numOfNotes++
        }
        return numOfNotes
        //helper method to determine how many active notes there are
    }

    fun numberOfActiveNotes(): Int {

            var numOfNotes = 0

        for (note in notes) {
                if(!note.isNoteArchived)
                    numOfNotes++
            }
            return numOfNotes
        //helper method to determine how many active notes there are
    }


    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        }
        else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if(notes.get(i).notePriority == priority)
                    listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        return if (notes.isEmpty()) {
            0
        }
        else {
            var numOfNotes = 0
            for (i in notes.indices) {
                if(notes.get(i).notePriority == priority)
                    numOfNotes++
            }
            numOfNotes
        }
        //helper method to determine how many notes there are of a specific priority
    }

fun deleteNote(indexToDelete: Int): Note? {
    return if(isValidListIndex(indexToDelete, notes)) {
        notes.removeAt(indexToDelete)
    }
    else null
}


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


    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }


    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }




}



