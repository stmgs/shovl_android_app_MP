package com.shovl.shovl_android.data

import java.io.Serializable

data class Bidders(
    var user_id : String? = null,
    var name : String? = null,
    val price : Int?= null,
    val time : Int?= null,
    val rejected : Boolean? = false,
    val confirmed: Boolean? = false
) : Serializable{

    override fun equals(other: Any?): Boolean {

        return this.name==(other as Bidders).name
                && this.price == other.price
                && this.time == other.time
                && this.user_id == other.user_id
                && this.rejected == other.rejected
                && this.confirmed == other.confirmed




    }
}
