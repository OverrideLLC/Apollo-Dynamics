package com.override.data.db

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.override.data.utils.enum.AttendanceStatus
import kotlinx.datetime.LocalDate

class Converters {

    // --- LocalDate Converter (a Int/EpochDays) ---
    @TypeConverter
    fun fromEpochDays(value: Int?): LocalDate? {
        return value?.let { LocalDate.fromEpochDays(it) }
    }

    @TypeConverter
    fun localDateToEpochDays(date: LocalDate?): Int? {
        return date?.toEpochDays()
    }

    // --- Color Converter (a Int/ARGB) ---
    @TypeConverter
    fun fromArgb(value: Int?): Color? {
        return value?.let { Color(it) }
    }

    @TypeConverter
    fun colorToArgb(color: Color?): Int? {
        // Convertimos el Color de Compose a su representación Int ARGB
        // Nota: androidx.compose.ui.graphics.Color ya tiene métodos para esto,
        // pero necesitamos manejar la conversión explícita para Room.
        // La forma más directa podría ser a través de su valor UInt y luego a Int.
        // O si estás seguro que no usas colores fuera del sRGB, puedes usar toArgb().
        // Vamos a usar un método seguro asumiendo que necesitas el Int.
        // IMPORTANTE: Revisa cómo tu versión de Compose maneja esto.
        // A partir de ciertas versiones, simplemente `color?.value?.toInt()` puede funcionar
        // o podrías necesitar `color?.toArgb()` si está disponible y es adecuado.
        // Aquí usamos una conversión común:
        return color?.let {
             // Convertir Color (Float 0..1) a Int (0..255) para cada canal ARGB
            val red = (it.red * 255).toInt()
            val green = (it.green * 255).toInt()
            val blue = (it.blue * 255).toInt()
            val alpha = (it.alpha * 255).toInt()
            (alpha shl 24) or (red shl 16) or (green shl 8) or blue
        }
    }


    // --- AttendanceStatus Converter (a String) ---
    @TypeConverter
    fun fromStatusName(value: String?): AttendanceStatus? {
        // Usamos `entries.find` para seguridad, en lugar de `valueOf` que lanza excepción
        return value?.let { AttendanceStatus.entries.find { status -> status.name == it } ?: AttendanceStatus.UNKNOWN }
    }

    @TypeConverter
    fun attendanceStatusToName(status: AttendanceStatus?): String? {
        return status?.name
    }
}