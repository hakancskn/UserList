package com.example.userlist.model

data class Person(val id: Int, val fullName: String){
    override fun toString(): String {
        return fullName.plus("(").plus(id.toString()).plus(")")
    }
}
