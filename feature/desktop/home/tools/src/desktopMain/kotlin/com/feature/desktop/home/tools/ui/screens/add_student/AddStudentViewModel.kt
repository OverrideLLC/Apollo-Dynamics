package com.feature.desktop.home.tools.ui.screens.add_student

import androidx.lifecycle.ViewModel
import com.network.interfaces.FirebaseGitliveRepository
import com.override.data.repository.contract.ClassRepository
import com.override.data.repository.contract.StudentRepository
import com.override.data.utils.data.ClassData
import com.override.data.utils.data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.io.Closeable
import java.util.UUID
import kotlin.random.Random

// ViewModel para añadir estudiante
class AddStudentViewModel(
    private val classRepository: ClassRepository,
    private val studentRepository: StudentRepository,
    private val firebaseGitliveRepository: FirebaseGitliveRepository
) : ViewModel(), Closeable {

    private val customViewModelScope =
        CoroutineScope(context = SupervisorJob() + Dispatchers.Swing) // O Dispatchers.Default/IO

    private val _state = MutableStateFlow(AddStudentState())
    val state = _state.asStateFlow()

    init {
        loadAvailableClasses()
    }

    // Carga las clases disponibles para el selector
    private fun loadAvailableClasses() {
        _state.update { it.copy(isLoadingClasses = true, error = null) }
        try {
            print("Loading classes")
            classRepository.getAllClasses().onEach { classes ->
                _state.update { it.copy(availableClasses = classes, isLoadingClasses = false) }
                print(classes)
            }.launchIn(customViewModelScope) // Lanzar la colección en el scope del ViewModel
        } catch (e: Exception) {
            print("Error loading classes")
            _state.update {
                it.copy(
                    isLoadingClasses = false,
                    error = "Error loading classes: ${e.message}"
                )
            }
            print(e.message)
        }
    }

    // --- Funciones para actualizar el estado desde la UI ---

    fun onStudentNameChange(newName: String) {
        _state.update { it.copy(studentName = newName, error = null, saveSuccess = false) }
    }

    fun onStudentIdChange(newId: String) {
        _state.update { it.copy(studentId = newId, error = null, saveSuccess = false) }
    }

    fun onClassSelected(selectedClass: ClassData) {
        _state.update { it.copy(selectedClass = selectedClass, error = null, saveSuccess = false) }
    }

    fun onStudentEmailChange(newEmail: String) {
        _state.update { it.copy(studentEmail = newEmail, error = null, saveSuccess = false) }
    }

    fun onStudentNumberChange(newNumber: String) {
        _state.update { it.copy(studentNumber = newNumber, error = null, saveSuccess = false) }
    }

    fun onStudentControlNumberChange(newControlNumber: String) {
        _state.update {
            it.copy(
                studentControlNumber = newControlNumber,
                error = null,
                saveSuccess = false
            )
        }
    }

    // --- Función para guardar el estudiante ---

    fun saveStudent() {
        val currentState = _state.value

        // Validación
        if (currentState.studentName.isBlank()) {
            _state.update { it.copy(error = "Please enter the student's name.") }
            return
        }
        if (currentState.selectedClass == null) {
            _state.update { it.copy(error = "Please select a class.") }
            return
        }
        // Puedes añadir más validaciones (ej: formato de ID si lo usas)

        _state.update { it.copy(isLoadingSave = true, error = null, saveSuccess = false) }

        customViewModelScope.launch {
            try {
                // Crear el objeto Student
                val newStudent = Student(
                    id = UUID.randomUUID().toString(),
                    name = currentState.studentName.trim(),
                    email = currentState.studentEmail.trim(),
                    number = currentState.studentNumber.trim(),
                    controlNumber = currentState.studentControlNumber.toInt()
                )
                firebaseGitliveRepository.addStudentsToCourse(
                    courseId = currentState.selectedClass.id,
                    newStudentIds = listOf(newStudent.id)
                )

                studentRepository.addOrUpdateStudent(newStudent)

                classRepository.addStudentToClass(
                    classId = currentState.selectedClass.id,
                    studentId = newStudent.id
                )

                // O si tienes un método específico en StudentRepository o ClassRepository:
                // studentRepository.addStudentToClass(newStudent, currentState.selectedClass.id)
                // classRepository.addStudentToClass(newStudent, currentState.selectedClass.id)

                // Actualizar estado para indicar éxito y limpiar formulario (o resetear)
                _state.update {
                    AddStudentState( // Resetea el estado manteniendo la lista de clases
                        availableClasses = it.availableClasses,
                        saveSuccess = true
                    )
                    // O solo limpiar campos específicos:
                    // it.copy(isLoadingSave = false, saveSuccess = true, studentName = "", studentId = "", selectedClass = null, error = null)
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingSave = false,
                        error = "Error saving student: ${e.message}"
                    )
                }
            } finally {
                // Asegurarse de quitar isLoadingSave si no se reseteó el estado completo
                if (!_state.value.saveSuccess) {
                    _state.update { it.copy(isLoadingSave = false) }
                }
            }
        }
    }

    companion object {
        private val FIRST_NAMES = listOf(
            "Sofía",
            "Valentina",
            "Regina",
            "María José",
            "Ximena",
            "Camila",
            "Valeria",
            "Renata",
            "Victoria",
            "Isabella",
            "Santiago",
            "Mateo",
            "Sebastián",
            "Leonardo",
            "Matías",
            "Emiliano",
            "Diego",
            "Daniel",
            "Miguel Ángel",
            "Alexander",
            "Juan",
            "José",
            "Luis",
            "Carlos",
            "Pedro",
            "Ana",
            "Laura",
            "Carmen",
            "Elena",
            "Patricia",
            "Alejandro",
            "David",
            "Fernando",
            "Javier",
            "Sergio",
            "Lucía",
            "Paula",
            "Marta",
            "Cristina",
            "Andrea"
        )
        private val LAST_NAMES = listOf(
            "Hernández",
            "García",
            "Martínez",
            "López",
            "González",
            "Pérez",
            "Rodríguez",
            "Sánchez",
            "Ramírez",
            "Cruz",
            "Flores",
            "Gómez",
            "Morales",
            "Vázquez",
            "Jiménez",
            "Reyes",
            "Díaz",
            "Torres",
            "Ortiz",
            "Gutíerrez",
            "Ruiz",
            "Mendoza",
            "Aguilar",
            "Castillo",
            "Moreno",
            "Rivera",
            "Romero",
            "Chávez",
            "Suárez",
            "Silva"
        )
    }

    fun addRandomStudentsToAllClasses(studentsPerClass: Int = 20) {
        _state.update {
            it.copy(
                isLoadingSave = true,
                error = null,
                saveSuccess = false,
            )
        }

        customViewModelScope.launch {
            var studentsAddedSuccessfully = 0
            var errorsEncountered = 0
            try {
                // Obtener la lista actual de clases. Si no están cargadas, esperar a que se carguen.
                val classes = _state.value.availableClasses.ifEmpty {
                    classRepository.getAllClasses().first()
                }

                if (classes.isEmpty()) {
                    _state.update {
                        it.copy(
                            isLoadingSave = false,
                            error = "No hay clases disponibles para agregar alumnos."
                        )
                    }
                    return@launch
                }

                for (classData in classes) {
                    for (i in 1..studentsPerClass) {
                        val firstName = FIRST_NAMES.random()
                        val lastName1 = LAST_NAMES.random()
                        val lastName2 =
                            LAST_NAMES.random() // Podría ser el mismo, para simplificar o se puede asegurar que sea diferente.

                        val studentName = "$firstName $lastName1 $lastName2"

                        // Generar email basado en el nombre y un sufijo aleatorio para unicidad
                        val emailPrefix =
                            "${firstName.take(1)}${lastName1}".lowercase().replace(" ", "")
                        val randomSuffix =
                            (1..99).random() // Añade un número aleatorio para mayor unicidad
                        val studentEmail = "${emailPrefix}${randomSuffix}@escuela.edu"

                        val studentNumber = (1000000000L + Random.nextLong(9000000000L)).toString()
                            .substring(0, 10) // Número de 10 dígitos
                        val studentControlNumber =
                            Random.nextInt(10000, 99999) // Número de control de 5 dígitos

                        val newStudent = Student(
                            id = UUID.randomUUID().toString(),
                            name = studentName,
                            email = studentEmail,
                            number = studentNumber,
                            controlNumber = studentControlNumber
                        )

                        try {
                            studentRepository.addOrUpdateStudent(newStudent)
                            classRepository.addStudentToClass(
                                classId = classData.id,
                                studentId = newStudent.id
                            )
                            studentsAddedSuccessfully++
                        } catch (e: Exception) {
                            errorsEncountered++
                            println("Error al agregar alumno ${newStudent.name} a la clase ${classData.name}: ${e.message}")
                        }
                    }
                }

                val finalMessage =
                    "Proceso completado. Alumnos agregados: $studentsAddedSuccessfully. Errores: $errorsEncountered."
                _state.update {
                    it.copy(
                        isLoadingSave = false,
                        saveSuccess = errorsEncountered == 0, // Éxito si no hubo errores
                        error = if (errorsEncountered > 0) "Algunos alumnos no pudieron ser agregados." else null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingSave = false,
                        error = "Error al agregar alumnos aleatorios: ${e.message}",
                    )
                }
            }
        }
    }

    override fun close() {
        customViewModelScope.cancel()
        println("AddStudentViewModel scope cancelled")
    }

    override fun onCleared() {
        super.onCleared()
        close()
    }
}
