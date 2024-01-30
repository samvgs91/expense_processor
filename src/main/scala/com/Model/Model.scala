package com.Model
import java.util.Date

case class PBICreditCardTransaction(
                         Date:Date
                        ,Type:String
                        ,Category:String
                        ,SubCategory:String
                        ,Detail:String
                        ,Currency:String
                        ,Amount:Double
                        ,Exchange:Double)


case class CreditCardTransaction(Pk: Integer
                                ,Date: Date
                                ,Year: Integer
                                ,Month: String
                                ,NMonth:Integer
                                ,CodCategory: Integer
                                ,Category:String
                                ,CodSubCategory:Integer
                                ,SubCategory:String
                                ,Detail:String
                                ,Currency:String
                                ,OriginalAmount:Double
                                ,Exchange:Double
                                ,SolesAmount:Double
                                ,USDAmount:Double)


