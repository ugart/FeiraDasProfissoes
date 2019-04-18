package com.example.feiradasprofissoes.modules

class UserData {
    var userUiD: String = ""
    var name: String? = ""
    var email: String? = ""
    var cityName: String? = ""
    var schoolName: String? = ""
    var schoolType: String? = null
    var isUserLoggedIn: Boolean? = false
    var isCheckBoxChecked: Boolean? = false

    constructor (
        userUiD: String,
        name: String?,
        email: String?,
        cityName: String?,
        schoolName: String?,
        schoolType: String?,
        isUserLoggedIn: Boolean,
        isCheckBoxChecked: Boolean
    ) {
        this.userUiD = userUiD
        this.name = name
        this.email = email
        this.cityName = cityName
        this.schoolName = schoolName
        this.schoolType = schoolType
        this.isUserLoggedIn = isUserLoggedIn
        this.isCheckBoxChecked = isCheckBoxChecked
    }

}