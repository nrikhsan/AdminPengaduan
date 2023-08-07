package com.example.admin.utils

class EnumClass {

    companion object{
        fun getNik(nik : String) : String {
            val typeNik = HashMap<String, String>()
            typeNik["11102001"] = "11102001/UCUP"
            typeNik["11106661"] = "11106661/IWAN"
            typeNik["11101999"] = "11101999/UDIN"
            typeNik["555666"] = "555666/CARLITOS"
            typeNik["1234567"] = "1234567/TUTI"
            typeNik["1122334"] = "1122334/ASTUTI"
            typeNik["654321"] = "654321/TOTO"
            typeNik["678910"] = "678910/JUBAEDAH"
            typeNik["1000200"] = "1000200/ENDANG"
            typeNik["24434"] = "24434/HASIM"
            return typeNik[nik].toString()
        }
    }
}