package com.sleetworks.serenity.android.newone.data.models.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

 open class BaseResponse : Serializable
 {

     @SerializedName("_rev")
     var revision: String?=""
     var header: Header? = null
     var tags: ArrayList<String>? = arrayListOf()
 }
