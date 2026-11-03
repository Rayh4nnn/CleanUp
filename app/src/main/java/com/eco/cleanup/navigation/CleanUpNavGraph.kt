package com.eco.cleanup.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eco.cleanup.data.local.CleanUpDatabase
import com.eco.cleanup.data.repository.GameRepository
import com.eco.cleanup.data.repository.SettingsRepository
import com.eco.cleanup.ui.screen.BerandaScreen
import com.eco.cleanup.ui.screen.GameplayScreen
import com.eco.cleanup.ui.screen.HasilScreen
import com.eco.cleanup.ui.screen.PanduanScreen
import com.eco.cleanup.ui.screen.PengaturanScreen
import com.eco.cleanup.ui.screen.PetaLevelScreen
import com.eco.cleanup.ui.screen.SplashScreen
import com.eco.cleanup.ui.viewmodel.GameplayViewModel
import com.eco.cleanup.ui.viewmodel.GameplayViewModelFactory
import com.eco.cleanup.ui.viewmodel.HasilViewModel
import com.eco.cleanup.ui.viewmodel.HasilViewModelFactory
import com.eco.cleanup.ui.viewmodel.PetaLevelViewModel
import com.eco.cleanup.ui.viewmodel.PetaLevelViewModelFactory
import com.eco.cleanup.ui.viewmodel.PengaturanViewModel
import com.eco.cleanup.ui.viewmodel.PengaturanViewModelFactory

@Composable
fun CleanUpNavGraph() {
    val navController   = rememberNavController()
    val context         = LocalContext.current
    val application     = context.applicationContext as Application
    val db              = CleanUpDatabase.getInstance(context)
    val gameRepo        = GameRepository(db)
    val settingsRepo    = SettingsRepository(context)

    NavHost(
        navController  = navController,
        startDestination = Screen.Splash.route
    ) {

        // ── Splash ────────────────────────────────────────────────
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Beranda.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Beranda ───────────────────────────────────────────────
        composable(Screen.Beranda.route) {
            BerandaScreen(
                onPlayClick       = { navController.navigate(Screen.PetaLevel.route) },
                onPanduanClick    = { navController.navigate(Screen.Panduan.route) },
                onPengaturanClick = { navController.navigate(Screen.Pengaturan.route) }
            )
        }

        // ── Panduan ───────────────────────────────────────────────
        composable(Screen.Panduan.route) {
            PanduanScreen(
                onKembali = { navController.popBackStack() }
            )
        }

        // ── Pengaturan ────────────────────────────────────────────
        composable(Screen.Pengaturan.route) {
            val vm: PengaturanViewModel = viewModel(
                factory = PengaturanViewModelFactory(settingsRepo, context)
            )
            PengaturanScreen(
                viewModel = vm,
                onKembali = { navController.popBackStack() }
            )
        }

        // ── Peta Level ────────────────────────────────────────────
        composable(Screen.PetaLevel.route) {
            val vm: PetaLevelViewModel = viewModel(
                factory = PetaLevelViewModelFactory(gameRepo)
            )
            PetaLevelScreen(
                viewModel = vm,
                onLevelSelected = { levelId ->
                    navController.navigate(Screen.Gameplay.createRoute(levelId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Gameplay ──────────────────────────────────────────────
        composable(
            route = Screen.Gameplay.route,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            val vm: GameplayViewModel = viewModel(
                factory = GameplayViewModelFactory(gameRepo, settingsRepo, context, levelId)
            )
            GameplayScreen(
                viewModel = vm,
                onHasil   = { isWin, skor, bintang ->
                    navController.navigate(
                        Screen.Hasil.createRoute(levelId, isWin, skor, bintang)
                    ) {
                        popUpTo(Screen.Gameplay.route) { inclusive = true }
                    }
                },
                onBackToPeta = {
                    navController.navigate(Screen.PetaLevel.route) {
                        popUpTo(Screen.PetaLevel.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Hasil ─────────────────────────────────────────────────
        composable(
            route = Screen.Hasil.route,
            arguments = listOf(
                navArgument("levelId") { type = NavType.IntType },
                navArgument("isWin")   { type = NavType.BoolType },
                navArgument("skor")    { type = NavType.IntType },
                navArgument("bintang") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val levelId  = backStackEntry.arguments?.getInt("levelId")   ?: 1
            val isWin    = backStackEntry.arguments?.getBoolean("isWin") ?: false
            val skor     = backStackEntry.arguments?.getInt("skor")      ?: 0
            val bintang  = backStackEntry.arguments?.getInt("bintang")   ?: 0
            val vm: HasilViewModel = viewModel(
                factory = HasilViewModelFactory(gameRepo, levelId, isWin, skor, bintang)
            )
            HasilScreen(
                viewModel    = vm,
                onCobaLagi   = {
                    navController.navigate(Screen.Gameplay.createRoute(levelId)) {
                        popUpTo(Screen.PetaLevel.route)
                    }
                },
                onPetaLevel  = {
                    navController.navigate(Screen.PetaLevel.route) {
                        popUpTo(Screen.PetaLevel.route) { inclusive = true }
                    }
                },
                onNextLevel  = { nextId ->
                    navController.navigate(Screen.Gameplay.createRoute(nextId)) {
                        popUpTo(Screen.PetaLevel.route)
                    }
                }
            )
        }
    }
}

