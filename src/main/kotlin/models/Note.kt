package models

import java.time.LocalDate
import java.util.Date

data class Note(
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var isNoteArchived:Boolean,
    var dateCreated: LocalDate
)
{
}