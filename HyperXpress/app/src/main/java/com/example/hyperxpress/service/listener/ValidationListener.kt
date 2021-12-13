package com.example.hyperxpress.service.listener

class ValidationListener (str:String = ""){
    private var mStatus = true
    private var mMessage = ""

    init {
        if (str != ""){
            mStatus = false
            mMessage = str
        }
    }
    fun sucess() = mStatus
    fun failure() = mMessage

}