package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.injection.ApplicationScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateAddress
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

//i use Multimap because room support only that in version 2.4 and higher


@Database(
    entities = [RealEstate::class, RealEstateMedia::class, RealEstatePOI::class],
    version = 1,
    exportSchema = false
)
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
                            numberOfBathRoom = 2,
                            numberOfBedRoom = 3,
                            numberOfRoom = 5,
                            surface = 100,
                            typeOfProduct = "Flat",
                            cityOfProduct = "Manhattan",
                            price = 17870000,
                            descriptionOfProduct = "Lorem ipsum dolor sit amet. Non galisum reprehenderit hic quidem repellendus cum eius enim cum asperiores natus et eius quam rem quisquam natus. Quo voluptates ratione in accusamus placeat in galisum possimus non reiciendis molestiae non sapiente magnam et iure quas? Et ratione dolorem et fugiat distinctio aut fugiat illum.",
                            address = RealEstateAddress(
                                zip_code = 35220,
                                city = "Saint Didier",
                                street_name = "rue du champ fleuri",
                                street_number = 7,
                                country = "FRANCE"
                            ),
                        )
                    )

                dao.insert(
                    RealEstate(
                        numberOfBathRoom = 3,
                        numberOfBedRoom = 2,
                        numberOfRoom = 4,
                        surface = 150,
                        typeOfProduct = "House",
                        cityOfProduct = "Montauk",
                        price = 21130000,
                        descriptionOfProduct = "Lorem ipsum dolor sit amet. Non galisum reprehenderit hic quidem repellendus cum eius enim cum",
                        address = RealEstateAddress(
                            zip_code = 35000,
                            city = "Rennes",
                            street_name = "cr des Alliés",
                            street_number = 1,
                            country = "FRANCE"
                        )
                    )
                )
                dao.insert(
                    RealEstate(
                        numberOfBathRoom = 1,
                        numberOfBedRoom = 1,
                        numberOfRoom = 2,
                        surface = 200,
                        typeOfProduct = "Duplex",
                        cityOfProduct = "Brooklyn",
                        price = 13990000,
                        descriptionOfProduct = "Non galisum reprehenderit hic quidem repellendus cum eius enim cum asperiores natus et eius quam rem quisquam natus. Quo volup",
                        address = RealEstateAddress(
                            zip_code = 44100,
                            city = "Nantes",
                            street_name = "rue de Malville",
                            street_number = 1,
                            country = "FRANCE"
                        )
                    )
                )

                dao.insertMedia(
                    RealEstateMedia(
                        realEstateParentId = 1,
                        name = "Photo cuisine",
                        uri = "/data/data/com.openclassrooms.realestatemanager/files/Photo_20211201_163141.jpg"

                    )
                )
                dao.insertMedia(
                    RealEstateMedia(
                        realEstateParentId = 2,
                        name = "Photo chambre",
                        uri = "/data/data/com.openclassrooms.realestatemanager/files/Photo_20211201_163141.jpg"
                    )
                )
                dao.insertMedia(
                    RealEstateMedia(
                        realEstateParentId = 3,
                        name = "Photo garage",
                        uri = "/data/data/com.openclassrooms.realestatemanager/files/Photo_20211201_163141.jpg"
                    )
                )
                dao.insertMedia(
                    RealEstateMedia(
                        realEstateParentId = 1,
                        name = "Photo chambre",
                        uri = "/data/data/com.openclassrooms.realestatemanager/files/Photo_20211201_163141.jpg"
                    )
                )

                dao.insertMedia(
                    RealEstateMedia(
                        realEstateParentId = 2,
                        name = "Photo chambre2",
                        uri = "/data/data/com.openclassrooms.realestatemanager/files/Photo_20211201_163141.jpg"
                    )
                )

                dao.insertMedia(
                    RealEstateMedia(
                        realEstateParentId = 3,
                        name = "Cuisine",
                        uri = "/data/data/com.openclassrooms.realestatemanager/files/Photo_20211201_163141.jpg"
                    )
                )

            }

        }
    }


}