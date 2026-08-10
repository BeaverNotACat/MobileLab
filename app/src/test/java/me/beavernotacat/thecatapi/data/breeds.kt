package me.beavernotacat.thecatapi.data

import me.beavernotacat.thecatapi.models.local.CatInfo

val abys = CatInfo(
    id = "abys",
    weight = "3 - 5", // Using the metric value from the JSON
    name = "Abyssinian",
    countryCode = "EG",
    description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
    lifeSpan = "14 - 15",
    indoor = 0,
    adaptability = 5,
    childFriendly = 3,
    dogFriendly = 4,
    socialNeeds = 5,
    strangerFriendly = 5,
    imageId = "0XYvRd7oD", // Mapped from reference_image_id
    catFriendly = null     // Not present in the provided JSON
)
val siamese = CatInfo(
    id = "siamese",
    name = "Siamese",
    weight = "4 - 6 kg",
    countryCode = "TH",
    description = "Known for their striking blue eyes and 'points' of color on their ears, face, and paws. They are highly intelligent, extremely vocal, and demand constant attention.",
    lifeSpan = "12 - 20 years",
    indoor = 1,
    adaptability = 5,
    childFriendly = 4,
    dogFriendly = 5,
    socialNeeds = 5,
    strangerFriendly = 5,
    imageId = "img_siamese_001",
    catFriendly = 4
)
val mainecoon = CatInfo(
    id = "mainecoon",
    name = "Maine Coon",
    weight = "5 - 11 kg",
    countryCode = "US",
    description = "Often called the 'gentle giant' of the cat world. They are one of the largest domesticated breeds, famous for their shaggy coats and tufted ears.",
    lifeSpan = "12 - 15 years",
    indoor = 0,
    adaptability = 5,
    childFriendly = 5,
    dogFriendly = 5,
    socialNeeds = 4,
    strangerFriendly = 5,
    imageId = "img_mainecoon_002",
    catFriendly = 5
)
val bengal = CatInfo(
    id = "bengal",
    name = "Bengal",
    weight = "4 - 8 kg",
    countryCode = "US",
    description = "Developed to look like wild cats such as leopards. They are incredibly energetic, athletic, and love to climb and even play in water.",
    lifeSpan = "12 - 16 years",
    indoor = 0,
    adaptability = 5,
    childFriendly = 4,
    dogFriendly = 5,
    socialNeeds = 5,
    strangerFriendly = 3,
    imageId = "img_bengal_003",
    catFriendly = 4
)
val persian = CatInfo(
    id = "persian",
    name = "Persian",
    weight = "3 - 7 kg",
    countryCode = "IR",
    description = "A quiet and sweet-tempered cat with a long, luxurious coat. They prefer calm environments and are perfectly content lounging on a sofa.",
    lifeSpan = "10 - 17 years",
    indoor = 1,
    adaptability = 2,
    childFriendly = 2,
    dogFriendly = 2,
    socialNeeds = 3,
    strangerFriendly = 2,
    imageId = "img_persian_004",
    catFriendly = 3
)
val ragdoll = CatInfo(
    id = "ragdoll",
    name = "Ragdoll",
    weight = "4 - 9 kg",
    countryCode = "US",
    description = "Named for their tendency to go limp like a ragdoll when picked up. They are exceptionally docile, affectionate, and follow their owners from room to room.",
    lifeSpan = "12 - 17 years",
    indoor = 1,
    adaptability = 5,
    childFriendly = 5,
    dogFriendly = 5,
    socialNeeds = 5,
    strangerFriendly = 4,
    imageId = "fail",
    catFriendly = 5
)

val breeds = listOf(
    siamese,
    mainecoon,
    bengal,
    persian,
    ragdoll
)
