package com.example.parkease.interfaces

import com.google.firebase.Timestamp

data class ParkingLotData(
    val id: String,
    val status: Int,
)

data class ActiveParking(
    val id: String = "",
    val start: Timestamp? = null
)

data class User(
    val activeParking: ActiveParking? = null,
    val name: String = ""
)