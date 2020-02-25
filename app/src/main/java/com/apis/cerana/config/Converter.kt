package com.apis.cerana.config

class Converter {
  fun map(hashMap: HashMap<String, String>): String {
    return hashMap.toString().replace(", ", "&").replace("{", "").replace("}", "")
  }
}