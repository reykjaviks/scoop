package com.marjorie.scoop.home

import com.marjorie.scoop.message.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HomeController {
    @GetMapping
    fun home(): List<String> = listOf("Hello!", "Hello again!")

}