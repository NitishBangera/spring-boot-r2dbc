package com.r2dbc.model

import org.springframework.http.HttpStatus

data class LoginResponse(val status: HttpStatus = HttpStatus.OK, val message: String)