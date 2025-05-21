package com.feature.desktop.home.services.utils

import com.feature.desktop.home.services.classroom.services.report.CourseReport
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.File

class PdfGenerator {

    // Define algunas constantes para el diseño de la tabla
    // Define some constants for table layout
    private val MARGIN = 50f // Margen de la página - Page margin
    private val FONT_SIZE_TITLE = 12f
    private val FONT_SIZE_HEADER = 10f
    private val FONT_SIZE_CELL = 8f
    private val ROW_HEIGHT = 20f // Altura de cada fila de la tabla - Height of each table row
    private val CELL_PADDING = 5f // Espaciado dentro de las celdas - Padding within cells

    fun generateReportPdf(reportData: CourseReport, filePath: String): Boolean {
        var document: PDDocument? = null
        return try {
            document = PDDocument()
            val page = PDPage(PDRectangle.A4) // Usar tamaño A4 - Use A4 size
            document.addPage(page)

            val contentStream = PDPageContentStream(document, page)

            // Obtener dimensiones de la página
            // Get page dimensions
            val pageHeight = page.mediaBox.height
            val pageWidth = page.mediaBox.width

            // Posición inicial para el contenido
            // Initial position for content
            var yPosition = pageHeight - MARGIN

            // Añadir título del reporte
            // Add report title
            contentStream.beginText()
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_TITLE)
            contentStream.newLineAtOffset(MARGIN, yPosition)
            contentStream.showText("Reporte del Curso: ${reportData.courseName ?: reportData.courseId}")
            contentStream.endText()

            yPosition -= FONT_SIZE_TITLE * 2 // Espacio después del título - Space after title

            // --- Preparar datos para la tabla ---
            // --- Prepare data for the table ---

            // Obtener todos los títulos de trabajos de clase únicos
            // Get all unique coursework titles
            val courseworkTitles = reportData.studentReports
                .flatMap { it.submissions }
                .map { it.courseworkTitle }
                .distinct()
                .sorted() // Opcional: ordenar los títulos - Optional: sort titles

            // Determinar el número de columnas (Nombre del Alumno + trabajos de clase)
            // Determine the number of columns (Student Name + coursework)
            val numberOfColumns = 1 + courseworkTitles.size

            // Calcular anchos de columna aproximados
            // Calculate approximate column widths
            // Un ancho fijo para el nombre del alumno y distribuir el resto entre los trabajos de clase
            // A fixed width for student name and distribute the rest among coursework
            val studentNameColumnWidth = 150f // Ancho fijo para el nombre - Fixed width for name
            val availableWidthForCoursework = pageWidth - (2 * MARGIN) - studentNameColumnWidth
            val courseworkColumnWidth = if (courseworkTitles.isNotEmpty()) {
                availableWidthForCoursework / courseworkTitles.size
            } else {
                availableWidthForCoursework // Si no hay trabajos, esta columna no se usará realmente para trabajos
            }

            // --- Dibujar la tabla y añadir contenido ---
            // --- Draw the table and add content ---

            // Posición inicial de la tabla
            // Initial position of the table
            val tableTopY = yPosition - ROW_HEIGHT
            var currentY = tableTopY

            // Dibujar la fila de encabezados
            // Draw the header row
            contentStream.setLineWidth(1f) // Grosor de la línea - Line thickness

            // Dibujar celda de encabezado "Alumno"
            // Draw "Student" header cell
            contentStream.addRect(MARGIN, currentY - ROW_HEIGHT, studentNameColumnWidth, ROW_HEIGHT)
            contentStream.stroke()

            // Añadir texto "Alumno"
            // Add "Student" text
            contentStream.beginText()
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER)
            contentStream.newLineAtOffset(
                MARGIN + CELL_PADDING,
                currentY - ROW_HEIGHT + CELL_PADDING
            )
            contentStream.showText("Alumno")
            contentStream.endText()

            var currentX = MARGIN + studentNameColumnWidth

            // Dibujar celdas de encabezado de trabajos de clase
            // Draw coursework header cells
            courseworkTitles.forEach { title ->
                contentStream.addRect(
                    currentX,
                    currentY - ROW_HEIGHT,
                    courseworkColumnWidth,
                    ROW_HEIGHT
                )
                contentStream.stroke()

                // Añadir texto del título del trabajo (puede necesitar truncamiento o ajuste de fuente si es largo)
                // Add coursework title text (may need truncation or font adjustment if long)
                contentStream.beginText()
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER)
                contentStream.newLineAtOffset(
                    currentX + CELL_PADDING,
                    currentY - ROW_HEIGHT + CELL_PADDING
                )
                // Simple truncamiento si el texto es demasiado largo
                // Simple truncation if text is too long
                val truncatedTitle =
                    if (title.length > 20) "${title.substring(0, 17)}..." else title
                contentStream.showText(truncatedTitle)
                contentStream.endText()

                currentX += courseworkColumnWidth
            }

            currentY -= ROW_HEIGHT // Mover a la siguiente fila (primera fila de datos) - Move to the next row (first data row)

            // Dibujar filas de datos de estudiantes
            // Draw student data rows
            reportData.studentReports.forEach { studentReport ->
                // Dibujar celda del nombre del alumno
                // Draw student name cell
                contentStream.addRect(
                    MARGIN,
                    currentY - ROW_HEIGHT,
                    studentNameColumnWidth,
                    ROW_HEIGHT
                )
                contentStream.stroke()

                // Añadir texto del nombre del alumno
                // Add student name text
                contentStream.beginText()
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_CELL)
                contentStream.newLineAtOffset(
                    MARGIN + CELL_PADDING,
                    currentY - ROW_HEIGHT + CELL_PADDING
                )
                val studentDisplayName =
                    studentReport.studentName ?: studentReport.studentEmail ?: "Desconocido"
                // Simple truncamiento si el nombre es demasiado largo
                // Simple truncation if the name is too long
                val truncatedStudentName = if (studentDisplayName.length > 25) "${
                    studentDisplayName.substring(
                        0,
                        22
                    )
                }..." else studentDisplayName
                contentStream.showText(truncatedStudentName)
                contentStream.endText()

                currentX = MARGIN + studentNameColumnWidth

                // Dibujar celdas de calificación para cada trabajo de clase
                // Draw grade cells for each coursework
                courseworkTitles.forEach { courseworkTitle ->
                    contentStream.addRect(
                        currentX,
                        currentY - ROW_HEIGHT,
                        courseworkColumnWidth,
                        ROW_HEIGHT
                    )
                    contentStream.stroke()

                    // Encontrar la calificación para este estudiante y trabajo de clase
                    // Find the grade for this student and coursework
                    val submission =
                        studentReport.submissions.find { it.courseworkTitle == courseworkTitle }
                    val gradeText = when {
                        submission == null -> "-" // No submission
                        submission.assignedGrade != null && submission.maxPoints != null -> "${submission.assignedGrade}/${submission.maxPoints}"
                        submission.assignedGrade != null -> submission.assignedGrade.toString()
                        submission.submissionState == "TURNED_IN" -> "Entregado" // Or some other indicator
                        else -> "" // Other states like CREATED, RECLAIMED_BY_STUDENT
                    }

                    // Añadir texto de la calificación
                    // Add grade text
                    contentStream.beginText()
                    contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_CELL)
                    // Centrar el texto en la celda (aproximado)
                    // Center text in the cell (approximate)
                    val textWidth =
                        PDType1Font.HELVETICA.getStringWidth(gradeText) / 1000 * FONT_SIZE_CELL
                    val textX = currentX + (courseworkColumnWidth - textWidth) / 2
                    contentStream.newLineAtOffset(textX, currentY - ROW_HEIGHT + CELL_PADDING)
                    contentStream.showText(gradeText)
                    contentStream.endText()

                    currentX += courseworkColumnWidth
                }

                currentY -= ROW_HEIGHT // Mover a la siguiente fila de datos - Move to the next data row

                if (currentY < MARGIN) {
                    // Añadir nueva página y continuar la tabla
                    // Add new page and continue table
                    // Esto requiere lógica más compleja para dibujar encabezados en la nueva página, etc.
                    // This requires more complex logic to draw headers on the new page, etc.
                    println("Advertencia: El reporte excede una página. La implementación actual no maneja múltiples páginas.")
                    return@forEach
                }
            }

            contentStream.close()

            // Guardar el documento
            // Save the document
            document.save(File(filePath))

            true // Éxito - Success
        } catch (e: Exception) {
            println("Error generating PDF: ${e.message}")
            e.printStackTrace()
            false // Fallo - Failure
        } finally {
            // Cerrar el documento en el bloque finally
            // Close the document in the finally block
            document?.close()
        }
    }
}
