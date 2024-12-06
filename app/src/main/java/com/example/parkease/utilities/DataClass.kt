package com.example.parkease.utilities

import com.google.firebase.Timestamp

data class ParkingLotData(
    val id: String,
    val status: Int,
)

data class Location(
    val costPerHour: Int = 0,
    val description: String = "",
    val id: String = "",
    val ip: String = "",
    val isOpen: Boolean = false,
    val name: String = "",
    val thumbnail: String = "",
)

data class ActiveParking(
    val id: String = "",
    val start: Timestamp? = null
)

data class User(
    val activeParking: ActiveParking? = null,
    val name: String = ""
)