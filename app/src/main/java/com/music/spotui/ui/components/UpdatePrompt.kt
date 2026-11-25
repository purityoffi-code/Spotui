package com.music.spotui.ui.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate
import kotlinx.coroutines.delay

/*
 * MANUAL SETTINGS
 * Change these values when preparing a new release.
 * Expiry is inclusive: the app is blocked on EXPIRY_DATE itself.
 */
private const val APP_VERSION = "1.6.0"
private const val EXPIRY_DATE = "2026-11-29"
private const val UPDATE_URL = "https://appteka.store/profile/595244"

private const val YOUTUBE_URL = "https://youtube.com/@hp_2.3?si=Q3H30k13eVWXW9M9"
private const val APPTEKA_URL = "https://appteka.store/profile/595244"
private const val INSTAGRAM_URL = "https://www.instagram.com/haaris_mods?jplx=MXZrbTc3ZDVpZDk2cA=="

private const val SOCIAL_PREFS = "purity_social_banner"
private const val SOCIAL_SHOWN_KEY = "social_banner_acknowledged"

private fun isAppExpired(): Boolean {
    val expiry = runCatching { LocalDate.parse(EXPIRY_DATE) }.getOrNull() ?: return false
    return !LocalDate.now().isBefore(expiry)
}

/**
 * Shows the app only before the configured expiry date.
 * Once expired, this non-dismissible dialog blocks the app.
 */
@Composable
fun ExpiryGate(content: @Composable () -> Unit) {
    val context = LocalContext.current
    var expired by remember { mutableStateOf(isAppExpired()) }

    // Re-check periodically so expiry also takes effect if the app stays open
    // when the configured calendar date begins.
    LaunchedEffect(Unit) {
        while (!expired) {
            delay(30_000)
            expired = isAppExpired()
        }
    }

    if (expired) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF171717)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "PURITY",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "UPDATE AVAILABLE",
                        color = Color(0xFFE879A5),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "New Version of PURITY is Available",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Version $APP_VERSION",
                        color = Color(0xFFBDBDBD),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Your current app version has expired. Please update to continue.",
                        color = Color(0xFFE879A5),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                (context as? Activity)?.finishAffinity()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF454545)
                            )
                        ) {
                            Text("EXIT NOW", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                runCatching {
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, Uri.parse(UPDATE_URL))
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2775E8)
                            )
                        ) {
                            Text("UPDATE NOW", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    } else {
        content()
    }
}

/**
 * One-time social banner. It is marked as shown only after the user taps OK.
 */
@Composable
fun SocialLinksPrompt() {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(SOCIAL_PREFS, Activity.MODE_PRIVATE)
    }
    var showDialog by remember {
        mutableStateOf(!preferences.getBoolean(SOCIAL_SHOWN_KEY, false))
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF171717)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Text(
                        text = "FOLLOW HAARIS MODS",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Stay connected with us!",
                        color = Color(0xFFBDBDBD),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    SocialLinkRow("▶  Subscribe on YouTube", YOUTUBE_URL)
                    SocialLinkRow("◉  Visit Appteka Profile", APPTEKA_URL)
                    SocialLinkRow("◎  Follow on Instagram", INSTAGRAM_URL)

                    Spacer(modifier = Modifier.padding(top = 2.dp))

                    Button(
                        onClick = {
                            preferences.edit().putBoolean(SOCIAL_SHOWN_KEY, true).apply()
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2775E8)
                        )
                    ) {
                        Text(
                            text = "OK",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialLinkRow(label: String, url: String) {
    val context = LocalContext.current

    Text(
        text = label,
        color = Color(0xFF70A7FF),
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF242424), RoundedCornerShape(10.dp))
            .clickable {
                runCatching {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
            .padding(horizontal = 14.dp, vertical = 13.dp)
    )
}
