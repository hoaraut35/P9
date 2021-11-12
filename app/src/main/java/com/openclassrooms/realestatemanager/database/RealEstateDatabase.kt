package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.injection.ApplicationScope
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [RealEstate::class], version = 1, exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun realEstateDao(): RealEStateDao //

    class Callback @Inject constructor(
        private val database: Provider<RealEstateDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {


        //load just once for init data
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().realEstateDao()

            applicationScope.launch {
                dao.insert(
                    RealEstate(
                        typeOfProduct = "Flat",
                        cityOfProduct = "Manhattan",
                        price = 17870000,
                        descriptionOfProduct = "Lorem ipsum dolor sit amet. Non galisum reprehenderit hic quidem repellendus cum eius enim cum asperiores natus et eius quam rem quisquam natus. Quo voluptates ratione in accusamus placeat in galisum possimus non reiciendis molestiae non sapiente magnam et iure quas? Et ratione dolorem et fugiat distinctio aut fugiat illum."
                    )
                )
                dao.insert(
                    RealEstate(
                        typeOfProduct = "House",
                        cityOfProduct = "Montauk",
                        price = 21130000,
                        descriptionOfProduct = "Lorem ipsum dolor sit amet. Non galisum reprehenderit hic quidem repellendus cum eius enim cum"
                    )
                )
                dao.insert(
                    RealEstate(
                        typeOfProduct = "Duplex",
                        cityOfProduct = "Brooklyn",
                        price = 13990000,
                        descriptionOfProduct = "Non galisum reprehenderit hic quidem repellendus cum eius enim cum asperiores natus et eius quam rem quisquam natus. Quo volup"
                    )
                )
                dao.insert(
                    RealEstate(
                        typeOfProduct = "House",
                        cityOfProduct = "Southhampton",
                        price = 41480000,
                        descriptionOfProduct = " rem quisquam natus. Quo volup"
                    )
                )
            }

        }
    }


}