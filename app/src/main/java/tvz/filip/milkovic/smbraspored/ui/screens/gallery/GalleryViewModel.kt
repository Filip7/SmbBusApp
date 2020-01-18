package tvz.filip.milkovic.smbraspored.ui.screens.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is just a Fragment test"
    }

    private val _imgUrl: String =
        "https://investorplace.com/wp-content/uploads/2014/05/DuckDuckGo-Logo.jpg"


    val text: LiveData<String> = _text
    val url: String = _imgUrl
}