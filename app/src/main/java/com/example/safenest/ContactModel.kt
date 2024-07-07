package com.example.safenest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("contact_table")
data class ContactModel(
    val name: String,
    @PrimaryKey
    val number: String
)
