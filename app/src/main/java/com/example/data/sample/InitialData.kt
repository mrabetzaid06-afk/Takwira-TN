package com.example.data.sample

import com.example.data.db.ChatMessageEntity
import com.example.data.db.MatchPostEntity
import com.example.data.db.NotificationEntity
import com.example.data.db.PitchEntity
import com.example.data.db.PlayerStatsEntity

object InitialData {

    val SAMPLE_PITCHES = listOf(
        PitchEntity(
            id = 1,
            name = "Complexe Sportif El Menzah",
            city = "Tunis",
            address = "Cité Olympique, El Menzah 1",
            pitchType = "5v5",
            surface = "Synthétique FIFA 2★",
            pricePerHour = 80.0,
            rating = 4.8,
            amenities = "Éclairage LED,Vestiaires,Douches Chaudes,Cafétéria,Parking Sécurisé,Chrono",
            managerPhone = "+216 98 123 456",
            isIndoor = false
        ),
        PitchEntity(
            id = 2,
            name = "Urban Soccer La Marsa",
            city = "Tunis",
            address = "Gammarth Express, La Marsa",
            pitchType = "5v5",
            surface = "Couvert Indoor Pro",
            pricePerHour = 100.0,
            rating = 4.9,
            amenities = "Climatisation,Gazon Synthétique Haut de Gamme,Café Lounge,Ecran Récap Match,Vestiaires Luxe",
            managerPhone = "+216 22 888 999",
            isIndoor = true
        ),
        PitchEntity(
            id = 3,
            name = "Club Arena Lac 2",
            city = "Tunis",
            address = "Les Berges du Lac 2, Tunis",
            pitchType = "7v7",
            surface = "Gazon Synthétique Dernier Cri",
            pricePerHour = 120.0,
            rating = 4.7,
            amenities = "Tribunes,Eclairage Nocturne Pro,Parking,Restaurant,Ballons Inclus",
            managerPhone = "+216 55 443 322",
            isIndoor = false
        ),
        PitchEntity(
            id = 4,
            name = "Kantaoui Foot Center",
            city = "Sousse",
            address = "Zone Touristique El Kantaoui",
            pitchType = "5v5",
            surface = "Synthétique Premium",
            pricePerHour = 70.0,
            rating = 4.6,
            amenities = "Vestiaires,Eclairage,Cafétéria Vue Terrain,Chrono Géant",
            managerPhone = "+216 97 111 222",
            isIndoor = false
        ),
        PitchEntity(
            id = 5,
            name = "Complexe Sportif Sousse Ville",
            city = "Sousse",
            address = "Avenue Léopold Senghor, Sousse",
            pitchType = "7v7",
            surface = "Gazon Naturel Traité",
            pricePerHour = 90.0,
            rating = 4.5,
            amenities = "Chassures Crampons Dispo,Arrosage Automatique,Parking,Douches",
            managerPhone = "+216 20 333 444",
            isIndoor = false
        ),
        PitchEntity(
            id = 6,
            name = "Sfax Takwira Club",
            city = "Sfax",
            address = "Route de Teniour Km 4",
            pitchType = "5v5",
            surface = "Synthétique FIFA 2★",
            pricePerHour = 65.0,
            rating = 4.7,
            amenities = "Vestiaires,Eclairage LED,Jus Frais & Buvette,Parking Gratuit",
            managerPhone = "+216 74 555 666",
            isIndoor = false
        ),
        PitchEntity(
            id = 7,
            name = "Hammamet Beach Soccer Stadium",
            city = "Nabeul",
            address = "Route Touristique Hammamet Sud",
            pitchType = "5v5",
            surface = "Couvert Indoor Pro",
            pricePerHour = 85.0,
            rating = 4.8,
            amenities = "Climatisation,Chrono digital,Caméra d'enregistrement match,Lounge VIP",
            managerPhone = "+216 98 777 888",
            isIndoor = true
        ),
        PitchEntity(
            id = 8,
            name = "Bizerte Arena Nord",
            city = "Bizerte",
            address = "Corniche de Bizerte",
            pitchType = "7v7",
            surface = "Synthétique Pro",
            pricePerHour = 75.0,
            rating = 4.6,
            amenities = "Vue sur Mer,Eclairage Nocturne,Vestiaires,Douches,Café",
            managerPhone = "+216 93 112 334",
            isIndoor = false
        )
    )

    val INITIAL_PLAYER_STATS = PlayerStatsEntity(
        id = 1,
        name = "Youssef Msakni",
        phone = "+216 98 765 432",
        favoriteClub = "Espérance de Tunis",
        position = "Attaquant / Milieu Offensif",
        matchesPlayed = 28,
        goals = 34,
        assists = 19,
        mvps = 8,
        winRatePercent = 78,
        fairPlayRating = 4.9f,
        badges = "Sniper du Tir,Buteur Né,Capitaine Infatigable,Master Takwira"
    )

    val SAMPLE_MATCH_POSTS = listOf(
        MatchPostEntity(
            id = 1,
            title = "Match 5v5 ce soir El Menzah - Cherche 2 Joueurs!",
            pitchName = "Complexe Sportif El Menzah",
            city = "Tunis",
            dateTime = "Ce Soir 20:00 - 21:00",
            requiredPlayers = 2,
            currentPlayers = 8,
            organizerName = "Kharroubi Ahmed",
            organizerPhone = "+216 52 111 222",
            notes = "Niveau intermédiaire, ambiance fraternelle! Partis 5 TND/joueur.",
            isFull = false
        ),
        MatchPostEntity(
            id = 2,
            title = "Takwira 7v7 Lac 2 - Gardien urgent",
            pitchName = "Club Arena Lac 2",
            city = "Tunis",
            dateTime = "Demain 19:00 - 20:30",
            requiredPlayers = 1,
            currentPlayers = 13,
            organizerName = "Ben Ali Sami",
            organizerPhone = "+216 98 333 444",
            notes = "On cherche un bon gardien pour fermer l'effectif 7v7!",
            isFull = false
        ),
        MatchPostEntity(
            id = 3,
            title = "Takwira Kantaoui Sousse - 5v5 Weekend",
            pitchName = "Kantaoui Foot Center",
            city = "Sousse",
            dateTime = "Samedi 21:00 - 22:00",
            requiredPlayers = 3,
            currentPlayers = 7,
            organizerName = "Gharbi Malek",
            organizerPhone = "+216 26 555 777",
            notes = "Terrain synthétique tout neuf. Buvette sur place.",
            isFull = false
        )
    )

    val SAMPLE_CHAT_MESSAGES = listOf(
        ChatMessageEntity(
            id = 1,
            matchGroup = "General",
            senderName = "Sami B.",
            senderRole = "Capitaine",
            message = "Aychkom chabab! Chkoun dispo pour une Takwira 5v5 ce soir à El Menzah?",
            timestamp = "18:20",
            isMine = false
        ),
        ChatMessageEntity(
            id = 2,
            matchGroup = "General",
            senderName = "Mohamed V.",
            senderRole = "Joueur",
            message = "Moi dispo! J'amène mon dossard rouge.",
            timestamp = "18:22",
            isMine = false
        ),
        ChatMessageEntity(
            id = 3,
            matchGroup = "General",
            senderName = "Youssef Msakni",
            senderRole = "Attaquant",
            message = "Je viens de réserver le terrain via Takwira TN, tout est réglé par D17!",
            timestamp = "18:25",
            isMine = true
        ),
        ChatMessageEntity(
            id = 4,
            matchGroup = "General",
            senderName = "Gérant El Menzah",
            senderRole = "Gérant",
            message = "Bien reçu votre réservation #TKW-8931! Terrain 1 éclairé et chasubles préparées. Bon match!",
            timestamp = "18:27",
            isMine = false
        )
    )

    val SAMPLE_NOTIFICATIONS = listOf(
        NotificationEntity(
            id = 1,
            title = "Réservation Confirmée (SMS)",
            body = "Takwira TN: Terrain El Menzah 5v5 le 26/07 à 20h00 confirmé. Code #TKW-8931. Paiement D17 validé (80 TND). Tél Gérant: +216 98 123 456.",
            timestamp = "Il y a 10 min",
            type = "SMS",
            isRead = false
        ),
        NotificationEntity(
            id = 2,
            title = "Alerte Dernière Minute",
            body = "Il manque 2 joueurs pour une Takwira 5v5 au Urban Soccer La Marsa ce soir à 21h! Rejoignez le groupe de chat.",
            timestamp = "Il y a 35 min",
            type = "PUSH",
            isRead = false
        ),
        NotificationEntity(
            id = 3,
            title = "Stats Joueur Mises à Jour",
            body = "Bravo! Vous avez été élu MVP du match hier à Arena Lac 2. +1 Trophée MVP ajouté à votre profil.",
            timestamp = "Hier",
            type = "PUSH",
            isRead = true
        )
    )
}
