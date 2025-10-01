package iut.nantes

data class Movie(val name: String, val releaseDate: Int, val rating: Int, val languages: List<String>)

val MOVIES = listOf(
    Movie("The Dark Knight", 2008, 9, listOf("VO")),
    Movie("Inception", 2010, 8, listOf("VF")),
    Movie("My Little Pony: The Movie", 2017, 99, listOf("VO", "VFF"))
)

val FR_FR = mapOf(
    "The Dark Knight" to "Le Chevalier Noir",
    "Inception" to "Inception",
    "My Little Pony" to "My Little Pony, le film"
)

val FR_CA = mapOf(
    "The Dark Knight" to "Le Chevalier Noir",
    "Inception" to "Origine ",
    "My Little Pony" to "Mon petit poney, le film"
)