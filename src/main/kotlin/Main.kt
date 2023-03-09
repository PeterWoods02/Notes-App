import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.Serializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))




fun main(args: Array<String>) {
    runMenu()
}


private val logger = KotlinLogging.logger {}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List notes                |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note            |
         > |   20) Save Notes               |
         > |   21) Load Notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}


fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5  -> archiveNote()
            20 -> save()
            21 -> load()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}


fun addNote(){

    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

    if (isAdded)
    {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}


fun listNotes(){

    println("1) List all notes")
    println("2) List Active notes")
    println("3) List Archived notes")
    println("")
    val listNotes = readNextInt("Select an option: ")
    do {
        val option = listNotes
        when (option) {
            1 -> noteAPI.listAllNotes()
            2 -> noteAPI.listActiveNotes()
            3 -> noteAPI.listArchivedNotes()

            else -> println("Invalid option entered: ${option}")}
        }while (true)



}

fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}


fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
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
    exit(0)
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


fun archiveNote()  {
    //logger.info { "updateNotes() function invoked" }
    println(noteAPI.listActiveNotes())
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note you wish to archive: ")
        if  (noteAPI.isValidIndex(indexToUpdate)) //and is active
             {

            noteAPI.findNote(indexToUpdate) != null
                val isNoteArchived = true


                println("Archived Successful")

        } else {
            println("There are no notes for this index number")
        }
    }
}






