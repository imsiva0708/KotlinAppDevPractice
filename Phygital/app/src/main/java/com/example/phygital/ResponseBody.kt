package com.example.phygital

data class ResponseBody(
    val message: String ?= null,
    val detail: String ?= null,
    val body: TagCreate ?= null
)
