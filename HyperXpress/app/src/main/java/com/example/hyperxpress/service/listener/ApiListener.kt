package com.example.hyperxpress.service.listener

interface ApiListener<T> {
    fun onSuccess(model:T)
    fun onFailure(str:String)
}