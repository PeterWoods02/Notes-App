package models

data class Note(val noteTitle: String,
                var notePriority: Int,
                val noteCategory: String,
                val isNoteArchived :Boolean){
}