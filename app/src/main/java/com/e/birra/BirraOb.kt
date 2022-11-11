package com.e.birra

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
data  class BirraOb (
    val id: String,
    val name:String,
    val brewery_tipe:String,

    ):Parcelable
