package org.quickness.dynamics.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta de colores completa para MaterialTheme en el tema oscuro
val primary: Color = Color(0xff000000) // #0d9b03

// Color del contenido "sobre" el primario (Texto/Iconos)
// Busca alto contraste. Suele ser Tono 100 (blanco) o Tono 0 (negro). Para #0d9b03, es blanco.
val onPrimary: Color = Color(0xffffffff) // #ffffff

// Un contenedor con un tono más claro/suave del primario.
// Usado para elementos que necesitan menos énfasis que el primario. Suele ser Tono 90.
val primaryContainer: Color = Color(0xffb9fabb) // #b9fabb  (Valor aproximado generado por M3)

// Color del contenido "sobre" el primaryContainer.
// Busca alto contraste con primaryContainer. Suele ser Tono 10.
val onPrimaryContainer: Color = Color(0xff002101) // #002101 (Valor aproximado generado por M3)

// El color primario como aparecería en un contexto inverso (p.ej., en un SnackBar oscuro).
// Corresponde al rol 'primary' pero del tema opuesto (oscuro en este caso). Suele ser Tono 80.
val inversePrimary: Color = Color(0xff9edda1) // #9edda1 (Valor aproximado generado por M3)

val secondary: Color = Color(0xff000000) // Un tono relacionado pero más apagado
val onSecondary: Color = Color(0xFFfefefe) // Mantenido como está
val secondaryContainer: Color = Color(0xFF004d51) // Tono oscuro relacionado con Secondary
val onSecondaryContainer: Color = Color(0xFFcfe8e8) // Contraste claro para contenedores secundarios

val tertiary: Color = Color(0xff000000) // Mantenido como está
val onTertiary: Color = Color(0xffffffff) // Buen contraste con blanco, tono neutro oscuro
val tertiaryContainer: Color = Color(0xFFd9d9d9) // Tono gris claro para contenedores terciarios
val onTertiaryContainer: Color = Color(0xFF2a2a2a) // Contraste oscuro para contenedores claros

val background: Color = Color(0xffffffff)
val onBackground: Color = Color(0xfff5f6f8)

// --- Recomendaciones para Superficies (Tema Claro) ---

// Surface: Ligeramente distinto del fondo, con un toque de verde muy sutil.
// Ideal para Cards, Menus, Sheets.
val surface: Color = Color(0xFFeaeaf3) // Blanco con un tinte verde muy, muy claro

// OnSurface: Texto/iconos sobre 'surface'. Necesita alto contraste.
// Un gris oscuro casi negro, derivado de tonos verdes.
val onSurface: Color = Color(0xFF191C19)

// SurfaceVariant: Una variante para elementos como outlines, dividers, chip borders.
// Un gris verdoso claro, notablemente diferente de 'surface'.
val surfaceVariant: Color = Color(0xFFDDE5D9)

// OnSurfaceVariant: Texto/iconos sobre 'surfaceVariant'.
// Un gris verdoso más oscuro para buen contraste sobre 'surfaceVariant'.
val onSurfaceVariant: Color = Color(0xFF414941)

// SurfaceTint: Usado para indicar elevación, usualmente el color primario.
val surfaceTint: Color = primary // Color(0xff0d9b03)

// InverseSurface: Para elementos que necesitan destacar (ej. Snackbars), imita la superficie oscura.
// Un gris verdoso oscuro.
val inverseSurface: Color = Color(0xFF2E312D)

// InverseOnSurface: Texto/iconos sobre 'inverseSurface'. Necesita alto contraste.
// Un blanco roto con tinte verde claro.
val inverseOnSurface: Color = Color(0xFFF0F1EC)
val error: Color = Color(0xFFff3131) // Mantenido como está
val onError: Color = Color(0xFFffffff) // Contraste claro para errores
val errorContainer: Color = Color(0xFF93000a) // Contenedor oscuro para errores
val onErrorContainer: Color = Color(0xFFffd6d6) // Contraste claro para contenedores de error

val outline: Color = Color(0xFF525252) // Tono gris intermedio para bordes
val outlineVariant: Color = Color(0xFF3a3a3a) // Variante más oscura del outline
val scrim: Color = Color(0xFF000000) // Fondo transparente/oscuro

val surfaceBright: Color = Color(0xffadd09d) // Ligeramente más claro que el Surface
val surfaceContainer: Color = Color(0xFF1c1c1c) // Contenedor similar a Surface pero algo más claro
val surfaceContainerHigh: Color = Color(0xFF2e2e2e) // Contenedor más destacado que SurfaceContainer
val surfaceContainerHighest: Color = Color(0xFF383838) // La variante más clara de los contenedores
val surfaceContainerLow: Color = Color(0xFF141414) // Contenedor más oscuro que SurfaceContainer
val surfaceContainerLowest: Color = Color(0xFF101010) // Contenedor más oscuro
val surfaceDim: Color = Color(0xFF181818) // Superficie tenue, más cerca del negro

val Success = Color(0xFF00FF00)
