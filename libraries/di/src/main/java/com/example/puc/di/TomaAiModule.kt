package com.example.puc.di

import androidx.navigation.NavHostController
import com.project.pos.auth.Auth
import com.project.pos.auth.FirebaseAuth
import com.project.pos.createmedicine.alarm.AlarmScheduler
import com.project.pos.createmedicine.alarm.AndroidAlarmScheduler
import com.project.pos.createmedicine.ui.CreateMedicineViewModel
import com.project.pos.data.api.repository.MedicineRepository
import com.project.pos.data.impl.repository.FirestoreMedicineRepository
import com.project.pos.home.ui.HomeScreenViewModel
import com.project.pos.navigation.DefaultNavigator
import com.project.pos.navigation.Navigator
import com.project.pos.onboarding.signin.SignInViewModel
import com.project.pos.onboarding.signup.SignUpViewModel
import com.project.pos.updatemedicine.ui.UpdateMedicineViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val authModule = module {
    single<Auth> { FirebaseAuth() }
}

val dataModule = module {
    single<MedicineRepository> { FirestoreMedicineRepository(get()) }
    single<AlarmScheduler> { AndroidAlarmScheduler(androidContext()) }
}

val navigationModule = module {
    factory<Navigator> { (navController: NavHostController) ->
        DefaultNavigator(navController)
    }
}

val viewModelModule = module {
    viewModel { (navigator: Navigator) -> SignInViewModel(get(), navigator) }
    viewModel { SignUpViewModel(get()) }
    viewModel { (navigator: Navigator) -> CreateMedicineViewModel(get(), navigator, get()) }
    viewModel { (navigator: Navigator) -> HomeScreenViewModel(get(), navigator, get(), get()) }
    viewModel { (medicineId: String, navigator: Navigator) ->
        UpdateMedicineViewModel(medicineId, get(), navigator, get())
    }
}

val tomaAiModules = listOf(
    authModule,
    dataModule,
    navigationModule,
    viewModelModule
)
