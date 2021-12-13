package com.example.hyperxpress.service.listener

import com.example.hyperxpress.service.model.entity.java.User

interface OnitemClickListenerFirebase<T> {

    fun  onItemClick(contact: T)
}