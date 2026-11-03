package com.eco.cleanup.navigation

sealed class Screen(val route: String) {
    object Splash       : Screen("splash")
    object Beranda      : Screen("beranda")
    object Panduan      : Screen("panduan")
    object Pengaturan   : Screen("pengaturan")
    object PetaLevel    : Screen("peta_level")
    object Gameplay     : Screen("gameplay/{levelId}") {
        fun createRoute(levelId: Int) = "gameplay/$levelId"
    }
    object Hasil        : Screen("hasil/{levelId}/{isWin}/{skor}/{bintang}") {
        fun createRoute(levelId: Int, isWin: Boolean, skor: Int, bintang: Int) =
            "hasil/$levelId/$isWin/$skor/$bintang"
    }
}

