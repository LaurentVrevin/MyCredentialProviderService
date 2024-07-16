package com.example.mycredentialproviderservice


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import com.example.mycredentialproviderservice.ui.components.GoogleSignInButton
import com.example.mycredentialproviderservice.ui.components.GoogleSignOutButton
import com.example.mycredentialproviderservice.ui.theme.MyCredentialProviderServiceTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()

        setContent {
            MyCredentialProviderServiceTheme {

                Surface(
                    modifier = Modifier
                        .padding(16.dp)

                ) {
                    Column {
                        GoogleSignInButton()
                        Spacer(modifier = Modifier.padding(16.dp))
                        GoogleSignOutButton()
                    }

                }
            }
        }
    }
}

