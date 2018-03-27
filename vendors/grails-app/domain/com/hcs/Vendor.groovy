package com.hcs

class Vendor {

    String name

    static constraints = {
        name(nullable: false, maxSize: 40 )
    }
}
