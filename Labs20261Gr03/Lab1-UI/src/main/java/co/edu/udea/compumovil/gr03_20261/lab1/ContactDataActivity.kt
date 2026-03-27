package co.edu.udea.compumovil.gr03_20261.lab1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactDataScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDataScreen() {
    val context = LocalContext.current
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pais by rememberSaveable { mutableStateOf("") }
    var ciudad by rememberSaveable { mutableStateOf("") }

    var errorTelefono by rememberSaveable { mutableStateOf(false) }
    var errorEmail by rememberSaveable { mutableStateOf(false) }
    var errorPais by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Datos para Autocomplete (Simulados con DropdownMenu)
    val paisesLatam = listOf("Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Costa Rica", "Cuba", "Ecuador", "El Salvador", "Guatemala", "Honduras", "México", "Nicaragua", "Panamá", "Paraguay", "Perú", "Puerto Rico", "República Dominicana", "Uruguay", "Venezuela")
    val ciudadesColombia = listOf("Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Cúcuta", "Bucaramanga", "Pereira", "Santa Marta", "Ibagué")

    var expandedPais by remember { mutableStateOf(false) }
    var expandedCiudad by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.contact_info_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // Teléfono
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it; errorTelefono = false },
            label = { Text(stringResource(id = R.string.phone_label)) },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            isError = errorTelefono,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
        )
        if (errorTelefono) Text(stringResource(id = R.string.required_error), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorEmail = false },
            label = { Text(stringResource(id = R.string.email_label)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = errorEmail,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        if (errorEmail) Text(stringResource(id = R.string.required_error), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

        // País (Autocomplete simulado)
        ExposedDropdownMenuBox(
            expanded = expandedPais,
            onExpandedChange = { expandedPais = !expandedPais }
        ) {
            OutlinedTextField(
                value = pais,
                onValueChange = { pais = it; errorPais = false },
                label = { Text(stringResource(id = R.string.country_label)) },
                leadingIcon = { Icon(Icons.Default.Public, contentDescription = null) },
                isError = errorPais,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPais) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            val filteredPaises = paisesLatam.filter { it.contains(pais, ignoreCase = true) }
            if (filteredPaises.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expandedPais,
                    onDismissRequest = { expandedPais = false }
                ) {
                    filteredPaises.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p) },
                            onClick = {
                                pais = p
                                expandedPais = false
                                errorPais = false
                            }
                        )
                    }
                }
            }
        }
        if (errorPais) Text(stringResource(id = R.string.required_error), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

        // Ciudad (Autocomplete simulado)
        ExposedDropdownMenuBox(
            expanded = expandedCiudad,
            onExpandedChange = { expandedCiudad = !expandedCiudad }
        ) {
            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text(stringResource(id = R.string.city_label)) },
                leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCiudad) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            val filteredCiudades = ciudadesColombia.filter { it.contains(ciudad, ignoreCase = true) }
            if (filteredCiudades.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expandedCiudad,
                    onDismissRequest = { expandedCiudad = false }
                ) {
                    filteredCiudades.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c) },
                            onClick = {
                                ciudad = c
                                expandedCiudad = false
                            }
                        )
                    }
                }
            }
        }

        // Dirección
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text(stringResource(id = R.string.address_label)) },
            leadingIcon = { Icon(Icons.Default.Map, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val tVacio = telefono.isBlank()
                val eVacio = email.isBlank()
                val pVacio = pais.isBlank()

                errorTelefono = tVacio
                errorEmail = eVacio
                errorPais = pVacio

                if (!tVacio && !eVacio && !pVacio) {
                    Log.d("ContactData", "Información de contacto:")
                    Log.d("ContactData", "Teléfono: $telefono")
                    Log.d("ContactData", "Dirección: $direccion")
                    Log.d("ContactData", "Email: $email")
                    Log.d("ContactData", "País: $pais")
                    Log.d("ContactData", "Ciudad: $ciudad")

                    Toast.makeText(context, context.getString(R.string.info_saved_msg), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.missing_fields_error), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.next_button))
        }
    }
}
