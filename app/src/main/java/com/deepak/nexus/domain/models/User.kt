package com.deepak.nexus.domain.models

// handle nullability at the API/mapping layer, not in UI.
// Do Not create variables like this val name: String? = null


data class User(
    val website: String,
    val address: Address,
    val phone: String,
    val name: String,
    val company: Company,
    val id: Int,
    val email: String,
    val username: String
)

data class Address(
    val zipcode: String,
    val geo: Geo,
    val suite: String,
    val city: String,
    val street: String
)

data class Geo(
    val lng: String,
    val lat: String
)

data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)

val dummyUser = listOf(
    User(
        id = 1,
        name = "John Doe",
        username = "johndoe",
        email = "john@example.com",
        phone = "1-770-736-8031",
        website = "https://johndoe.com",
        address = Address(
            street = "Kulas Light",
            suite = "Apt. 556",
            city = "Gwenborough",
            zipcode = "92998-3874",
            geo = Geo(
                lat = "-37.3159",
                lng = "81.1496"
            )
        ),
        company = Company(
            name = "Romaguera-Crona",
            catchPhrase = "Multi-layered client-server neural-net",
            bs = "harness real-time e-markets"
        )
    ),
    User(
        id = 2,
        name = "Jane Smith",
        username = "janesmith",
        email = "jane@example.com",
        phone = "010-692-6593",
        website = "https://janesmith.org",
        address = Address(
            street = "Victor Plains",
            suite = "Suite 879",
            city = "Wisokyburgh",
            zipcode = "51798-7054",
            geo = Geo(
                lat = "-43.9695",
                lng = "-34.4804"
            )
        ),
        company = Company(
            name = "Deckow-Crist",
            catchPhrase = "Proactive didactic contingency",
            bs = "synergize scalable supply-chains"
        )
    )
)

