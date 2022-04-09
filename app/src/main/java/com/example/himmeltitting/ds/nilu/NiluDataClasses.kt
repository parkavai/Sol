package com.example.himmeltitting.ds.nilu

data class AirQuality(val id: Number?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val type: String?, val eoi: String?, val component: String?, val latitude: Double?, val longitude: Double?, val timestep: Number?, val isVisible: Boolean?, val unit: String?, val values: List<Value>?)

// class containing values of all times
data class Value(val fromTime: String?, val toTime: String?, val value: Number?, val qualityControlled: Boolean?, val index: Number?, val color: String?)

// compact class for easy return of values
data class CollectiveAirQuality(val type: String, val value: Double)

// Har laget en annen klasse som matcher api respons fra historisk data
//data class LuftKvalitet (val id: String?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val eoi: String?, val type: String?, val component: String?, val fromTime: String?, val toTime: String?, val value: Double?, val unit: String?, val latitude: Double?, val longitude: Double?, val timestep: Int?, val index: Int?, val color: String?, val isValid: Boolean?, val isVisible: Boolean?)