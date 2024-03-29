import controllers.NoteAPI
import models.Note
import persistence.XMLSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.time.LocalDate
import kotlin.system.exitProcess

//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))





fun main() {
    runMenu()
}


fun mainMenu() : Int {
    return readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List notes                |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note            |
         > ----------------------------------
         > |   6) Search Notes              |
         > |   7) List by Date Created      |
         > |   8) List by Priority          |
         > ---------------------------------- 
         > |   9) Count notes per category  |
         > |  10) Count notes by priority   |
         > ----------------------------------
         > |  20) Save Notes                |
         > |  21) Load Notes                |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}


fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5  -> archiveNote()
            6 -> searchNotes()
            7 -> listDate()
            8 -> listByPriority()
            9 -> countCategory()
            10 -> countPriority()
            20 -> save()
            21 -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}


fun addNote(){

    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val noteDate = LocalDate.now()
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false,noteDate))

    if (isAdded)
    {
        println("Added Successfully")
    } else {
        println("Add Failed!")
    }
}

//lists notes and displays option
fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNotes()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No notes stored")
    }
}

//updates note
fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    println(noteAPI.listAllNotes())
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")
            val noteDate = LocalDate.now()

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, noteDate))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

//delete note
fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    println(noteAPI.listAllNotes())
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if the note exist
        val indexToDelete = readNextInt(
            "Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}


fun exitApp(){
    println("Exiting...bye")
    exitProcess(0)
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}


fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}
fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}



fun archiveNote() {
    println(noteAPI.listActiveNotes())
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
       noteAPI.archiveNote(indexToArchive)
    }
}

//takes user input and searches for note title
fun searchNotes(){
    val searchTitle = readNextLine("Enter Title to search By: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if(searchResults.isEmpty()){
        println("No Note Found")
    }else{
        println(searchResults)
    }
}

//list by newest made notes to oldest
fun listDate() {
    noteAPI.listByDate()
    println(noteAPI.listAllNotes())
}

//list by priority of notes
fun listByPriority(){
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > -------------------------------------------
                  > |   1) List by Most - Least important     |
                  > |   2) List by Least - Most important     |
                  > -------------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> noteAPI.listByMost()
            2 -> noteAPI.listByLeast()
            else -> println("Invalid option entered: $option")
        }
        println(noteAPI.listAllNotes())
    } else {
        println("Option Invalid - No notes stored")
    }
}


//count number of notes in each category
fun countCategory(){
println(noteAPI.numberOfNotesByCategory())

}

//count notes on inputed priority
fun countPriority() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------------------
                  > | Enter priority 1-5 you wish to search by |
                  > --------------------------------------------
         > ==>> """.trimMargin(">")
        )

        println("${noteAPI.numberOfNotesByPriority(option)} note/notes with a priority of $option")
    }
}







