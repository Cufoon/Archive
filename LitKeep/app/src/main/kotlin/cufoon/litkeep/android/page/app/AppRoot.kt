package cufoon.litkeep.android.page.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import cufoon.litkeep.android.component.DialogGlobal
import cufoon.litkeep.android.page.record.AddRecordPage
import cufoon.litkeep.android.page.kind.ManageBillKindPage
import cufoon.litkeep.android.store.MainViewModel
import cufoon.litkeep.android.theme.LitKeepTheme
import cufoon.litkeep.android.util.createContext
import cufoon.litkeep.android.util.useContext

val AppNavContext = createContext<NavHostController>()

@Composable
fun rememberAppNavController(): (NavHostController.() -> Unit) -> Boolean {
    val anc = useContext(AppNavContext)
    return fun(block: NavHostController.() -> Unit): Boolean {
        anc?.let {
            it.block()
            return true
        }
        return false
    }
}

@OptIn(ExperimentalAnimationApi::class)
val AppRoot = @Composable {
    val viewModel: MainViewModel = viewModel()
    val navController = rememberAnimatedNavController()

    LaunchedEffect(true) {
        viewModel.launchAppReady = true
    }

    AppNavContext.Provider(navController) {
        LitKeepTheme {
            DialogGlobal {
                if (viewModel.tokenChecked) {
                    NavHost(
                        navController = navController,
                        startDestination = if (viewModel.tokenVerified) "record_kind_manage" else "login",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("index") { AppMain() }
                        composable("login") { LoginPage() }
                        composable("record_add") { AddRecordPage() }
                        composable("record_kind_manage") { ManageBillKindPage() }
                    }
                }
            }
        }
    }
}
