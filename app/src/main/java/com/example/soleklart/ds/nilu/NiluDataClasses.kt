package com.example.soleklart.ds.nilu

data class AirQuality(
    val id: Number?,
    val zone: String?,
    val municipality: String?,
    val area: String?,
    val station: String?,
    val type: String?,
    val eoi: String?,
    val component: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestep: Number?,
    val isVisible: Boolean?,
    val unit: String?,
    val values: List<Value>?
)

// class containing values of all times
data class Value(
    val fromTime: String?,
    val toTime: String?,
    val value: Number?,
    val qualityControlled: Boolean?,
    val index: Number?,
    val color: String?
)