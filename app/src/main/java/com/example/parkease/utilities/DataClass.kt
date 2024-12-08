package com.example.parkease.utilities

import com.google.firebase.Timestamp

data class ParkingLotData(
    val id: String,
    val status: Int,
)

open class Location(
    var costPerHour: Int = 3000,
    var description: String = "",
    var id: String = "",
    var ip: String = "",
    var isOpen: Boolean = false,
    var name: String = "",
    var thumbnail: String = "",
)

class ActiveParking(
    var activeParkingId: String = "",
    var start: Timestamp? = null
) : Location()

data class User(
    val activeParking: ActiveParking? = null,
    val name: String = ""
)