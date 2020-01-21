package tvz.filip.milkovic.smbraspored.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "You have no favourite bus lines!"
    }
    val text: LiveData<String> = _text
}
