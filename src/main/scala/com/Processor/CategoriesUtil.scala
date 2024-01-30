package com.Processor

import upickle.default._

object CategoriesUtil {
    
    val subCategoryPath = "resources/category_parsing_conf/SubCategory.json"
    val sourceSubCategory = scala.io.Source.fromFile(subCategoryPath,"UTF-8")
    val subcategoryJson = try sourceSubCategory.mkString finally sourceSubCategory.close()
    val subcategoryMap: Map[String, String] = read[Map[String, String]](subcategoryJson)

    val CategoryPath = "resources/category_parsing_conf/Category.json"
    val sourceCategory = scala.io.Source.fromFile(CategoryPath,"UTF-8")
    val categoryJson = try sourceCategory.mkString finally sourceCategory.close()
    val categoryMap: Map[String, String] = read[Map[String, String]](categoryJson)
}
