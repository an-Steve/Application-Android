package com.example.trackerplane
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import com.example.trackerplane.ui.theme.TrackerPlaneTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerPlaneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFB3E5FC), Color(0xFF03A9F4))
                                )
                            )
                    )
                }
            }
        }
    }
}
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var isDarkMode by remember { mutableStateOf(false) }  // Variable pour le mode clair/sombre
    var showLoginForm by remember { mutableStateOf(false) }
    var showSignupForm by remember { mutableStateOf(false) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var showValidationScreen by remember { mutableStateOf(false) }
    var showScanScreen by remember { mutableStateOf(false) }

    // Variable de langue (français/anglais)
    var language by remember { mutableStateOf("fr") }

    // Fonction pour changer la langue
    fun toggleLanguage() {
        language = if (language == "fr") "en" else "fr"
    }

    // Obtenir et mettre à jour la date et l'heure toutes les secondes
    var currentDateTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            currentDateTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }

    // Choix des couleurs en fonction du mode
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFBBDEFB)
    val textColor = if (isDarkMode) Color(0xFFFFFFFF) else Color(0xFF000000)

    // Fond avec dégradé
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Titre en haut
            Text(
                text = if (language == "fr") "Plane Tracker" else "Plane Tracker",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(top = 32.dp),
                textAlign = TextAlign.Center
            )

            // Affichage de la date et de l'heure actuelles
            Text(
                text = currentDateTime,
                fontSize = 18.sp,
                color = textColor,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(120.dp))

            // Vérification des écrans (connexion, inscription, etc.)
            when {
                showLoginForm -> LoginForm(
                    onBack = { showLoginForm = false },
                    onLoginSuccess = { showSearchScreen = true }
                )
                showSignupForm -> SignupForm(onBack = { showSignupForm = false })
                showSearchScreen -> SearchScreen(
                    onBack = { showSearchScreen = false },
                    onSearch = { query ->
                        println("Recherche pour: $query")
                        showValidationScreen = true
                    }
                )
                showValidationScreen -> ValidationScreen(
                    onBack = { showValidationScreen = false },
                    onGoToScan = { showScanScreen = true }
                )
                showScanScreen -> ScanScreen(onBack = { showScanScreen = false })
                else -> {
                    // Boutons de Connexion et Inscription
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { showLoginForm = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65)),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier
                                .width(220.dp)
                                .height(55.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = if (language == "fr") "Connexion" else "Login",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showSignupForm = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DB6AC)),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier
                                .width(220.dp)
                                .height(55.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = if (language == "fr") "S'inscrire" else "Sign Up",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Bouton de bascule clair/sombre
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { isDarkMode = !isDarkMode },
                colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFFBBDEFB) else Color(0xFF303030))
            ) {
                Text(if (isDarkMode) "Mode Clair" else "Mode Sombre", color = Color.White)
            }

            // Bouton Langue
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { toggleLanguage() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                shape = RoundedCornerShape(30),
                modifier = Modifier
                    .width(220.dp)
                    .height(55.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = if (language == "fr") "Changer en Anglais" else "Switch to French",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Texte "Par ANTON NELCON Steve" en bas
        Text(
            text = "Par ANTON NELCON Steve",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}




@Composable
fun LoginForm(onBack: () -> Unit, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Variable de langue (français/anglais)
    var language by remember { mutableStateOf("fr") }

    // Fonction pour changer la langue
    fun toggleLanguage() {
        language = if (language == "fr") "en" else "fr"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (language == "fr") "Formulaire de Connexion" else "Login Form",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0288D1),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(if (language == "fr") "Adresse Mail" else "Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(if (language == "fr") "Mot de passe" else "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    // Ajoutez ici la logique de connexion ou l'appel à une API
                    println("Connexion réussie avec $email")
                    onLoginSuccess() // Appelle la fonction de redirection
                } else {
                    println("Veuillez remplir tous les champs")
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (language == "fr") "Se connecter" else "Log In",
                color = Color.White,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (language == "fr") "Retour" else "Back",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Bouton Langue
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { toggleLanguage() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
            shape = RoundedCornerShape(30),
            modifier = Modifier
                .width(220.dp)
                .height(55.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = if (language == "fr") "Changer en Anglais" else "Switch to French",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun SignupForm(onBack: () -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("fr") } // Ajout d'une variable pour la langue

    // Fonction pour changer la langue
    fun changeLanguage(lang: String) {
        language = lang
    }

    // Déterminer les textes à afficher en fonction de la langue
    val titleText = if (language == "fr") "Formulaire d'Inscription" else "Signup Form"
    val firstNameLabel = if (language == "fr") "Prénom" else "First Name"
    val lastNameLabel = if (language == "fr") "Nom" else "Last Name"
    val emailLabel = if (language == "fr") "Adresse Mail" else "Email Address"
    val birthDateLabel = if (language == "fr") "Date d'anniversaire" else "Birth Date"
    val passwordLabel = if (language == "fr") "Mot de passe" else "Password"
    val confirmPasswordLabel = if (language == "fr") "Confirmer le mot de passe" else "Confirm Password"
    val signupButtonText = if (language == "fr") "S'inscrire" else "Sign Up"
    val backButtonText = if (language == "fr") "Retour" else "Back"
    val changeLangButtonText = if (language == "fr") "Changer en Anglais" else "Change to French"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titleText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0288D1),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(firstNameLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(lastNameLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(emailLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text(birthDateLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(passwordLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(confirmPasswordLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() &&
                    birthDate.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                ) {
                    // Ajoutez ici la logique d'inscription ou l'appel à une API
                    if (password == confirmPassword) {
                        println("Inscription réussie pour $firstName $lastName")
                    } else {
                        println("Les mots de passe ne correspondent pas")
                    }
                } else {
                    println("Veuillez remplir tous les champs")
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(signupButtonText, color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(backButtonText, color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Changer la langue
                changeLanguage(if (language == "fr") "en" else "fr")
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(changeLangButtonText, color = Color.White, fontSize = 16.sp)
        }
    }
}


@Composable
fun SearchScreen(onBack: () -> Unit, onSearch: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("fr") } // Variable pour la langue

    // Fonction pour changer la langue
    fun changeLanguage(lang: String) {
        language = lang
    }

    // Déterminer les textes à afficher en fonction de la langue
    val titleText = if (language == "fr") "Écran de Recherche" else "Search Screen"
    val searchLabel = if (language == "fr") "Rechercher un vol" else "Search a flight"
    val searchButtonText = if (language == "fr") "Rechercher" else "Search"
    val backButtonText = if (language == "fr") "Retour" else "Back"
    val changeLangButtonText = if (language == "fr") "Changer en Anglais" else "Change to French"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(
                color = Color(0xFFBBDEFB),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titleText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0288D1),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(searchLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Appel de la fonction de recherche pour naviguer vers l'écran de validation
                onSearch(searchQuery)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(searchButtonText, color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(backButtonText, color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Changer la langue
                changeLanguage(if (language == "fr") "en" else "fr")
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(changeLangButtonText, color = Color.White, fontSize = 16.sp)
        }
    }
}


@Composable
fun ValidationScreen(onBack: () -> Unit, onGoToScan: () -> Unit) {
    var dateInput by remember { mutableStateOf("") } // État pour la date
    var language by remember { mutableStateOf("fr") } // Variable pour la langue

    // Fonction pour changer la langue
    fun changeLanguage(lang: String) {
        language = lang
    }

    // Déterminer les textes à afficher en fonction de la langue
    val titleText = if (language == "fr") "Choisissez une Date :" else "Choose a Date:"
    val dateLabel = if (language == "fr") "Entrez une date :" else "Enter a date:"
    val validateButtonText = if (language == "fr") "Valider" else "Validate"
    val backButtonText = if (language == "fr") "Retour" else "Back"
    val changeLangButtonText = if (language == "fr") "Changer en Anglais" else "Change to English"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(
                color = Color(0xFFBBDEFB),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titleText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0288D1),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Barre de texte pour saisir une date
        TextField(
            value = dateInput,
            onValueChange = { dateInput = it },
            label = { Text(dateLabel) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Redirige vers l'écran de scan
                onGoToScan()
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF03A9F4)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(validateButtonText, color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(backButtonText, color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Changer la langue
                changeLanguage(if (language == "fr") "en" else "fr")
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(changeLangButtonText, color = Color.White, fontSize = 16.sp)
        }
    }
}


@Composable
fun ScanScreen(onBack: () -> Unit) {
    var language by remember { mutableStateOf("fr") } // Variable pour la langue

    // Fonction pour changer la langue
    fun changeLanguage(lang: String) {
        language = lang
    }

    // Déterminer les textes à afficher en fonction de la langue
    val titleText = if (language == "fr") "Scanner le Ticket" else "Scan the Ticket"
    val instructionText = if (language == "fr") "Placez votre ticket sous le scanner." else "Place your ticket under the scanner."
    val backButtonText = if (language == "fr") "Retour" else "Back"
    val changeLangButtonText = if (language == "fr") "Changer en Anglais" else "Change to English"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .background(
                color = Color(0xFFBBDEFB),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titleText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0288D1),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Ajout de l'instruction pour l'utilisateur
        Text(
            text = instructionText,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(backButtonText, color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Changer la langue
                changeLanguage(if (language == "fr") "en" else "fr")
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF0288D1)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(changeLangButtonText, color = Color.White, fontSize = 16.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainContent() {
    TrackerPlaneTheme {
        MainContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFB3E5FC), Color(0xFF03A9F4))
                    )
                )
        )
    }
}
