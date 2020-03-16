package com.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("merchant")
data class Merchant(
        @Id val id: Long? = null,
        var registeredName: String? = null,
        var doingBusinessAs: String? = null,
        var email: String,
        var password: String,
        var status: String? = null
) {
    constructor(id: Long) : this(
            id,
            "",
            "",
            "",
            "",
            "")
    {}
}