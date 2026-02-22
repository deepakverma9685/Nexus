package com.deepak.nexus.data.mappers

import com.deepak.nexus.data.dto.AddressDto
import com.deepak.nexus.data.dto.CompanyDto
import com.deepak.nexus.data.dto.GeoDto
import com.deepak.nexus.data.dto.UserDto
import com.deepak.nexus.domain.models.Address
import com.deepak.nexus.domain.models.Company
import com.deepak.nexus.domain.models.Geo
import com.deepak.nexus.domain.models.User


fun UserDto.toDomain(): User {
    return User(
        id = this.id ?: 0,
        name = this.name ?: "",
        username = this.username ?: "",
        email = this.email ?: "",
        phone = this.phone ?: "",
        website = this.website ?: "",
        address = this.addressDto?.toDomain() ?: Address(
            street = "",
            suite = "",
            city = "",
            zipcode = "",
            geo = Geo(lat = "", lng = "")
        ),
        company = this.companyDto?.toDomain() ?: Company(
            name = "",
            catchPhrase = "",
            bs = ""
        )
    )
}

fun AddressDto.toDomain(): Address {
    return Address(
        street = this.street ?: "",
        suite = this.suite ?: "",
        city = this.city ?: "",
        zipcode = this.zipcode ?: "",
        geo = this.geoDto?.toDomain() ?: Geo(lat = "", lng = "")
    )
}

fun GeoDto.toDomain(): Geo {
    return Geo(
        lat = this.lat ?: "",
        lng = this.lng ?: ""
    )
}

fun CompanyDto.toDomain(): Company {
    return Company(
        name = this.name ?: "",
        catchPhrase = this.catchPhrase ?: "",
        bs = this.bs ?: ""
    )
}