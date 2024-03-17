package com.example.vkapp

import java.util.UUID

data class WebSiteConfig(
    val id: UUID,
    val webSite: String,
    val login: String,
    val password: String,
    val thumbnail: String
)