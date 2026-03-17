package co.edu.udea.compumovil.gr03_20261.lab1

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import java.util.*

class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PersonalDataScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen() {
    val context = LocalContext.current

    // Usamos rememberSaveable para que los datos no se borren al rotar la pantalla
    var nombres by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("Masculino") }
    var fecha by rememberSaveable { mutableStateOf("") }

    val grados = listOf("Primaria", "Secundaria", "Universitaria", "Otro")
    var gradoSeleccionado by rememberSaveable { mutableStateOf(grados[0]) }
    var expanded by remember { mutableStateOf(false) }

    var errorNombres by rememberSaveable { mutableStateOf(false) }
    var errorApellidos by rememberSaveable { mutableStateOf(false) }
    var errorFecha by rememberSaveable { mutableStateOf(false) }

    // Estado del scroll para pantallas pequeñas
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título (Corregido: Material 3 usa titleLarge o headlineSmall)
        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // Nombres
        OutlinedTextField(
            value = nombres,
            onValueChange = {
                nombres = it
                if (it.isNotBlank()) errorNombres = false
            },
            label = { Text("Nombres *") },
            isError = errorNombres,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )
        if (errorNombres) ErrorText("Este campo es obligatorio")

        // Apellidos
        OutlinedTextField(
            value = apellidos,
            onValueChange = {
                apellidos = it
                if (it.isNotBlank()) errorApellidos = false
            },
            label = { Text("Apellidos *") },
            isError = errorApellidos,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )
        if (errorApellidos) ErrorText("Este campo es obligatorio")

        // Sexo
        Text(text = "Sexo", style = MaterialTheme.typography.bodyLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            listOf("Masculino", "Femenino", "Otro").forEach { opcion ->
                RadioButton(
                    selected = sexo == opcion,
                    onClick = { sexo = opcion }
                )
                Text(
                    text = opcion,
                    modifier = Modifier.clickable { sexo = opcion }.padding(end = 8.dp)
                )
            }
        }

        // Fecha de nacimiento mejorada
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, y, m, d ->
                fecha = "%02d/%02d/%04d".format(d, m + 1, y)
                errorFecha = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            readOnly = true,
            enabled = false, // Evita que salga el teclado, pero permite clicks
            label = { Text("Fecha de nacimiento *") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePicker.show() },
            trailingIcon = {
                IconButton(onClick = { datePicker.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario")
                }
            },
            isError = errorFecha,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (errorFecha) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        if (errorFecha) ErrorText("Selecciona tu fecha de nacimiento")

        // Grado de escolaridad (Uso de ExposedDropdownMenuBox de Material 3)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = gradoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Grado de escolaridad") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                grados.forEach { grado ->
                    DropdownMenuItem(
                        text = { Text(grado) },
                        onClick = {
                            gradoSeleccionado = grado
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val nVacio = nombres.isBlank()
                val aVacio = apellidos.isBlank()
                val fVacia = fecha.isBlank()

                errorNombres = nVacio
                errorApellidos = aVacio
                errorFecha = fVacia

                if (!nVacio && !aVacio && !fVacia) {
                    Toast.makeText(context, "Datos: $nombres $sexo en $gradoSeleccionado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Faltan campos obligatorios", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }
    }
}

@Composable
fun ErrorText(mensaje: String) {
    Text(
        text = mensaje,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = 16.dp)
    )
}