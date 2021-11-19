package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.injection.ApplicationScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateAddress
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

//i use Multimap because room support only that in version 2.4 and higher


@Database(entities = [RealEstate::class, RealEstatePhoto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun realEstateDao(): RealEStateDao //

    class Callback @Inject constructor(
        private val database: Provider<RealEstateDatabase>,//provide instance of database
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        //load just once for init data
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().realEstateDao()

            applicationScope.launch {
                dao.insert(
                    RealEstate(
                        numberOfBathRoom =2 ,
                        numberOfBedRoom = 3,
                        numberOfRoom = 5,
                        surface = 100,
                        typeOfProduct = "Flat",
                        cityOfProduct = "Manhattan",
                        price = 17870000,
                       // photo ="" ,
                        descriptionOfProduct = "Lorem ipsum dolor sit amet. Non galisum reprehenderit hic quidem repellendus cum eius enim cum asperiores natus et eius quam rem quisquam natus. Quo voluptates ratione in accusamus placeat in galisum possimus non reiciendis molestiae non sapiente magnam et iure quas? Et ratione dolorem et fugiat distinctio aut fugiat illum.",
                        address = RealEstateAddress(zip_code = 35000 , city = "Rennes", street_name = "rue du stade", street_number = 7 )

                        )
                )
                dao.insert(
                    RealEstate(
                        numberOfBathRoom =3 ,
                        numberOfBedRoom = 2,
                        numberOfRoom = 4,
                        surface = 150,
                        typeOfProduct = "House",
                        cityOfProduct = "Montauk",
                        price = 21130000,
                      //  photo = "",
                        descriptionOfProduct = "Lorem ipsum dolor sit amet. Non galisum reprehenderit hic quidem repellendus cum eius enim cum"
                    )
                )
                dao.insert(
                    RealEstate(
                        numberOfBathRoom =1 ,
                        numberOfBedRoom = 1,
                        numberOfRoom = 2,
                        surface = 200,
                        typeOfProduct = "Duplex",
                        cityOfProduct = "Brooklyn",
                        price = 13990000,
                        descriptionOfProduct = "Non galisum reprehenderit hic quidem repellendus cum eius enim cum asperiores natus et eius quam rem quisquam natus. Quo volup"
                    )
                )
                dao.insert(
                    RealEstate(
                        numberOfBathRoom =4 ,
                        numberOfBedRoom = 5,
                        numberOfRoom = 8,
                        surface = 200,
                        typeOfProduct = "House",
                        cityOfProduct = "Southhampton",
                        price = 41480000,
                        descriptionOfProduct = " rem quisquam natus. Quo volup"
                    )
                )
                dao.insert(
                    RealEstate(
                        numberOfBathRoom =3 ,
                        numberOfBedRoom = 8,
                        numberOfRoom = 15,
                        surface = 400,
                        typeOfProduct = "Castle",
                        cityOfProduct = "Rennes",
                        price = 141480000,
                        descriptionOfProduct = " rem efrjfer jirjfioerf jfoie jrfijre foije iojfroie jfoierjquisquam natus. Quo volup"
                    )
                )

                dao.insert(
                    RealEstate(
                        numberOfBathRoom =2 ,
                        numberOfBedRoom = 18,
                        numberOfRoom = 16,
                        surface = 200,
                        typeOfProduct = "Chalet",
                        cityOfProduct = "Nantes",
                        price = 11480000,
                        descriptionOfProduct = " rem efrjfer jirjfioerf jfoie jrfijre foije iojfroie jfoierjquis ksdfjf kjfdklfjs sdkjfl ksfj skdljsd flkj sfkljsdfkljetrkjktljet fgùdfmgkfdmlùkfd gùquam natus. Quo volup"
                    )
                )



                dao.insertPhoto(RealEstatePhoto(realEstateId = 1, name = "Photo cuisine", uri = "hhh"))

                dao.insertPhoto(RealEstatePhoto(realEstateId = 1, name = "Photo chambre", uri = "hhh"))
            }

        }
    }


}