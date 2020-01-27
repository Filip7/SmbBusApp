package tvz.filip.milkovic.smbraspored.ui.screens.gallery.model

import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _smbStationImgUrl: String =
        "https://live.staticflickr.com/4796/39858288045_eda6466afe_b.jpg"

    private val _zgStationImgUrl: String =
        "https://www.akz.hr/UserDocsImages/kolodvor_procelje2.jpg"


    val smbStationUrl: String = _smbStationImgUrl
    val zgStationUrl: String = _zgStationImgUrl

}