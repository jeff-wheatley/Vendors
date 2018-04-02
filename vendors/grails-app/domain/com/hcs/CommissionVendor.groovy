package com.hcs

class CommissionVendor {

    String name

    static constraints = {
        name(nullable: false, maxSize: 40 )
    }

String toString() { name }

}
