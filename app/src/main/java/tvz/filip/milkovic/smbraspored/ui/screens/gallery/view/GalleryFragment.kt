package tvz.filip.milkovic.smbraspored.ui.screens.gallery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.drawee.view.SimpleDraweeView
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.ui.screens.gallery.model.GalleryViewModel

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    @BindView(R.id.text_gallery)
    lateinit var textView: TextView

    @BindView(R.id.bus_station_smb)
    lateinit var smbDraweeView: SimpleDraweeView

    @BindView(R.id.bus_station_zg)
    lateinit var zgDraweeView: SimpleDraweeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        ButterKnife.bind(this, root)

        textView.text = getString(R.string.gallery_title)

        smbDraweeView.setImageURI(galleryViewModel.smbStationUrl)

        zgDraweeView.setImageURI(galleryViewModel.zgStationUrl)

        return root
    }
}