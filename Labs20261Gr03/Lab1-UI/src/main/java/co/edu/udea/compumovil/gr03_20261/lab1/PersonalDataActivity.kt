package co.edu.udea.compumovil.gr03_20261.lab1

import android.app.DatePickerDialog
import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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

    val scrollState = rememberScrollState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // Nombres y Apellidos
        if (isLandscape) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    InputField(
                        value = nombres,
                        onValueChange = { nombres = it; errorNombres = false },
                        label = "Nombres *",
                        isError = errorNombres,
                        icon = Icons.Default.Person
                    )
                    if (errorNombres) ErrorText("Obligatorio")
                }
                Column(modifier = Modifier.weight(1f)) {
                    InputField(
                        value = apellidos,
                        onValueChange = { apellidos = it; errorApellidos = false },
                        label = "Apellidos *",
                        isError = errorApellidos,
                        icon = Icons.Default.Person
                    )
                    if (errorApellidos) ErrorText("Obligatorio")
                }
            }
        } else {
            InputField(nombres, { nombres = it; errorNombres = false }, "Nombres *", errorNombres, Icons.Default.Person)
            if (errorNombres) ErrorText("Este campo es obligatorio")
            InputField(apellidos, { apellidos = it; errorApellidos = false }, "Apellidos *", errorApellidos, Icons.Default.Person)
            if (errorApellidos) ErrorText("Este campo es obligatorio")
        }

        // Sexo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sexo:", style = MaterialTheme.typography.bodyLarge)
            listOf("Masculino", "Femenino").forEach { opcion ->
                RadioButton(selected = sexo == opcion, onClick = { sexo = opcion })
                Text(text = opcion, modifier = Modifier.clickable { sexo = opcion })
            }
        }

        // Fecha de nacimiento
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Fecha de nacimiento *") },
                modifier = Modifier
                    .weight(1f)
                    .clickable { datePicker.show() },
                isError = errorFecha,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = if (errorFecha) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { datePicker.show() }) { Text("Cambiar") }
        }
        if (errorFecha) ErrorText("Selecciona tu fecha de nacimiento")

        // Grado de escolaridad y Botón Siguiente
        val footerModifier = if (isLandscape) Modifier.fillMaxWidth() else Modifier.fillMaxWidth()

        @Composable
        fun ContentFooter() {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = if (isLandscape) Modifier.weight(1f) else Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gradoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Grado de escolaridad") },
                    leadingIcon = { Icon(Icons.Default.School, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    grados.forEach { grado ->
                        DropdownMenuItem(text = { Text(grado) }, onClick = { gradoSeleccionado = grado; expanded = false })
                    }
                }
            }

            if (isLandscape) Spacer(modifier = Modifier.width(16.dp)) else Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val nVacio = nombres.isBlank()
                    val aVacio = apellidos.isBlank()
                    val fVacia = fecha.isBlank()
                    errorNombres = nVacio
                    errorApellidos = aVacio
                    errorFecha = fVacia

                    if (!nVacio && !aVacio && !fVacia) {
                        val intent = android.content.Intent(context, ContactDataActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Faltan campos obligatorios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = if (isLandscape) Modifier.wrapContentWidth() else Modifier.fillMaxWidth()
            ) {
                Text("Siguiente")
            }
        }

        if (isLandscape) {
            Row(verticalAlignment = Alignment.CenterVertically) { ContentFooter() }
        } else {
            ContentFooter()
        }
    }
}

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String, isError: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next)
    )
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