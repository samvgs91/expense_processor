package com.Processor

import upickle.default._

import com.Model.PBICreditCardTransaction

object CategoryParser {
    def bucketTransaction(transaction:PBICreditCardTransaction
                        ,subCategoryMap:Map[String,String]
                        ,categoryMap:Map[String,String]):PBICreditCardTransaction = {

        val subCategory = subCategoryMap
                .find { case (key, _) => 
                        transaction.Detail.contains(key) }
                .map(_._2)

        val category = categoryMap
                .find { 
                    case (key,_) =>
                        subCategory.contains(key)}
                .map(_._2)

        val newTransaction = PBICreditCardTransaction(
                            transaction.Date,
                            transaction.Type,
                            category.getOrElse("Sin Categoría"),
                            subCategory.getOrElse("Sin SubCategoría"),
                            transaction.Detail,
                            transaction.Currency,
                            transaction.Amount,
                            transaction.Exchange
                            )
        newTransaction
    }
}
