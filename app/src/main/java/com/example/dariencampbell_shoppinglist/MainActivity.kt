package com.example.dariencampbell_shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dariencampbell_shoppinglist.ui.theme.DarienCampbellShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DarienCampbellShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppLayout(
                        modifier = Modifier.padding(innerPadding).displayCutoutPadding()
                    )
                }
            }
        }
    }
}

data class ShoppingEntry (
    val label: String,
    val count: Int,
    val checked: Boolean
)

@Composable
fun RowScope.ActionButton(text: String, onClick: ()->Unit) {
    FilledTonalButton(onClick=onClick, modifier=Modifier.padding(8.dp).weight(1f)) {
        Text(text)
    }
}

@Composable
fun ShoppingItem(index: Int, label: String, onLabelChange: (String)->Unit, count: Int, onCountChange: (Int)->Unit, checked: Boolean, onCheckedChange: (Boolean)->Unit) {
    Card(modifier=Modifier.padding(8.dp)) {
        Row(modifier=Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value=label, onValueChange=onLabelChange, modifier=Modifier.weight(1f), label={
                Text("Name")
            })
            OutlinedTextField(value=count.toString(), keyboardOptions= KeyboardOptions(keyboardType=KeyboardType.Number), onValueChange={
                try {
                    onCountChange(it.toInt())
                } catch (e: NumberFormatException) {
                    onCountChange(0)
                }
            }, modifier=Modifier.width(80.dp).padding(8.dp, 0.dp, 0.dp, 0.dp), label={
                Text("Count")
            })
            Checkbox(checked=checked, onCheckedChange=onCheckedChange, modifier=Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp))
        }
    }
}

@Composable
fun AppLayout(modifier: Modifier = Modifier) {
    val shoppingItems = remember {mutableStateListOf<ShoppingEntry>()}

    Column(modifier=modifier.padding(8.dp, 0.dp)) {
        Spacer(modifier=Modifier.height(8.dp))
        Text("Shopping List", fontSize=24.sp)
        Spacer(modifier=Modifier.height(16.dp))
        LazyColumn() {
            items(shoppingItems.size) {index:Int->
                val entry = shoppingItems[index]
                ShoppingItem(index, entry.label, {shoppingItems[index] = ShoppingEntry(it, entry.count, entry.checked)}, entry.count, {shoppingItems[index] = ShoppingEntry(entry.label, it, entry.checked)}, entry.checked, {shoppingItems[index] = ShoppingEntry(entry.label, entry.count, it)})
            }
            item() {
                Row() {
                    ActionButton("Add Item") {
                        shoppingItems.add(ShoppingEntry("", 1, false))
                    }
                    ActionButton("Clear Checked") {
                        var toRemove = emptyList<ShoppingEntry>()
                        for (item in shoppingItems) {
                            if (item.checked) {
                                toRemove = toRemove.plusElement(item)
                            }
                        }
                        for (item in toRemove) {
                            shoppingItems.remove(item)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DarienCampbellShoppingListTheme {
        AppLayout()
    }
}