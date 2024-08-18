@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.yournotes.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.Text

import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yournotes.Utils.aniduration
import com.example.yournotes.database.NoteEntity

import com.example.yournotes.ui.theme.ubuntu
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import java.util.Calendar
import kotlin.math.absoluteValue
import kotlin.math.sqrt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsState()
    val searchedNotes by viewModel.searchedNotes.collectAsState()

    val (dialogOpen, setDialogOpen) = remember {
        mutableStateOf(false)
    }
    val (note, setNote) = remember {
        mutableStateOf("")
    }
    var editNote by remember {
        mutableStateOf(false)
    }
    var editedNote by remember {
        mutableStateOf<NoteEntity?>(null)
    }
    var initialColor = 0xff1dffc0
    var colorPickerColor by remember { mutableStateOf(Color(initialColor)) }

    if (dialogOpen) {
        val controller = rememberColorPickerController()
        controller.setWheelRadius(7.dp)

        val noteColor by animateColorAsState(targetValue = controller.selectedColor.value)
        val focusRequester = remember { FocusRequester() }

        Dialog(onDismissRequest = {
            setDialogOpen(false)
        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                LaunchedEffect(dialogOpen) {
                    // Request focus and move cursor to the end
                    focusRequester.requestFocus()
                    setNote(note)
                    if (editedNote != null) {
                        colorPickerColor = Color(editedNote!!.color)
                    }
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .focusRequester(focusRequester),
                    value = TextFieldValue(note).copy(selection = TextRange(note.length)),
                    onValueChange = { setNote(it.text) },
                    label = { Text(text = "Note") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Note Color:", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = controller,
                    initialColor = colorPickerColor
                )
                Spacer(modifier = Modifier.height(10.dp))
                AlphaSlider(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    borderRadius = 6.dp,
                    wheelRadius = 7.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
                BrightnessSlider(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    borderRadius = 6.dp,
                    wheelRadius = 7.dp
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (note.isNotEmpty()) {
                            val date = Calendar.getInstance().time

                            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val currentTime = format.format(date)
                            if (editedNote == null) {
                                viewModel.addNote(
                                    NoteEntity(
                                        note = note,
                                        color = noteColor.toArgb(),
                                        time = currentTime
                                    )
                                )
                            } else {
                                val updatedNote = editedNote!!.copy(
                                    note = note,
                                    color = noteColor.toArgb(),
                                    time = currentTime
                                )
                                viewModel.updateNote(updatedNote)
                                editedNote = null
                            }
                            setNote("")
                            setDialogOpen(false)
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = noteColor
                    )
                ) {
                    Text(
                        text = if (editedNote == null) ("Add Note") else ("Update Note"),
                        color = if (areColorsNear(noteColor, Color.White)) Color.Black else Color.White,
                        fontFamily = ubuntu
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { setDialogOpen(true)
                    setNote("")},
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        val focusRequester = remember { FocusRequester() }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = 18.dp,
                    start = 12.dp,
                    end = 12.dp
                )
        ) {
            val (searchOpen, setSearchOpen) = remember { mutableStateOf(false) }
            val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

            LaunchedEffect(searchQuery) {
                viewModel.searchNote(searchQuery)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = !searchOpen,
                    enter = fadeIn(tween(aniduration)) + expandHorizontally(tween(aniduration)),
                    exit = fadeOut(tween(aniduration)) + shrinkHorizontally(tween(aniduration))
                ) {
                    Text(
                        text = "My Notes",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    visible = searchOpen,
                    enter = fadeIn(tween(aniduration)) + scaleIn(tween(aniduration)),
                    exit = fadeOut(tween(aniduration)) + scaleOut(tween(aniduration))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { setSearchQuery(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            placeholder = { Text(text = "Search") },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable { setSearchOpen(!searchOpen) },
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        AnimatedVisibility(
                            visible = !searchOpen,
                            enter = scaleIn(tween(500)),
                            exit = scaleOut(tween(500))
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                    }

                    Row {
                        AnimatedVisibility(
                            visible = searchOpen,
                            enter = scaleIn(tween(500)),
                            exit = scaleOut(tween(500))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(28.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(searchedNotes ?: notes, key = { it.id }) { thisNote ->
                    val noteColor = Color(thisNote.color)
                    val textColor = if (isColorNearWhite(noteColor)) Color.Black else Color.White

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(noteColor)
                            .border(
                                BorderStroke(1.dp, Color.White),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .fillMaxWidth()
                            .padding(8.dp)
                            .animateItemPlacement(tween(500))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        setDialogOpen(true)
                                        setNote(thisNote.note)
                                        editedNote = thisNote
                                        colorPickerColor = noteColor
                                    },
                                    onLongPress = {
                                        viewModel.deleteNote(thisNote)
                                    }
                                )
                            }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(text = thisNote.note, color = textColor)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Text(text = thisNote.time, color = textColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun areColorsNear(color1: Color, color2: Color, threshold: Double = 110.0): Boolean {
    val r1 = color1.red * 255
    val g1 = color1.green * 255
    val b1 = color1.blue * 255

    val r2 = color2.red * 255
    val g2 = color2.green * 255
    val b2 = color2.blue * 255

    val distance = sqrt(
        (r1 - r2) * (r1 - r2) +
                (g1 - g2) * (g1 - g2) +
                (b1 - b2) * (b1 - b2)
    )

    return distance < threshold
}

fun isColorNearWhite(color: Color, threshold: Double = 160.0): Boolean {
    val r = color.red * 255
    val g = color.green * 255
    val b = color.blue * 255

    // White color components
    val whiteR = 255
    val whiteG = 255
    val whiteB = 255

    // Euclidean distance
    val distance = sqrt(
        (r - whiteR) * (r - whiteR) +
                (g - whiteG) * (g - whiteG) +
                (b - whiteB) * (b - whiteB)
    )

    return distance < threshold
}

@Composable
fun CustomToast(message: String, isVisible: Boolean, onDismiss: () -> Unit) {
    var visible by remember { mutableStateOf(isVisible) }

    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = message, color = Color.White)
        }

        LaunchedEffect(Unit) {
            delay(2000) // Show toast for 2 seconds
            visible = false
            onDismiss()
        }
    }
}
