package com.feature.desktop.home.services.utils

import com.feature.desktop.home.services.classroom.services.report.CourseReport
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.File

class PdfGenerator {
    fun generateReportPdf(reportData: CourseReport, filePath: String): Boolean {
        var document: PDDocument? = null
        return try {
            document = PDDocument()
            val page = PDPage()
            document.addPage(page)

            val contentStream = PDPageContentStream(document, page)

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12f)
            contentStream.beginText()
            contentStream.setLeading(14.5f)
            contentStream.newLineAtOffset(25f, 725f)

            contentStream.showText("Reporte del Curso: ${reportData.courseName ?: reportData.courseId}")
            contentStream.newLine()
            contentStream.newLine()

            // Iterar sobre los estudiantes y sus entregas
            // Iterate over students and their submissions
            reportData.studentReports.forEach { studentReport ->
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10f)
                contentStream.showText("Estudiante: ${studentReport.studentName ?: studentReport.studentEmail}")
                contentStream.newLine()

                if (studentReport.submissions.isEmpty()) {
                    contentStream.setFont(PDType1Font.HELVETICA, 9f)
                    contentStream.showText("  Sin entregas registradas.")
                    contentStream.newLine()
                } else {
                    studentReport.submissions.forEach { submission ->
                        contentStream.setFont(PDType1Font.HELVETICA, 9f)
                        contentStream.showText("  - ${submission.courseworkTitle}: ${submission.submissionState}")
                        if (submission.assignedGrade != null && submission.maxPoints != null) {
                            contentStream.showText(" (Calificación: ${submission.assignedGrade}/${submission.maxPoints})")
                        } else if (submission.assignedGrade != null) {
                            contentStream.showText(" (Calificación: ${submission.assignedGrade})")
                        }
                        contentStream.newLine()
                    }
                }
                contentStream.newLine() // Espacio entre estudiantes - Space between students
            }


            contentStream.endText()
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
        