package tvz.filip.milkovic.smbraspored.shared.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ColumnIgnore
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import kotlinx.android.parcel.Parcelize
import tvz.filip.milkovic.smbraspored.database.AppDatabase
import java.io.Serializable
import java.time.LocalTime

@Keep
@Parcelize
object Model : Parcelable {

    @Keep
    @Parcelize
    @Table(database = AppDatabase::class)
    data class BusLine(
        @PrimaryKey var id: Long = 0,
        @Column var name: String = "",
        @Column var code: String = "",
        @ColumnIgnore var departures: List<Departure>? = null
    ) : Parcelable, Serializable

    @Keep
    @Parcelize
    @Table(database = AppDatabase::class)
    data class Departure(
        @PrimaryKey var id: Long = 0,
        @Column var busLineId: Long = 0,
        @Column var departureTime: String = "00:00:00",
        @Column var typeOfDay: TypeOfDay = TypeOfDay.WEEK_DAY,
        @Column var startingPointIsFirstListed: Int = 0
    ) : Parcelable {
        fun getDepartureTimeInLocalDateTime(): LocalTime {
            return LocalTime.parse(departureTime)
        }
    }

    @Keep
    @Parcelize
    @Table(database = AppDatabase::class)
    data class FavBusLine(
        @PrimaryKey var idOfFavouriteBusLine: Long = 0
    ) : Parcelable

}