package com.feature.desktop.home.tools.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.feature.desktop.home.tools.ui.screens.add_class.AddClassScreen
import com.feature.desktop.home.tools.ui.screens.add_student.AddStudentScreen
import com.feature.desktop.home.tools.ui.screens.take_attendees.TakeAttendeesScreen
import com.feature.desktop.home.tools.utils.routes.ToolRoute

/**
 * Componente Composable que gestiona la navegación dentro de la sección de herramientas.
 *
 * Este componente define el grafo de navegación utilizando [NavHost] de Jetpack Compose
 * y gestiona las diferentes pantallas de la sección de herramientas.
 *
 * @param route La ruta inicial a la que debe navegar el [NavHost] cuando se inicia.
 */
@Composable
internal fun NavigationTools(
    route: String
) {
    // Crea y recuerda una instancia de NavController para gestionar la navegación.
    val navController = rememberNavController()
    // Define el host de navegación.
    NavHost(
        navController = navController,
        // Define la ruta de inicio del grafo de navegación.
        startDestination = route
    ) {
        // Define una ruta de navegación para la pantalla de toma de asistencia.
        composable(route = ToolRoute.TakeAttendees.route) {
            // Muestra la pantalla de toma de asistencia.
            TakeAttendeesScreen()
        }
        // Define una ruta de navegación para la pantalla de agregar clase.
        composable(route = ToolRoute.AddClass.route) {
            // Muestra la pantalla de agregar clase.
            AddClassScreen(onCompletion = {})
        }
        composable(route = ToolRoute.AddStudent.route) {
            AddStudentScreen(onCompletion = {})
        }
    }
}