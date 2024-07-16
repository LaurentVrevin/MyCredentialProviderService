package com.example.mycredentialproviderservice.ui.components

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.mycredentialproviderservice.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID





const val serverIdGoogle = BuildConfig.MY_API_KEY

fun getNonce(): String {
    // Génère une chaîne UUID aléatoire
    val rawNonce = UUID.randomUUID().toString()

    // Convertit cette chaîne en un tableau de bytes
    val bytes = rawNonce.toByteArray()

    // Obtient une instance de l'algorithme de hachage SHA-256
    val md = MessageDigest.getInstance("SHA-256")

    // Hache le tableau de bytes
    val digest = md.digest(bytes)

    // Convertit le hachage en une chaîne hexadécimale
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun createCredentialRequest(context: Context, serverIdGoogle: String, nonce: String): GetCredentialRequest {
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverIdGoogle)
        .setNonce(nonce)
        .build()
    return GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
}

suspend fun signInWithGoogle(context: Context, request: GetCredentialRequest) {
    val credentialManager = CredentialManager.create(context)

    try {
        val result = credentialManager.getCredential(
            request = request,
            context = context
        )
        val credential = result.credential
        val googleIdTokenCredential = GoogleIdTokenCredential
            .createFrom(credential.data)

        val googleIdToken = googleIdTokenCredential.idToken

        Log.i(TAG, googleIdToken)

        Toast.makeText(context, "You are signed in !!", Toast.LENGTH_SHORT).show()
    } catch (
        e: GetCredentialException
    ) {
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    } catch (
        e: GoogleIdTokenParsingException
    ) {
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
suspend fun clearCredentials(context: Context) {
    val credentialManager = CredentialManager.create(context)

    // Crée une requête pour effacer les états des credentials
    val request = ClearCredentialStateRequest()

    // Supprime toutes les clés d'authentification
    credentialManager.clearCredentialState(request)
}

@Composable
fun GoogleSignInButton() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val nonce = getNonce()
    val request = createCredentialRequest(context, serverIdGoogle, nonce)

    val onClick: () -> Unit = {
        coroutineScope.launch {
            signInWithGoogle(context, request)
        }
    }

    Button(onClick = onClick) {
        Text(text = "Sign in with Google")
    }
}


@Composable
fun GoogleSignOutButton() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        coroutineScope.launch {
            clearCredentials(context)
            Toast.makeText(context, "You are signed out", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = onClick) {
        Text(text = "Sign out from Google")
    }
}

