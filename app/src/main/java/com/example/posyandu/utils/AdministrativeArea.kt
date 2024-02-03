import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStreamReader

data class Area(
    val kode: String,
    val nama: String,
    val kodeParent: String? = null
) {
    override fun toString(): String {
        return nama
    }
}

class DataAdapter<T>(
    context: Context,
    resource: Int,
    objects: List<T>,
    private val textExtractor: (T) -> String,
    private val idExtractor: (T) -> String
) : ArrayAdapter<T>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = super.getView(position, convertView, parent)

        val dataItem = getItem(position)
        val textView = itemView.findViewById<TextView>(android.R.id.text1)
        textView.text = textExtractor.invoke(dataItem!!)

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getItemId(position: Int): Long {
        val dataItem = getItem(position)
        return idExtractor.invoke(dataItem!!).hashCode().toLong()
    }
}

class AdministrativeArea {
    private lateinit var provinsiAdapter: DataAdapter<Area>
    private lateinit var kabkoAdapter: DataAdapter<Area>
    private lateinit var kecamatanAdapter: DataAdapter<Area>
    private lateinit var kelurahanAdapter: DataAdapter<Area>

    @OptIn(DelicateCoroutinesApi::class)
    fun populateDropdowns(
        context: Context,
        provinsiDropdown: AutoCompleteTextView,
        kabkoDropdown: AutoCompleteTextView,
        kecamatanDropdown: AutoCompleteTextView,
        kelurahanDropdown: AutoCompleteTextView
    ) {
        // Load Area data
        GlobalScope.launch(Dispatchers.Main) {
            val areaDataList = loadAreaData(context, "provinsi.csv")
            provinsiAdapter = DataAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                areaDataList,
                { it.nama },
                { it.kode }
            )
            provinsiDropdown.setAdapter(provinsiAdapter)
        }

        // Set Area selection listener for Provinsi
        provinsiDropdown.setOnItemClickListener { _, _, provinsiPos, _ ->
            val selectedProvinsi = provinsiAdapter.getItem(provinsiPos)
            GlobalScope.launch(Dispatchers.Main) {
                val kabkoDataList = loadAreaData(context, "kabko.csv", selectedProvinsi?.kode)
                kabkoAdapter = DataAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    kabkoDataList,
                    { it.nama },
                    { it.kode }
                )
                kabkoDropdown.setAdapter(kabkoAdapter)
            }
            kabkoDropdown.isEnabled = true

            // Set Area selection listener for Kabko if needed
            kabkoDropdown.setOnItemClickListener { _, _, kabkoPos, _ ->
                val selectedKabko = kabkoAdapter.getItem(kabkoPos)
                GlobalScope.launch(Dispatchers.Main) {
                    val kecamatanDataList =
                        loadAreaData(context, "kecamatan.csv", selectedKabko?.kode)
                    kecamatanAdapter = DataAdapter(
                        context,
                        android.R.layout.simple_dropdown_item_1line,
                        kecamatanDataList,
                        { it.nama },
                        { it.kode }
                    )
                    kecamatanDropdown.setAdapter(kecamatanAdapter)
                }
                kecamatanDropdown.isEnabled = true

                // Set Area selection listener for Kecamatan if needed
                kecamatanDropdown.setOnItemClickListener { _, _, kecamatanPos, _ ->
                    val selectedKecamatan = kecamatanAdapter.getItem(kecamatanPos)
                    GlobalScope.launch(Dispatchers.Main) {
                        val kelurahanDataList =
                            loadAreaData(context, "kelurahan.csv", selectedKecamatan?.kode)
                        kelurahanAdapter = DataAdapter(
                            context,
                            android.R.layout.simple_dropdown_item_1line,
                            kelurahanDataList,
                            { it.nama },
                            { it.kode }
                        )
                        kelurahanDropdown.setAdapter(kelurahanAdapter)
                    }
                    kelurahanDropdown.isEnabled = true
                }
            }
        }

        // Disable other AutoCompleteTextViews initially
        kabkoDropdown.isEnabled = false
        kecamatanDropdown.isEnabled = false
        kelurahanDropdown.isEnabled = false
    }

    // Simplified data loading function for all areas
    private fun loadAreaData(
        context: Context,
        fileName: String,
        parentCode: String? = null
    ): List<Area> {
        val areaDataList = mutableListOf<Area>()

        try {
            val inputStream = context.assets.open(fileName)
            val reader = CSVReaderBuilder(InputStreamReader(inputStream)).build()

            val records = reader.readAll()
            for (record in records) {
                if (record.size >= 2 && (parentCode == null || record[2] == parentCode)) {
                    val kode = record[0]
                    val nama = record[1]
                    areaDataList.add(Area(kode, nama, parentCode))
                }
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., file not found, parsing errors)
            e.printStackTrace()
        }

        return areaDataList
    }
}
